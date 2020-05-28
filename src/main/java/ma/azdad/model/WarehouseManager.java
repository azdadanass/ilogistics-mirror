package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;

@Entity
public class WarehouseManager extends GenericBean {

	private Warehouse warehouse;
	private User user;

	@Transient
	public String getUserUsername() {
		return user == null ? null : user.getUsername();
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null || ObjectUtils.notEqual(userUsername, user.getUsername()))
			user = new User();
		user.setUsername(userUsername);
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
