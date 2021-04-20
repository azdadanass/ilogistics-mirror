package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class PartNumberDetail extends GenericModel<Integer> implements Serializable {

	private Integer number;
	private PartNumber partNumber;
	private Double quantity = 0.0;

	private PartNumber parent;

	// TMP
	private Integer partNumberId;

	public void init() {
		if (partNumber != null)
			partNumberId = partNumber.getId();
	}

	public PartNumberDetail() {
		super();
	}

	public PartNumberDetail(Integer number, PartNumber parent) {
		super();
		this.number = number;
		this.parent = parent;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Transient
	public Integer getPartNumberId() {
		return partNumberId;
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		this.partNumberId = partNumberId;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getParent() {
		return parent;
	}

	public void setParent(PartNumber parent) {
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
