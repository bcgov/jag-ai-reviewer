package ca.bc.gov.open.jag.aidiligenclient.diligen.processor;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.aidiligenclient.diligen.model.PropertyConfig;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FieldProcessorImpl implements FieldProcessor {

    Logger logger = LoggerFactory.getLogger(FieldProcessorImpl.class);

    public ObjectNode getJson(DocumentConfig formData, List<Field> fields, JSONObject details) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {

            if(StringUtils.equals("object", property.getValue().getType())) {
                if (!property.getValue().getBusinessObject().equalsIgnoreCase("details")) {
                    objectNode.set(property.getKey(), parseSchema(property.getValue(), fields, details));
                } else {
                    objectNode.set(property.getKey(), parseDetailsSchema(property.getValue(), fields, details));
                }
            }

            if(StringUtils.equals("string", property.getValue().getType())) {
                if (!property.getValue().getBusinessObject().equalsIgnoreCase("details")) {
                    objectNode.put(property.getKey(), extractStringValue(property.getValue(), fields));
                } else {
                    objectNode.put(property.getKey(), extractDetailsStringValue(property.getValue(), details));
                }
            }

            if(StringUtils.equals("array", property.getValue().getType())) {
                if (!property.getValue().getBusinessObject().equalsIgnoreCase("details")) {
                    objectNode.set(property.getKey(), parseArraySchema(property.getValue(), fields, details));
                } else {
                    objectNode.set(property.getKey(), parseDetailsArraySchema(property.getValue(), fields, details, property.getValue().getFieldKey()));
                }

            }

        }

        return objectNode;

    }

    private ObjectNode parseSchema(PropertyConfig formData, List<Field> fields, JSONObject details) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {
            if(StringUtils.equals("object", property.getValue().getType())) {
                if (!property.getValue().getBusinessObject().equalsIgnoreCase("details")) {
                    objectNode.set(property.getKey(), parseSchema(property.getValue(), fields, details));
                } else {
                    objectNode.set(property.getKey(), parseDetailsSchema(property.getValue(), fields, details));
                }
            }

            if(StringUtils.equals("string", property.getValue().getType())) {
                if (!property.getValue().getBusinessObject().equalsIgnoreCase("details")) {
                    String value = extractStringValue(property.getValue(), fields);
                    objectNode.put(property.getKey(), value);

                    //This logic is a requirement to report on error codes
                    if (property.getKey().equals("errorCodes")) {

                        logger.info("Reviewer extracted: {} errors", value.split(",").length);

                        for(String errorCode : value.split(",")) {
                            logger.info("Error code: {} detected", errorCode);
                        }

                    }
                } else {
                    objectNode.put(property.getKey(), extractDetailsStringValue(property.getValue(), details));
                }

            }

            if(StringUtils.equals("array", property.getValue().getType())) {

                if (!property.getValue().getBusinessObject().equalsIgnoreCase("details")) {
                    objectNode.set(property.getKey(), parseArraySchema(property.getValue(), fields, details));
                } else {
                    objectNode.set(property.getKey(), parseDetailsArraySchema(property.getValue(), fields, details, property.getValue().getFieldKey()));
                }
            }

        }

        return objectNode;

    }

    private ObjectNode parseDetailsSchema(PropertyConfig formData, List<Field> fields, JSONObject details) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {
            if(StringUtils.equals("object", property.getValue().getType())) {
                objectNode.set(property.getKey(), parseDetailsSchema(property.getValue(), fields, details));
            }

            if(StringUtils.equals("string", property.getValue().getType())) {

                objectNode.put(property.getKey(), extractDetailsStringValue(property.getValue(), details));

            }

            if(StringUtils.equals("array", property.getValue().getType())) {

                objectNode.set(property.getKey(), parseDetailsArraySchema(property.getValue(), fields, details, property.getValue().getFieldKey()));

            }

        }

        return objectNode;

    }

    private ArrayNode parseArraySchema(PropertyConfig formData, List<Field> fields, JSONObject details) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ArrayNode arrayNode = objectMapper.createArrayNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {

            if(StringUtils.equals("object", property.getValue().getType())) {

                arrayNode.addAll(extractArrayValue(formData, fields, property.getKey()));

            }

            if(StringUtils.equals("string", property.getValue().getType())) {

                arrayNode.addAll(extractArrayValue(formData, fields, property.getKey()));

            }

            if(StringUtils.equals("array", property.getValue().getType())) {

                arrayNode.add(parseArraySchema(property.getValue(), fields, details));

            }

        }

        return arrayNode;

    }

    private ArrayNode parseDetailsArraySchema(PropertyConfig formData, List<Field> fields, JSONObject details, String key) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ArrayNode arrayNode = objectMapper.createArrayNode();

        for (Map.Entry<String, PropertyConfig> property : formData.getProperties().entrySet()) {

            if(StringUtils.equals("object", property.getValue().getType())) {

                JSONArray jsonArray = details.getJSONArray(key);

                for (int i = 0; i < jsonArray.length(); i++) {

                    arrayNode.add(parseDetailsSchema(property.getValue(), fields, jsonArray.getJSONObject(i)));

                }

            }

            if(StringUtils.equals("string", property.getValue().getType())) {

                arrayNode.addAll(extractDetailsArrayValue(formData, details.getJSONArray(key), property.getKey()));

            }

            if(StringUtils.equals("array", property.getValue().getType())) {

                arrayNode.add(parseDetailsArraySchema(property.getValue(), fields, details, property.getKey()));

            }

        }

        return arrayNode;

    }

    private String extractStringValue(PropertyConfig formDataProperty, List<Field> fields) {

        Optional<List<String>> values = fields.stream().filter(x -> x.getProjectFieldKey().equals(formDataProperty.getFieldKey())).findFirst().map(x -> x.getValues());

        return values.map(strings -> strings.stream().map(Object::toString)
                .collect(Collectors.joining(","))).orElse("");

    }
    
    private List<ObjectNode> extractArrayValue(PropertyConfig formDataProperty, List<Field> fields, String key) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<ObjectNode> objectNodes = new ArrayList<>();

        Optional<List<String>> values = fields.stream().filter(x -> x.getProjectFieldKey().equals(formDataProperty.getFieldKey())).findFirst().map(x -> x.getValues());

        if (values.isPresent()) {
            for (String value : values.get()) {

                ObjectNode objectNode = objectMapper.createObjectNode();

                objectNode.put(key, value);

                objectNodes.add(objectNode);

            }
        }

        return objectNodes;

    }


    private String extractDetailsStringValue(PropertyConfig formDataProperty, JSONObject details) {

        return details.getString(formDataProperty.getFieldKey());

    }

    private List<ObjectNode> extractDetailsArrayValue(PropertyConfig formDataProperty, JSONArray objects, String key) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<ObjectNode> objectNodes = new ArrayList<>();

        for (int i = 0; i < objects.length(); i++) {

            ObjectNode objectNode = objectMapper.createObjectNode();

            objectNode.put(key,  objects.getString(i));

            objectNodes.add(objectNode);

        }

        return objectNodes;

    }

}
