package ma.azdad.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TransportationJobComment extends GenericComment<TransportationJob> {

	public TransportationJobComment() {
	}

	public TransportationJobComment(String title, User user) {
		super(title, user);
	}

	public TransportationJobComment(String title, User user, TransportationJob parent) {
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

