package ca.bc.gov.open.jag.aidiligenclient.diligen.diligenServiceImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.open.jag.aidiligenclient.api.DocumentsApi;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiDocumentsFileIdDetailsGet200Response;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiDocumentsFileIdDetailsGet200ResponseData;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiDocumentsFileIdDetailsGet200ResponseDataFileDetails;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import ca.bc.gov.open.jag.aidiligenclient.api.model.FieldType;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ProjectFieldsResponseData;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenProperties;
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenServiceImpl;
import ca.bc.gov.open.jag.aidiligenclient.diligen.mapper.DiligenDocumentDetailsMapperImpl;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.aidiligenclient.exception.DiligenDocumentException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenServiceImpl test suite")
public class GetDocumentDetailsTest {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String JWT = "IMMAJWT";
    private static final Object JSON_OBJECT = "{ \"garbage\":\"garbage\" }";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String EXECUTION_STATUS = "EXECUTION_STATUS";
    public static final String STATUS = "PROCESSED";
    public static final String NOT_PROCESSED = "NOT_PROCESSED";
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final String STRING = "STRING";

    DiligenServiceImpl sut;

    @Mock
    DiligenAuthService diligenAuthServiceMock;

    @Mock
    DocumentsApi documentsApiMock;

    @BeforeAll
    public void beforeAll() throws ApiException {

        MockitoAnnotations.openMocks(this);

        DiligenProperties diligenProperties = new DiligenProperties();
        diligenProperties.setBasePath("http:/test");
        diligenProperties.setUsername(USERNAME);
        diligenProperties.setPassword(PASSWORD);

        Mockito.when(diligenAuthServiceMock.getDiligenJWT(any(), any())).thenReturn(JWT);

        Mockito.when(documentsApiMock.getApiClient()).thenReturn(new ApiClient());

        Mockito.when(documentsApiMock.apiDocumentsFileIdDetailsGet(ArgumentMatchers.eq(BigDecimal.ONE.intValue()))).thenReturn(getMockData(STATUS));

        Mockito.when(documentsApiMock.apiDocumentsFileIdProjectFieldsGet(ArgumentMatchers.eq(BigDecimal.ONE.intValue()))).thenReturn(getMockAnswers());

        Mockito.when(documentsApiMock.apiDocumentsFileIdDetailsGet(ArgumentMatchers.eq(BigDecimal.TEN.intValue()))).thenReturn(getMockData(STATUS));

        Mockito.when(documentsApiMock.apiDocumentsFileIdProjectFieldsGet(ArgumentMatchers.eq(BigDecimal.TEN.intValue()))).thenThrow(new ApiException());

        Mockito.when(documentsApiMock.apiDocumentsFileIdDetailsGet(ArgumentMatchers.eq(BigDecimal.ZERO.intValue()))).thenThrow(new ApiException());

        sut = new DiligenServiceImpl(null, diligenProperties, diligenAuthServiceMock, null, documentsApiMock, new DiligenDocumentDetailsMapperImpl());

    }

    @Test
    @DisplayName("Ok: document was returned")
    public void withValidDocumentIdDocumentSubmissionReturned() {

        DiligenDocumentDetails result = sut.getDocumentDetails(BigDecimal.ONE);

       assertEquals(JSON_OBJECT, result.getMlJson());
       assertEquals(EXECUTION_STATUS, result.getExecutionStatus());
       assertEquals(FILE_NAME, result.getFileName());
       assertEquals(STATUS, result.getFileStatus());
       assertTrue(result.getOcr());
       assertTrue(result.getConverted());
       assertEquals(BigDecimal.ONE, result.getOutOfScope());
       assertEquals(JSON_OBJECT, result.getExtractedDocument());

       assertEquals(1, result.getAnswers().size());
       assertEquals(NAME, result.getAnswers().get(0).getName());
       assertEquals(1, result.getAnswers().get(0).getCreatedBy());
       assertEquals(1, result.getAnswers().get(0).getId());
       assertEquals(TYPE, result.getAnswers().get(0).getFieldType().getType());
       assertTrue(result.getAnswers().get(0).getFieldType().isMulti());
       assertEquals(1, result.getAnswers().get(0).getValues().size());
       assertEquals(STRING, result.getAnswers().get(0).getValues().get(0));

    }

    @Test
    @DisplayName("Error: API Exception thrown retrieving details")
    public void withApiErrorInDetailApiException() {

        Assertions.assertThrows(DiligenDocumentException.class, () -> sut.getDocumentDetails(BigDecimal.ZERO));

    }

    @Test
    @DisplayName("Error: API Exception thrown retrieving answers")
    public void withApiErrorInAnswersApiException() {

        Assertions.assertThrows(DiligenDocumentException.class, () -> sut.getDocumentDetails(BigDecimal.TEN));

    }

    private ApiDocumentsFileIdDetailsGet200Response getMockData(String status) {

    	ApiDocumentsFileIdDetailsGet200Response inlineResponse2003 = new ApiDocumentsFileIdDetailsGet200Response();
    	ApiDocumentsFileIdDetailsGet200ResponseData data = new ApiDocumentsFileIdDetailsGet200ResponseData();
    	ApiDocumentsFileIdDetailsGet200ResponseDataFileDetails fileDetails = new ApiDocumentsFileIdDetailsGet200ResponseDataFileDetails();
        fileDetails.setExtractedDocument(JSON_OBJECT);
        fileDetails.setMlJson(JSON_OBJECT);
        fileDetails.setFileStatus(status);
        fileDetails.setFileName(FILE_NAME);
        fileDetails.setExecutionStatus(EXECUTION_STATUS);
        fileDetails.setIsOcr(true);
        fileDetails.setIsConverted(true);
        fileDetails.setOutOfScope(BigDecimal.ONE);
        data.setFileDetails(fileDetails);
        inlineResponse2003.setData(data);

        return inlineResponse2003;

    }

    private ProjectFieldsResponse getMockAnswers() {
        ProjectFieldsResponse projectFieldsResponse = new ProjectFieldsResponse();
        ProjectFieldsResponseData projectFieldsResponseData = new ProjectFieldsResponseData();

        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setId(1);
        field.setCreatedBy(1);
        field.setName(NAME);

        FieldType fieldType = new FieldType();
        fieldType.setMulti(true);
        fieldType.setType(TYPE);
        List<Object> objects = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        strings.add(STRING);
        fieldType.setOptions(objects);
        field.setFieldType(fieldType);
        field.setValues(strings);

        fields.add(field);

        projectFieldsResponseData.setFields(fields);
        projectFieldsResponse.setData(projectFieldsResponseData);

        return projectFieldsResponse;
    }

}
