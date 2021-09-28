package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.Keys;
import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.cso.model.CSOResult;
import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
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

                HttpEntity<Object> entity = new HttpEntity<>(processedDocument, setupBasicAuth(csoProperties.getOrdsUsername(), csoProperties.getOrdsPassword()));

                ResponseEntity<CSOResult> result = restTemplate.exchange(MessageFormat.format(Keys.CSO_PATH, csoProperties.getOrdsBasePath()),
                        HttpMethod.PUT,
                        entity,
                        CSOResult.class);

                if (result.getStatusCode().is2xxSuccessful()) {
                    logger.info("Transaction {} has been received by cso", processedDocument.getExtract().getTransactionId());

                    autoProcess(result.getBody());

                    return;
                }
            } catch (HttpStatusCodeException e) {
                logger.info("CSO returned status code {}", e.getStatusCode());
            } catch (Exception ex) {
                logger.error("Exception when sending extract to cso");
            }
            attempt++;

        }

        logger.info("Transaction {} failed to send data to cso", processedDocument.getExtract().getTransactionId());

    }

    private void autoProcess(CSOResult csoResult) {

        if (csoResult.getSuccess() != null && csoResult.getSuccess()) {

            logger.info("CSO validation succeeded");

            int attempt = 1;
            int maxAttempt = 5;

            logger.info("Triggering auto efile");

            while (attempt <= maxAttempt) {

                logger.info("Attempting to trigger auto efile try number {}", attempt);

                try {

                    HttpEntity<?> entity = new HttpEntity<>(setupBasicAuth(csoProperties.getEfileUsername(), csoProperties.getEfilePassword()));

                    ResponseEntity<Object> result = restTemplate.exchange(MessageFormat.format(Keys.AUTO_EFILE_PATH, csoProperties.getEfileBasePath(), csoResult.getPackageId()),
                            HttpMethod.GET,
                            entity,
                            Object.class);

                    if (result.getStatusCode().is2xxSuccessful()) {
                        logger.info("Transaction has been auto filed");
                        return;
                    }

                } catch (HttpStatusCodeException e) {

                    logger.info("Auto file returned status code {}", e.getStatusCode());

                } catch (Exception ex) {

                    logger.error("Exception when executing auto efile");
                    
                }
                attempt++;

            }

            logger.info("Failure in triggering auto efile");

        } else {
            logger.info("CSO validation failed");
        }

    }

    private HttpHeaders setupBasicAuth(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        return headers;
    }

}
