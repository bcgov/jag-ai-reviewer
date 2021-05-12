package ca.bc.gov.open.jag.aireviewercsoapi.model;

import java.net.URI;
import java.math.BigDecimal;
import java.util.UUID;

public class ProcessedDocument {
    private UUID transactionId;
    private String type;
    private BigDecimal documentId;
    private String returnUri;

    public String getType() {
        return type;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public BigDecimal getDocumentId() {
        return documentId;
    }

    public String getReturnUri() { return returnUri; }

    public ProcessedDocument(Builder builder) {
        this.transactionId = builder.transactionId;
        this.documentId = builder.documentId;
        this.type = builder.type;
        this.returnUri = builder.returnUri;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UUID transactionId;
        private String type;
        private BigDecimal documentId;
        private String returnUri;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder transactionId(UUID transactionId) {
            this.transactionId = transactionId;
            return this;
        }


        public Builder documentId(BigDecimal documentId) {
            this.documentId = documentId;
            return this;
        }

        public Builder returnUri(String returnUri) {
            this.returnUri = returnUri;
            return this;
        }

        public ProcessedDocument create() {
            return new ProcessedDocument(this);
        }

    }
}
