package ca.bc.gov.open.jag.aireviewermockapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoConfigurationTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withPropertyValues(
                    "ai-reviewer-backend.base-url=localhost"
            )
            .withUserConfiguration(AutoConfiguration.class);


    @Test
    @DisplayName("autoconfiguration test")
    public void testExtractRequestCacheManager() {
        context.run(it -> {
            assertThat(it).hasBean("aiReviewerApiProperties");
            assertThat(it).hasBean("restTemplate");
        });
    }

}
