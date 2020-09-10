package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity
@SuppressWarnings("serial")
public class TeamHistory extends GenericHistory<Team> implements Serializable {

	public TeamHistory() {

	}

	public TeamHistory(Date date, String status, Team parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public TeamHistory(Date date, String status, Team parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

}
