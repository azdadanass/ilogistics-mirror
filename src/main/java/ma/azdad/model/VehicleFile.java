package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VehicleFile extends GenericFile<Vehicle> {

	public VehicleFile() {
	}

	public VehicleFile(String folder, File file, String type, String name, User user) {
		super(file, type, name, user);
	}

	public VehicleFile(String folder, File file, String type, String name, User user, Vehicle parent) {
		super(file, type, name, user, parent);
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
