package ca.bc.gov.open.jag.aireviewerapi.webhook;

import ca.bc.gov.open.jag.aireviewerapi.Keys;
import ca.bc.gov.open.jag.aireviewerapi.webhook.model.DocumentReady;
import ca.bc.gov.open.jag.aireviewerapi.webhook.properties.WebHookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;

@Service
public class WebHookServiceImpl implements WebHookService{

    private final RestTemplate restTemplate;
    private final WebHookProperties webHookProperties;

    Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);

    public WebHookServiceImpl(RestTemplate restTemplate, WebHookProperties webHookProperties) {
        this.restTemplate = restTemplate;
        this.webHookProperties = webHookProperties;
    }


    @Override
    public void sendDocumentReady(BigDecimal documentId, String documentType, UUID transactionId) {

        int attempt = 0;
        int maxAttempt = 5;
        logger.info("Sending {} to parent", documentId);

        while(attempt < maxAttempt) {
            logger.info("Attempting to send {} try number {}", documentId, (attempt + 1));
            try {
                
                HttpHeaders headers = new HttpHeaders();
                headers.add("X-Transaction-Id", String.valueOf(transactionId));

                HttpEntity<DocumentReady> entity = new HttpEntity<>(DocumentReady.builder()
                        .documentId(documentId)
                        .documentType(documentType)
                        .returnUri(MessageFormat.format(Keys.WEBHOOK_RETURN_PATH, webHookProperties.getReturnPath(), documentId.toPlainString()))
                        .create(), headers);

                ResponseEntity result = restTemplate.postForEntity(MessageFormat.format(Keys.WEBHOOK_PATH, webHookProperties.getBasePath()),
                        entity,
                        DocumentReady.class);

                if (result.getStatusCode().is2xxSuccessful()) {
                    logger.info("Document {} has been received by parent", documentId);
                    break;
                }
            } catch (Exception ex) {
                logger.error("Failed to notify: {}", ex.getMessage());
            }
            attempt++;

        }

    }

}
