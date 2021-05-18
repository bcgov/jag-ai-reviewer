package ca.bc.gov.open.jag.aireviewerapi.error;

public class AiReviewerRestrictedDocumentException extends AiReviewerException {
    public AiReviewerRestrictedDocumentException(String message) { super(message, ErrorCode.RESTRICTED_DOCUMENT); }
}
