package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import ma.azdad.utils.Color;

@Entity
public class DeliveryRequestHistory extends GenericHistory<DeliveryRequest> {

	public DeliveryRequestHistory() {
	}

	public DeliveryRequestHistory(String status, User user) {
		super(status, user);
	}

	public DeliveryRequestHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public DeliveryRequestHistory(String status, User user, String description, DeliveryRequest parent) {
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
		switch (status) {
		case "Created":
			return Color.ORANGE.getBadge();
		case "Handover":
			return Color.PURPLE.getBadge();
		default:
			try {
				return DeliveryRequestStatus.getByValue(status).getBadge();
			} catch (Exception e) {
				return "badge";
			}
		}

	}

}
