package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PartNumberPricingComment extends GenericComment<PartNumberPricing> {

	public PartNumberPricingComment() {
	}

	public PartNumberPricingComment(String title, User user) {
		super(title, user);
	}

	public PartNumberPricingComment(String title, User user, PartNumberPricing parent) {
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