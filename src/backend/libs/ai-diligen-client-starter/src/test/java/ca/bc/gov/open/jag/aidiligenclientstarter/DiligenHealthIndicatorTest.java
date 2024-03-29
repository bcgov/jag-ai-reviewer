package ca.bc.gov.open.jag.aidiligenclientstarter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;

import ca.bc.gov.open.jag.aidiligenclient.api.HealthCheckApi;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiIsServerUpGet200Response;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiligenHealthIndicatorTest {

    private DiligenHealthIndicator sut;

    @Mock
    private HealthCheckApi healthCheckApiMock;


    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new DiligenHealthIndicator(healthCheckApiMock);

    }

    @Test
    @DisplayName("ok: with diligen up should return up")
    public void withDiligenUpShouldReturnUp() throws ApiException {

    	ApiIsServerUpGet200Response inlineResponse200 = new ApiIsServerUpGet200Response();
        inlineResponse200.setAnswer("yes");

        Mockito.when(healthCheckApiMock.apiIsServerUpGet()).thenReturn(inlineResponse200);

        Health result = sut.health();

        Assertions.assertEquals("UP", result.getStatus().getCode());
        Assertions.assertEquals("UP", result.getDetails().get("Diligen Api"));

    }

    @Test
    @DisplayName("error: when diligen return 200 but answer is not yes")
    public void withAnserNoYesShouldReturnDown() throws ApiException {

        ApiIsServerUpGet200Response inlineResponse200 = new ApiIsServerUpGet200Response();
        inlineResponse200.setAnswer("it’s not wise to upset a Wookiee.");

        Mockito.when(healthCheckApiMock.apiIsServerUpGet()).thenReturn(inlineResponse200);

        Health result = sut.health();

        Assertions.assertEquals("DOWN", result.getStatus().getCode());
        Assertions.assertEquals("DOWN", result.getDetails().get("Diligen Api"));
        Assertions.assertEquals("200 OK", result.getDetails().get("StatusCode"));
        Assertions.assertEquals("unknown", result.getDetails().get("Error"));


    }

    @Test
    @DisplayName("error: when diligen return 4xx, 5xx")
    public void withDiligenReturningErrorHttpStatus() throws ApiException {

        ApiIsServerUpGet200Response inlineResponse200 = new ApiIsServerUpGet200Response();
        inlineResponse200.setAnswer("it’s not wise to upset a Wookiee.");

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        Mockito.when(healthCheckApiMock.apiIsServerUpGet()).thenThrow(new ApiException(400, headers, "Let’s keep a little optimism here."));

        Health result = sut.health();

        Assertions.assertEquals("DOWN", result.getStatus().getCode());
        Assertions.assertEquals("DOWN", result.getDetails().get("Diligen Api"));
        Assertions.assertEquals("400", result.getDetails().get("StatusCode"));
        Assertions.assertEquals("Let’s keep a little optimism here.", result.getDetails().get("Error"));


    }



}
