package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity

public class StockRow extends GenericModel<Integer> implements Serializable {

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

	private String deliverToEntityName;

	// c0
	public StockRow(Double quantity, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
	}

	// c1
	public StockRow(Integer id, Double quantity, StockRowStatus status, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Location location) {
		super(id);
		this.quantity = quantity;
		this.status = status;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
	}

	// c2
	public StockRow(Double quantity, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, //
			DeliveryRequest inboundDeliveryRequest, Double unitCost, Location location, Packing packing) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.originNumber = originNumber;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	// c3
	public StockRow(Double quantity, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, //
			DeliveryRequest inboundDeliveryRequest, Double unitCost, Location location) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.originNumber = originNumber;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.deliveryRequest = deliveryRequest;
		this.unitCost = unitCost;
	}

	// c4
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, //
			Double inboundQuantity, Double outboundQuantity) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.inboundQuantity = inboundQuantity;
		this.outboundQuantity = outboundQuantity;
	}

	// c5 & c13
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, //
			DeliveryRequest deliveryRequest) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.deliveryRequest = deliveryRequest;
	}

	// c6
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
	}

	// c7
	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, Location location) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.location = location;
	}

	// c8
	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
	}

	// c9
	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inboundDeliveryRequest, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	// c10
	public StockRow(Double quantity, DeliveryRequest inboundDeliveryRequest, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	// c12
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, //
			StockRowStatus status, Date inboundDeliveryRequestDeliveryDate, Double qTotalCost) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.status = status;
		this.inboundDeliveryRequestDeliveryDate = inboundDeliveryRequestDeliveryDate;
		this.qTotalCost = qTotalCost;
	}

	// c15
	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inboundDeliveryRequest, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, //
			Double qTotalCost, String deliverToCompany, String deliverToCustomer, String deliverToSupplier, String deliverToOther) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.qTotalCost = qTotalCost;
		this.deliverToEntityName = deliverToCompany != null ? deliverToCompany : deliverToCustomer != null ? deliverToCustomer : deliverToSupplier != null ? deliverToSupplier : deliverToOther;
	}

	// c16
	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inbouDeliveryRequest) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.deliveryRequest = deliveryRequest;
		this.inboundDeliveryRequest = inbouDeliveryRequest;
	}

	// c17
	public StockRow(PartNumber partNumber, String tmpProjectName, Double inboundQuantity, Double outboundQuantity) {
		super();
		this.partNumber = partNumber;
		this.tmpProjectName = tmpProjectName;
		this.inboundQuantity = inboundQuantity;
		this.outboundQuantity = outboundQuantity;
	}

	// c18
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

	// c19
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

	// c20
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

	// c21
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

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	public StockRow(Double quantity, StockRowStatus status, String partNumberName, DeliveryRequestType deliveryRequestType, Integer deliveryRequestReferenceNumber, String originNumber) {
//		super();
//		this.quantity = quantity;
//		this.status = status;
//		this.setPartNumberName(partNumberName);
//		this.setDeliveryRequestType(deliveryRequestType);
//		this.setDeliveryRequestReferenceNumber(deliveryRequestReferenceNumber);
//		this.originNumber = originNumber;
//
//	}

//	public StockRow(Double quantity, PartNumber partNumber, StockRowStatus status, Date inboundDeliveryRequestDeliveryDate, Double qTotalCost) {
//		super();
//		this.quantity = quantity;
//		this.partNumber = partNumber;
//		this.status = status;
//		this.inboundDeliveryRequestDeliveryDate = inboundDeliveryRequestDeliveryDate;
//		this.qTotalCost = qTotalCost;
//	}

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

//	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inboundDeliveryRequest, PartNumber partNumber, Double qTotalCost, String deliverToCompany, String deliverToCustomer, String deliverToSupplier, String deliverToOther) {
//		super();
//		this.quantity = quantity;
//		this.status = status;
//		this.deliveryRequest = deliveryRequest;
//		this.partNumber = partNumber;
//		this.inboundDeliveryRequest = inboundDeliveryRequest;
//		this.qTotalCost = qTotalCost;
//		this.deliverToEntityName = deliverToCompany != null ? deliverToCompany : deliverToCustomer != null ? deliverToCustomer : deliverToSupplier != null ? deliverToSupplier : deliverToOther;
//	}

//	public StockRow(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest) {
//		super();
//		this.quantity = quantity;
//		this.partNumber = partNumber;
//		this.deliveryRequest = deliveryRequest;
//	}

//	public StockRow(Double quantity, DeliveryRequest inboundDeliveryRequest, PartNumber partNumber) {
//		super();
//		this.quantity = quantity;
//		this.partNumber = partNumber;
//		this.inboundDeliveryRequest = inboundDeliveryRequest;
//	}

//	public StockRow(Double quantity, PartNumber partNumber, Double inboundQuantity, Double outboundQuantity) {
//		super();
//		this.quantity = quantity;
//		this.partNumber = partNumber;
//		this.inboundQuantity = inboundQuantity;
//		this.outboundQuantity = outboundQuantity;
//	}

	@Override
	public boolean filter(String query) {
		return contains(query, originNumber, getPartNumberName(), getPartNumberDescription(), getDeliveryRequestReference(), getInboundDeliveryRequestReference() //
				, getPartNumberIndustryName(), getPartNumberCategoryName(), getPartNumberTypeName(), getPartNumberBrandName(), getWarehouseName(), getProjectName());
	}

	public StockRow() {
		super();
		this.creationDate = new Date();
	}

//	public StockRow(Double quantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Location location, Packing packing) {
//		super();
//		this.quantity = quantity;
//		this.status = status;
//		this.originNumber = originNumber;
//		this.partNumber = partNumber;
//		this.inboundDeliveryRequest = inboundDeliveryRequest;
//		this.location = location;
//		this.unitCost = unitCost;
//		this.packing = packing;
//	}

//	public StockRow(Double quantity, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Location location) {
//		super();
//		this.quantity = quantity;
//		this.status = status;
//		this.originNumber = originNumber;
//		this.partNumber = partNumber;
//		this.inboundDeliveryRequest = inboundDeliveryRequest;
//		this.location = location;
//		this.deliveryRequest = deliveryRequest;
//		this.unitCost = unitCost;
//	}

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
	@Enumerated(EnumType.STRING)
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
	public String getDeliverToEntityName() {
		return deliverToEntityName;
	}

	@Transient
	public String getWarehouseName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getWarehouseName();
	}

	@Transient
	public String getProjectName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getProjectName();
	}

	@Transient
	public Integer getDeliveryRequestId() {
		return deliveryRequest != null ? deliveryRequest.getId() : null;
	}

	@Transient
	public void setDeliveryRequestId(Integer deliveryRequestId) {
		if (deliveryRequest == null || !deliveryRequestId.equals(deliveryRequest.getId()))
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setId(deliveryRequestId);
	}

	@Transient
	public Integer getInboundDeliveryRequestId() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getId() : null;
	}

	@Transient
	public void setInboundDeliveryRequestId(Integer inboundDeliveryRequestId) {
		if (inboundDeliveryRequest == null || !inboundDeliveryRequestId.equals(deliveryRequest.getId()))
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setId(inboundDeliveryRequestId);
	}

	@Transient
	public String getInboundDeliveryRequestReference() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getReference() : null;
	}

	@Transient
	public void setInboundDeliveryRequestReference(String inboundDeliveryRequestReference) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setReference(inboundDeliveryRequestReference);
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public Integer getPartNumberId() {
		return partNumber == null ? null : partNumber.getId();
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		if (partNumber == null || !partNumberId.equals(partNumber.getId()))
			partNumber = new PartNumber();
		partNumber.setId(partNumberId);
	}

	@Transient
	public String getPartNumberName() {
		return partNumber == null ? null : partNumber.getName();
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}

	@Transient
	public String getPartNumberIndustryName() {
		return partNumber == null ? null : partNumber.getIndustryName();
	}

	@Transient
	public void setPartNumberIndustryName(String partNumberIndustryName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setIndustryName(partNumberIndustryName);
	}

	@Transient
	public String getPartNumberCategoryName() {
		return partNumber == null ? null : partNumber.getCategoryName();
	}

	@Transient
	public void setPartNumberCategoryName(String partNumberCategoryName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setCategoryName(partNumberCategoryName);
	}

	@Transient
	public String getPartNumberTypeName() {
		return partNumber == null ? null : partNumber.getTypeName();
	}

	@Transient
	public void setPartNumberTypeName(String partNumberTypeName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setTypeName(partNumberTypeName);
	}

	@Transient
	public String getPartNumberBrandName() {
		return partNumber == null ? null : partNumber.getBrandName();
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setBrandName(partNumberBrandName);
	}

	@Transient
	public String getPartNumberDescription() {
		return partNumber == null ? null : partNumber.getDescription();
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
	}
}
