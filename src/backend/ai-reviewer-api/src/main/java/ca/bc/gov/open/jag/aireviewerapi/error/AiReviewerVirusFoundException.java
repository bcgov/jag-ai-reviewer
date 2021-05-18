package ca.bc.gov.open.jag.aireviewerapi.error;

public class AiReviewerVirusFoundException extends AiReviewerException{

    public AiReviewerVirusFoundException(String message) { super(message, ErrorCode.VIRUS_FOUND); }

}
