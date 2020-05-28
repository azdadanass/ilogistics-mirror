package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class VehicleTypeHistory extends GenericHistory<VehicleType> implements Serializable {

	public VehicleTypeHistory() {

	}

	public VehicleTypeHistory(Date date, String status, VehicleType parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}

	public VehicleTypeHistory(Date date, String status, VehicleType parent, User user, String description) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
		this.description = description;
	}

}
