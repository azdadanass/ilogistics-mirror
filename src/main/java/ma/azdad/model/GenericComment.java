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

@MappedSuperclass
public class GenericComment<A extends GenericBean> extends GenericBean implements Serializable {

	protected Date date;
	protected String title;
	protected String content;

	protected A parent;
	protected User user;

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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(columnDefinition = "TEXT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "parent_id")
	public A getParent() {
		return parent;
	}

	public void setParent(A parent) {
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
