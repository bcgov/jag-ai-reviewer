package ca.bc.gov.open.jag.ai.reviewer.services;

import ca.bc.gov.open.jag.ai.reviewer.error.AiReviewerTestException;
import ca.bc.gov.open.jag.ai.reviewer.helpers.TokenHelper;
import ca.bc.gov.open.jag.ai.reviewer.models.UserIdentity;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class OauthService {

    @Value("${KEYCLOAK_HOST:http://localhost:8081}")
    private String keycloakHost;

    @Value("${KEYCLOAK_REALM:ai-reviewer}")
    private String keycloakRealm;

    @Value("${KEYCLOAK_CLIENT_ID:ai-reviewer-developer}")
    private String keycloakClientId;

    @Value("${KEYCLOAK_CREDENTIALS_SECRET:ae18ccc9-06c2-4227-8c64-3d06d95511d6}")
    private String keycloakCredentialsSecret;

    private final Logger logger = LoggerFactory.getLogger(OauthService.class);

    public UserIdentity getUserIdentity() {

        logger.info("Requesting bearer token from {} issuer", keycloakHost);

        Response response = TokenHelper.getUserAccessToken(keycloakHost, keycloakRealm, keycloakClientId, keycloakCredentialsSecret);

        logger.info("Issuer respond with http status: {}", response.getStatusCode());

        JsonPath oidcTokenJsonPath = new JsonPath(response.asString());

        if (oidcTokenJsonPath.get("access_token") == null)
            throw new AiReviewerTestException("access_token not present in response");

        String actualUserToken = oidcTokenJsonPath.get("access_token");

        return new UserIdentity(actualUserToken);

    }

}
