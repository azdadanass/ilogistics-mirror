package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class UserVehicle extends GenericModel<Integer> {

	private Boolean active = true;

	private User user;
	private Vehicle vehicle;

	public boolean filter(String query) {
		return contains(query, getUserFullName());
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	@Transient
	public String getUserUsername() {
		return user != null ? user.getUsername() : null;
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null || !userUsername.equals(user.getUsername()))
			user = new User();
		user.setUsername(userUsername);
	}

	@Transient
	public String getUserFullName() {
		return user != null ? user.getFullName() : null;
	}

	@Transient
	public void setUserFullName(String userFullName) {
		if (user == null)
			user = new User();
		user.setFullName(userFullName);
	}

	@Transient
	public String getUserPhoto() {
		return user != null ? user.getPhoto() : null;
	}

	@Transient
	public void setUserPhoto(String userPhoto) {
		if (user == null)
			user = new User();
		user.setPhoto(userPhoto);
	}

	@Transient
	public String getUserPhone() {
		return user != null ? user.getPhone() : null;
	}

	@Transient
	public void setUserPhone(String userPhone) {
		if (user == null)
			user = new User();
		user.setPhone(userPhone);
	}

	@Transient
	public Integer getVehicleId() {
		return vehicle != null ? vehicle.getId() : null;
	}

	@Transient
	public void setVehicleId(Integer vehicleId) {
		if (vehicle == null || !vehicleId.equals(vehicle.getId()))
			vehicle = new Vehicle();
		vehicle.setId(vehicleId);
	}

	@Transient
	public String getVehicleMatricule() {
		return vehicle != null ? vehicle.getCorrectMatricule() : null;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

}
