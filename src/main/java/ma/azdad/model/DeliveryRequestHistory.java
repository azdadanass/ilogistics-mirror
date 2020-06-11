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

	public DeliveryRequestHistory(Date date, String status, DeliveryRequest parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

	@Override
	@Transient
	public String getStatusStyleClass() {
		switch (status) {
		case "Created":
			return "badge badge-inverse";
		case "Edited":
			return "badge badge-warning";
		case "Requested":
			return "badge badge-pink";
		case "Approved L1":
			return "badge badge-success";
		case "Approved L2":
			return "badge badge-success";
		case "Partially Delivered":
			return "badge badge-info";
		case "Delivered":
			return "badge badge-primary";
		case "Acknowledged":
			return "badge badge-purple";
		case "Rejected":
			return "badge badge-danger";
		case "Canceled":
			return "badge badge-danger";
		default:
			return "badge badge-inverse";
		}
	}
}
