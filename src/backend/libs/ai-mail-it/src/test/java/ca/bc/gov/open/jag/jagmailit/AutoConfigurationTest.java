package ca.bc.gov.open.jag.jagmailit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import ca.bc.gov.open.jag.jagmailit.api.handler.ApiClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AutoConfigurationTest {

	private ApplicationContextRunner context;

	@Test
	public void validConfigurationShouldProduceBeans() {

		context = new ApplicationContextRunner()
				.withUserConfiguration(AutoConfiguration.class)
				.withPropertyValues("mailsend.baseUrl=http://test.com");

		context.run(it -> {
			assertThat(it).hasSingleBean(ApiClient.class);
		});

	}

}
