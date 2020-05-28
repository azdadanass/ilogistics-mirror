package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity

public class ExternalResourceHistory extends GenericHistory<ExternalResource> implements Serializable {

	public ExternalResourceHistory(){
		
	}
	
	public ExternalResourceHistory(Date date, String status, ExternalResource parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}
	
}

