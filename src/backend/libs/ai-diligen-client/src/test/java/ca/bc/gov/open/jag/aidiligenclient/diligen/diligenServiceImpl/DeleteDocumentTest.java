package ca.bc.gov.open.jag.aidiligenclient.diligen.diligenServiceImpl;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenProperties;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenServiceImpl;
import ca.bc.gov.open.jag.aidiligenclient.diligen.mapper.DiligenDocumentDetailsMapperImpl;
import ca.bc.gov.open.jag.aidiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.aidiligenclient.api.DocumentsApi;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenServiceImpl test suite")
public class DeleteDocumentTest {

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    DiligenServiceImpl sut;

    @Mock
    DiligenAuthService diligenAuthServiceMock;

    @Mock
    DocumentsApi documentsApiMock;

    @BeforeEach
    public void beforeEach() throws ApiException {

        MockitoAnnotations.openMocks(this);

        DiligenProperties diligenProperties = new DiligenProperties();
        diligenProperties.setBasePath("http:/test");
        diligenProperties.setUsername(USERNAME);
        diligenProperties.setPassword(PASSWORD);

        Mockito.when(diligenAuthServiceMock.getDiligenJWT(any(), any())).thenReturn(JWT);

        Mockito.when(documentsApiMock.getApiClient()).thenReturn(new ApiClient());

        sut = new DiligenServiceImpl(null, diligenProperties, diligenAuthServiceMock, null, documentsApiMock, new DiligenDocumentDetailsMapperImpl());

    }

    @Test
    @DisplayName("Ok: document was deleted")
    public void whenValidRequestDeleteDocument() throws ApiException {

        Mockito.doNothing().when(documentsApiMock).apiDocumentsDelete(any());

        Assertions.assertDoesNotThrow(() -> sut.deleteDocument(BigDecimal.ONE));
        
    }

    @Test
    @DisplayName("Error: document was not deleted")
    public void whenInValidRequestDeleteDocument() throws ApiException {

        Mockito.doThrow(ApiException.class).when(documentsApiMock).apiDocumentsDelete(any());

        Assertions.assertThrows(DiligenDocumentException.class, () ->  sut.deleteDocument(BigDecimal.ONE));

    }

}
