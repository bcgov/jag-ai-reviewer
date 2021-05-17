package ca.bc.gov.open.jag.aireviewerapi;

import java.util.Arrays;
import java.util.List;

public class Keys {

    private Keys() {}

    public static final String PROCESSED_STATUS = "PROCESSED";
    public static final String DOCUMENT_TYPE = "document-type";
    public static final List<String> ACCEPTED_STATUS = Arrays.asList("QUEUED_FOR_ML_ANALYSIS", "DONE_OCR_PROCESSING","QUEUED_FOR_OCR_PROCESSING", "QUEUED_FOR_TRANSLATE", "QUEUED_FOR_PROCESSING");
    public static final Integer ANSWER_DOCUMENT_TYPE_ID = 232;
    public static final Integer ANSWER_PLAINTIFF_ID = 319;
    public static final Integer ANSWER_DEFENDANT_ID = 320;
    public static final String WEBHOOK_PATH = "{0}/parent/documentReady";
    public static final String WEBHOOK_RETURN_PATH = "{0}/documents/processed/{1}";

}