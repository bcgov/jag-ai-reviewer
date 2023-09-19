package ca.bc.gov.open.jag.aidiligenclient.diligen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.bc.gov.open.jag.aidiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.aidiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiLoginPost200Response;
import ca.bc.gov.open.jag.aidiligenclient.api.model.ApiLoginPostRequest;
import ca.bc.gov.open.jag.aidiligenclient.exception.DiligenAuthenticationException;

@Service
public class DiligenAuthServiceImpl implements DiligenAuthService {
    Logger logger = LoggerFactory.getLogger(DiligenAuthServiceImpl.class);

    private final AuthenticationApi authenticationApi;

    public DiligenAuthServiceImpl(AuthenticationApi authenticationApi) {
        this.authenticationApi = authenticationApi;
    }

    @Override
    public String getDiligenJWT(String userName, String password) {
    	ApiLoginPostRequest loginParams = new ApiLoginPostRequest();
        loginParams.setEmail(userName);
        loginParams.setPassword(password);
        try {

        	ApiLoginPost200Response result = authenticationApi.apiLoginPost(loginParams);

            if (result.getData() == null) throw new DiligenAuthenticationException("No login data");

            logger.info("diligen login complete");

            return result.getData().getJwt();

        } catch (ApiException e) {
            throw new DiligenAuthenticationException(e.getMessage());
        }
    }
}
