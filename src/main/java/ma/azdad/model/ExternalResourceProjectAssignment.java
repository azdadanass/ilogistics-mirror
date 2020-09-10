package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity

public class ExternalResourceProjectAssignment extends GenericBeanOld implements Serializable {

	private Date startDate;
	private Date endDate;

	private ExternalResource externalResource;
	private Project project;

	public void init() {
	}

	public ExternalResourceProjectAssignment() {
		super();
	}

	public ExternalResourceProjectAssignment(ExternalResource externalResource) {
		super();
		this.externalResource = externalResource;
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

	@Transient
	public Integer getProjectId() {
		if (project == null)
			return null;
		return project.getId();
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ExternalResource getExternalResource() {
		return externalResource;
	}

	public void setExternalResource(ExternalResource externalResource) {
		this.externalResource = externalResource;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
