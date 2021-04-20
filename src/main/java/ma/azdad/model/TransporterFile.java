package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TransporterFile extends GenericFile<Transporter> {

	public TransporterFile() {
	}

	public TransporterFile(String folder, File file, String type, String name, User user) {
		super(folder, file, type, name, user);
	}

	public TransporterFile(String folder, File file, String type, String name, User user, Transporter parent) {
		super(folder, file, type, name, user, parent);
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
