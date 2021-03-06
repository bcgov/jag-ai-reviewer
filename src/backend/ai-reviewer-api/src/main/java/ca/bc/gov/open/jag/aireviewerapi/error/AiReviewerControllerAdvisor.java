package ca.bc.gov.open.jag.aireviewerapi.error;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.bc.gov.open.jag.aidiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.jag.aidiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.aireviewerapi.api.model.ApiError;
import ca.bc.gov.open.jag.jagmailit.api.MailSendApi;
import ca.bc.gov.open.jag.jagmailit.api.handler.ApiException;
import ca.bc.gov.open.jag.jagmailit.api.model.EmailObject;
import ca.bc.gov.open.jag.jagmailit.api.model.EmailRequest;
import ca.bc.gov.open.jag.jagmailit.api.model.EmailRequestContent;

@ControllerAdvice
@EnableConfigurationProperties(ErrorEmailProperties.class)
public class AiReviewerControllerAdvisor {

    Logger logger = LoggerFactory.getLogger(AiReviewerControllerAdvisor.class);

    private final MailSendApi mailSendApi;
    private final ErrorEmailProperties errorEmailProperties;

    public AiReviewerControllerAdvisor(MailSendApi mailSendApi, ErrorEmailProperties errorEmailProperties) {
        this.mailSendApi = mailSendApi;
        this.errorEmailProperties = errorEmailProperties;
    }

    @ExceptionHandler(DiligenDocumentException.class)
    public ResponseEntity<Object> handleDiligenDocumentException(DiligenDocumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DiligenAuthenticationException.class)
    public ResponseEntity<Object> handleDiligenAuthenticationException(DiligenAuthenticationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AiReviewerDocumentException.class)
    public ResponseEntity<Object> handleAiReviewerDocumentException(AiReviewerDocumentException ex) {
        return new ResponseEntity<>(buildApiError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AiReviewerVirusFoundException.class)
    public ResponseEntity<Object> handleDocumentExtractVirusFoundException(AiReviewerVirusFoundException ex) {
        return new ResponseEntity<>(buildApiError(ex), HttpStatus.BAD_GATEWAY);
    }
  
    @ExceptionHandler(AiReviewerCacheException.class)
    public ResponseEntity<Object> handleAiReviewerCacheException(AiReviewerCacheException ex) {
        return new ResponseEntity<>(buildApiError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AiReviewerDocumentTypeMismatchException.class)
    public ResponseEntity<Object> handleDocumentMismatchException(AiReviewerDocumentTypeMismatchException ex) {
        //TODO: Does this exception require an email?
        return new ResponseEntity<>(buildApiError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AiReviewerRestrictedDocumentException.class)
    public ResponseEntity<Object> handleRestrictedDocumentException(AiReviewerRestrictedDocumentException ex) {

        try {
            EmailRequest emailRequest = new EmailRequest();
            EmailObject emailFrom = new EmailObject();
            emailFrom.email(errorEmailProperties.getFromEmail());
            EmailObject emailTo = new EmailObject();
            emailTo.email(errorEmailProperties.getToEmail());
            EmailRequestContent content = new EmailRequestContent();
            content.setValue(ex.getMessage());

            emailRequest.setFrom(emailFrom);
            emailRequest.setTo(Collections.singletonList(emailTo));
            emailRequest.setSubject(ex.getErrorCode());
            emailRequest.setContent(content);

            mailSendApi.mailSend(emailRequest);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(buildApiError(ex), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AiReviewerDocumentTypeConfigurationException.class)
    public ResponseEntity<Object> handleDocumentTypeConfigurationException(AiReviewerDocumentTypeConfigurationException ex) {
        return new ResponseEntity<>(buildApiError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AiReviewerInvalidTransactionIdException.class)
    public ResponseEntity<Object> handleInvalidTransactionIdException(AiReviewerInvalidTransactionIdException ex) {
        return new ResponseEntity<>(buildApiError(ex), HttpStatus.FORBIDDEN);
    }

    private ApiError buildApiError(AiReviewerException ex) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return apiError;
    }

}
