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
//	private Double unitCost;
//	private Double unitPrice;
	private Date creationDate;

	private StockRowState state = StockRowState.NORMAL;
	private StockRowStatus status = StockRowStatus.BRAND_NEW;
	private String originNumber;

	private DeliveryRequest deliveryRequest;

	private Location location;
	private Packing packing;

	private PartNumber partNumber;
	private DeliveryRequestDetail deliveryRequestDetail;
	private DeliveryRequest inboundDeliveryRequest;
	private DeliveryRequestDetail inboundDeliveryRequestDetail; // case inbound/outbound stock row

	// cache
	private Integer companyId;
	private Integer customerId;
	private Integer supplierId;
	private Integer inboundCompanyId;
	private Integer inboundCustomerId;
	private Integer inboundSupplierId;

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

	private Double projectSubTypeStockQuantity;
	private Double pendingQuantity;
	private Double forecastQuantity;

	private Double installedQuantity;

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
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			DeliveryRequest inboundDeliveryRequest, Double unitCost, Integer costCurrencyId, Location location, Packing packing) {
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.setUnitCost(unitCost);
		this.setCostCurrencyId(costCurrencyId);
		this.packing = packing;
	}

	// c3
	public StockRow(Double quantity, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			DeliveryRequest inboundDeliveryRequest, Double unitCost, Integer costCurrencyId, Location location) {
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.location = location;
		this.deliveryRequest = deliveryRequest;
		this.setUnitCost(unitCost);
		this.setCostCurrencyId(costCurrencyId);
	}

	// c4
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundQuantity = inboundQuantity;
		this.outboundQuantity = outboundQuantity;
	}

	// c5
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.deliveryRequest = deliveryRequest;
	}

	// c6
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberImage, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberImage(partNumberImage);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
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
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription) {
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	// c10
//	public StockRow(Double quantity, DeliveryRequest inboundDeliveryRequest, //
//			Integer partNumberId, String partNumberName, String partNumberImage, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
//			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription) {
//		super();
//		this.quantity = quantity;
//		this.setPartNumberId(partNumberId);
//		this.setPartNumberName(partNumberName);
//		this.setPartNumberImage(partNumberImage);
//		this.setPartNumberDescription(partNumberDescription);
//		this.setPartNumberIndustryName(partNumberIndustryName);
//		this.setPartNumberCategoryName(partNumberCategoryName);
//		this.setPartNumberTypeName(partNumberTypeName);
//		this.setPartNumberBrandName(partNumberBrandName);
//		this.setInternalPartNumberName(internalPartNumberName);
//		this.setInternalPartNumberDescription(internalPartNumberDescription);
//		this.inboundDeliveryRequest = inboundDeliveryRequest;
//	}

	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberImage, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			Integer inboundDeliveryRequestId, String inboundDeliveryRequestReference, DeliveryRequestType inboundDeliveryRequestType, Date inboundDeliveryRequestDate4,
			Integer inboundDeliveryRequestApproximativeStoragePeriod, //
			String inboundDeliveryRequestProjectName, String inboundDeliveryRequestWarehouseName) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberImage(partNumberImage);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.setInboundDeliveryRequestId(inboundDeliveryRequestId);
		this.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
		this.setInboundDeliveryRequestType(inboundDeliveryRequestType);
		this.setInboundDeliveryRequestDate4(inboundDeliveryRequestDate4);
		this.setInboundDeliveryRequestApproximativeStoragePeriod(inboundDeliveryRequestApproximativeStoragePeriod);
		this.setInboundProjectName(inboundDeliveryRequestProjectName);
		this.setInboundWarehouseName(inboundDeliveryRequestWarehouseName);
	}

	// c12
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			StockRowStatus status, Date inboundDeliveryRequestDeliveryDate, Double unitCost, Integer costCurrencyId, Double qTotalCost) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.status = status;
		this.inboundDeliveryRequestDeliveryDate = inboundDeliveryRequestDeliveryDate;
		this.setUnitCost(unitCost);
		this.setCostCurrencyId(costCurrencyId);
		this.qTotalCost = qTotalCost;
	}

	// c13
	public StockRow(Double quantity, Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, Integer deliveryRequestId, DeliveryRequestType deliveryRequestType, Integer projectId,
			String projectName, Integer destinationProjectCustomerId) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.setDeliveryRequestId(deliveryRequestId);
		this.setDeliveryRequestType(deliveryRequestType);
		this.setProjectId(projectId);
		this.setProjectName(projectName);
		this.setDestinationProjectCustomerId(destinationProjectCustomerId);
	}

	// c15
	public StockRow(Double quantity, StockRowStatus status, DeliveryRequest deliveryRequest, DeliveryRequest inboundDeliveryRequest, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			Double qTotalCost, CompanyType deliverToCompanyType, String deliverToCompany, String deliverToCustomer, String deliverToSupplier, String deliverToOther) {
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
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.qTotalCost = qTotalCost;

		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompany);
		this.setDeliverToCustomerName(deliverToCustomer);
		this.setDeliverToSupplierName(deliverToSupplier);
		this.setDeliverToOther(deliverToOther);
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
	public StockRow(DeliveryRequestDetail deliveryRequestDetail, Double quantity, Date creationDate, String originNumber, PartNumber partNumber, DeliveryRequest deliveryRequest, Packing packing) {
		super();
		this.deliveryRequestDetail = deliveryRequestDetail;
		this.quantity = quantity;
		this.creationDate = creationDate;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.packing = packing;
	}

	// c19
	public StockRow(DeliveryRequestDetail deliveryRequestDetail, Double quantity, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, PartNumber partNumber,
			DeliveryRequest inboundDeliveryRequest, DeliveryRequestDetail inboundDeliveryRequestDetail, Location location, Date creationDate, Packing packing) {
		super();
		this.deliveryRequestDetail = deliveryRequestDetail;
		this.quantity = quantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
		this.location = location;
		this.deliveryRequest = deliveryRequest;
		this.creationDate = creationDate;
		this.packing = packing;
	}

	// c20
	public StockRow(Double quantity, Double tmpQuantity, PartNumber partNumber, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, DeliveryRequest inboundDeliveryRequest,
			DeliveryRequestDetail inboundDeliveryRequestDetail, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.tmpQuantity = tmpQuantity;
		this.status = status;
		this.creationDate = new Date();
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
		this.packing = packing;
	}

	// c21
	public StockRow(DeliveryRequestDetail deliveryRequestDetail, Double quantity, Double tmpQuantity, PartNumber partNumber, DeliveryRequest deliveryRequest, Boolean initial, String originNumber,
			DeliveryRequest inboundDeliveryRequest, DeliveryRequestDetail inboundDeliveryRequestDetail, Date creationDate, Packing packing) {
		super();
		this.deliveryRequestDetail = deliveryRequestDetail;
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.tmpQuantity = tmpQuantity;
		this.initial = initial;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
		this.creationDate = creationDate;
		this.packing = packing;
	}

	// c22 --> deliveryReporting
	public StockRow(Double quantity, StockRowStatus status, Double unitCost, Integer costCurrencyId, Double unitPrice, Integer priceCurrencyId, String projectName, String warehouseName, //
			Integer partNumberId, String partNumberName, String partNumberImage, String partNumberDescription, String partNumberBrandName, //
			Integer deliveryRequestId, DeliveryRequestType deliveryRequestType, InboundType deliveryRequestInboundType, String deliveryRequestReference, String deliveryRequestSmsRef,
			Date deliveryRequestDate4, Boolean deliveryRequestSdm, String destinationProjectCustomerName, String destinationName, String originName, String destinationProjectName, //
			CompanyType deliverToCompanyType, String deliverToCompanyName, String deliverToCustomerName, String deliverToSupplierName, String deliverToOther, //
			String poNumero, String endCustomerName) {
		this.quantity = quantity;
		this.status = status;
		this.setUnitCost(unitCost);
		this.setCostCurrencyId(costCurrencyId);
		this.setUnitPrice(unitPrice);
		this.setPriceCurrencyId(priceCurrencyId);
		this.setDeliveryRequestId(deliveryRequestId);
		this.setProjectName(projectName);
		this.setWarehouseName(warehouseName);
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberImage(partNumberImage);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setDeliveryRequestType(deliveryRequestType);
		this.setDeliveryRequestInboundType(deliveryRequestInboundType);
		this.setDeliveryRequestReference(deliveryRequestReference);
		this.setDeliveryRequestSmsRef(deliveryRequestSmsRef);
		this.setDeliveryRequestDate4(deliveryRequestDate4);
		this.setDeliveryRequestSdm(deliveryRequestSdm);
		this.setDestinationProjectCustomerName(destinationProjectCustomerName);
		this.setDestinationName(destinationName);
		this.setOriginName(originName);
		this.setDestinationProjectName(destinationProjectName);
		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setDeliverToOther(deliverToOther);
		this.setPoNumero(poNumero);
		this.setEndCustomerName(endCustomerName);
	}

	// c23
	public StockRow(Double quantity, //
			Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberIndustryName, String partNumberCategoryName, String partNumberTypeName,
			String partNumberBrandName, String internalPartNumberName, String internalPartNumberDescription, //
			StockRowStatus status, Date inboundDeliveryRequestDeliveryDate, Double unitCost, Integer costCurrencyId, Double qTotalCost, String projectName) {
		super();
		this.quantity = quantity;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberIndustryName(partNumberIndustryName);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInternalPartNumberName(internalPartNumberName);
		this.setInternalPartNumberDescription(internalPartNumberDescription);
		this.status = status;
		this.inboundDeliveryRequestDeliveryDate = inboundDeliveryRequestDeliveryDate;
		this.setUnitCost(unitCost);
		this.setCostCurrencyId(costCurrencyId);
		this.qTotalCost = qTotalCost;
		this.setProjectName(projectName);
	}
	
	// c24 --> deliveryReporting 2
		public StockRow(Double quantity, StockRowStatus status, Double unitCost, Integer costCurrencyId, Double unitPrice, Integer priceCurrencyId, String projectName, String warehouseName, //
				Integer partNumberId, String partNumberName, String partNumberImage, String partNumberDescription, String partNumberBrandName, //
				Integer deliveryRequestId, DeliveryRequestType deliveryRequestType, InboundType deliveryRequestInboundType, String deliveryRequestReference, String deliveryRequestSmsRef,
				Date deliveryRequestDate4, Boolean deliveryRequestSdm,Integer inboundDeliveryRequestId,String inboundDeliveryRequestReference, String destinationProjectCustomerName, String destinationName, String originName, String destinationProjectName, //
				CompanyType deliverToCompanyType, String deliverToCompanyName, String deliverToCustomerName, String deliverToSupplierName, String deliverToOther, //
				String poNumero,String inboundPoNumero, String endCustomerName) {
			this.quantity = quantity;
			this.status = status;
			this.setUnitCost(unitCost);
			this.setCostCurrencyId(costCurrencyId);
			this.setUnitPrice(unitPrice);
			this.setPriceCurrencyId(priceCurrencyId);
			this.setDeliveryRequestId(deliveryRequestId);
			this.setProjectName(projectName);
			this.setWarehouseName(warehouseName);
			this.setPartNumberId(partNumberId);
			this.setPartNumberName(partNumberName);
			this.setPartNumberImage(partNumberImage);
			this.setPartNumberDescription(partNumberDescription);
			this.setPartNumberBrandName(partNumberBrandName);
			this.setDeliveryRequestType(deliveryRequestType);
			this.setDeliveryRequestInboundType(deliveryRequestInboundType);
			this.setDeliveryRequestReference(deliveryRequestReference);
			this.setDeliveryRequestSmsRef(deliveryRequestSmsRef);
			this.setDeliveryRequestDate4(deliveryRequestDate4);
			this.setDeliveryRequestSdm(deliveryRequestSdm);
			this.setInboundDeliveryRequestId(inboundDeliveryRequestId);
			this.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
			this.setDestinationProjectCustomerName(destinationProjectCustomerName);
			this.setDestinationName(destinationName);
			this.setOriginName(originName);
			this.setDestinationProjectName(destinationProjectName);
			this.setDeliverToCompanyType(deliverToCompanyType);
			this.setDeliverToCompanyName(deliverToCompanyName);
			this.setDeliverToCustomerName(deliverToCustomerName);
			this.setDeliverToSupplierName(deliverToSupplierName);
			this.setDeliverToOther(deliverToOther);
			this.setPoNumero(poNumero);
			this.setInboundPoNumero(inboundPoNumero);
			this.setEndCustomerName(endCustomerName);
		}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean filter(String query) {
		return contains(query, originNumber, getPartNumberName(), getPartNumberDescription(), getDeliveryRequestReference(), getInboundDeliveryRequestReference() //
				, getPartNumberIndustryName(), getPartNumberCategoryName(), getPartNumberTypeName(), getPartNumberBrandName(), getWarehouseName(), getProjectName(),
				status != null ? status.getValue() : null, getProjectSubType());
	}

	public StockRow() {
		super();
		this.creationDate = new Date();
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
	public Integer getPurchaseCurrencyId() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getPurchaseCurrencyId() : null;
	}

	@Transient
	public String getStatusValue() {
		return status != null ? status.getValue() : null;
	}

	@Transient
	public void setPurchaseCurrencyId(Integer purchaseCurrencyId) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setPurchaseCurrencyId(purchaseCurrencyId);
	}

	@Transient
	public Integer getCostCurrencyId() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getCostCurrencyId() : null;
	}

	@Transient
	public void setCostCurrencyId(Integer costCurrencyId) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setCostCurrencyId(costCurrencyId);
	}

	@Transient
	public Integer getPriceCurrencyId() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getPriceCurrencyId() : null;
	}

	@Transient
	public void setPriceCurrencyId(Integer priceCurrencyId) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setPriceCurrencyId(priceCurrencyId);
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
	public Boolean getDeliveryRequestSdm() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getSdm();
	}

	@Transient
	public void setDeliveryRequestSdm(Boolean deliveryRequestSdm) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setSdm(deliveryRequestSdm);
	}

	@Transient
	public String getDeliveryRequestSmsRef() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getSmsRef();
	}

	@Transient
	public void setDeliveryRequestSmsRef(String deliveryRequestSmsRef) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setSmsRef(deliveryRequestSmsRef);
	}

	@Transient
	public Date getDeliveryRequestDate4() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getDate4();
	}

	@Transient
	public void setDeliveryRequestDate4(Date deliveryRequestDate4) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDate4(deliveryRequestDate4);
	}

	@Transient
	public String getDeliveryYear() {
		if (getDeliveryRequestDate4() == null)
			return null;
		return String.valueOf(UtilsFunctions.getYear(getDeliveryRequestDate4()));
	}

	@Transient
	public String getDeliveryMonthAndYear() {
		if (getDeliveryRequestDate4() == null)
			return null;
		return String.valueOf(UtilsFunctions.getMonth(getDeliveryRequestDate4()) + "/" + UtilsFunctions.getYear(getDeliveryRequestDate4()));
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
	public String getInStockeDateLabel() {
		Long inStockDays = getInStockDays();
		if (inStockDays == null)
			return null;

		if (inStockDays < 365)
			return "Less than 1 Year";
		else if (inStockDays < 2 * 365)
			return "2 Years";
		else if (inStockDays < 3 * 365)
			return "3 Years";
		else if (inStockDays < 4 * 365)
			return "4 Years";
		else
			return "5+ Years";
	}

	@Transient
	public Double getTotalCost() {
		try {
			return quantity * getUnitCost();
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Transient
	public Double getTotalPrice() {
		try {
			return quantity * getUnitPrice();
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

	@Enumerated(EnumType.STRING)
	public StockRowState getState() {
		return state;
	}

	public void setState(StockRowState state) {
		this.state = state;
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

	@Transient
	public Double getUnitCost() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getUnitCost() : null;
	}

	@Transient
	public void setUnitCost(Double deliveryRequestDetailUnitCost) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setUnitCost(deliveryRequestDetailUnitCost);
	}

	@Transient
	public Double getUnitPrice() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getUnitPrice() : null;
	}

	@Transient
	public void setUnitPrice(Double deliveryRequestDetailUnitPrice) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setUnitPrice(deliveryRequestDetailUnitPrice);
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
	public String getDeliverTo() {
		if (getDeliverToCompanyType() == null)
			return null;
		return getDeliverToCompanyType().getValue() + " / " + getDeliverToEntityName();
	}

	@Transient
	public String getDeliverToEntityName() {
		if (getDeliverToCompanyType() == null)
			return null;
		switch (getDeliverToCompanyType()) {
		case COMPANY:
			return getDeliverToCompanyName();
		case CUSTOMER:
			return getDeliverToCustomerName();
		case SUPPLIER:
			return getDeliverToSupplierName();
		default:
			return getDeliverToOther();
		}
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
	public String getDestinationProjectName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getDestinationProjectName();
	}

	@Transient
	public void setDestinationProjectName(String destinationProjectName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDestinationProjectName(destinationProjectName);
	}

	@Transient
	public Integer getDestinationProjectCustomerId() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getDestinationProjectCustomerId();
	}

	@Transient
	public void setDestinationProjectCustomerId(Integer destinationProjectCustomerId) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDestinationProjectCustomerId(destinationProjectCustomerId);
	}

	@Transient
	public String getDestinationProjectCustomerName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getDestinationProjectCustomerName();
	}

	@Transient
	public void setDestinationProjectCustomerName(String destinationProjectCustomerName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDestinationProjectCustomerName(destinationProjectCustomerName);
	}

	@Transient
	public Integer getProjectId() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getProjectId();
	}

	@Transient
	public void setProjectId(Integer ProjectId) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setProjectId(ProjectId);
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
	public String getDestinationName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getDestinationName();
	}

	@Transient
	public void setDestinationName(String destinationName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDestinationName(destinationName);
	}

	@Transient
	public String getOriginName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getOriginName();
	}

	@Transient
	public void setOriginName(String originName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setOriginName(originName);
	}

	@Transient
	public String getEndCustomerName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getEndCustomerName();
	}

	@Transient
	public void setEndCustomerName(String endCustomerName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setEndCustomerName(endCustomerName);
	}

	@Transient
	public String getExternalRequesterFullName() {
		return deliveryRequest != null ? deliveryRequest.getExternalRequesterFullName() : null;
	}

	@Transient
	public void setExternalRequesterFullName(String externalRequesterFullName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setExternalRequesterFullName(externalRequesterFullName);
	}

	@Transient
	public CompanyType getDeliverToCompanyType() {
		return deliveryRequest != null ? deliveryRequest.getDeliverToCompanyType() : null;
	}

	@Transient
	public void setDeliverToCompanyType(CompanyType deliverToCompanyType) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDeliverToCompanyType(deliverToCompanyType);
	}

	@Transient
	public String getDeliverToCompanyName() {
		return deliveryRequest != null ? deliveryRequest.getDeliverToCompanyName() : null;
	}

	@Transient
	public void setDeliverToCompanyName(String deliverToCompanyName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDeliverToCompanyName(deliverToCompanyName);
	}

	@Transient
	public String getDeliverToCustomerName() {
		return deliveryRequest != null ? deliveryRequest.getDeliverToCustomerName() : null;
	}

	@Transient
	public void setDeliverToCustomerName(String deliverToCustomerName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDeliverToCustomerName(deliverToCustomerName);
	}

	@Transient
	public String getDeliverToSupplierName() {
		return deliveryRequest != null ? deliveryRequest.getDeliverToSupplierName() : null;
	}

	@Transient
	public void setDeliverToSupplierName(String deliverToSupplierName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDeliverToSupplierName(deliverToSupplierName);
	}

	@Transient
	public String getDeliverToOther() {
		return deliveryRequest != null ? deliveryRequest.getDeliverToOther() : null;
	}

	@Transient
	public void setDeliverToOther(String deliverToOther) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDeliverToOther(deliverToOther);
	}

	@Transient
	public String getPoNumero() {
		return deliveryRequest != null ? deliveryRequest.getPoNumero() : null;
	}

	@Transient
	public void setPoNumero(String poNumero) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setPoNumero(poNumero);
	}
	
	@Transient
	public String getInboundPoNumero() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getPoNumero() : null;
	}

	@Transient
	public void setInboundPoNumero(String inboundPoNumero) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setPoNumero(inboundPoNumero);
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
	public String getInboundProjectName() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getProjectName() : null;
	}

	@Transient
	public void setInboundProjectName(String inboundProjectName) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setProjectName(inboundProjectName);
	}
	
	@Transient
	public String getInboundWarehouseName() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getWarehouseName() : null;
	}

	@Transient
	public void setInboundWarehouseName(String inboundWarehouseName) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setWarehouseName(inboundWarehouseName);
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
	public Integer getInboundDeliveryRequestApproximativeStoragePeriod() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getApproximativeStoragePeriod() : null;
	}

	@Transient
	public void setInboundDeliveryRequestApproximativeStoragePeriod(Integer inboundDeliveryRequestApproximativeStoragePeriod) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setApproximativeStoragePeriod(inboundDeliveryRequestApproximativeStoragePeriod);
	}

	@Transient
	public DeliveryRequestType getInboundDeliveryRequestType() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getType() : null;
	}

	@Transient
	public void setInboundDeliveryRequestType(DeliveryRequestType inboundDeliveryRequestType) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setType(inboundDeliveryRequestType);
	}

	@Transient
	public Date getInboundDeliveryRequestDate4() {
		return inboundDeliveryRequest != null ? inboundDeliveryRequest.getDate4() : null;
	}

	@Transient
	public void setInboundDeliveryRequestDate4(Date inboundDeliveryRequestDate4) {
		if (inboundDeliveryRequest == null)
			inboundDeliveryRequest = new DeliveryRequest();
		inboundDeliveryRequest.setDate4(inboundDeliveryRequestDate4);
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
	public InboundType getDeliveryRequestInboundType() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getInboundType();
	}

	@Transient
	public void setDeliveryRequestInboundType(InboundType deliveryRequestInboundType) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setInboundType(deliveryRequestInboundType);
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

	@Transient
	public String getPartNumberImage() {
		return partNumber == null ? null : partNumber.getImage();
	}

	@Transient
	public void setPartNumberImage(String partNumberImage) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setImage(partNumberImage);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public DeliveryRequestDetail getInboundDeliveryRequestDetail() {
		return inboundDeliveryRequestDetail;
	}

	public void setInboundDeliveryRequestDetail(DeliveryRequestDetail inboundDeliveryRequestDetail) {
		this.inboundDeliveryRequestDetail = inboundDeliveryRequestDetail;
	}

	@Transient
	public Double getProjectSubTypeStockQuantity() {
		return projectSubTypeStockQuantity;
	}

	@Transient
	public void setProjectSubTypeStockQuantity(Double projectSubTypeStockQuantity) {
		this.projectSubTypeStockQuantity = projectSubTypeStockQuantity;
	}

	@Transient
	public Double getForecastQuantity() {
		return forecastQuantity;
	}

	@Transient
	public void setForecastQuantity(Double forecastQuantity) {
		this.forecastQuantity = forecastQuantity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public DeliveryRequestDetail getDeliveryRequestDetail() {
		return deliveryRequestDetail;
	}

	public void setDeliveryRequestDetail(DeliveryRequestDetail deliveryRequestDetail) {
		this.deliveryRequestDetail = deliveryRequestDetail;
	}

	@Transient
	public Double getInstalledQuantity() {
		return installedQuantity;
	}

	@Transient
	public void setInstalledQuantity(Double installedQuantity) {
		this.installedQuantity = installedQuantity;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getInboundCompanyId() {
		return inboundCompanyId;
	}

	public void setInboundCompanyId(Integer inboundCompanyId) {
		this.inboundCompanyId = inboundCompanyId;
	}

	public Integer getInboundCustomerId() {
		return inboundCustomerId;
	}

	public void setInboundCustomerId(Integer inboundCustomerId) {
		this.inboundCustomerId = inboundCustomerId;
	}

	public Integer getInboundSupplierId() {
		return inboundSupplierId;
	}

	public void setInboundSupplierId(Integer inboundSupplierId) {
		this.inboundSupplierId = inboundSupplierId;
	}

}
