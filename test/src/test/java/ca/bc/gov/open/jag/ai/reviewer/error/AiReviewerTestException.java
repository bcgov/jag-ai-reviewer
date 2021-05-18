package ca.bc.gov.open.jag.ai.reviewer.error;

public class AiReviewerTestException extends RuntimeException {

    public AiReviewerTestException(String message) {
        super(message);
    }

    public AiReviewerTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
