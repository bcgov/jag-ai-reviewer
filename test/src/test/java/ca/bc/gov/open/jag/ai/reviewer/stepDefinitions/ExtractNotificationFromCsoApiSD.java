package ca.bc.gov.open.jag.ai.reviewer.stepDefinitions;

import ca.bc.gov.open.jag.ai.reviewer.Keys;
import ca.bc.gov.open.jag.ai.reviewer.services.ExtractNotificationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class ExtractNotificationFromCsoApiSD {

    private final ExtractNotificationService extractNotificationService;
    private Response actualExtractNotificationServiceResponse;

    private final Logger logger = LoggerFactory.getLogger(ExtractValidatedDocumentsSD.class);

    public ExtractNotificationFromCsoApiSD(ExtractNotificationService extractNotificationService) {
        this.extractNotificationService = extractNotificationService;
    }

    @Given("document details are posted to notification api")
    public void postDocumentDetails() throws IOException {

        logger.info("Posting request with document details");

        actualExtractNotificationServiceResponse = extractNotificationService.extractNotificationResponse(UUID.fromString(Keys.ACTUAL_X_TRANSACTION_ID), Keys.EXTRACT_NOTIFICATION_PAYLOAD);

        logger.info("Api response status code: {}", actualExtractNotificationServiceResponse.getStatusCode());
    }

    @Then("valid response is returned")
    public void documentFormDataIsExtracted() {
        logger.info("Asserting extract notification response");

        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, actualExtractNotificationServiceResponse.getStatusCode());

        logger.info("Response matched requirements");

    }
}
