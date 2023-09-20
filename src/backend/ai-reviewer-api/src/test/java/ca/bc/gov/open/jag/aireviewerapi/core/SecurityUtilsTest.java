package ca.bc.gov.open.jag.aireviewerapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.keycloak.KeycloakPrincipal;
//import org.keycloak.KeycloakSecurityContext;
//import org.keycloak.representations.AccessToken;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Security Utils Test Suite")
@ExtendWith(MockitoExtension.class)
public class SecurityUtilsTest {

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Jwt jwtMock;
    
    @Mock
    private Authentication authenticationMock;
    
    @Test
    public void testLoggedInUsername() {

        String expectedUsername = RandomStringUtils.randomAlphanumeric(10);

        // arrange
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        Mockito.when(jwtMock.getClaim("preferred_username")).thenReturn(expectedUsername);
        SecurityContextHolder.setContext(securityContextMock);

        // act
        Optional<String> actualUsername = SecurityUtils.getLoggedInUsername();

        // assert
        assertTrue(actualUsername.isPresent());
        assertEquals(expectedUsername, actualUsername.get());
    }
}
