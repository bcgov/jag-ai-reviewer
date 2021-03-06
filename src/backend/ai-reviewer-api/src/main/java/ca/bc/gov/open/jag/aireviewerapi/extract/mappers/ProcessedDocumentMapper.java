package ca.bc.gov.open.jag.aireviewerapi.extract.mappers;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentValidationResult;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;

@Mapper(componentModel = "spring")
public interface ProcessedDocumentMapper {

    @Mapping(target = "document.type", source = "extractResponse.document.type")
    @Mapping(target = "document.fileName", source = "extractResponse.document.fileName")
    @Mapping(target = "document.size", source = "extractResponse.document.size")
    @Mapping(target = "document.contentType", source = "extractResponse.document.contentType")
    @Mapping(target = "extract.id", source = "extractResponse.extract.id")
    @Mapping(target = "extract.transactionId", source = "extractResponse.extract.transactionId")
    @Mapping(target = "result", source = "extractResponse.formData")
    @Mapping(target = "validation", source = "documentValidationResults")
    ProcessedDocument toProcessedDocument(ExtractResponse extractResponse, List<DocumentValidationResult> documentValidationResults);


    List<Object> map(List<DocumentValidationResult> documentValidationResults);
}
