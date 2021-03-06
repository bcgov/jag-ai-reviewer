package ca.bc.gov.open.jag.aireviewerapi.document.store;

import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DocumentTypeConfigurationRepository extends MongoRepository<DocumentTypeConfiguration, UUID> {

    DocumentTypeConfiguration findByDocumentType(String documentType);
    
    boolean existsByDocumentType(String documentType);

    boolean existsByDocumentTypeAndId(String documentType, UUID id);

}
