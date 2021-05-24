package ca.bc.gov.open.jag.aireviewerapi.documentconfiguration.audit;

import ca.bc.gov.open.jag.aireviewerapi.core.SecurityUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class MongoAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtils.getClientId();
    }
}
