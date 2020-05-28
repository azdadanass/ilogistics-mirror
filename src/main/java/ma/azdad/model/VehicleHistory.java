package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class VehicleHistory extends GenericHistory<Vehicle> implements Serializable {

	public VehicleHistory() {

	}

	public VehicleHistory(Date date, String status, Vehicle parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public VehicleHistory(Date date, String status, Vehicle parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

}

