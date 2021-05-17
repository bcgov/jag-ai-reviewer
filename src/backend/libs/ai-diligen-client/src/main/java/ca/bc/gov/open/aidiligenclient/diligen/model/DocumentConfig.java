package ca.bc.gov.open.aidiligenclient.diligen.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentConfig {

    Map<String, PropertyConfig> properties = new LinkedHashMap<>();

    public Map<String, PropertyConfig> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, PropertyConfig> properties) {
        this.properties = properties;
    }

}
