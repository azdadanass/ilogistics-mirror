package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestDetail extends GenericModel<Integer> implements Serializable {

	private Double quantity = 0.0;
	private Double purchaseCost = 0.0;
	private Double unitCost = 0.0;
	private Double unitPrice = 0.0;

	private DeliveryRequest deliveryRequest;

	private Currency pruchaseCurrency;
	private Currency costCurrency;
	private Currency priceCurrency;

	// outbound case
	private StockRowStatus status;
	private String originNumber;

	private Packing packing;

	private PartNumber partNumber;
	private DeliveryRequest inboundDeliveryRequest;
	private DeliveryRequestDetail inboundDeliveryRequestDetail; // case outbound delivery detail

	private Double remainingQuantity;

	// TMP
	private Double tmpQuantity;
	private String tmpPartNumberName;
	private String tmpPartNumberDescription;
	private Integer tmpDeliveryRequestId;
	private DeliveryRequestType tmpDeliveryRequestType;
	private Integer tmpDeliveryRequestTypeReferenceNumber;
	private String tmpDeliveryRequestReference;
	private Double tmpUsedQuantity;
	private String tmpDeliverToFullName;
	private Date tmpDeliveryRequestDeliveryDate;
	private String tmpProjectName;

	public DeliveryRequestDetail() {
		super();
	}

	// c1
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String partNumberOrangeName, String partNumberOrangeDescription, //
			DeliveryRequest inboundDeliveryRequest, Double unitCost) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.status = status;
		this.originNumber = originNumber;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setPartNumberOrangeName(partNumberOrangeName);
		this.setPartNumberOrangeDescription(partNumberOrangeDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
	}

	// c2
	public DeliveryRequestDetail(Double remainingQuantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String partNumberOrangeName, String partNumberOrangeDescription, //
			Double unitCost) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setPartNumberOrangeName(partNumberOrangeName);
		this.setPartNumberOrangeDescription(partNumberOrangeDescription);
		this.unitCost = unitCost;
	}

	// c3
	public DeliveryRequestDetail(Integer id, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String partNumberOrangeName, String partNumberOrangeDescription, //
			Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType, String tmpDeliveryRequestReference, Double quantity, Double tmpUsedQuantity, String toUserFullName) {
		super(id);
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setPartNumberOrangeName(partNumberOrangeName);
		this.setPartNumberOrangeDescription(partNumberOrangeDescription);
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestReference = tmpDeliveryRequestReference;
		this.quantity = quantity;
		this.tmpUsedQuantity = tmpUsedQuantity;
		this.tmpDeliverToFullName = toUserFullName;
	}

	// c4
	public DeliveryRequestDetail(Integer id, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String partNumberOrangeName, String partNumberOrangeDescription, //
			Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType, String tmpDeliveryRequestReference, Double unitCost, Date tmpDeliveryRequestDeliveryDate, String tmpProjectName) {
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setPartNumberOrangeName(partNumberOrangeName);
		this.setPartNumberOrangeDescription(partNumberOrangeDescription);
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestReference = tmpDeliveryRequestReference;
		this.unitCost = unitCost;
		this.tmpDeliveryRequestDeliveryDate = tmpDeliveryRequestDeliveryDate;
		this.tmpProjectName = tmpProjectName;
	}

	// c5
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String partNumberOrangeName, String partNumberOrangeDescription, //
			DeliveryRequest inboundDeliveryRequest, Double unitCost, Packing packing) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.status = status;
		this.originNumber = originNumber;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setPartNumberOrangeName(partNumberOrangeName);
		this.setPartNumberOrangeDescription(partNumberOrangeDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	// c6 & c7
	public DeliveryRequestDetail(Double remainingQuantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String partNumberOrangeName, String partNumberOrangeDescription, //
			Double unitCost, Double unitPrice) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setPartNumberOrangeName(partNumberOrangeName);
		this.setPartNumberOrangeDescription(partNumberOrangeDescription);
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// c1
	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.packing = packing;
	}

	// c2
	public DeliveryRequestDetail(Integer id, String tmpPartNumberName, String tmpPartNumberDescription, Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType,
			Integer tmpDeliveryRequestTypeReferenceNumber, Double unitCost, Date tmpDeliveryRequestDeliveryDate, String tmpProjectName) {
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestTypeReferenceNumber = tmpDeliveryRequestTypeReferenceNumber;
		this.tmpDeliveryRequestReference = "DN" + (tmpDeliveryRequestType.ordinal() + 1) + String.format("%05d", tmpDeliveryRequestTypeReferenceNumber);
		this.unitCost = unitCost;
		this.tmpDeliveryRequestDeliveryDate = tmpDeliveryRequestDeliveryDate;
		this.tmpProjectName = tmpProjectName;
	}

	// c3
	public DeliveryRequestDetail(Integer id, String tmpPartNumberName, String tmpPartNumberDescription, Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType,
			Integer tmpDeliveryRequestTypeReferenceNumber, Double quantity, Double tmpUsedQuantity, String toUserFullName) {
		super(id);
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestTypeReferenceNumber = tmpDeliveryRequestTypeReferenceNumber;
		this.tmpDeliveryRequestReference = "DN" + (tmpDeliveryRequestType.ordinal() + 1) + String.format("%05d", tmpDeliveryRequestTypeReferenceNumber);
		this.quantity = quantity;
		this.tmpUsedQuantity = tmpUsedQuantity;
		this.tmpDeliverToFullName = toUserFullName;
	}

	// c4
	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, DeliveryRequest inboundDeliveryRequest,
			DeliveryRequestDetail inboundDeliveryRequestDetail, Double unitCost, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.status = status;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	// c5
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost,
			Packing packing) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	// c6
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
	}

	// c7
	public DeliveryRequestDetail(Double remainingQuantity, PartNumber partNumber, Double unitCost) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
		this.unitCost = unitCost;
	}

	// c8
	public DeliveryRequestDetail(Double remainingQuantity, PartNumber partNumber, Double unitCost, Double unitPrice) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
	}

	// c9
	public DeliveryRequestDetail(Double remainingQuantity, PartNumber partNumber, Double unitCost, Double unitPrice, Packing packing) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
		this.packing = packing;
	}

	@Override // if id == null compare by key
	public boolean equals(Object obj) {
		if (id != null)
			return super.equals(obj);
		try {
			return getKey().equals(((DeliveryRequestDetail) obj).getKey());
		} catch (Exception e) {
			return false;
		}
	}

	@Transient
	public Integer getDeliveryRequestId() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getId();
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
	public PartNumberOrange getPartNumberOrange() {
		return partNumber == null ? null : partNumber.getPartNumberOrange();
	}

	@Transient
	public void setPartNumberOrange(PartNumberOrange partNumberOrange) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setPartNumberOrange(partNumberOrange);
	}

	@Transient
	public String getPartNumberOrangeName() {
		return partNumber == null ? null : partNumber.getPartNumberOrangeName();
	}

	@Transient
	public void setPartNumberOrangeName(String partNumberOrangeName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setPartNumberOrangeName(partNumberOrangeName);
	}

	@Transient
	public String getPartNumberOrangeDescription() {
		return partNumber == null ? null : partNumber.getPartNumberOrangeDescription();
	}

	@Transient
	public void setPartNumberOrangeDescription(String partNumberOrangeDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setPartNumberOrangeDescription(partNumberOrangeDescription);
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

	public void init() {
	}

	public DeliveryRequestDetail(DeliveryRequest deliveryRequest) {
		super();
		this.deliveryRequest = deliveryRequest;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
	}

	public DeliveryRequestDetail(Double quantity, Double remainingQuantity, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, StockRowStatus status, String originNumber, DeliveryRequest inboundDeliveryRequest) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.status = status;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	@Transient
	public Double getNetWeight() {
		if (partNumber != null)
			return quantity * partNumber.getNetWeight();
		return 0.0;
	}

	@Transient
	public Double getGrossWeight() {
		if (packing != null)
			return (quantity / packing.getQuantity()) * packing.getGrossWeight();
		return 0.0;
	}

	@Transient
	public Double getVolume() {
		if (packing != null)
			return (quantity / packing.getQuantity()) * packing.getVolume();
		return 0.0;
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

	@Transient
	public String getKey() {
		return StockRow.getKey(status, originNumber, partNumber.getId(), inboundDeliveryRequest);
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && originNumber != null)
			result = originNumber.toLowerCase().contains(query);
		if (!result && inboundDeliveryRequest != null)
			result = inboundDeliveryRequest.getReference().toLowerCase().contains(query);
		if (!result && partNumber != null)
			result = partNumber.filter(query);
		if (!result && tmpPartNumberName != null)
			result = tmpPartNumberName.toLowerCase().contains(query);
		if (!result && tmpPartNumberDescription != null)
			result = tmpPartNumberDescription.toLowerCase().contains(query);
		if (!result && tmpDeliveryRequestReference != null)
			result = tmpDeliveryRequestReference.toLowerCase().contains(query);
		if (!result && tmpProjectName != null)
			result = tmpProjectName.toLowerCase().contains(query);

		return result;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	public Double getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(Double remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	@Transient
	public Double getTmpQuantity() {
		return tmpQuantity;
	}

	@Transient
	public void setTmpQuantity(Double tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	@Enumerated(EnumType.STRING)
	public StockRowStatus getStatus() {
		return status;
	}

	public void setStatus(StockRowStatus status) {
		this.status = status;
	}

	public String getOriginNumber() {
		return originNumber;
	}

	public void setOriginNumber(String originNumber) {
		this.originNumber = originNumber;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public DeliveryRequest getInboundDeliveryRequest() {
		return inboundDeliveryRequest;
	}

	public void setInboundDeliveryRequest(DeliveryRequest inboundDeliveryRequest) {
		this.inboundDeliveryRequest = inboundDeliveryRequest;
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
	public String getTmpPartNumberName() {
		return tmpPartNumberName;
	}

	@Transient
	public String getTmpPartNumberDescription() {
		return tmpPartNumberDescription;
	}

	@Transient
	public Integer getTmpDeliveryRequestId() {
		return tmpDeliveryRequestId;
	}

	@Transient
	public DeliveryRequestType getTmpDeliveryRequestType() {
		return tmpDeliveryRequestType;
	}

	@Transient
	public Integer getTmpDeliveryRequestTypeReferenceNumber() {
		return tmpDeliveryRequestTypeReferenceNumber;
	}

	@Transient
	public String getTmpDeliveryRequestReference() {
		return tmpDeliveryRequestReference;
	}

	@Transient
	public Double getTmpUsedQuantity() {
		return tmpUsedQuantity;
	}

	@Transient
	public String getTmpDeliverToFullName() {
		return tmpDeliverToFullName;
	}

	@Transient
	public Date getTmpDeliveryRequestDeliveryDate() {
		return tmpDeliveryRequestDeliveryDate;
	}

	@Transient
	public String getTmpProjectName() {
		return tmpProjectName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	@Override
	public String toString() {
		return "DRD [quantity=" + quantity + ", partNumber=" + partNumber.getName() + ", unitCost=" + unitCost + "]\n";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getPruchaseCurrency() {
		return pruchaseCurrency;
	}

	public void setPruchaseCurrency(Currency pruchaseCurrency) {
		this.pruchaseCurrency = pruchaseCurrency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getCostCurrency() {
		return costCurrency;
	}

	public void setCostCurrency(Currency costCurrency) {
		this.costCurrency = costCurrency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(Currency priceCurrency) {
		this.priceCurrency = priceCurrency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public DeliveryRequestDetail getInboundDeliveryRequestDetail() {
		return inboundDeliveryRequestDetail;
	}

	public void setInboundDeliveryRequestDetail(DeliveryRequestDetail inboundDeliveryRequestDetail) {
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
	}

}
