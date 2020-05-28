package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity

public class TransportationRequestHistory extends GenericHistory<TransportationRequest> implements Serializable {

	public TransportationRequestHistory() {

	}

	public TransportationRequestHistory(Date date, String status, TransportationRequest parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public TransportationRequestHistory(Date date, String status, TransportationRequest parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}
	
	
	@Override
	@Transient
	public String getStatusStyleClass() {
		if ("Requested".equals(status))
			return "badge badge-primary";
		else if ("Assigned".equals(status))
			return "badge badge-info";
		else if ("Assigned".equals(status))
			return "badge badge-pink";
		else if ("Delivred".equals(status))
			return "badge badge-success";
		else if ("Acknowledged".equals(status))
			return "badge badge-purple";
		
		
		
		return super.getStatusStyleClass();
	}

}

