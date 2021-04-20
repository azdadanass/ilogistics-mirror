package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class TransportationRequestHistory extends GenericHistory<TransportationRequest> {

	public TransportationRequestHistory() {
	}

	public TransportationRequestHistory(String status, User user) {
		super(status, user);
	}

	public TransportationRequestHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public TransportationRequestHistory(String status, User user, String description, TransportationRequest parent) {
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

	@Override
	@Transient
	public String getStatusStyleClass() {
		try {
			return TransportationRequestStatus.getByValue(status).getBadge();
		} catch (Exception e) {
			return "badge";
		}
	}

}
