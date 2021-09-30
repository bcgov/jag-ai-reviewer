package ca.bc.gov.open.jag.aireviewerapi.cso.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.ai.cso" )
public class CSOProperties {

    private String ordsBasePath;
    private String ordsUsername;
    private String ordsPassword;
    private String efileBasePath;
    private String efileUsername;
    private String efilePassword;

    public String getOrdsBasePath() { return ordsBasePath; }

    public void setOrdsBasePath(String ordsBasePath) { this.ordsBasePath = ordsBasePath; }

    public String getOrdsUsername() { return ordsUsername; }

    public void setOrdsUsername(String ordsUsername) { this.ordsUsername = ordsUsername; }

    public String getOrdsPassword() { return ordsPassword; }

    public void setOrdsPassword(String ordsPassword) {  this.ordsPassword = ordsPassword; }

    public String getEfileBasePath() { return efileBasePath;  }

    public void setEfileBasePath(String efileBasePath) { this.efileBasePath = efileBasePath; }

    public String getEfileUsername() { return efileUsername; }

    public void setEfileUsername(String efileUsername) { this.efileUsername = efileUsername; }

    public String getEfilePassword() { return efilePassword; }

    public void setEfilePassword(String efilePassword) { this.efilePassword = efilePassword; }
}
