package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;


public interface CSOORDSService {
    /**
     * Send the extracted data back to parent
     * @param processedDocument data extracted from diligen
     */
    void sendExtractedData(ProcessedDocument processedDocument);

}
