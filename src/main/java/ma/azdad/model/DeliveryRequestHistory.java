package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestHistory extends GenericHistory<DeliveryRequest> implements Serializable {

	public DeliveryRequestHistory() {

	}

	public DeliveryRequestHistory(Date date, String status, DeliveryRequest parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}
	
	public DeliveryRequestHistory(Date date, String status, DeliveryRequest parent, User user,String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

	@Transient
	public String getStatusStyleClass() {
		if ("Created".equals(status))
			return "badge badge-inverse";
		else if ("Edited".equals(status))
			return "badge badge-warning";
		else if ("Requested".equals(status))
			return "badge badge-pink";
		else if ("Approved".equals(status))
			return "badge badge-success";
		else if ("Partially Delivered".equals(status))
			return "badge badge-info";
		else if ("Delivred".equals(status))
			return "badge badge-primary";
		else if ("Acknowledged".equals(status))
			return "badge badge-purple";
		else if ("Rejected".equals(status))
			return "badge badge-danger";
		else if ("Canceled".equals(status))
			return "badge badge-danger";
		
		return "badge badge-inverse";
	}
}
