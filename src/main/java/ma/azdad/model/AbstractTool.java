package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass

public abstract class AbstractTool implements Serializable {

	// GENERAL
	protected Integer id;
	protected String serialNumber;
	protected String partNumber;
	protected String description;
	protected String matricule;
	protected Date creationDate;
	protected String status;
	protected String state;
	protected Boolean isKit = false;
	protected String comment;
	protected Date lastAllocationDate;
	protected Date firstAllocationDate;

	// TOOL COST
	protected Integer initialAge = 0;
	protected Double purchasingValue = 0.0;

	// CAR
	protected String owner;
	protected String rentContractEngagement;
	protected Date startDate;
	protected Date endDate;

	// CARD
	protected String cardType;
	protected String plafond;

	// CAR,CARD
	protected Date validUntil;

	// CARD,TELECOM
	protected String pinNumber;

	// TELECOM
	protected String pukNumber;
	protected String imei;

	protected String supplierCompant;
	
	protected Tooltype tooltype;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "serial_number", length = 200)
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Column(name = "part_number", length = 200)
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	@Column(name = "description", columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "last_allocation_date", length = 10)
	public Date getLastAllocationDate() {
		return lastAllocationDate;
	}

	public void setLastAllocationDate(Date lastAllocationDate) {
		this.lastAllocationDate = lastAllocationDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "first_allocation_date", length = 10)
	public Date getFirstAllocationDate() {
		return firstAllocationDate;
	}

	public void setFirstAllocationDate(Date firstAllocationDate) {
		this.firstAllocationDate = firstAllocationDate;
	}

	@Column(name = "is_kit")
	public Boolean getIsKit() {
		return isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	@Column(name = "comment", columnDefinition = "TEXT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "owner", length = 45)
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "rent_contract_engagement", length = 45)
	public String getRentContractEngagement() {
		return rentContractEngagement;
	}

	public void setRentContractEngagement(String rentContractEngagement) {
		this.rentContractEngagement = rentContractEngagement;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", length = 10)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", length = 10)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "card_type", length = 45)
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@Column(name = "plafond", length = 45)
	public String getPlafond() {
		return plafond;
	}

	public void setPlafond(String plafond) {
		this.plafond = plafond;
	}

	@Column(name = "valid_until", length = 45)
	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	@Column(name = "pin_type", length = 45)
	public String getPinNumber() {
		return pinNumber;
	}

	public void setPinNumber(String pinNumber) {
		this.pinNumber = pinNumber;
	}

	@Column(name = "puk_type", length = 45)
	public String getPukNumber() {
		return pukNumber;
	}

	public void setPukNumber(String pukNumber) {
		this.pukNumber = pukNumber;
	}

	@Column(name = "imei", length = 100)
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	@Column(name = "supplier_compant", length = 255)
	public String getSupplierCompant() {
		return supplierCompant;
	}

	public void setSupplierCompant(String supplierCompant) {
		this.supplierCompant = supplierCompant;
	}

	@Column(name = "initial_age", columnDefinition = "INT(4) default '0'")
	public Integer getInitialAge() {
		return initialAge;
	}

	public void setInitialAge(Integer initialAge) {
		this.initialAge = initialAge;
	}

	@Column(name = "purchasing_value", columnDefinition = "DOUBLE default '0'")
	public Double getPurchasingValue() {
		return purchasingValue;
	}

	public void setPurchasingValue(Double purchasingValue) {
		this.purchasingValue = purchasingValue;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tooltype_id", nullable = false)
	public Tooltype getTooltype() {
		return tooltype;
	}

	public void setTooltype(Tooltype tooltype) {
		this.tooltype = tooltype;
	}
}