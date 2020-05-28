package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tool")

public class Tool extends AbstractTool implements java.io.Serializable {

	public Tool() {
	}

	public Tool(Integer id, String matricule) {
		this.id = id;
		this.matricule = matricule;
	}
	
	public Tool(String matricule) {
		this.matricule = matricule;
	}

	@Column(name = "matricule", length = 100, unique = true, nullable = false)
	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	@Column(name = "status", length = 15, nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "state", length = 20, nullable = false)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}