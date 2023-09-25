package ca.bc.gov.open.jag.aireviewerapi.documentconfiguration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import ca.bc.gov.open.jag.aireviewerapi.api.DocumentTypeConfigurationsApiDelegate;
import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfigurationRequest;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.aireviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.aireviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapper;
import ca.bc.gov.open.jag.aireviewerapi.error.AiReviewerDocumentTypeConfigurationException;

@Service
public class DocumentConfigurationsApiDelegateImpl implements DocumentTypeConfigurationsApiDelegate {

    private final DocumentTypeConfigurationRepository documentTypeConfigurationRepository;
    private final DocumentTypeConfigurationMapper documentTypeConfigurationMapper;


    public DocumentConfigurationsApiDelegateImpl(DocumentTypeConfigurationRepository documentTypeConfigurationRepository, DocumentTypeConfigurationMapper documentTypeConfigurationMapper) {
        this.documentTypeConfigurationRepository = documentTypeConfigurationRepository;
        this.documentTypeConfigurationMapper = documentTypeConfigurationMapper;
    }

    @Override
    @PreAuthorize("hasRole('ai-reviewer-api-client')")
    public ResponseEntity<List<ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfiguration>> getDocumentConfigurations(String documentType) {

        List<DocumentTypeConfiguration> documentTypeConfigurations;
        if (StringUtils.isBlank(documentType)) {
            documentTypeConfigurations = documentTypeConfigurationRepository.findAll();
        } else {
            documentTypeConfigurations = Collections.singletonList(documentTypeConfigurationRepository.findByDocumentType(documentType));
        }

        return ResponseEntity.ok(documentTypeConfigurations.stream()
                .map(x -> documentTypeConfigurationMapper.toDocumentTypeConfiguration(x))
        .collect(Collectors.toList()));

    }

    @Override
    @PreAuthorize("hasRole('ai-reviewer-api-admin')")
    public ResponseEntity<ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfiguration> createDocumentTypeConfiguration(DocumentTypeConfigurationRequest documentTypeConfigurationRequest) {

        if(documentTypeConfigurationRepository.findByDocumentType(documentTypeConfigurationRequest.getDocumentType().getType()) != null) {
            throw new AiReviewerDocumentTypeConfigurationException("There is already a document of that type");
        }

        DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                .builder()
                .documentType(documentTypeConfigurationRequest.getDocumentType().getType())
                .documentTypeDescription(documentTypeConfigurationRequest.getDocumentType().getDescription())
                .projectId(documentTypeConfigurationRequest.getProjectId())
                .documentConfig((LinkedHashMap<String, Object>) documentTypeConfigurationRequest.getDocumentConfig())
                .create();

        DocumentTypeConfiguration savedDocument = documentTypeConfigurationRepository.save(documentTypeConfiguration);

        return ResponseEntity.ok(documentTypeConfigurationMapper.toDocumentTypeConfiguration(savedDocument));

    }

    @Override
    @PreAuthorize("hasRole('ai-reviewer-api-admin')")
	public ResponseEntity<ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfiguration> updateDocumentTypeConfiguration(
			ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfiguration documentTypeConfiguration) {

		if (!documentTypeConfigurationRepository.existsByDocumentTypeAndId(
				documentTypeConfiguration.getDocumentType().getType(), documentTypeConfiguration.getId())) {
			throw new AiReviewerDocumentTypeConfigurationException("No matches found for that type and id");
		}
		
		DocumentTypeConfiguration dbDocumentTypeConfiguration = documentTypeConfigurationRepository.findById(documentTypeConfiguration.getId()).get();
		if (!documentTypeConfiguration.getDocumentType().getType().equals(dbDocumentTypeConfiguration.getDocumentType())) {
			throw new AiReviewerDocumentTypeConfigurationException("Document type cannot be updated");
		}
		
        DocumentTypeConfiguration tmp = DocumentTypeConfiguration
                .builder()
                .documentConfig((LinkedHashMap<String, Object>) documentTypeConfiguration.getDocumentConfig())
                .create();

        // We need to update, not replace the existing object, otherwise auditing @Version and @CreatedDate won't work.
		dbDocumentTypeConfiguration.setDocumentType(documentTypeConfiguration.getDocumentType().getType());
		dbDocumentTypeConfiguration.setDocumentTypeDescription(documentTypeConfiguration.getDocumentType().getDescription());	
		dbDocumentTypeConfiguration.setDocumentConfig(tmp.getDocumentConfig());

		DocumentTypeConfiguration updatedDocumentTypeConfiguration = documentTypeConfigurationRepository.save(dbDocumentTypeConfiguration);

		return ResponseEntity.ok(documentTypeConfigurationMapper.toDocumentTypeConfiguration(updatedDocumentTypeConfiguration));
	}

    @Override
    @PreAuthorize("hasRole('ai-reviewer-api-admin')")
    public ResponseEntity<Void> deleteDocumentTypeConfiguration(UUID id) {

        if (!documentTypeConfigurationRepository.existsById(id)) return ResponseEntity.notFound().build();

        documentTypeConfigurationRepository.deleteById(id);

        return ResponseEntity.noContent().build();

    }

}
