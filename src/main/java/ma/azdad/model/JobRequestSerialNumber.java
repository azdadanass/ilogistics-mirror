package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity

public class JobRequestSerialNumber extends GenericModel<Integer> implements Serializable {

	private String serialNumber;

	private DeliveryRequest deliveryRequest;
	private PackingDetail packingDetail;
	private JobRequest jobRequest;
	// sn mobile infos
	private Double longitude;
	private Double latitude;
	private String entryMode;
	private String phoneModel;
	private Date date;

	public JobRequestSerialNumber() {
		super();
	}

	//c1
	public JobRequestSerialNumber(Integer id,String serialNumber,Double longitude,Double latitude,String entryMode,String phoneModel,Date date,//
			String partNumberName, String partNumberDescription,String partNumberBrandName,String projectName,//
			String deliveryRequestReference,Date deliveryRequestDate4, //
			CompanyType deliverToCompanyType,String deliverToCompanyName,String deliverToCustomerName,String deliverToSupplierName,String deliverToOther,//
			String destinationName,String destinationProjectCustomerName,String destinationProjectName,String endCustomerName, //
			String poNumero,String  toUserFullName,String warehouseName
			) {
		super(id);
		this.serialNumber=serialNumber;
		this.longitude=longitude;
		this.latitude=latitude;
		this.entryMode=entryMode;
		this.phoneModel=phoneModel;
		this.date=date;
		
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setProjectName(projectName);
		this.setDeliveryRequestReference(deliveryRequestReference);
		this.setDeliveryRequestDate4(deliveryRequestDate4);
		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setDeliverToOther(deliverToOther);
		this.setDestinationName(destinationName);
		this.setDestinationProjectCustomerName(destinationProjectCustomerName);
		this.setDestinationProjectName(destinationProjectName);
		this.setEndCustomerName(endCustomerName);
		this.setPoNumero(poNumero);
		this.setToUserFullName(toUserFullName);
		this.setWarehouseName(warehouseName);
		
	}

	@Transient
	public String getProjectName() {
		return jobRequest != null ? jobRequest.getProjectName() : null;
	}

	@Transient
	public void setProjectName(String projectName) {
		if (jobRequest == null)
			jobRequest = new JobRequest();
		jobRequest.setProjectName(projectName);
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
	public String getToUserFullName() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getToUserFullName();
	}

	@Transient
	public void setToUserFullName(String toUserFullName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setToUserFullName(toUserFullName);
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
	public String getPartNumberName(){
		return packingDetail!=null?packingDetail.getPartNumberName():null;
	}

	@Transient
	public void setPartNumberName(String partNumberName){
		if(packingDetail==null)
			packingDetail=new PackingDetail();
		packingDetail.setPartNumberName(partNumberName);
	}
	
	@Transient
	public String getPartNumberDescription(){
		return packingDetail!=null?packingDetail.getPartNumberDescription():null;
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription){
		if(packingDetail==null)
			packingDetail=new PackingDetail();
		packingDetail.setPartNumberDescription(partNumberDescription);
	}
	
	@Transient
	public String getPartNumberBrandName(){
		return packingDetail!=null?packingDetail.getPartNumberBrandName():null;
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName){
		if(packingDetail==null)
			packingDetail=new PackingDetail();
		packingDetail.setPartNumberBrandName(partNumberBrandName);
	}


	
	@Column(columnDefinition = "LONGTEXT")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PackingDetail getPackingDetail() {
		return packingDetail;
	}

	public void setPackingDetail(PackingDetail packingDetail) {
		this.packingDetail = packingDetail;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public JobRequest getJobRequest() {
		return jobRequest;
	}

	public void setJobRequest(JobRequest jobRequest) {
		this.jobRequest = jobRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getEntryMode() {
		return entryMode;
	}

	public void setEntryMode(String entryMode) {
		this.entryMode = entryMode;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
