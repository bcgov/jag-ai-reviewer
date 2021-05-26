package ca.bc.gov.open.jag.aireviewerapi.error;

public class AiReviewerDocumentTypeMismatchException extends AiReviewerException {
    public AiReviewerDocumentTypeMismatchException(String message) { super(message, ErrorCode.DOCUMENT_TYPE_MISMATCH); }
}
