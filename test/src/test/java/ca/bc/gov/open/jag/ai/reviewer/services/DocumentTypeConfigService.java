package ca.bc.gov.open.jag.ai.reviewer.services;

import ca.bc.gov.open.jag.ai.reviewer.Keys;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.UUID;

public class DocumentTypeConfigService {

    @Value("${AI_REVIEWER_HOST:http://localhost:8090}")
    private String aiReviewerHost;

    private final String COMMON_MESSAGE_FORMAT = "{0}/{1}";

    public Response createDocumentTypeConfigResponse(String accessToken, String payloadPath, String pathParam) throws IOException {

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", payloadPath)).getFile();

        RequestSpecification request = RestAssured
                .given()
                .auth().preemptive()
                .oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(resource);
        return request
                .when()
                .post(MessageFormat.format(COMMON_MESSAGE_FORMAT, aiReviewerHost, pathParam))
                .then()
                .extract()
                .response();

    }

    public Response updateDocumentTypeConfigResponse(String accessToken, String id, String payloadPath, String pathParam) throws IOException, JSONException {

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", payloadPath)).getFile();

        String json = new String(Files.readAllBytes(Paths.get(String.valueOf(resource))));

        JSONObject jsonObject = new JSONObject(json);
        jsonObject.put("id", id);

        String updatedJsonWithId = jsonObject.toString();

        RequestSpecification request = RestAssured
                .given()
                .auth().preemptive()
                .oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(updatedJsonWithId);
        return request
                .when()
                .put(MessageFormat.format(COMMON_MESSAGE_FORMAT, aiReviewerHost, pathParam))
                .then()
                .extract()
                .response();

    }

    public Response getDocumentTypeConfiguration(String accessToken, String pathParam) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .get(MessageFormat.format(COMMON_MESSAGE_FORMAT, aiReviewerHost, pathParam))
                .then()
                .extract()
                .response();
    }

    public Response getRestrictedDocumentTypeByIdResponse(String accessToken, UUID id) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .get(MessageFormat.format("{0}/{1}/{2}", aiReviewerHost,
                        Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH, id))
                .then()
                .extract()
                .response();
    }

    public Response deleteDocumentTypeByIdResponse(String accessToken, UUID id, String pathParam) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .delete(MessageFormat.format("{0}/{1}/{2}", aiReviewerHost,
                        pathParam, id))
                .then()
                .extract()
                .response();
    }
}
