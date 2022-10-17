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

	private Currency purchaseCurrency;
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
	private String tmpPoNumero;
	private Date tmpPoDate;
	private String tmpPoCurrencyName;
	private String tmpSupplierName;
	private CompanyType tmpOwnerType;
	private Integer tmpCompanyId;
	private Integer tmpCustomerId;
	private Integer tmpSupplierId;

	public DeliveryRequestDetail() {
		super();
	}

	// c1
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
	}

	// c2
	public DeliveryRequestDetail(Double remainingQuantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.unitCost = unitCost;
	}

	// c3
	public DeliveryRequestDetail(Integer id, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType, String tmpDeliveryRequestReference, Double quantity, Double tmpUsedQuantity,
			String toUserFullName) {
		super(id);
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
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
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType, String tmpDeliveryRequestReference, Double unitCost, Double purchaseCost,
			Date tmpDeliveryRequestDeliveryDate, String tmpProjectName, String tmpPoNumero, Date tmpPoDate, String tmpPoCurrencyName, String tmpSupplierName) {
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestReference = tmpDeliveryRequestReference;
		this.unitCost = unitCost;
		this.purchaseCost = purchaseCost;
		this.tmpDeliveryRequestDeliveryDate = tmpDeliveryRequestDeliveryDate;
		this.tmpProjectName = tmpProjectName;
		this.tmpPoNumero = tmpPoNumero;
		this.tmpPoDate = tmpPoDate;
		this.tmpPoCurrencyName = tmpPoCurrencyName;
		this.tmpSupplierName = tmpSupplierName;
	}

	// c5
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	// c6 & c7
	public DeliveryRequestDetail(Double remainingQuantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			Double unitCost, Double unitPrice, CompanyType ownerType, Integer ownerCompanyId, Integer ownerCustomerId, Integer ownerSupplierId) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
		this.tmpOwnerType = ownerType;
		this.tmpCompanyId = ownerCompanyId;
		this.tmpCustomerId = ownerCustomerId;
		this.tmpSupplierId = ownerSupplierId;

	}

	// c8
	public DeliveryRequestDetail(Double quantity, StockRowStatus status, Integer deliveryRequestId, String deliveryRequestReference, DeliveryRequestType deliveryRequestType,
			String projectName, String projectSubType, String warehouseName) {
		this.quantity = quantity;
		this.status = status;
		this.setDeliveryRequestId(deliveryRequestId);
		this.setDeliveryRequestReference(deliveryRequestReference);
		this.setDeliveryRequestType(deliveryRequestType);
		this.setProjectName(projectName);
		this.setProjectSubType(projectSubType);
		this.setWarehouseName(warehouseName);
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
	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber,
			DeliveryRequest inboundDeliveryRequest, DeliveryRequestDetail inboundDeliveryRequestDetail, Double unitCost, Double unitPrice, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.status = status;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
		this.packing = packing;
	}

	// c5
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest,
			Double unitCost, Packing packing) {
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
	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest,
			Double unitCost) {
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
	public Integer getPurchaseCurrencyId() {
		return purchaseCurrency != null ? purchaseCurrency.getId() : null;
	}

	@Transient
	public void setPurchaseCurrencyId(Integer purchaseCurrencyId) {
		if (purchaseCurrency == null || !purchaseCurrencyId.equals(purchaseCurrency.getId()))
			purchaseCurrency = new Currency();
		purchaseCurrency.setId(purchaseCurrencyId);
	}

	@Transient
	public Integer getCostCurrencyId() {
		return costCurrency != null ? costCurrency.getId() : null;
	}

	@Transient
	public void setCostCurrencyId(Integer costCurrencyId) {
		if (costCurrency == null || !costCurrencyId.equals(costCurrency.getId()))
			costCurrency = new Currency();
		costCurrency.setId(costCurrencyId);
	}

	@Transient
	public Integer getPriceCurrencyId() {
		return priceCurrency != null ? priceCurrency.getId() : null;
	}

	@Transient
	public void setPriceCurrencyId(Integer priceCurrencyId) {
		if (priceCurrency == null || !priceCurrencyId.equals(priceCurrency.getId()))
			priceCurrency = new Currency();
		priceCurrency.setId(priceCurrencyId);
	}

	@Transient
	public String getDeliveryRequestReference() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getReference();
	}

	@Transient
	public void setDeliveryRequestReference(String deliveryRequestReference) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setReference(deliveryRequestReference);
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
	public String getProjectName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getProjectName();
	}

	@Transient
	public void setProjectName(String ProjectName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setProjectName(ProjectName);
	}

	@Transient
	public String getProjectSubType() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getProjectSubType();
	}

	@Transient
	public void setProjectSubType(String ProjectSubType) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setProjectSubType(ProjectSubType);
	}

	@Transient
	public String getWarehouseName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getWarehouseName();
	}

	@Transient
	public void setWarehouseName(String warehouseName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setWarehouseName(warehouseName);
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
	public InternalPartNumber getInternalPartNumber() {
		return partNumber == null ? null : partNumber.getInternalPartNumber();
	}

	@Transient
	public void setInternalPartNumber(InternalPartNumber internalPartNumber) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setInternalPartNumber(internalPartNumber);
	}

	@Transient
	public String getInternalPartNumberName() {
		return partNumber == null ? null : partNumber.getInternalPartNumberName();
	}

	@Transient
	public void setInternalPartNumberName(String internalPartNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setInternalPartNumberName(internalPartNumberName);
	}

	@Transient
	public String getInternalPartNumberDescription() {
		return partNumber == null ? null : partNumber.getInternalPartNumberDescription();
	}

	@Transient
	public void setInternalPartNumberDescription(String internalPartNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setInternalPartNumberDescription(internalPartNumberDescription);
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

	@Transient
	public Integer getTmpOwnerId() {
		switch (tmpOwnerType) {
		case COMPANY:
			return tmpCompanyId;
		case CUSTOMER:
			return tmpCustomerId;
		case SUPPLIER:
			return tmpSupplierId;
		default:
			return null;
		}
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

	@ManyToOne(fetch = FetchType.EAGER)
	public Currency getPurchaseCurrency() {
		return purchaseCurrency;
	}

	public void setPurchaseCurrency(Currency purchaseCurrency) {
		this.purchaseCurrency = purchaseCurrency;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Currency getCostCurrency() {
		return costCurrency;
	}

	public void setCostCurrency(Currency costCurrency) {
		this.costCurrency = costCurrency;
	}

	@ManyToOne(fetch = FetchType.EAGER)
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

	@Transient
	public CompanyType getTmpOwnerType() {
		return tmpOwnerType;
	}

	@Transient
	public Integer getTmpCompanyId() {
		return tmpCompanyId;
	}

	@Transient
	public Integer getTmpCustomerId() {
		return tmpCustomerId;
	}

	@Transient
	public Integer getTmpSupplierId() {
		return tmpSupplierId;
	}

	@Transient
	public String getTmpPoNumero() {
		return tmpPoNumero;
	}

	@Transient
	public Date getTmpPoDate() {
		return tmpPoDate;
	}

	@Transient
	public String getTmpSupplierName() {
		return tmpSupplierName;
	}

	@Transient
	public String getTmpPoCurrencyName() {
		return tmpPoCurrencyName;
	}

}
