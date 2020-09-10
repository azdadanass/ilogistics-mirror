package ma.azdad.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.utils.Color;

@Entity

public class UserHistory extends GenericBeanOld {

	private Date date;
	private String status;
	private String description;
	private User user;
	private User parent;

	public UserHistory() {
	}

	public UserHistory(String status, User user, String description) {
		this.date = new Date();
		this.status = status;
		this.description = description;
		this.user = user;
	}

	@Transient
	public String getStatusStyleClass() {
		switch (status) {
		case "Created":
			return Color.BLUE.getBadge();
		case "Edited":
			return Color.ORANGE.getBadge();
		case "Approved":
			return Color.GREEN.getBadge();
		case "Rejected":
		case "Canceled":
			return Color.RED.getBadge();
		default:
			return Color.GREEN.getBadge();
		}
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

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getParent() {
		return parent;
	}

	public void setParent(User parent) {
		this.parent = parent;
	}

}