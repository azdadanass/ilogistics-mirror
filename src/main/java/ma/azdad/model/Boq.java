package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class Boq extends GenericModel<Integer> implements Serializable {

	private Integer reference;
	private Double quantity = 1.0;
	private Double unitPrice = 0.0; // podetails.unitPrice = sum(boq.qty * boq.unitPrice)
	private Double totalPrice = 0.0; // = quantity * unitPrice
	private Double totalQuantity = 0.0; // = quantity * podetails.unit
	private Double totalUsedQuantity = 0.0; // = sum(qty) from BoqMapping where boq.id = this.id

	private Podetails podetails;
	private PartNumber partNumber;

	// tmp
	private Boolean directEquivalence = true;

	@Transient
	public Double getRemainingQuantity() {
		return getTotalQuantity() - totalUsedQuantity;
	}

	public Integer getReference() {
		return reference;
	}

	public void setReference(Integer reference) {
		this.reference = reference;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "unit_price")
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "podetails_idpodetails")
	public Podetails getPodetails() {
		return podetails;
	}

	public void setPodetails(Podetails podetails) {
		this.podetails = podetails;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "partnumber_id")
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@Transient
	public Boolean getDirectEquivalence() {
		return directEquivalence;
	}

	@Transient
	public void setDirectEquivalence(Boolean directEquivalence) {
		this.directEquivalence = directEquivalence;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Double getTotalUsedQuantity() {
		return totalUsedQuantity;
	}

	public void setTotalUsedQuantity(Double totalUsedQuantity) {
		this.totalUsedQuantity = totalUsedQuantity;
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
