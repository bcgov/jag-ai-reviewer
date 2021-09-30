package ca.bc.gov.open.jag.aireviewerapi.cso.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CSOResult {

    @JsonProperty("is_valid")
    private Boolean success;
    @JsonProperty("packageId")
    private BigDecimal packageId;

    public Boolean getSuccess() {  return success; }

    public void setSuccess(Boolean success) { this.success = success; }

    public BigDecimal getPackageId() { return packageId;  }

    public void setPackageId(BigDecimal packageId) { this.packageId = packageId; }

}
