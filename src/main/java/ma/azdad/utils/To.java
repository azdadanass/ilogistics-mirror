package ma.azdad.utils;

import ma.azdad.model.ExternalResource;
import ma.azdad.model.ToNotify;
import ma.azdad.model.User;
import ma.azdad.service.UtilsFunctions;

public class To {

	private String fullName;
	private String email;
	private String phone;

	public To() {
		super();
	}

	public To(String fullName, String email) {
		super();
		this.fullName = fullName;
		this.email = email;
	}

	public To(String fullName, String email, String phone) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
	}

//	public To(UserData employe) {
//		this.fullName = employe.getFullname();
//		this.email = employe.getEmail();
//		this.phone = employe.getPhone();
//	}

	public To(User user) {
		this.fullName = user.getFullName();
		this.email = user.getEmail();
		this.phone = user.getPhone();
	}

	public To(ExternalResource externalResource) {
		this.fullName = externalResource.getFullName();
		this.email = externalResource.getEmail();
		this.phone = externalResource.getPhone();
	}

	public To(ToNotify toNotify) {
		this.fullName = toNotify.getFullName();
		this.email = toNotify.getEmail();
		this.phone = toNotify.getPhone();
	}

	public Boolean hasValidEmail() {
		return UtilsFunctions.isValidEmail(email);
	}

	public Boolean hasValidPhone() {
		return UtilsFunctions.isValidPhoneNumber(phone);
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		To other = (To) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

}
