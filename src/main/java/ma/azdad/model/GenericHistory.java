package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@MappedSuperclass

public class GenericHistory<A extends GenericBean> extends GenericBean implements Serializable {

	protected Date date;
	protected String status;
	protected String description;
	protected A parent;
	protected User user;

	@Transient
	public String getStatusStyleClass() {
		if ("Created".equals(status))
			return "badge badge-primary";
		else if ("Edited".equals(status))
			return "badge badge-warning";
		else if ("Approved".equals(status))
			return "badge badge-success";
		else if ("Rejected".equals(status))
			return "badge badge-danger";
		else if ("Canceled".equals(status))
			return "badge badge-danger";
		return "badge badge-inverse";
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public A getParent() {
		return parent;
	}

	public void setParent(A parent) {
		this.parent = parent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
