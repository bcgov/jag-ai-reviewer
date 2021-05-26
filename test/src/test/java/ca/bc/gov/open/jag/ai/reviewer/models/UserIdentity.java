package ca.bc.gov.open.jag.ai.reviewer.models;

public class UserIdentity {

    private String accessToken;

    public UserIdentity(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
