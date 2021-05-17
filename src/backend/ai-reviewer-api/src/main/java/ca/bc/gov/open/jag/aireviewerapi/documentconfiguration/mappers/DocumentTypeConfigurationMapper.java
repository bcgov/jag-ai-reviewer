package ca.bc.gov.open.jag.aireviewerapi.documentconfiguration.mappers;

import ca.bc.gov.open.jag.aireviewerapi.api.model.DocumentTypeConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface DocumentTypeConfigurationMapper {

    @Mapping( source = "documentType", target = "documentType.type" )
    @Mapping( source = "documentTypeDescription", target = "documentType.description" )
    DocumentTypeConfiguration toDocumentTypeConfiguration(ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration documentTypeConfiguration);


}
