package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestHistory extends GenericHistory<DeliveryRequest> implements Serializable {

	public DeliveryRequestHistory() {

	}

	public DeliveryRequestHistory(DeliveryRequest parent, User user) {
		this.date = new Date();
		this.parent = parent;
		this.user = user;
		this.status = parent.getStatus().getValue();
	}

	public DeliveryRequestHistory(DeliveryRequest parent, User user, String description) {
		this(parent, user);
		this.description = description;
	}

	public DeliveryRequestHistory(DeliveryRequest parent, User user, String description, String status) {
		this(parent, user, description);
		this.status = status;
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
