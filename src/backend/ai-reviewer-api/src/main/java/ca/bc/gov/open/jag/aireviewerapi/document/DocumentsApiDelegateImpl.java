package ca.bc.gov.open.jag.aireviewerapi.document;

import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.aidiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.aireviewerapi.Keys;
import ca.bc.gov.open.jag.aireviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentEvent;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentWebhookEvent;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentWebhookEventData;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerInvalidTransactionIdException;
import ca.bc.gov.open.jag.aireviewerapi.webhook.WebHookService;
import ca.bc.gov.open.jag.aireviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.aireviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerDocumentConfigException;
import ca.bc.gov.open.jag.aireviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.aireviewerapi.extract.mappers.ProcessedDocumentMapper;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;
import ca.bc.gov.open.jag.aireviewerapi.extract.store.ExtractStore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    private final DiligenService diligenService;
    private final ExtractRequestMapper extractRequestMapper;
    private final ExtractStore extractStore;
    private final StringRedisTemplate stringRedisTemplate;
    private final FieldProcessor fieldProcessor;
    private final DocumentValidator documentValidator;
    private final DocumentTypeConfigurationRepository documentTypeConfigurationRepository;
    private final ProcessedDocumentMapper processedDocumentMapper;
    private final WebHookService webHookService;

    public DocumentsApiDelegateImpl(
            DiligenService diligenService,
            ExtractRequestMapper extractRequestMapper,
            ExtractStore extractStore,
            StringRedisTemplate stringRedisTemplate,
            FieldProcessor fieldProcessor,
            DocumentValidator documentValidator, DocumentTypeConfigurationRepository documentTypeConfigurationRepository, ProcessedDocumentMapper processedDocumentMapper, WebHookService webHookService) {

        this.diligenService = diligenService;
        this.extractRequestMapper = extractRequestMapper;
        this.extractStore = extractStore;
        this.stringRedisTemplate = stringRedisTemplate;
        this.fieldProcessor = fieldProcessor;
        this.documentValidator = documentValidator;
        this.documentTypeConfigurationRepository = documentTypeConfigurationRepository;
        this.processedDocumentMapper = processedDocumentMapper;
        this.webHookService = webHookService;
    }

    @Override
    public ResponseEntity<DocumentExtractResponse> extractDocumentFormData(UUID xTransactionId, String xDocumentType, Boolean xUseWebhook, MultipartFile file) {

        MDC.put(Keys.DOCUMENT_TYPE, xDocumentType);

        DocumentTypeConfiguration documentTypeConfiguration = documentTypeConfigurationRepository.findByDocumentType(xDocumentType);

        if (documentTypeConfiguration == null) {
            return new ResponseEntity("Document type not found", HttpStatus.BAD_REQUEST);
        }

        long receivedTimeMillis = System.currentTimeMillis();

        logger.info("document extract request received");

        documentValidator.validateDocument(xDocumentType, file);

        logger.info("document is valid");

        //Due to a bug in mapstruct logic has been performed inline
        ExtractRequest extractRequest = extractRequestMapper.toExtractRequest(extractRequestMapper.toExtract(xTransactionId, (xUseWebhook == null || xUseWebhook)), xDocumentType, file, receivedTimeMillis);

        BigDecimal response = diligenService.postDocument(xDocumentType, file, documentTypeConfiguration.getProjectId());

        //Temporary
        stringRedisTemplate.convertAndSend("documentWait", response.toPlainString());

        Optional<ExtractRequest> extractRequestCached = extractStore.put(response, extractRequest);

        if (!extractRequestCached.isPresent())
            throw new AiReviewerCacheException("Could not cache extract request");

        MDC.remove(Keys.DOCUMENT_TYPE);

        return ResponseEntity.ok(extractRequestMapper.toDocumentExtractResponse(extractRequestCached.get(), response));

    }

    @Override
    public ResponseEntity<Void> documentEvent(UUID xTransactionId, DocumentEvent documentEvent) {
        return handleDocumentEvent(documentEvent);
    }

    @Override
    public ResponseEntity<Void> documentWebhookEvent(DocumentWebhookEvent documentWebhookEvent) {
        logger.info("Received webhook event");

        ArrayList<DocumentEvent> documentEvents = new ArrayList<>();
        List<DocumentWebhookEventData> eventList = documentWebhookEvent.getData();

        if(eventList == null || eventList.size() < 1) {
            throw new AiReviewerDocumentException("Invalid data array in request body.");
        }

        for(DocumentWebhookEventData event : eventList) {
            DocumentEvent documentEvent = new DocumentEvent();
            documentEvent.setDocumentId(event.getId());
            documentEvent.setStatus(event.getStatus());
            documentEvents.add(documentEvent);
        }

        for(DocumentEvent documentEvent : documentEvents) {
            handleDocumentEvent(documentEvent);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProcessedDocument> documentProcessed(UUID xTransactionId, BigDecimal documentId) {

        logger.info("document {} requested ", documentId);

        Optional<ExtractResponse> extractResponseCached = extractStore.getResponse(documentId);

        if (!extractResponseCached.isPresent()) throw new AiReviewerCacheException("Document not found in cache");
        if (!extractResponseCached.get().getExtract().getTransactionId().equals(xTransactionId)) throw new AiReviewerInvalidTransactionIdException("Requested transaction id is not valid");

        //Clear cache
        extractStore.evict(documentId);
        extractStore.evictResponse(documentId);

        return ResponseEntity.ok(processedDocumentMapper.toProcessedDocument(extractResponseCached.get(), extractResponseCached.get().getDocumentValidation().getValidationResults()));

    }

    private ObjectNode buildFormData(ProjectFieldsResponse response, DocumentTypeConfiguration config) {

        if (config == null)
            throw new AiReviewerDocumentConfigException("Document Configuration not found");

        return fieldProcessor.getJson(config.getDocumentConfig(),
                response.getData().getFields());

    }

    private ResponseEntity<Void> handleDocumentEvent(DocumentEvent documentEvent) {
        logger.info("document {} status has changed to {}", documentEvent.getDocumentId(), documentEvent.getStatus());

        if (documentEvent.getStatus().equalsIgnoreCase(Keys.PROCESSED_STATUS)) {

            diligenService.getDocumentDetails(documentEvent.getDocumentId());

            DiligenDocumentDetails response = diligenService.getDocumentDetails(documentEvent.getDocumentId());

            Optional<ExtractRequest> extractRequestCached = extractStore.get(documentEvent.getDocumentId());

            extractRequestCached.ifPresent(extractRequest -> {

                DocumentTypeConfiguration config = documentTypeConfigurationRepository.findByDocumentType(extractRequest.getDocument().getType());

                if(config == null)
                    throw new AiReviewerDocumentConfigException("document configuration not found");

                ObjectNode formData = buildFormData(response.getProjectFieldsResponse(), config);

                ExtractResponse extractedResponse = ExtractResponse
                        .builder()
                        .document(extractRequestCached.get().getDocument())
                        .formData(formData)
                        .extract(extractRequestCached.get().getExtract())
                        .documentValidation(documentValidator.validateExtractedDocument(documentEvent.getDocumentId(), config, response.getAnswers(), formData))
                        .create();

                extractStore.put(documentEvent.getDocumentId(), extractedResponse);

                MDC.put(Keys.DOCUMENT_TYPE, extractRequest.getDocument().getType());

                extractRequest.updateProcessedTimeMillis();
                logger.info("document processing time: [{}]", extractRequest.getProcessedTimeMillis());
                extractStore.put(documentEvent.getDocumentId(), extractRequest);

                if (extractRequestCached.get().getExtract().getUseWebhook()) {
                    //Send document ready message
                    webHookService.sendDocumentReady(documentEvent.getDocumentId(), extractRequest.getDocument().getType(), extractRequest.getExtract().getTransactionId());
                }
            });

        }

        MDC.remove(Keys.DOCUMENT_TYPE);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
