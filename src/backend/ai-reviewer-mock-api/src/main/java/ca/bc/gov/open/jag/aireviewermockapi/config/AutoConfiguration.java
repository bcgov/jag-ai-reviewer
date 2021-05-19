package ca.bc.gov.open.jag.aireviewermockapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AiReviewerApiProperties.class)
public class AutoConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AiReviewerApiProperties aiReviewerApiProperties() {
        return new AiReviewerApiProperties();
    }
}
