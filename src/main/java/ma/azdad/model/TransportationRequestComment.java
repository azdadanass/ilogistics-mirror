package ma.azdad.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TransportationRequestComment extends GenericComment<TransportationRequest> {

	public TransportationRequestComment() {
	}

	public TransportationRequestComment(String title, User user) {
		super(title, user);
	}

	public TransportationRequestComment(String title, User user, TransportationRequest parent) {
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

}