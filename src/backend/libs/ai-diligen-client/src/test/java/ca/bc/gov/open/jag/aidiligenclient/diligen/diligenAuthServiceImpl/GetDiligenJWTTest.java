package ca.bc.gov.open.jag.aidiligenclient.diligen.diligenAuthServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.open.jag.aidiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiLoginPost200Response;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiLoginPost200ResponseData;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenAuthServiceImpl;
import ca.bc.gov.open.jag.aidiligenclient.exception.DiligenAuthenticationException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenAuthServiceImpl test suite")
public class GetDiligenJWTTest {

    public static final String JWT = "IMMAJWT";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String NO_DATA_USERNAME = "NO_DATA_USERNAME";
    public static final String NO_DATA_PASSWORD = "NO_DATA_PASSWORD";
    public static final String FAILURE_USERNAME = "FAILURE_USERNAME";
    public static final String FAILURE_PASSWORD = "FAILURE_PASSWORD";

    private

    DiligenAuthServiceImpl sut;

    @Mock
    AuthenticationApi authenticationApiMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new DiligenAuthServiceImpl(authenticationApiMock);

    }

    @Test
    @DisplayName("200: jwt returned ")
    public void withValidCredentialsJWTReturned() throws ApiException {

    	ApiLoginPost200Response response2001 = new ApiLoginPost200Response();
        ApiLoginPost200ResponseData response2001Data = new ApiLoginPost200ResponseData();
        response2001Data.setJwt(JWT);
        response2001.setData(response2001Data);
        Mockito.when(authenticationApiMock.apiLoginPost(any())).thenReturn(response2001);

        String result = sut.getDiligenJWT(USERNAME, PASSWORD);

        assertEquals(JWT, result);

    }

    @Test
    @DisplayName("401: with invalid credentials not authorized ")
    public void withInvalidCredentialsNotAuthorized() throws ApiException {

        Mockito.when(authenticationApiMock.apiLoginPost(any())).thenThrow(new ApiException());

        Assertions.assertThrows(DiligenAuthenticationException.class, () -> sut.getDiligenJWT(FAILURE_USERNAME, FAILURE_PASSWORD));

    }

    @Test
    @DisplayName("Error: valid credentials but diligen returns null ")
    public void withValidCredentailsDiligenReturnsNull() throws ApiException {

        ApiLoginPost200Response noDataResponse2001 = new ApiLoginPost200Response();
        Mockito.when(authenticationApiMock.apiLoginPost(any())).thenReturn(noDataResponse2001);

        Assertions.assertThrows(DiligenAuthenticationException.class, () -> sut.getDiligenJWT(NO_DATA_USERNAME, NO_DATA_PASSWORD));

    }
}
