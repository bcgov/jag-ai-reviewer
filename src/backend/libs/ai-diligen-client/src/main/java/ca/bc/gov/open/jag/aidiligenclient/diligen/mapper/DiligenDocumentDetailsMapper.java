package ca.bc.gov.open.jag.aidiligenclient.diligen.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiDocumentsFileIdDetailsGet200ResponseDataFileDetails;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenDocumentDetails;


@Mapper
public interface DiligenDocumentDetailsMapper {	
    DiligenDocumentDetails toDiligenDocumentDetails(ApiDocumentsFileIdDetailsGet200ResponseDataFileDetails fileDetails, List<Field> answers, ProjectFieldsResponse projectFieldsResponse);

}
