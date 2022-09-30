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
	private Double deliveredQuantity = 0.0; // DN where status in ('DELIVRED','ACKNOWLEDGED') ps : case P.DELIVRED ignored

	public Boq() {
		super();
	}

	// c1
	public Boq(Integer id, Integer reference, Double quantity, Double unitPrice, Double totalPrice, Double totalQuantity, Double totalUsedQuantity, Integer poDetailReference,
			String poDetailDescription, Double poDetailQuantity, String partNumberName, String partNumberDescription, String partNumberImage, Double deliveredQuantity) {
		super(id);
		this.reference = reference;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
		this.totalQuantity = totalQuantity;
		this.totalUsedQuantity = totalUsedQuantity;
		this.setPoDetailReference(poDetailReference);
		this.setPoDetailDescription(poDetailDescription);
		this.setPoDetailQuantity(poDetailQuantity);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberImage(partNumberImage);
		this.deliveredQuantity = deliveredQuantity;
	}

	// c2
	public Boq(Double totalQuantity, Double deliveredQuantity, String partNumberName, String partNumberDescription, String partNumberImage) {
		this.totalQuantity = totalQuantity;
		this.deliveredQuantity = deliveredQuantity;
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberImage(partNumberImage);
	}

	// c3
	public Boq(String partNumberName, String partNumberDescription, String partNumberImage,Double totalQuantity, Double totalUsedQuantity) {
		this.totalQuantity = totalQuantity;
		this.totalUsedQuantity = totalUsedQuantity;
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberImage(partNumberImage);
	}

	@Transient
	public Integer getPartNumberId() {
		return partNumber != null ? partNumber.getId() : null;
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		if (partNumber == null || !partNumberId.equals(partNumber.getId()))
			partNumber = new PartNumber();
		partNumber.setId(partNumberId);
	}

	@Transient
	public Integer getPoDetailReference() {
		if (podetails == null)
			return null;
		return podetails.getReference();
	}

	@Transient
	public void setPoDetailReference(Integer poDetailReference) {
		if (podetails == null)
			podetails = new Podetails();
		podetails.setReference(poDetailReference);
	}

	@Transient
	public Double getPoDetailQuantity() {
		if (podetails == null)
			return null;
		return podetails.getUnit();
	}

	@Transient
	public void setPoDetailQuantity(Double poDetailQuantity) {
		if (podetails == null)
			podetails = new Podetails();
		podetails.setUnit(poDetailQuantity);
	}

	@Transient
	public String getPoDetailDescription() {
		if (podetails == null)
			return null;
		return podetails.getDescription();
	}

	@Transient
	public void setPoDetailDescription(String poDetailDescription) {
		if (podetails == null)
			podetails = new Podetails();
		podetails.setDescription(poDetailDescription);
	}

	@Transient
	public String getPartNumberName() {
		if (partNumber == null)
			return null;
		return partNumber.getName();
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}

	@Transient
	public String getPartNumberImage() {
		if (partNumber == null)
			return null;
		return partNumber.getImage();
	}

	@Transient
	public void setPartNumberImage(String partNumberImage) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setImage(partNumberImage);
	}

	@Transient
	public String getPartNumberDescription() {
		if (partNumber == null)
			return null;
		return partNumber.getDescription();
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
	}

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

	@ManyToOne(fetch = FetchType.EAGER)
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

	@Transient
	public Double getDeliveredQuantity() {
		return deliveredQuantity;
	}

	@Transient
	public void setDeliveredQuantity(Double deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}

}
