package ca.bc.gov.open.jag.aidiligenclient.diligen.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DiligenData {
    @JsonProperty("documents")
    private List<DiligenDocument> documents;

    public List<DiligenDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DiligenDocument> documents) {
        this.documents = documents;
    }

}
