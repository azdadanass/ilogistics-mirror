package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VehicleTypeHistory extends GenericHistory<VehicleType> {

	public VehicleTypeHistory() {
	}

	public VehicleTypeHistory(String status, User user) {
		super(status, user);
	}

	public VehicleTypeHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public VehicleTypeHistory(String status, User user, String description, VehicleType parent) {
		super(status, user, description, parent);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
