package ca.bc.gov.open.jag.aireviewerapi.document.store;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfigurationAudit;

public interface DocumentConfigurationAuditRepository extends MongoRepository<DocumentTypeConfigurationAudit, UUID> {

}
