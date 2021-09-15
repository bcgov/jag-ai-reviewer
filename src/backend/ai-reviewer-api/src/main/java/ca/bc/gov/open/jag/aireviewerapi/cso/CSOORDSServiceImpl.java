package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.Keys;
import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
@EnableConfigurationProperties({CSOProperties.class})
public class CSOORDSServiceImpl implements CSOORDSService {

    private final RestTemplate restTemplate;
    private final CSOProperties csoProperties;

    Logger logger = LoggerFactory.getLogger(CSOORDSServiceImpl.class);

    public CSOORDSServiceImpl(RestTemplate restTemplate, CSOProperties csoProperties) {
        this.restTemplate = restTemplate;
        this.csoProperties = csoProperties;
    }


    @Override
    public void sendExtractedData(ProcessedDocument processedDocument) {

        int attempt = 1;
        int maxAttempt = 5;
        logger.info("Sending transaction {} extract to cso", processedDocument.getExtract().getTransactionId());

        while (attempt <= maxAttempt) {
            logger.info("Attempting to send extract try number {}", attempt);
            try {
                
                HttpHeaders headers = new HttpHeaders();
                headers.setBasicAuth(csoProperties.getUsername(), csoProperties.getPassword());

                HttpEntity<Object> entity = new HttpEntity<>(processedDocument, headers);

                ResponseEntity result = restTemplate.exchange(MessageFormat.format(Keys.CSO_PATH , csoProperties.getBasePath()),
                        HttpMethod.PUT,
                        entity,
                        String.class);

                if (result.getStatusCode().is2xxSuccessful()) {
                    logger.info("Transaction {} has been received by cso", processedDocument.getExtract().getTransactionId());
                    return;
                }
            } catch (Exception ex) {
                logger.error("Exception when sending extract to cso");
            }
            attempt++;

        }

        logger.info("Transaction {} failed to send data to cso", processedDocument.getExtract().getTransactionId());

    }

}
