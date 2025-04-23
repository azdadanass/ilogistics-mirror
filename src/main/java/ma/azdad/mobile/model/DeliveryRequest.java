package ma.azdad.mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import ma.azdad.model.Company;
import ma.azdad.model.CompanyType;
import ma.azdad.model.Customer;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestFile;
import ma.azdad.model.DeliveryRequestHistory;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.Supplier;
import ma.azdad.utils.Public;

public class DeliveryRequest {

	private Integer id;
	private String reference;
	private DeliveryRequestType type;
	private Date neededDeliveryDate;
	private Date deliveryDate;
	private InboundType inboundType;
	private DeliveryRequestStatus status;
	private Boolean isForReturn = false;
	private Boolean isForTransfer = false;
	private Boolean isSnRequired = false;
	private Boolean showExpiryData = false;
	private Integer approximativeStoragePeriod = 0;


	
	private String ownerName;
	private Boolean transportationNeeded = false;
	private String requesterFullName;
	private String requesterPhoto;
	private Integer projectId;
	private String projectName;
	private Integer destinationProjectId;
	private String destinationProjectName;
	private Integer warehouseId;
	private String warehouseName;
	private Integer destinationId;
	private String destinationName;
	private Integer originId;
	private String originName;
	private String toUser;
	private Boolean toUserInternal;
	private String toCompany;
	private String toCompanyLogo;
	//
	private String company;
	private String supplier;
	private String customer;
	private CompanyType ownerType;
	
	private List<ma.azdad.mobile.model.PackingDetail> packingDetailList = new ArrayList<>();
	private List<ma.azdad.mobile.model.DeliveryRequestDetail> detailList = new ArrayList<>();
	private List<DeliveryRequestFile> fileList = new ArrayList<>();
	private List<ma.azdad.mobile.model.DeliveryRequestHistory> historyList = new ArrayList<>();
	private List<ma.azdad.mobile.model.HardwareStatusData> stockRowList = new ArrayList<>();

	// TIMELINE

	private User user1; // Edited
	private User user2; // Requested
	private User user3; // Approved PM
	private User user8; // Approved HM
	private User user4; // Delivered
	private User user5; // Acknowledged

	private Date date1; // Edited
	private Date date2; // Requested
	private Date date3; // Approved PM
	private Date date8; // Approved HM
	private Date date4; // Delivered
	private Date date5; // Acknowledged

	public DeliveryRequest() {
		super();
	}

	public DeliveryRequest(Integer id, String reference, DeliveryRequestType type, Date neededDeliveryDate,
			Date date4,
			InboundType inboundType, DeliveryRequestStatus status, Boolean isForReturn, Boolean isForTransfer,
			String requesterFullName, Integer projectId, String projectName, Integer destinationProjectId,
			String destinationProjectName, Integer warehouseId, String warehouseName, Integer destinationId,
			String destinationName, Integer originId, String originName, String requesterPhoto, Date deliveryDate,
			Boolean transportationNeeded,Boolean isSnRequired,String company,String supplier,String customer,CompanyType ownerType
			,Integer approximativeStoragePeriod) {
		super();
		this.id = id;
		this.reference = reference;
		this.type = type;
		this.neededDeliveryDate = neededDeliveryDate;
		this.date4 = date4;
		this.approximativeStoragePeriod = approximativeStoragePeriod;
		this.inboundType = inboundType;
		this.status = status;
		this.isForReturn = isForReturn;
		this.isForTransfer = isForTransfer;
		this.requesterFullName = requesterFullName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.destinationProjectId = destinationProjectId;
		this.destinationProjectName = destinationProjectName;
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.destinationId = destinationId;
		this.destinationName = destinationName;
		this.originId = originId;
		this.originName = originName;
		this.requesterPhoto = Public.getPublicUrl(requesterPhoto);
		this.deliveryDate = deliveryDate;
		this.transportationNeeded = transportationNeeded;
		this.isSnRequired = isSnRequired;
		this.reference = reference;
		this.ownerType = ownerType;
		this.company = company;
		this.supplier = supplier;
		this.customer = customer;
		this.ownerName = getOwnerName2();
		

	}
	
	@Transient
	public String getOwnerName2() {
		if (ownerType == null)
			return null;
		switch (ownerType) {
		case COMPANY:
			return getCompanyName();
		case CUSTOMER:
			return getCustomerName();
		case SUPPLIER:
			return getSupplierName();
		default:
			return null;
		}
	}
	
	@Transient
	public String getCustomerName() {
		if (customer == null)
			return null;
		return customer;
	}


	@Transient
	public String getSupplierName() {
		if (supplier == null)
			return null;
		return supplier;
	}

	

