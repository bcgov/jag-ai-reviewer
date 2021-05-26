package ca.bc.gov.open.jag.aireviewerapi.document.models;

import java.util.Date;

import org.springframework.data.annotation.*;

/**
 * An abstract Auditable class that auto-populates <code>createdBy</code>, <code>lastModifiedBy</code>,
 * <code>createdDate</code>, <code>modifiedDate</code>, and <code>version</code> fields. Class need only to
 * extend this class to add auditing fields to a model object.
 */
public abstract class Auditable {

	@CreatedBy
	protected String createdBy;

	@LastModifiedBy
	protected String lastModifiedBy;

	@CreatedDate
	protected Date createdDate;

	@LastModifiedDate
	protected Date modifiedDate;

	@Version
	private Integer version;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate == null ? new Date() : createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
