package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class DeliveryRequestComment extends GenericComment<DeliveryRequest> implements Serializable {

	private Boolean isOk;
	private String type;

	public DeliveryRequestComment() {

	}

	public DeliveryRequestComment(Date date, String title, DeliveryRequest parent, User user) {
		this.date = date;
		this.title = title;
		this.parent = parent;
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
