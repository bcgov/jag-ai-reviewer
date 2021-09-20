package ca.bc.gov.open.jag.aidiligenclient.diligen.processor;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.PropertyConfig;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FieldProcessorImpl implements FieldProcessor {

    Logger logger = LoggerFactory.getLogger(FieldProcessorImpl.class);

    public ObjectNode getJson(DocumentConfig formData, List<Field> fields) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {

            if(StringUtils.equals("object", property.getValue().getType())) {
                objectNode.set(property.getKey(), parseSchema(property.getValue(), fields));
            }

            if(StringUtils.equals("string", property.getValue().getType())) {
                objectNode.put(property.getKey(), extractStringValue(property.getValue(), fields));
            }
        }

        return objectNode;

    }

    private ObjectNode parseSchema(PropertyConfig formData, List<Field> fields) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {
            if(StringUtils.equals("object", property.getValue().getType())) {
                objectNode.set(property.getKey(), parseSchema(property.getValue(), fields));
            }

            if(StringUtils.equals("string", property.getValue().getType())) {
                String value = extractStringValue(property.getValue(), fields);
                objectNode.put(property.getKey(), value);

                //This logic is a requirement to report on error codes
                if (property.getKey().equals("errorCodes")) {

                    logger.info("Reviewer extracted: {} errors", value.split(",").length);

                    for(String errorCode : value.split(",")) {
                        logger.info("Error code: {} detected", errorCode);
                    }

                }

            }

            if(StringUtils.equals("array", property.getValue().getType())) {
                ArrayNode array = objectMapper.valueToTree(extractArrayValue(property.getValue(), fields));
                objectNode.putArray(property.getKey()).addAll(array);

            }

        }

        return objectNode;

    }

    private String extractStringValue(PropertyConfig formDataProperty, List<Field> fields) {

        Optional<List<String>> values = fields.stream().filter(x -> x.getProjectFieldKey().equals(formDataProperty.getFieldKey())).findFirst().map(x -> x.getValues());

        return values.map(strings -> strings.stream().map(Object::toString)
                .collect(Collectors.joining(","))).orElse("");

    }
    
    private List<String> extractArrayValue(PropertyConfig formDataProperty, List<Field> fields) {

        Optional<List<String>> values = fields.stream().filter(x -> x.getProjectFieldKey().equals(formDataProperty.getFieldKey())).findFirst().map(x -> x.getValues());

        return values.orElseGet(ArrayList::new);

    }

}
