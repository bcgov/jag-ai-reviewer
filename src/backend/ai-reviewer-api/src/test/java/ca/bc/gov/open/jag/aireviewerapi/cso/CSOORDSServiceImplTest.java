package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.cso.properties.CSOProperties;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.Extract;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
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

        CSOProperties CSOProperties = new CSOProperties();

        CSOProperties.setBasePath(FAKE_PATH);

        sut = new CSOORDSServiceImpl(restTemplateMock, CSOProperties);

    }

    @Test()
    @DisplayName("Success: accept any event")
    public void withParentAppAvailableDocumentReadySent() {

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(), any(Class.class))).thenReturn(ResponseEntity.ok("success"));

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(ExtractResponse.builder()
                .extract(
                        Extract.builder()
                                .transactionId(UUID.randomUUID())
                                .create()
                )
                .create()));

    }

    @Test()
    @DisplayName("Error: message was not sent")
    public void withParentAppDownFiveAttemptsMade() {

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(), any(Class.class))).thenReturn(ResponseEntity.notFound().build());

        Assertions.assertDoesNotThrow(() -> sut.sendExtractedData(ExtractResponse.builder()
                .extract(
                        Extract.builder()
                                .transactionId(UUID.randomUUID())
                                .create()
                )
                .create()));

    }

}
