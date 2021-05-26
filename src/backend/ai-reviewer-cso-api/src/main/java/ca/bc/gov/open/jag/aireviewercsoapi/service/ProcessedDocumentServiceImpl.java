package ca.bc.gov.open.jag.aireviewercsoapi.service;

import ca.bc.gov.open.jag.aireviewercsoapi.model.ProcessedDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessedDocumentServiceImpl implements ProcessedDocumentService {

    private final Logger logger = LoggerFactory.getLogger(ProcessedDocumentServiceImpl.class);

    private final RestTemplate restTemplate;

    public ProcessedDocumentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void processDocument(ProcessedDocument processedDocument) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Transaction-Id", processedDocument.getTransactionId().toString());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> result = restTemplate.exchange(processedDocument.getReturnUri().toString(), HttpMethod.GET, entity, String.class);

        if (result.getStatusCode().is2xxSuccessful()) {
            logger.info("Processed document retrieved ");
            //TODO: off to validation
        } else {
            //TODO: this workflow needs development
            throw new RuntimeException("Something went wrong");
        }

    }

}