	@Transient
	public String getCompanyName() {
		if (company == null)
			return null;
		return company;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public DeliveryRequestType getType() {
		return type;
	}

	public void setType(DeliveryRequestType type) {
		this.type = type;
	}

	public Date getNeededDeliveryDate() {
		return neededDeliveryDate;
	}

	public void setNeededDeliveryDate(Date neededDeliveryDate) {
		this.neededDeliveryDate = neededDeliveryDate;
	}

	public InboundType getInboundType() {
		return inboundType;
	}

	public void setInboundType(InboundType inboundType) {
		this.inboundType = inboundType;
	}

	public DeliveryRequestStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryRequestStatus status) {
		this.status = status;
	}

	public Boolean getIsForReturn() {
		return isForReturn;
	}

	public void setIsForReturn(Boolean isForReturn) {
		this.isForReturn = isForReturn;
	}

	public Boolean getIsForTransfer() {
		return isForTransfer;
	}

	public void setIsForTransfer(Boolean isForTransfer) {
		this.isForTransfer = isForTransfer;
	}

	public String getRequesterFullName() {
		return requesterFullName;
	}

	public void setRequesterFullName(String requesterFullName) {
		this.requesterFullName = requesterFullName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getDestinationProjectId() {
		return destinationProjectId;
	}

	public void setDestinationProjectId(Integer destinationProjectId) {
		this.destinationProjectId = destinationProjectId;
	}

	public String getDestinationProjectName() {
		return destinationProjectName;
	}

	public void setDestinationProjectName(String destinationProjectName) {
		this.destinationProjectName = destinationProjectName;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public Integer getOriginId() {
		return originId;
	}

	public void setOriginId(Integer originId) {
		this.originId = originId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getRequesterPhoto() {
		return requesterPhoto;
	}

	public void setRequesterPhoto(String requesterPhoto) {
		this.requesterPhoto = requesterPhoto;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(Date date3) {
		this.date3 = date3;
	}

	public Date getDate8() {
		return date8;
	}

	public void setDate8(Date date8) {
		this.date8 = date8;
	}

	public Date getDate4() {
		return date4;
	}

	public void setDate4(Date date4) {
		this.date4 = date4;
	}

	public Date getDate5() {
		return date5;
	}

	public void setDate5(Date date5) {
		this.date5 = date5;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public User getUser8() {
		return user8;
	}

	public void setUser8(User user8) {
		this.user8 = user8;
	}

	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	public Boolean getTransportationNeeded() {
		return transportationNeeded;
	}

	public void setTransportationNeeded(Boolean transportationNeeded) {
		this.transportationNeeded = transportationNeeded;
	}


	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public List<ma.azdad.mobile.model.DeliveryRequestDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ma.azdad.mobile.model.DeliveryRequestDetail> detailList) {
		this.detailList = detailList;
	}

	public List<DeliveryRequestFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<DeliveryRequestFile> fileList) {
		this.fileList = fileList;
	}

	public List<ma.azdad.mobile.model.DeliveryRequestHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<ma.azdad.mobile.model.DeliveryRequestHistory> historyList) {
		this.historyList = historyList;
	}

	public List<ma.azdad.mobile.model.PackingDetail> getPackingDetailList() {
		return packingDetailList;
	}

	public void setPackingDetailList(List<ma.azdad.mobile.model.PackingDetail> packingDetailList) {
		this.packingDetailList = packingDetailList;
	}

	public List<ma.azdad.mobile.model.HardwareStatusData> getStockRowList() {
		return stockRowList;
	}

	public void setStockRowList(List<ma.azdad.mobile.model.HardwareStatusData> stockRowList) {
		this.stockRowList = stockRowList;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public Boolean getToUserInternal() {
		return toUserInternal;
	}

	public void setToUserInternal(Boolean toUserInternal) {
		this.toUserInternal = toUserInternal;
	}

	public String getToCompany() {
		return toCompany;
	}

	public void setToCompany(String toCompany) {
		this.toCompany = toCompany;
	}

	public String getToCompanyLogo() {
		return toCompanyLogo;
	}

	public void setToCompanyLogo(String toCompanyLogo) {
		this.toCompanyLogo = toCompanyLogo;
	}

	public Boolean getIsSnRequired() {
		return isSnRequired;
	}

	public void setIsSnRequired(Boolean isSnRequired) {
		this.isSnRequired = isSnRequired;
	}

	public Boolean getShowExpiryData() {
		return showExpiryData;
	}

	public void setShowExpiryData(Boolean showExpiryData) {
		this.showExpiryData = showExpiryData;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public CompanyType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(CompanyType ownerType) {
		this.ownerType = ownerType;
	}

	public Integer getApproximativeStoragePeriod() {
		return approximativeStoragePeriod;
	}

	public void setApproximativeStoragePeriod(Integer approximativeStoragePeriod) {
		this.approximativeStoragePeriod = approximativeStoragePeriod;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	

}
