package ca.bc.gov.open.jag.aireviewerapi.restricteddocument.mappers;

import ca.bc.gov.open.jag.aireviewerapi.api.model.RestrictedDocumentType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface RestrictedDocumentTypeMapper {

    @Mapping( source = "documentType", target = "documentType.type" )
    @Mapping( source = "documentTypeDescription", target = "documentType.description" )
    RestrictedDocumentType toDocumentType(ca.bc.gov.open.jag.aireviewerapi.document.models.RestrictedDocumentType documentType);


}
