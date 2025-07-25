package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class PartNumberEquivalenceDetail extends GenericModel<Integer> implements Serializable {

	private Integer quantity = 1;

	private PartNumber partNumber;
	private PartNumberEquivalence parent;

	public PartNumberEquivalenceDetail() {
		super();
	}

	public PartNumberEquivalenceDetail(PartNumberEquivalence parent) {
		super();
		this.parent = parent;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		// if (!result && name != null)
		// result = name.toLowerCase().contains(query);
		return result;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumberEquivalence getParent() {
		return parent;
	}

	public void setParent(PartNumberEquivalence parent) {
		this.parent = parent;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
