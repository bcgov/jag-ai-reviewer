package ca.bc.gov.open.jag.aireviewerapi.error;

public class AiReviewerInvalidTransactionIdException extends AiReviewerException {
    public AiReviewerInvalidTransactionIdException(String message) { super(message, ErrorCode.INVALID_TRANSACTION_ID); }
}
