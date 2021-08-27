package ca.bc.gov.open.jag.aireviewerapi.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.ai.feature" )
public class FeatureProperties {

    private Boolean redisQueue;

    public Boolean getRedisQueue() {
        return redisQueue;
    }

    public void setRedisQueue(Boolean redisQueue) {
        this.redisQueue = redisQueue;
    }

}
