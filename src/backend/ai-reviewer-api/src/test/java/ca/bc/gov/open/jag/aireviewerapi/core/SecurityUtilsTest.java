package ca.bc.gov.open.jag.aireviewerapi.core;

import ca.bc.gov.open.jag.aireviewerapi.Keys;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Security Utils Test Suite")
@ExtendWith(MockitoExtension.class)
public class SecurityUtilsTest {

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @BeforeEach
    public void setup() {

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    @DisplayName("Assert client id returned")
    public void shouldConvertToUUID() {

        String expectedUUID = UUID.randomUUID().toString();

        // arrange
        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.CLIENT_ID, expectedUUID);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        // act
        Optional<String> actual = SecurityUtils.getClientId();

        // assert
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expectedUUID, actual.get());
    }
}
