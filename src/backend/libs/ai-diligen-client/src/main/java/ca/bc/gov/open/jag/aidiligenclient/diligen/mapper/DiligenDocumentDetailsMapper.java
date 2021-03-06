package ca.bc.gov.open.jag.aidiligenclient.diligen.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import ca.bc.gov.open.jag.aidiligenclient.api.model.InlineResponse2003DataFileDetails;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ProjectFieldsResponse;


@Mapper
public interface DiligenDocumentDetailsMapper {
    DiligenDocumentDetails toDiligenDocumentDetails(InlineResponse2003DataFileDetails fileDetails, List<Field> answers, ProjectFieldsResponse projectFieldsResponse);

}
