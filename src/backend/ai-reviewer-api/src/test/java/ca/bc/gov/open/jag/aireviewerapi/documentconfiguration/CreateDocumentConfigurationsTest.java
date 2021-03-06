package ca.bc.gov.open.jag.aireviewerapi.documentconfiguration;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentType;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfigurationRequest;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.aireviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.aireviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapper;
import ca.bc.gov.open.jag.aireviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapperImpl;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerDocumentTypeConfigurationException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateDocumentConfigurationsTest {
    private static final String CASE_1 = "CASE1";
    private static final String CASE_2 = "CASE2";
    private static final String DESCRIPTION = "DESCRIPTION";

    private DocumentConfigurationsApiDelegateImpl sut;
    @Mock
    private DocumentTypeConfigurationRepository documentTypeConfigurationRepositoryMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        List<DocumentTypeConfiguration> documentTypeConfigurationList = new ArrayList<>();

        DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                .builder()
                .documentConfig(new DocumentConfig())
                .documentType(CASE_1)
                .create();

        documentTypeConfigurationList.add(documentTypeConfiguration);

        Mockito.when(documentTypeConfigurationRepositoryMock.findAll()).thenReturn(documentTypeConfigurationList);

        Mockito.when(documentTypeConfigurationRepositoryMock.findByDocumentType(CASE_1)).thenReturn(documentTypeConfiguration);

        DocumentTypeConfiguration documentTypeConfiguration2 = DocumentTypeConfiguration
                .builder()
                .documentConfig(new DocumentConfig())
                .documentType("CASE2")
                .create();

        Mockito.doReturn(documentTypeConfiguration2).when(documentTypeConfigurationRepositoryMock).save(ArgumentMatchers.argThat(x -> x.getDocumentType().equals(CASE_2)));

        DocumentTypeConfigurationMapper documentTypeConfigurationMapper = new DocumentTypeConfigurationMapperImpl();
        sut = new DocumentConfigurationsApiDelegateImpl(documentTypeConfigurationRepositoryMock, documentTypeConfigurationMapper);

    }

    @Test
    @DisplayName("ok: POST /documentTypeConfigurations should create a new document type configuration")
    public void withValidDocumentShouldCreateNewConfiguration() {

        DocumentTypeConfigurationRequest documentTypeConfigurationRequest = new DocumentTypeConfigurationRequest();
        DocumentType documentType = new DocumentType();
        documentType.setType(CASE_2);
        documentType.setDescription(DESCRIPTION);
        documentTypeConfigurationRequest.setDocumentType(documentType);
        documentTypeConfigurationRequest.setProjectId(1);

        LinkedHashMap<String, Object> testProperty = new LinkedHashMap<>();

        testProperty.put("type", "string");
        testProperty.put("fieldId", "230");

        LinkedHashMap<String, Object> courtProperties = new LinkedHashMap<>();
        courtProperties.put("test", testProperty);

        LinkedHashMap<String, Object> courtProperty = new LinkedHashMap<>();
        courtProperty.put("type", "object");
        courtProperty.put("properties", courtProperties);

        LinkedHashMap<String, Object> documentProperties = new LinkedHashMap<>();
        documentProperties.put("court", courtProperty);

        LinkedHashMap<String, Object> documentConfig = new LinkedHashMap<>();
        documentConfig.put("properties", documentProperties);

        documentTypeConfigurationRequest.setDocumentConfig(documentConfig);

        ResponseEntity<ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfiguration> response = sut.createDocumentTypeConfiguration(documentTypeConfigurationRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals("CASE2", response.getBody().getDocumentType().getType());

    }

    @Test
    @DisplayName("Error: POST /documentTypeConfigurations should return bad request if the document type already exists")
    public void withExistingDocumentShouldReturnBadRequest() {

        DocumentTypeConfigurationRequest documentTypeConfigurationRequest = new DocumentTypeConfigurationRequest();
        DocumentType documentType = new DocumentType();
        documentType.setType(CASE_1);
        documentType.setDescription(DESCRIPTION);
        documentTypeConfigurationRequest.setDocumentType(documentType);
        documentTypeConfigurationRequest.setProjectId(1);

        LinkedHashMap<String, Object> testProperty = new LinkedHashMap<>();

        testProperty.put("type", "string");
        testProperty.put("fieldId", "230");

        LinkedHashMap<String, Object> courtProperties = new LinkedHashMap<>();
        courtProperties.put("test", testProperty);

        LinkedHashMap<String, Object> courtProperty = new LinkedHashMap<>();
        courtProperty.put("type", "object");
        courtProperty.put("properties", courtProperties);

        LinkedHashMap<String, Object> documentProperties = new LinkedHashMap<>();
        documentProperties.put("court", courtProperty);

        LinkedHashMap<String, Object> documentConfig = new LinkedHashMap<>();
        documentConfig.put("properties", documentProperties);

        documentTypeConfigurationRequest.setDocumentConfig(documentConfig);

        Assertions.assertThrows(AiReviewerDocumentTypeConfigurationException.class, () -> sut.createDocumentTypeConfiguration(documentTypeConfigurationRequest));

    }
}

