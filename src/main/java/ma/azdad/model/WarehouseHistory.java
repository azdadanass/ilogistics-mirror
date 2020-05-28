package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class WarehouseHistory extends GenericHistory<Warehouse> implements Serializable {

	public WarehouseHistory(){
		
	}
	
	
	public WarehouseHistory(Date date, String status, Warehouse parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}
}

