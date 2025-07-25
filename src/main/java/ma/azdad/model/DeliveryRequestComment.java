package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeliveryRequestComment extends GenericComment<DeliveryRequest> {

	private Boolean isOk;
	private String type;

	public DeliveryRequestComment() {
	}

	public DeliveryRequestComment(String title, User user) {
		super(title, user);
	}

	public DeliveryRequestComment(String title, User user, DeliveryRequest parent) {
		super(title, user, parent);
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

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
