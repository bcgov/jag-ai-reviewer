package ca.bc.gov.open.jag.aidiligenclient.diligen.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyConfig {

    private String type;
    private Integer fieldId;
    private String fieldKey;
    private String businessObject;
    Map<String, PropertyConfig> properties = new LinkedHashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getBusinessObject() { return businessObject; }

    public void setBusinessObject(String businessObject) { this.businessObject = businessObject; }

    public Map<String, PropertyConfig> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, PropertyConfig> properties) {
        this.properties = properties;
    }

    public String getFieldKey() { return fieldKey; }

    public void setFieldKey(String fieldKey) { this.fieldKey = fieldKey; }

}
