package ma.azdad.mobile.model;

import ma.azdad.model.Issue;
import ma.azdad.model.User;

public class ToNotify {
	
	private User internalResource;
	private Boolean notifyByEmail = true;
	private Boolean notifyBySms = true;

	
	

	public ToNotify() {
		super();
	}

	public ToNotify(User internalResource, Boolean notifyByEmail, Boolean notifyBySms, Issue issue) {
		super();
		this.internalResource = internalResource;
		this.notifyByEmail = notifyByEmail;
		this.notifyBySms = notifyBySms;
	}

	public User getInternalResource() {
		return internalResource;
	}

	public void setInternalResource(User internalResource) {
		this.internalResource = internalResource;
	}

	public Boolean getNotifyByEmail() {
		return notifyByEmail;
	}

	public void setNotifyByEmail(Boolean notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}

	public Boolean getNotifyBySms() {
		return notifyBySms;
	}

	public void setNotifyBySms(Boolean notifyBySms) {
		this.notifyBySms = notifyBySms;
	}

	

}
