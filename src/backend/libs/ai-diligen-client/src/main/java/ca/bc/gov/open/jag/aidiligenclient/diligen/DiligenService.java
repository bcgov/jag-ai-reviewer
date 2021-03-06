package ca.bc.gov.open.jag.aidiligenclient.diligen;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenDocumentDetails;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface DiligenService {
    BigDecimal postDocument(String documentType, MultipartFile file, Integer projectIdentifier);

    DiligenDocumentDetails getDocumentDetails(BigDecimal documentId);

    void deleteDocument(BigDecimal documentId);

}
