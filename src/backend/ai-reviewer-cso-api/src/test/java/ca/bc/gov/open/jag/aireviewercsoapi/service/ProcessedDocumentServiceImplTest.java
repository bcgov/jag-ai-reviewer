package ca.bc.gov.open.jag.aireviewercsoapi.service;

import ca.bc.gov.open.jag.aireviewercsoapi.extract.ExtractApiDelegateImpl;
import ca.bc.gov.open.jag.aireviewercsoapi.model.ProcessedDocument;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessedDocumentServiceImplTest {

    private static final String DOCUMENT_TYPE = "TYPE";
    private static final String RETURN_URI = "http://localhost:8080";

    ProcessedDocumentServiceImpl sut;

    @Mock
    RestTemplate restTemplateMock;

    @BeforeEach
    public void beforeEach() throws URISyntaxException {

        MockitoAnnotations.openMocks(this);
        sut = new ProcessedDocumentServiceImpl(restTemplateMock);

    }


    @Test
    @DisplayName("ok: POST processed document was retrieved")
    public void withValidRequestDocumentWasRetrieved() {

        Mockito.when(restTemplateMock.exchange(ArgumentMatchers.eq(RETURN_URI), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok("Success"));

        Assertions.assertDoesNotThrow(() -> sut.processDocument(createProcessedDocument()));

    }

    @Test
    @DisplayName("bad request: POST processed document was not retrieved")
    public void withInValidRequestDocumentWasNotRetrieved() {

        Mockito.when(restTemplateMock.exchange(ArgumentMatchers.eq(RETURN_URI), any(), any(), any(Class.class))).thenReturn(ResponseEntity.badRequest().body("Nope"));

        Assertions.assertThrows(RuntimeException.class, () -> sut.processDocument(createProcessedDocument()))
;
    }

    private ProcessedDocument createProcessedDocument() throws URISyntaxException {
        return ProcessedDocument.builder()
                .documentId(BigDecimal.ONE)
                .type(DOCUMENT_TYPE)
                .returnUri(RETURN_URI)
                .transactionId(UUID.randomUUID())
                .create();
    }

}
