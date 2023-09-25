package ca.bc.gov.open.jag.aireviewerapi.core;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import ca.bc.gov.open.jag.aireviewerapi.config.KeycloakJwtAuthConverter;

public class SecurityUtils {

	private SecurityUtils() {
	}

	/**
	 * Returns the principle claim name of the currently logged in user from the JWT.
	 */
	public static Optional<String> getLoggedInUsername() {
		try {
			Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return Optional.of(KeycloakJwtAuthConverter.getPrincipleClaimName(jwt));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
