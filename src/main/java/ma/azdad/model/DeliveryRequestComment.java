package ma.azdad.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class DeliveryRequestComment extends GenericBean {

	private Date date;
	private String title;
	private String content;

	private DeliveryRequest deliveryRequest;
	private User user;

	private Boolean isOk;
	private String type;

	public DeliveryRequestComment() {
	}

	public DeliveryRequestComment(String title, String content, User user) {
		this.date = new Date();
		this.title = title;
		this.content = content;
		this.user = user;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(columnDefinition = "LONGTEXT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "parent_id")
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}