package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity

public class PartNumberHistory extends GenericHistory<PartNumber> implements Serializable {

	public PartNumberHistory() {

	}

	public PartNumberHistory(Date date, String status, PartNumber parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public PartNumberHistory(Date date, String status, String description, PartNumber parent, User user) {
		this.date = date;
		this.status = status;
		this.description = description;
		this.parent = parent;
		this.user = user;
	}

	@Override
	@Transient
	public String getStatusStyleClass() {
		if ("Created".equals(status))
			return "badge-primary";
		else if ("Edited".equals(status))
			return "badge-warning";
		return "badge-inverse";
	}

}
