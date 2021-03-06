package ca.bc.gov.open.jag.aireviewerapi.document.validators;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.aireviewerapi.document.models.DocumentValidation;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface DocumentValidator {

    void validateDocument(String documentType, MultipartFile file);

    DocumentValidation validateExtractedDocument(BigDecimal documentId, DocumentTypeConfiguration documentTypeConfiguration, List<DiligenAnswerField> answers, ObjectNode formData);

}
