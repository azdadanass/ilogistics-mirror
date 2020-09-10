package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity

public class StockRow extends GenericBeanOld implements Serializable {

	private Double quantity;
	private Double unitCost;
	private Double unitPrice;
	private Date creationDate;

	private StockRowStatus status = StockRowStatus.NORMAL;
	private String originNumber;

	private PartNumber partNumber;
	private DeliveryRequest deliveryRequest;
	private DeliveryRequest inboundDeliveryRequest;
	private Location location;
	private Packing packing;

	// TMP
	private Double tmpQuantity;
	private Date inboundDeliveryRequestDeliveryDate;
	private Boolean initial = false;
	private Double qTotalCost;
	private Double qTotalPrice;
	private String tmpProjectName;
	private Integer tmpNumberOfOutbounds;
	private Integer tmpNumberOfCustomers;

	private Double inboundQuantity;
	private Double outboundQuantity;
	private Location newLocation;

	private Double pendingQuantity;

	private String deliverToCompany;

	public StockRow(Double quantity, StockRowStatus status, String partNumberName, DeliveryRequestType deliveryRequestType, Integer deliveryRequestReferenceNumber, String originNumber) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.setPartNumberName(partNumberName);
		this.setDeliveryRequestType(deliveryRequestType);
		this.setDeliveryRequestReferenceNumber(deliveryRequestReferenceNumber);
		this.originNumber = originNumber;

	}

	public StockRow(PartNumber partNumber, String tmpProjectName, Double inboundQuantity, Double outboundQuantity) {
		super();
		this.partNumber = partNumber;
		this.tmpProjectName = tmpProjectName;
		this.inboundQuantity = inboundQuantity;
		this.outboundQuantity = outboundQuantity;
	}

	public StockRow(Double quantity, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
	}

	public StockRow(Double quantity, PartNumber partNumber, StockRowStatus status, Date inboundDeliveryRequestDeliveryDate, Double qTotalCost) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.status = status;
		this.inboundDeliveryRequestDeliveryDate = inboundDeliveryRequestDeliveryDate;
		this.qTotalCost = qTotalCost;
	}

	// public StockRow(Double quantity, StockRowStatus status, DeliveryRequest
	// deliveryRequest, DeliveryRequest inboundDeliveryRequest, PartNumber
	// partNumber,Double qTotalCost) {
	// super();
	// this.quantity = quantity;
	// this.status = status;
	// this.deliveryRequest = deliveryRequest;
	// this.partNumber = partNumber;
	// this.inboundDeliveryRequest = inboundDeliveryRequest;
	// this.qTotalCost = qTotalCost;
	// }

	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inboundDeliveryRequest, PartNumber partNumber, Double qTotalCost, String internalCompany, String externalCompanyCustomer, String externalCompanySupplier, String externalCompany) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.qTotalCost = qTotalCost;
		this.deliverToCompany = internalCompany != null ? internalCompany : externalCompanyCustomer != null ? externalCompanyCustomer : externalCompanySupplier != null ? externalCompanySupplier : externalCompany;
	}

	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, Location location) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.location = location;
	}

	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
	}

	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inboundDeliveryRequest, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	public StockRow(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
	}

	public StockRow(Double quantity, DeliveryRequest inboundDeliveryRequest, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	public StockRow(Double quantity, PartNumber partNumber, Double inboundQuantity, Double outboundQuantity) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.inboundQuantity = inboundQuantity;
		this.outboundQuantity = outboundQuantity;
	}

	public StockRow(Double quantity, Date creationDate, String originNumber, PartNumber partNumber, DeliveryRequest deliveryRequest, Double unitCost, Packing packing) {
		super();
		this.quantity = quantity;
		this.creationDate = creationDate;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	public StockRow(Integer id, Double quantity, StockRowStatus status, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Location location) {
		super(id);
		this.quantity = quantity;
		this.status = status;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && originNumber != null)
			result = originNumber.toLowerCase().contains(query);
		if (!result && partNumber != null)
			result = partNumber.getName().toLowerCase().contains(query);
		if (!result && partNumber != null)
			result = partNumber.getDescription().toLowerCase().contains(query);
		if (!result && partNumber != null && partNumber.getIndustryName() != null)
			result = partNumber.getIndustryName().toLowerCase().contains(query);
		if (!result && partNumber != null && partNumber.getCategoryName() != null)
			result = partNumber.getCategoryName().toLowerCase().contains(query);
		if (!result && partNumber != null && partNumber.getTypeName() != null)
			result = partNumber.getTypeName().toLowerCase().contains(query);
		if (!result && deliveryRequest != null)
			result = deliveryRequest.getReference().toLowerCase().contains(query);
		if (!result && deliveryRequest != null && deliveryRequest.getWarehouse() != null)
			result = deliveryRequest.getWarehouse().getName().toLowerCase().contains(query);
		if (!result && deliveryRequest != null && deliveryRequest.getProject() != null)
			result = deliveryRequest.getProject().getName().toLowerCase().contains(query);
		return result;
	}

	public StockRow() {
		super();
		this.creationDate = new Date();
	}

	public StockRow(Double quantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Location location, Packing packing) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	public StockRow(Double quantity, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Double unitPrice, Location location, Date creationDate, Packing packing) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.deliveryRequest = deliveryRequest;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
		this.creationDate = creationDate;
		this.packing = packing;
	}

	public StockRow(Double quantity, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Location location) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.deliveryRequest = deliveryRequest;
		this.unitCost = unitCost;
	}

	public StockRow(Double quantity, Double tmpQuantity, PartNumber partNumber, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.tmpQuantity = tmpQuantity;
		this.status = status;
		this.creationDate = new Date();
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	public StockRow(Double quantity, Double tmpQuantity, PartNumber partNumber, DeliveryRequest deliveryRequest, Boolean initial, String originNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Date creationDate, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.tmpQuantity = tmpQuantity;
		this.initial = initial;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.creationDate = creationDate;
		this.packing = packing;
	}

	@Transient
	public String getKey() {
		return getKey(status, originNumber, partNumber.getId(), inboundDeliveryRequest);
	}

	public static String getKey(StockRowStatus status, String originNumber, Integer partNumberId, DeliveryRequest inboundDeliveryRequest) {
		Integer statusOrdinal = status != null ? status.ordinal() : 0;
		Integer inboundDeliveryRequestId = inboundDeliveryRequest != null ? inboundDeliveryRequest.getId() : 0;
		return getKey(statusOrdinal, originNumber, partNumberId, inboundDeliveryRequestId);
	}

	public static String getKey(Integer statusOrdinal, String originNumber, Integer partNumberId, Integer inboundDeliveryRequestId) {
		Integer originNumberHashCode = originNumber != null ? originNumber.hashCode() : 0;
		return statusOrdinal + ";" + originNumberHashCode + ";" + partNumberId + ";" + inboundDeliveryRequestId;
	}

	@Transient
	public String getDeliveryRequestReference() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getReference();
	}

	@Transient
	public Double getAvailableQuantity() {
		return quantity - pendingQuantity;
	}

	@Transient
	public Long getOverdue() {
		if (inboundDeliveryRequest == null || inboundDeliveryRequest.getDate4() == null)
			return null;
		return UtilsFunctions.getDateDifference(new Date(), inboundDeliveryRequest.getDate4()) - inboundDeliveryRequest.getApproximativeStoragePeriod();
	}

	@Transient
	public Long getInStockDays() {
		return UtilsFunctions.getDateDifference(new Date(), inboundDeliveryRequestDeliveryDate);
	}

	@Transient
	public Double getTotalCost() {
		try {
			return quantity * unitCost;
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Transient
	public Double getTotalPrice() {
		try {
			return quantity * unitPrice;
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public DeliveryRequest getInboundDeliveryRequest() {
		return inboundDeliveryRequest;
	}

	public void setInboundDeliveryRequest(DeliveryRequest inboundDeliveryRequest) {
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	@Transient
	public Double getTmpQuantity() {
		return tmpQuantity;
	}

	@Transient
	public void setTmpQuantity(Double tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	@Transient
	public Boolean getInitial() {
		return initial;
	}

	@Transient
	public void setInitial(Boolean initial) {
		this.initial = initial;
	}

	@Column(nullable = true)
	@Enumerated(EnumType.ORDINAL)
	public StockRowStatus getStatus() {
		return status;
	}

	public void setStatus(StockRowStatus status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "StockRow [quantity=" + quantity + ", partNumber=" + partNumber + "]\n";
	}

	public String getOriginNumber() {
		return originNumber;
	}

	public void setOriginNumber(String originNumber) {
		this.originNumber = originNumber;
	}

	@Transient
	public Double getInboundQuantity() {
		return inboundQuantity;
	}

	@Transient
	public Double getOutboundQuantity() {
		return outboundQuantity;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Transient
	public Location getNewLocation() {
		return newLocation;
	}

	@Transient
	public void setNewLocation(Location newLocation) {
		this.newLocation = newLocation;
	}

	@Transient
	public Date getInboundDeliveryRequestDeliveryDate() {
		return inboundDeliveryRequestDeliveryDate;
	}

	@Transient
	public void setInboundDeliveryRequestDeliveryDate(Date inboundDeliveryRequestDeliveryDate) {
		this.inboundDeliveryRequestDeliveryDate = inboundDeliveryRequestDeliveryDate;
	}

	@Transient
	public Double getqTotalCost() {
		return qTotalCost;
	}

	@Transient
	public String getTmpProjectName() {
		return tmpProjectName;
	}

	@Transient
	public void setTmpProjectName(String tmpProjectName) {
		this.tmpProjectName = tmpProjectName;
	}

	@Transient
	public void setInboundQuantity(Double inboundQuantity) {
		this.inboundQuantity = inboundQuantity;
	}

	@Transient
	public void setOutboundQuantity(Double outboundQuantity) {
		this.outboundQuantity = outboundQuantity;
	}

	@Transient
	public Integer getTmpNumberOfOutbounds() {
		return tmpNumberOfOutbounds;
	}

	@Transient
	public void setTmpNumberOfOutbounds(Integer tmpNumberOfOutbounds) {
		this.tmpNumberOfOutbounds = tmpNumberOfOutbounds;
	}

	@Transient
	public Integer getTmpNumberOfCustomers() {
		return tmpNumberOfCustomers;
	}

	@Transient
	public void setTmpNumberOfCustomers(Integer tmpNumberOfCustomers) {
		this.tmpNumberOfCustomers = tmpNumberOfCustomers;
	}

	@Transient
	public Double getPendingQuantity() {
		return pendingQuantity;
	}

	@Transient
	public void setPendingQuantity(Double pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	@Transient
	public Double getqTotalPrice() {
		return qTotalPrice;
	}

	@Transient
	public void setqTotalCost(Double qTotalCost) {
		this.qTotalCost = qTotalCost;
	}

	@Transient
	public void setqTotalPrice(Double qTotalPrice) {
		this.qTotalPrice = qTotalPrice;
	}

	@Transient
	public String getDeliverToCompany() {
		return deliverToCompany;
	}

	@Transient
	public String getPartNumberName() {
		if (partNumber == null)
			return null;
		return partNumber.getName();
	}

	@Transient
	public void setPartNumberName(String name) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(name);
	}

	@Transient
	public DeliveryRequestType getDeliveryRequestType() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getType();
	}

	@Transient
	public void setDeliveryRequestType(DeliveryRequestType deliveryRequestType) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setType(deliveryRequestType);
	}

	@Transient
	public Integer getDeliveryRequestReferenceNumber() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getReferenceNumber();
	}

	@Transient
	public void setDeliveryRequestReferenceNumber(Integer deliveryRequestReferenceNumber) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setReferenceNumber(deliveryRequestReferenceNumber);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

}
