package ma.azdad.mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ma.azdad.model.Role;
import ma.azdad.service.UtilsFunctions;

public class Token {

	private String username;
	private String key;
	private Date expirationTime;

	private User user;
	private List<Role> roleList = new ArrayList<Role>();
	private List<Integer> warehouseList = new ArrayList<Integer>();

	public void updateExpirationTime() {
		expirationTime = UtilsFunctions.addMinutesToDate(new Date(), 120);
	}

	public Boolean isActive() {
		return UtilsFunctions.getCurrentDate().compareTo(expirationTime) <= 0;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Integer> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Integer> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return "Token [username=" + username + ", key=" + key + ", expirationTime=" + expirationTime + ", isActive()="
				+ isActive() + "]\n";
	}

}
