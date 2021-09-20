package ca.bc.gov.open.jag.aireviewerapi.document.validators.documentValidator;

import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentValidation;
import ca.bc.gov.open.jag.aireviewerapi.document.models.ValidationTypes;
import ca.bc.gov.open.jag.aireviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.aireviewerapi.document.validators.DocumentValidatorImpl;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerRestrictedDocumentException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentsValidatorImpl test suite")
public class ValidateExtractedDocumentTest {

    private static final String RESPONSE_TO_CIVIL_CLAIM = "RCC";
    private static final String NOT_RESPONSE_TO_CIVIL_CLAIM = "THIS IS NOT VALID";
    private static final String RESTRICTED_DOCUMENT = "This is a temporary";
    private static final int NOT_DOCUMENT_TYPE_ID = 123;
    private static final String THE_VALUE = "THIS IS A VALUE";
    private static final DocumentTypeConfiguration DOCUMENT_TYPE_CONFIGURATION = DocumentTypeConfiguration
            .builder()
            .documentType("RCC")
            .documentTypeDescription("Response to Civil Claim")
            .projectId(2)
            .create();


    DocumentValidatorImpl sut;

    @Mock
    DiligenService diligenServiceMock;

    @Mock
    RestrictedDocumentRepository restrictedDocumentRepositoryMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.doNothing().when(diligenServiceMock).deleteDocument(ArgumentMatchers.eq(BigDecimal.ONE));

        Mockito.when(restrictedDocumentRepositoryMock.existsByDocumentTypeDescription(ArgumentMatchers.eq(RESTRICTED_DOCUMENT))).thenReturn(true);

        Mockito.when(restrictedDocumentRepositoryMock.existsByDocumentTypeDescription(ArgumentMatchers.eq(RESPONSE_TO_CIVIL_CLAIM))).thenReturn(false);

        Mockito.when(restrictedDocumentRepositoryMock.existsByDocumentTypeDescription(ArgumentMatchers.eq(NOT_RESPONSE_TO_CIVIL_CLAIM))).thenReturn(false);

        sut = new DocumentValidatorImpl(null, diligenServiceMock, null, restrictedDocumentRepositoryMock);

    }

    @Test
    @DisplayName("Ok: executes with no exception")
    public void withValidDocumentExecuteWithoutThrowing() throws JsonProcessingException {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField documentAnswerField = DiligenAnswerField.builder()
                .id(NOT_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(documentAnswerField);

        ObjectNode json = new ObjectMapper().readValue("{\"document\": {\n" +
                "            \"documentType\": \"" + RESPONSE_TO_CIVIL_CLAIM + "\",\n" +
                "            \"dateFiled\": \"\",\n" +
                "            \"filedBy\": \"\",\n" +
                "            \"amended\": \"New\"\n" +
                "        }}", ObjectNode.class);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers, json);

        Assertions.assertEquals(0, actual.getValidationResults().size());

    }

    @Test
    @DisplayName("Error: no document type found ")
    public void withInValidDocumentTypeThrowException() throws JsonProcessingException {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(NOT_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(THE_VALUE))
                .create();

        answers.add(answerField);

        ObjectNode json = new ObjectMapper().readValue("{\"document\": {\n" +
                "            \"documentType\": \"\",\n" +
                "            \"dateFiled\": \"\",\n" +
                "            \"filedBy\": \"\",\n" +
                "            \"amended\": \"New\"\n" +
                "        }}", ObjectNode.class);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers, json);

        Assertions.assertEquals(ValidationTypes.DOCUMENT_TYPE, actual.getValidationResults().get(0).getType());
        Assertions.assertEquals("No Document Found", actual.getValidationResults().get(0).getActual());
        Assertions.assertEquals("Response to Civil Claim", actual.getValidationResults().get(0).getExpected());

    }

    @Test
    @DisplayName("Error: invalid document type found")
    public void withInValidDocumentThrowException() throws JsonProcessingException {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(NOT_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(NOT_RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(answerField);

        ObjectNode json = new ObjectMapper().readValue("{\"document\": {\n" +
                "            \"documentType\": \"" + NOT_RESPONSE_TO_CIVIL_CLAIM + "\",\n" +
                "            \"dateFiled\": \"\",\n" +
                "            \"filedBy\": \"\",\n" +
                "            \"amended\": \"New\"\n" +
                "        }}", ObjectNode.class);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers, json);

        Assertions.assertEquals(ValidationTypes.DOCUMENT_TYPE, actual.getValidationResults().get(0).getType());
        Assertions.assertEquals(NOT_RESPONSE_TO_CIVIL_CLAIM, actual.getValidationResults().get(0).getActual());
        Assertions.assertEquals("Response to Civil Claim", actual.getValidationResults().get(0).getExpected());

    }

    @Test
    @DisplayName("Error: throws exception when restricted document type found")
    public void withRestrictedDocumentThrowException() throws JsonProcessingException {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(NOT_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESTRICTED_DOCUMENT))
                .create();

        answers.add(answerField);

        ObjectNode json = new ObjectMapper().readValue("{\"document\": {\n" +
                "            \"documentType\": \"" + RESTRICTED_DOCUMENT + "\",\n" +
                "            \"dateFiled\": \"\",\n" +
                "            \"filedBy\": \"\",\n" +
                "            \"amended\": \"New\"\n" +
                "        }}", ObjectNode.class);


        Assertions.assertThrows(AiReviewerRestrictedDocumentException.class, () -> sut.validateExtractedDocument(BigDecimal.ONE, DOCUMENT_TYPE_CONFIGURATION, answers, json));

    }

}
