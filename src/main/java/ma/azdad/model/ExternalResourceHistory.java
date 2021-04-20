package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExternalResourceHistory extends GenericHistory<ExternalResource> {

	public ExternalResourceHistory() {
	}

	public ExternalResourceHistory(String status, User user) {
		super(status, user);
	}

	public ExternalResourceHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public ExternalResourceHistory(String status, User user, String description, ExternalResource parent) {
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
