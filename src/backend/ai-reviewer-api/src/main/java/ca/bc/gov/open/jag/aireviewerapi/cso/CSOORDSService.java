package ca.bc.gov.open.jag.aireviewerapi.cso;

import ca.bc.gov.open.jag.aireviewerapi.extract.models.ExtractResponse;


public interface CSOORDSService {
    /**
     * Send the extracted data back to parent
     * @param extractResponse data extracted from diligen
     */
    void sendExtractedData(ExtractResponse extractResponse);

}
