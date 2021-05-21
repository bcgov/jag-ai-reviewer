package ca.bc.gov.open.jag.ai.reviewer.helpers;

import ca.bc.gov.open.jag.ai.reviewer.error.AiReviewerTestException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.MessageFormat;

public class TokenHelper {

    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_SECRET = "client_secret";

    private TokenHelper() {
    }

    public static Response getUserAccessToken(String keycloakHost, String keycloakRealm, String clientId, String clientSecret) {

        try {
            URIBuilder uriBuilder = new URIBuilder(keycloakHost);

            uriBuilder.setPath(MessageFormat.format("/auth/realms/{0}/protocol/openid-connect/token", keycloakRealm));

            RequestSpecification request = RestAssured.given()
                    .formParam(CLIENT_ID, clientId)
                    .formParam(GRANT_TYPE, "client_credentials")
                    .formParam(CLIENT_SECRET, clientSecret);

            return request.when().post(uriBuilder.build().toURL().toString()).then()
                    .extract().response();


        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            throw new AiReviewerTestException("Could not build access token url", e);
        }

    }
}
