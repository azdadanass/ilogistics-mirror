package ma.azdad.mobile.model;

import java.util.Date;

import ma.azdad.utils.Public;

public class TransportationJobHistory {

	private Integer id;
	private Date date;
	private String status;
	private String description;
	private String userFullName;
	private String userPhoto;
	
	

	public TransportationJobHistory() {
		super();
	}

	public TransportationJobHistory(Integer id,Date date, String status, String description, String userFullName, String userPhoto) {
		super();
		this.id = id;
		this.date=date;
		this.status = status;
		this.description = description;
		this.userFullName = userFullName;
		this.userPhoto =  Public.getPublicUrl(userPhoto);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}

