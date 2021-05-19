package ca.bc.gov.open.jag.aireviewerapi.webhook;

import java.math.BigDecimal;

public interface WebHookService {
    void sendDocumentReady(BigDecimal documentId, String documentType);
}
