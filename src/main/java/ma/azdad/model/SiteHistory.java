package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity

public class SiteHistory extends GenericHistory<Site> implements Serializable {

	public SiteHistory(){
		
	}
	
	

	public SiteHistory(Date date, String status, Site parent, User user) {
		this.date = date;
		this.status = status;
		this.parent = parent;
		this.user = user;
	}
	
	
	
	@Transient
	public String getStatusStyleClass() {
		return super.getStatusStyleClass();
	}
}

