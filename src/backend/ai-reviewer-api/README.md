# ai-reviewer-api

## Maven Profiles

### Openshift

Current image build in openshift runs with `openshift` profile by default.
This profile mostly enables splunk logging.

```bash
mvn install -P openshift
```

Require to run locally:

```bash
mvn install -P ai-reviewer
```

## Configuration

You should use environment variables to configure the jag ai-reviewer-api

| Environment Variable            				  | Type    | Description                                  | Notes                          |
| ----------------------------------------------- | ------- | -------------------------------------------- | ------------------------------ |
| SERVER_PORT                     				  | Integer | web application server port                  | defaulted to `8080`            |
| DILIGEN_HEALTH_ENABLED   						  | Boolean | if Diligen should be monitored on healt check| defaulted to `true`            |
| DILIGEN_BASE_PATH               				  | String  | Diligen api base path                        |                                |
| DILIGEN_USERNAME    							  | String  | Diligen username  						   |                                |
| DILIGEN_PASSWORD              				  | String  | Diligen password                             |                                |
| CLAMAV_HOST              						  | String  | Clamav host                                  | defaulted to `localhost`       |
| CLAMAV_PORT    								  | Integer | Clamav port                                  | defaulted to `true`            |
| CLAMAV_TIMEOUT               					  | Integer | Clamav timeout                               | defaulted to `50000`           |
| MAIL_SEND_BASE_URL    						  | String  | base url of mailit api                       | defaulted to `/localhost:8090` |
| ERROR_TO_ADDRESS               				  | String  | Error email to address                       | defaulted to `test@somewhere.co`|
| ERROR_FROM_ADDRESS   							  | String  | Error email from address                     | defaulted to `test@somewhere.co`|
| AIR_SPRING_DATA_MONGODB_AUTHENTICATION_DATABASE | String  | MongoDb auth database                        |                                |
| AIR_SPRING_DATA_MONGODB_HOST    				  | String  | MongoDb host database                        |                                |
| AIR_SPRING_DATA_MONGODB_PORT               	  | Integer | MongoDb port database                        |                                |
| AIR_SPRING_DATA_MONGODB_DATABASE                | String  | MongoDb database database                    |                                |
| AIR_SPRING_DATA_MONGODB_USERNAME 				  | String  | MongoDb username database                    |                                |
| AIR_SPRING_DATA_MONGODB_PASSWORD                | String  | MongoDb password database                    |                                |
| REDIS_HOST               						  | String  | Redis host                                   | defaulted to `localhost`       |
| REDIS_PORT    								  | Integer | Redis port                                   | defaulted to `6379`            |
| REDIS_PASSWORD               					  | String  | Redis password                               | defaulted to `admin`           |
| CSO_WEBHOOK_ENDPOINT               			  | String  | CSO extract                                  | defaulted to  mock service     |
| CSO_WEBHOOK_USERNAME               			  | String  | CSO username                                 | defaulted to  mock service     |
| CSO_WEBHOOK_PASSWORD               			  | String  | CSO password                                 | defaulted to  mock service     |
| KEYCLOAK_REALM_SERVER_URL                       | String  | The keycloak JWT issuer uri                  | not set by default             |
| KEYCLOAK_AUTH_SERVER_URL                        | String  | The keycloak auth server URL                 | not set by default             |
| KEYCLOAK_REALM                                  | String  | The keycloak realm name                      | not set by default             |
| KEYCLOAK_RESOURCE                               | String  | The keycloak resource name                   | not set by default             |
| KEYCLOAK_CREDENTIALS_SECRET                     | String  | The keycloak Credentials Secrets             | not set by default             |
| KEYCLOAK_SSL_REQUIRED                           | String  | The keycloak SSL required                    | defaulted to `external`        |
| FEATURE_USE_REDIS_QUEUE                         | Boolean | Toggle between queue and webhook features    | defaulted to `true`            |


## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
