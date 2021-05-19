package ca.bc.gov.open.jag.aireviewerapi.webhook;

import java.math.BigDecimal;
import java.util.UUID;

public interface WebHookService {
    void sendDocumentReady(BigDecimal documentId, String documentType, UUID transactionId);
}
