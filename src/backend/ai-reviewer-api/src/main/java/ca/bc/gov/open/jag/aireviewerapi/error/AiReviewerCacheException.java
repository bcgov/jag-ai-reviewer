package ca.bc.gov.open.jag.aireviewerapi.error;

public class AiReviewerCacheException extends AiReviewerException {

    public AiReviewerCacheException(String message) {
        super(message, ErrorCode.CACHE_UNAVAILABLE);
    }

}
