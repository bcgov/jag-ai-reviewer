package ca.bc.gov.open.jag.aireviewerapi.core;

import ca.bc.gov.open.jag.aireviewerapi.Keys;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getClientId() {
        return getOtherClaim(Keys.CLIENT_ID);
    }

    private static Optional<String> getOtherClaim(String claim) {
        try {
            return Optional.of(((KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getOtherClaims().get(claim).toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
