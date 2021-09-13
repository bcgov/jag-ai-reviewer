package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.Keys;
import ca.bc.gov.open.jag.aireviewerapi.cso.model.DocumentReady;
import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;
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
public class CSOORDSServiceImpl implements CSOORDSService {

    private final RestTemplate restTemplate;
    private final CSOProperties CSOProperties;

    Logger logger = LoggerFactory.getLogger(CSOORDSServiceImpl.class);

    public CSOORDSServiceImpl(RestTemplate restTemplate, CSOProperties CSOProperties) {
        this.restTemplate = restTemplate;
        this.CSOProperties = CSOProperties;
    }


    @Override
    public void sendDocumentReady(ExtractResponse extractResponse) {

        int attempt = 0;
        int maxAttempt = 5;
        logger.info("Sending extract to parent");

        while(attempt < maxAttempt) {
            logger.info("Attempting to send extract try number {}", (attempt + 1));
            try {
                
                HttpHeaders headers = new HttpHeaders();

                ResponseEntity result = restTemplate.postForEntity(MessageFormat.format(Keys.CSO_PATH, CSOProperties.getBasePath()),
                        extractResponse,
                        ExtractResponse.class);

                if (result.getStatusCode().is2xxSuccessful()) {
                    logger.info("Document has been received by parent");
                    break;
                }
            } catch (Exception ex) {
                logger.error("Exception when sending extract to cso");
            }
            attempt++;

        }

    }

}
