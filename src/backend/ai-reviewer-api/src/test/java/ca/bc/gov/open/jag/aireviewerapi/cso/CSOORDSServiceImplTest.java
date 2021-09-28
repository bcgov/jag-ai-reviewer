package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.api.model.Extract;
import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

        sut = new CSOORDSServiceImpl(restTemplateMock, csoproperties);

    }

    @Test()
    @DisplayName("Success: accept any event")
    public void withParentAppAvailableDocumentReadySent() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok("success"));

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

}
