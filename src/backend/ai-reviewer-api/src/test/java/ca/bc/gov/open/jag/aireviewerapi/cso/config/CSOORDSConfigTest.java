package ca.bc.gov.open.jag.aireviewerapi.cso.config;

import ca.bc.gov.open.jag.aireviewerapi.cso.CSOORDSService;
import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CSOORDSConfigTest {
    ApplicationContextRunner context;

    @BeforeAll
    public void setup() {
        context = new ApplicationContextRunner()
                .withUserConfiguration(CSOORDSConfig.class)
                .withBean(RestTemplate.class)
                .withPropertyValues("jag.ai.cso.webhook.basePath=http://test",
                        "jag.ai.cso.webhook.returnPath=http://test")
                .withUserConfiguration(CSOProperties.class);
    }

    @Test
    @DisplayName("Test that beans are created")
    public void testBeansAreGenerated() {

        context.run(it -> {
            assertThat(it).hasSingleBean(CSOORDSService.class);
        });

    }
}
