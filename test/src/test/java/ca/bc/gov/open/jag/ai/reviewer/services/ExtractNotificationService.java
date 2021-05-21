package ca.bc.gov.open.jag.ai.reviewer.services;

import ca.bc.gov.open.jag.ai.reviewer.Keys;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class ExtractNotificationService {

    @Value("${AI_REVIEWER_CSO_API_HOST:http://localhost:8686}")
    private String aiReviewerCsoApiHost;

    public Response extractNotificationResponse(UUID transactionId, String payloadPath) throws IOException {

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", payloadPath)).getFile();

        RequestSpecification request = RestAssured
                .given()
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .contentType(ContentType.JSON)
                .body(resource);

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}", aiReviewerCsoApiHost, Keys.EXTRACT_NOTIFICATION_PATH))
                .then()
                .extract()
                .response();

    }
}
