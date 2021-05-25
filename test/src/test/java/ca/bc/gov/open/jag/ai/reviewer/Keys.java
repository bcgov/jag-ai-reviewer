package ca.bc.gov.open.jag.ai.reviewer;

public class Keys {
    protected Keys() {
    }

    // Document Service
    public static final String X_TRANSACTION_ID = "X-Transaction-Id";
    public static final String X_DOCUMENT_TYPE = "X-Document-Type";
    public static final String ACTUAL_X_TRANSACTION_ID = "1d4e38ba-0c88-4c92-8367-c8eada8cca19";

    // Commons
    public static final String TEST_VALID_DOCUMENT_PDF = "test-valid-document.pdf";
    public static final String TEST_INVALID_DOCUMENT_PDF = "test-invalid-document.pdf";

    // Assertions
    public static final String ID_INDEX_FROM_RESPONSE = "id[0]";

    // File path
    public static final String DOCUMENT_TYPE_CONFIG_PAYLOAD = "document-type-config-payload.json";
    public static final String RESTRICTED_DOCUMENT_TYPE_PAYLOAD = "restricted-document-type-payload.json";
    public static final String ADDITIONAL_RESTRICTED_DOCUMENT_TYPE_PAYLOAD = "additional-restricted-document-type-payload.json";
    public static final String RESTRICTED_DOCUMENT_TYPE_UPDATE_PAYLOAD = "restricted-document-type-update-payload.json";
    public static final String DOCUMENT_TYPE_CONFIG_UPDATE_PAYLOAD = "document-type-config-update-payload.json";
    public static final String EXTRACT_NOTIFICATION_PAYLOAD = "extract-notification-payload.json";


    // Api endpoint Paths
    public static final String EXTRACT_DOCUMENTS_PATH = "documents/extract";
    public static final String DOCUMENT_TYPE_CONFIGURATION_PATH = "documentTypeConfigurations";
    public static final String RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH = "restrictedDocumentTypes";
    public static final String DOCUMENTS_PROCESSED_PATH = "documents/processed";
    public static final String EXTRACT_NOTIFICATION_PATH = "extract/notification";

}
