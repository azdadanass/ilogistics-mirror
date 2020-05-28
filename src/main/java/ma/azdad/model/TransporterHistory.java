package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class TransporterHistory extends GenericHistory<Transporter> implements Serializable {

	public TransporterHistory() {

	}

	public TransporterHistory(Date date, String status, Transporter parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public TransporterHistory(Date date, String status, Transporter parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

}

