package ca.bc.gov.open.jag.aireviewermockapi.controller;

import ca.bc.gov.open.jag.aireviewermockapi.model.DocumentReady;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class ParentControllerTest {

    private static final String DOCUMENT_TYPE = "TYPE";
    private static final String RETURN_URI = "http://localhost:8080";
    ParentController sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new ParentController();

    }

    @Test
    @DisplayName("ok: POST document was retrieved")
    public void withValidRequestDocumentWasRetrieved() {

        DocumentReady request = new DocumentReady();
        request.setDocumentId(BigDecimal.ONE);
        request.setDocumentType(DOCUMENT_TYPE);
        request.setReturnUri(RETURN_URI);

        ResponseEntity actual = sut.documentReady(request);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

}
