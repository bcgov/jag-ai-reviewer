package ca.bc.gov.open.jag.aireviewerapi;

import java.util.Arrays;
import java.util.List;

public class Keys {

    private Keys() {}

    public static final String PROCESSED_STATUS = "PROCESSED";
    public static final String DOCUMENT_TYPE = "document-type";
    public static final String TRANSACTION_ID = "transaction-id";
    public static final List<String> ACCEPTED_STATUS = Arrays.asList("QUEUED_FOR_ML_ANALYSIS", "DONE_OCR_PROCESSING","QUEUED_FOR_OCR_PROCESSING", "QUEUED_FOR_OCR_DECISION", "QUEUED_FOR_TRANSLATE", "QUEUED_FOR_PROCESSING");
    public static final String CSO_PATH = "{0}/savejson";
    public static final String AUTO_EFILE_PATH = "{0}/efilehub/autoefile/process?packageId={1}";

}
