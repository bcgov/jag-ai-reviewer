package ca.bc.gov.open.jag.aireviewerapi.document.validators;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentValidation;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentValidationResult;
import ca.bc.gov.open.jag.aireviewerapi.document.models.ValidationTypes;
import ca.bc.gov.open.jag.aireviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.aireviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerRestrictedDocumentException;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerVirusFoundException;
import ca.bc.gov.open.jag.aireviewerapi.utils.TikaAnalysis;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentValidatorImpl implements DocumentValidator {

    Logger logger = LoggerFactory.getLogger(DocumentValidatorImpl.class);

    private final ClamAvService clamAvService;

    private final DiligenService diligenService;

    private final DocumentTypeConfigurationRepository documentTypeConfigurationRepository;

    private final RestrictedDocumentRepository restrictedDocumentRepository;

    public DocumentValidatorImpl(ClamAvService clamAvService, DiligenService diligenService, DocumentTypeConfigurationRepository documentTypeConfigurationRepository, RestrictedDocumentRepository restrictedDocumentRepository) {
        this.clamAvService = clamAvService;
        this.diligenService = diligenService;
        this.documentTypeConfigurationRepository = documentTypeConfigurationRepository;
        this.restrictedDocumentRepository = restrictedDocumentRepository;
    }

    @Override
    public void validateDocument(String documentType, MultipartFile file) {

        if (!documentTypeConfigurationRepository.existsByDocumentType(documentType)) {
            logger.error("A document of type {} is not valid", documentType);
            throw new AiReviewerDocumentException("Invalid document type");
        }

        try {
            clamAvService.scan(new ByteArrayInputStream(file.getBytes()));
            if (!TikaAnalysis.isPdf(file)) {
                logger.error("A document is not PDF");
            	throw new AiReviewerDocumentException("Invalid file type");
            }
        } catch (VirusDetectedException e) {
            logger.error("Virus found in document");
            throw new AiReviewerVirusFoundException("Virus found in document");
        } catch (IOException e) {
            logger.error("File is corrupt");
            throw new AiReviewerDocumentException("File is corrupt");
        }

    }

    @Override
    public DocumentValidation validateExtractedDocument(BigDecimal documentId, DocumentTypeConfiguration documentTypeConfiguration, List<DiligenAnswerField> answers, ObjectNode formData) {

        List<DocumentValidationResult> validationResults = new ArrayList<>();

        Optional<DocumentValidationResult> documentTypeResult = validateDocumentType(documentId, documentTypeConfiguration, formData);

        documentTypeResult.ifPresent(validationResults::add);
        if (documentTypeResult.isPresent()) {
			logger.warn("document {} failed validation: {}", documentId, documentTypeResult.get().toJSON());
        }

        return new DocumentValidation(validationResults);

    }

    private Optional<DocumentValidationResult> validateDocumentType(BigDecimal documentId,
                                                                DocumentTypeConfiguration documentTypeConfiguration,
                                                                ObjectNode formData) {




        Optional<String> returnedDocumentType = (!StringUtils.isBlank(formData.get("document").get("documentType").asText()) ? Optional.of(formData.get("document").get("documentType").asText()) : Optional.empty());

        if (!returnedDocumentType.isPresent() || !returnedDocumentType.get().equals(documentTypeConfiguration.getDocumentType())) {
            if (returnedDocumentType.isPresent() && restrictedDocumentRepository.existsByDocumentTypeDescription(returnedDocumentType.get())) {
                logger.error("Document {} of type {} detected.", documentId, returnedDocumentType.get());
                diligenService.deleteDocument(documentId);
                logger.info("Document {} has been deleted.", documentId);
                throw new AiReviewerRestrictedDocumentException("Document type mismatch detected");
            }
            logger.warn("Document {} of type {} was expected but {} was returned.", documentId, documentTypeConfiguration.getDocumentTypeDescription(), returnedDocumentType);

            return Optional.of(DocumentValidationResult.builder()
                    .actual((returnedDocumentType.orElse("No Document Found")))
                    .expected(documentTypeConfiguration.getDocumentTypeDescription())
                    .type(ValidationTypes.DOCUMENT_TYPE)
                    .create());
        }
        return Optional.empty();
    }

}
