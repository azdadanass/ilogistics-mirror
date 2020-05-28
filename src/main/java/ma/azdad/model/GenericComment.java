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

public class GenericComment<A extends GenericBean> extends GenericBean implements Serializable {

	protected Date date;
	protected String title;
	protected String content;

	protected A parent;
	protected User user;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column
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
	public A getParent() {
		return parent;
	}

	public void setParent(A parent) {
		this.parent = parent;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "GenericComment [title=" + title + ", content=" + content + "]\n";
	}

}
