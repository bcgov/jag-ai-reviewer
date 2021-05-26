package ca.bc.gov.open.jag.ai.reviewer.services;

import ca.bc.gov.open.jag.ai.reviewer.Keys;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.UUID;

public class ExtractDocumentService {

    @Value("${AI_REVIEWER_HOST:http://localhost:8090}")
    private String aiReviewerHost;

    public Response extractDocumentsResponse(String accessToken, UUID transactionId, String documentType, MultiPartSpecification fileSpec) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .relaxedHTTPSValidation("TLS")
                .contentType("multipart/form-data")
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .header(Keys.X_DOCUMENT_TYPE, documentType)
                .multiPart(fileSpec);

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}", aiReviewerHost, Keys.EXTRACT_DOCUMENTS_PATH))
                .then()
                .extract()
                .response();

    }

    public Response getProcessedDocumentDataById(String accessToken, UUID transactionId, Integer documentId) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .header(Keys.X_TRANSACTION_ID, transactionId);

        return request
                .when()
                .get(MessageFormat.format("{0}/{1}/{2}", aiReviewerHost, Keys.DOCUMENTS_PROCESSED_PATH, String.valueOf(documentId)))
                .then()
                .extract()
                .response();

    }
}
