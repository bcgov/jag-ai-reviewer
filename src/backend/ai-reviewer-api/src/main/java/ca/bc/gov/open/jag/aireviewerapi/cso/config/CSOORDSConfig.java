package ca.bc.gov.open.jag.aireviewerapi.cso.config;

import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import ca.bc.gov.open.jag.aireviewerapi.cso.CSOORDSService;
import ca.bc.gov.open.jag.aireviewerapi.cso.CSOORDSServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({CSOProperties.class})
public class CSOORDSConfig {

    private final CSOProperties CSOProperties;

    public CSOORDSConfig(CSOProperties CSOProperties) {
        this.CSOProperties = CSOProperties;
    }

    @Bean
    public CSOORDSService webHookService(RestTemplate restTemplate) {
        return new CSOORDSServiceImpl(restTemplate, CSOProperties);
    }

}
