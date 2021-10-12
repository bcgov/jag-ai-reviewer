package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.api.model.Extract;
import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.cso.model.CSOResult;
import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CSOORDSServiceImplTest {

    private static final String FAKE_PATH = "http://test";

    CSOORDSService sut;

    @Mock
    private RestTemplate restTemplateMock;


    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        CSOProperties csoproperties = new CSOProperties();

        csoproperties.setOrdsBasePath(FAKE_PATH);
        csoproperties.setOrdsUsername("test");
        csoproperties.setOrdsPassword("test");
        csoproperties.setEfileBasePath(FAKE_PATH);
        csoproperties.setEfileUsername("test");
        csoproperties.setEfilePassword("test");

        sut = new CSOORDSServiceImpl(restTemplateMock, csoproperties);

    }

    @Test()
    @DisplayName("Success: accept any event")
    public void withParentAppAvailableDocumentReadySent() {

        CSOResult csoResult = new CSOResult();
        csoResult.setPackageId(BigDecimal.ONE);
        csoResult.setSuccess("TRUE");

        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.PUT), any(), any(Class.class))).thenReturn(ResponseEntity.ok(csoResult));
        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.GET), any(), any(Class.class))).thenReturn(ResponseEntity.ok("success"));

        ProcessedDocument processedDocument = new ProcessedDocument();

        Extract extract = new Extract();

        extract.setTransactionId(UUID.randomUUID());

        processedDocument.setExtract(extract);

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(processedDocument));

    }

    @Test()
    @DisplayName("Error: auto efile failed bad request")
    public void withAutoEfileFailsBadRequest() {

        CSOResult csoResult = new CSOResult();
        csoResult.setPackageId(BigDecimal.ONE);
        csoResult.setSuccess("TRUE");

        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.PUT), any(), any(Class.class))).thenReturn(ResponseEntity.ok(csoResult));
        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.GET), any(), any(Class.class))).thenReturn(ResponseEntity.badRequest().build());

        ProcessedDocument processedDocument = new ProcessedDocument();

        Extract extract = new Extract();

        extract.setTransactionId(UUID.randomUUID());

        processedDocument.setExtract(extract);

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(processedDocument));

    }

    @Test()
    @DisplayName("Error: auto efile failed HttpException")
    public void withAutoEfileFails() {

        CSOResult csoResult = new CSOResult();
        csoResult.setPackageId(BigDecimal.ONE);
        csoResult.setSuccess("TRUE");

        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.PUT), any(), any(Class.class))).thenReturn(ResponseEntity.ok(csoResult));
        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.GET), any(), any(Class.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ProcessedDocument processedDocument = new ProcessedDocument();

        Extract extract = new Extract();

        extract.setTransactionId(UUID.randomUUID());

        processedDocument.setExtract(extract);

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(processedDocument));

    }

    @Test()
    @DisplayName("Success: accept any event")
    public void withValidatioFailed() {

        CSOResult csoResult = new CSOResult();
        csoResult.setPackageId(BigDecimal.ONE);
        csoResult.setSuccess("FALSE");

        Mockito.when(restTemplateMock.exchange(any(String.class), ArgumentMatchers.eq(HttpMethod.PUT), any(), any(Class.class))).thenReturn(ResponseEntity.ok(csoResult));

        ProcessedDocument processedDocument = new ProcessedDocument();

        Extract extract = new Extract();

        extract.setTransactionId(UUID.randomUUID());

        processedDocument.setExtract(extract);

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(processedDocument));

    }

    @Test()
    @DisplayName("Error: message was not sent")
    public void withParentAppDownFiveAttemptsMade() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok("success"));

        ProcessedDocument processedDocument = new ProcessedDocument();

        Extract extract = new Extract();

        extract.setTransactionId(UUID.randomUUID());

        processedDocument.setExtract(extract);

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(processedDocument));

    }

    @Test()
    @DisplayName("Error: message was not sent")
    public void withParentAppDownException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ProcessedDocument processedDocument = new ProcessedDocument();

        Extract extract = new Extract();

        extract.setTransactionId(UUID.randomUUID());

        processedDocument.setExtract(extract);

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(processedDocument));

    }

}
