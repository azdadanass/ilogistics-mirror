package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.utils.Color;

@MappedSuperclass
@SuppressWarnings("rawtypes")
public class GenericHistory<M extends GenericModel> extends GenericModel<Integer> implements Serializable {

	protected Date date = new Date();
	protected String status;
	protected String description;

	protected M parent;
	protected User user;

	public GenericHistory() {
	}

	public GenericHistory(String status, User user) {
		this.status = status;
		this.user = user;
	}

	public GenericHistory(String status, User user, String description) {
		this(status, user);
		this.description = description;
	}

	public GenericHistory(String status, User user, String description, M parent) {
		this(status, user, description);
		this.parent = parent;
	}

	@Transient
	public String getStatusStyleClass() {
		return Color.GREY.getBadge();
	}

	@Transient
	public String getUserFullName() {
		if (user != null)
			return user.getFullName();
		return null;
	}

	@Transient
	public String getUserPhoto() {
		if (user != null)
			return user.getPhoto();
		return null;
	}

	@Transient
	public String getUserPhone() {
		if (user != null)
			return user.getPhone();
		return null;
	}

	@Transient
	public String getUserEmail() {
		if (user != null)
			return user.getEmail();
		return null;
	}

	@Transient
	public String getUserJob() {
		if (user != null)
			return user.getJob();
		return null;
	}

	@Transient
	public String getFullName() {
		if (user != null)
			return user.getFullName();
		return null;
	}

	@Transient
	public String getPhoto() {
		if (user != null)
			return user.getPhoto();
		return null;
	}

	@Transient
	public String getPhone() {
		if (user != null)
			return user.getPhone();
		return null;
	}

	@Transient
	public String getEmail() {
		if (user != null)
			return user.getEmail();
		return null;
	}

	@Transient
	public String getJob() {
		if (user != null)
			return user.getJob();
		return null;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "parent_id")
	public M getParent() {
		return parent;
	}

	public void setParent(M parent) {
		this.parent = parent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_username")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
