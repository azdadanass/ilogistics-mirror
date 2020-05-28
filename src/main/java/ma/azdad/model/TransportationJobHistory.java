package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class TransportationJobHistory extends GenericHistory<TransportationJob> implements Serializable {

	public TransportationJobHistory() {

	}

	public TransportationJobHistory(Date date, String status, TransportationJob parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public TransportationJobHistory(Date date, String status, TransportationJob parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

}
