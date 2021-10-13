package ca.bc.gov.open.jag.aidiligenclient.diligen.processor;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import java.util.List;

public interface FieldProcessor {

    ObjectNode getJson(DocumentConfig formData, List<Field> fields, JSONObject details);

}
