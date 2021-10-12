package ca.bc.gov.open.jag.aireviewerapi.cso.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CSOResult {

    @JsonProperty("is_valid")
    private String success;
    @JsonProperty("package_id")
    private BigDecimal packageId;

    public String getSuccess() {  return success; }

    public void setSuccess(String success) { this.success = success; }

    public BigDecimal getPackageId() { return packageId;  }

    public void setPackageId(BigDecimal packageId) { this.packageId = packageId; }

}
