package ca.bc.gov.open.jag.aireviewercsoapi.extract;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.bc.gov.open.jag.aireviewercsoapi.api.model.ExtractNotification;
import ca.bc.gov.open.jag.aireviewercsoapi.service.ProcessedDocumentService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExtractApiDelegateImplTest {

    private static final String DOCUMENT_TYPE = "TEST";
    private static final String LOCALHOST_HELLO = "http://localhost/hello";
    ExtractApiDelegateImpl sut;

    @Mock
    ProcessedDocumentService processedDocumentServiceMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new ExtractApiDelegateImpl(processedDocumentServiceMock);

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withRequestReturnResult() throws URISyntaxException {

        Mockito.doNothing().when(processedDocumentServiceMock).processDocument(any());

        ResponseEntity actual = sut.extractNotification(UUID.randomUUID(), createExtractNotification());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withExceptionReturnResult() throws URISyntaxException {

        Mockito.doThrow(new RuntimeException("BAD")).when(processedDocumentServiceMock).processDocument(any());

        ResponseEntity actual = sut.extractNotification(UUID.randomUUID(), createExtractNotification());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    private ExtractNotification createExtractNotification() throws URISyntaxException {
        ExtractNotification extractNotification = new ExtractNotification();
        extractNotification.setDocumentId(BigDecimal.ONE);
        extractNotification.documentType(DOCUMENT_TYPE);
        extractNotification.setReturnUri(LOCALHOST_HELLO);
        return extractNotification;
    }

}
