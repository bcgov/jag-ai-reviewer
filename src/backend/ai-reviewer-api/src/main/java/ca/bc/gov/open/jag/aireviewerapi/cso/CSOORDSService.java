package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface CSOORDSService {
    void sendDocumentReady(ExtractResponse extractResponse);
}
