package ca.bc.gov.open.jag.aireviewerapi.cso.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.ai.cso" )
public class CSOProperties {

    private String basePath;
    private String username;
    private String password;

    public String getBasePath() { return basePath; }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

}