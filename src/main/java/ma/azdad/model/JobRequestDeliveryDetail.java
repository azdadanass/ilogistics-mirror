package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class JobRequestDeliveryDetail extends GenericModel<Integer> implements Serializable {

	private Double quantity = 0.0;
	private Double receivedQuantity = 0.0;
	private Double installedQuantity = 0.0;
	private Double additionalQuantity = 0.0;
	private Boolean isSerialNumberRequired = false;

	private JobRequest jobRequest;
	private DeliveryRequestDetail deliveryRequestDetail;

	// TMP
	private Integer tmpDeliveryRequestDetailId;
	private Integer tmpPartNumberId;
	private String tmpPartNumberName;
	private String tmpPartNumberImage;
	private String tmpPartNumberDescription;
	private Integer tmpDeliveryRequestId;
	private String tmpDeliveryRequestReference;
	private String tmpJobRequestReference;
	private String tmpSiteName;
	private String tmpTeamName;
	private String tmpSerialNumber;
	private Integer tmpJobRequestId;

	public JobRequestDeliveryDetail() {
		super();
	}

	public JobRequestDeliveryDetail(Double installedQuantity, String tmpSerialNumber, String tmpPartNumberName, String tmpPartNumberDescription, String tmpDeliveryRequestReference,
			Integer tmpJobRequestId, String tmpJobRequestReference, String tmpSiteName, String tmpTeamName) {
		super();
		this.installedQuantity = installedQuantity;
		this.tmpSerialNumber = tmpSerialNumber;
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestReference = tmpDeliveryRequestReference;
		this.tmpJobRequestId = tmpJobRequestId;
		this.tmpJobRequestReference = tmpJobRequestReference;
		this.tmpSiteName = tmpSiteName;
		this.tmpTeamName = tmpTeamName;
	}

	// c1
	public JobRequestDeliveryDetail(Double installedQuantity, Boolean isSerialNumberRequired, Integer tmpDeliveryRequestDetailId, //
			Integer tmpPartNumberId, String tmpPartNumberName, String tmpPartNumberImage, //
			String tmpPartNumberDescription, Integer tmpDeliveryRequestId, Integer referenceNumber, DeliveryRequestType deliveryRequestType, //
			Integer tmpJobRequestId, String tmpSiteName, String tmpTeamName, //
			CompanyType deliverToCompanyType, String deliverToCompanyName, String deliverToCustomerName, String deliverToSupplierName, String toUserFullName) {
		super();
		this.installedQuantity = installedQuantity;
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.tmpDeliveryRequestDetailId = tmpDeliveryRequestDetailId;
		this.tmpPartNumberId = tmpPartNumberId;
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberImage = tmpPartNumberImage;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestReference = "DN" + (deliveryRequestType.ordinal() + 1) + String.format("%05d", referenceNumber);
		this.tmpJobRequestId = tmpJobRequestId;
		this.tmpJobRequestReference = "JR" + String.format("%05d", tmpJobRequestId);
		this.tmpSiteName = tmpSiteName;
		this.tmpTeamName = tmpTeamName;

		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setToUserFullName(toUserFullName);
	}

	public JobRequestDeliveryDetail(Double quantity, Boolean isSerialNumberRequired, JobRequest jobRequest, DeliveryRequestDetail deliveryRequestDetail) {
		super();
		this.quantity = quantity;
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.jobRequest = jobRequest;
		this.deliveryRequestDetail = deliveryRequestDetail;
		this.receivedQuantity = quantity;
		this.installedQuantity = quantity;
	}

	@Override
	public boolean filter(String query) {
		return contains(query, tmpPartNumberName, tmpPartNumberDescription, tmpDeliveryRequestReference, //
				tmpJobRequestReference, tmpSiteName, tmpSerialNumber, tmpTeamName, //
				getDeliverToCompanyType() != null ? getDeliverToCompanyType().getValue() : null, getDeliverToCompanyName(), getDeliverToCustomerName(), getDeliverToSupplierName(),
				getToUserFullName());
	}

	@Transient
	public Integer getDeliveryRequestId() {
		if (deliveryRequestDetail == null)
			return null;
		return deliveryRequestDetail.getDeliveryRequestId();
	}

	@Transient
	public DeliveryRequest getDeliveryRequest() {
		if (deliveryRequestDetail == null)
			return null;
		return deliveryRequestDetail.getDeliveryRequest();
	}

	@Transient
	public Integer getDeliveryRequestDetailId() {
		if (deliveryRequestDetail == null)
			return null;
		return deliveryRequestDetail.getId();
	}

	@Transient
	public String getPartNumberName() {
		if (deliveryRequestDetail == null)
			return null;
		return deliveryRequestDetail.getPartNumberName();
	}

	@Transient
	public String getPartNumberDescription() {
		if (deliveryRequestDetail == null)
			return null;
		return deliveryRequestDetail.getPartNumberDescription();
	}

	@Column(nullable = false)
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public JobRequest getJobRequest() {
		return jobRequest;
	}

	public void setJobRequest(JobRequest jobRequest) {
		this.jobRequest = jobRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public DeliveryRequestDetail getDeliveryRequestDetail() {
		return deliveryRequestDetail;
	}

	public void setDeliveryRequestDetail(DeliveryRequestDetail deliveryRequestDetail) {
		this.deliveryRequestDetail = deliveryRequestDetail;
	}

	public Double getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Double receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public Double getInstalledQuantity() {
		return installedQuantity;
	}

	public void setInstalledQuantity(Double installedQuantity) {
		this.installedQuantity = installedQuantity;
	}

	public Boolean getIsSerialNumberRequired() {
		return isSerialNumberRequired;
	}

	public void setIsSerialNumberRequired(Boolean isSerialNumberRequired) {
		this.isSerialNumberRequired = isSerialNumberRequired;
	}

	public Double getAdditionalQuantity() {
		return additionalQuantity;
	}

	public void setAdditionalQuantity(Double additionalQuantity) {
		this.additionalQuantity = additionalQuantity;
	}

	@Transient
	public Integer getTmpPartNumberId() {
		return tmpPartNumberId;
	}

	@Transient
	public void setTmpPartNumberId(Integer tmpPartNumberId) {
		this.tmpPartNumberId = tmpPartNumberId;
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
	public String getTmpDeliveryRequestReference() {
		return tmpDeliveryRequestReference;
	}

	@Transient
	public String getTmpJobRequestReference() {
		return tmpJobRequestReference;
	}

	@Transient
	public String getTmpSiteName() {
		return tmpSiteName;
	}

	@Transient
	public String getTmpTeamName() {
		return tmpTeamName;
	}

	@Transient
	public Integer getTmpDeliveryRequestDetailId() {
		return tmpDeliveryRequestDetailId;
	}

	@Transient
	public String getTmpSerialNumber() {
		return tmpSerialNumber;
	}

	@Transient
	public Integer getTmpJobRequestId() {
		return tmpJobRequestId;
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
	public String getTmpPartNumberImage() {
		return tmpPartNumberImage;
	}

	@Transient
	public Integer getTmpDeliveryRequestId() {
		return tmpDeliveryRequestId;
	}

	@Transient
	public CompanyType getDeliverToCompanyType() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getDeliverToCompanyType() : null;
	}

	@Transient
	public void setDeliverToCompanyType(CompanyType deliverToCompanyType) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setDeliverToCompanyType(deliverToCompanyType);
	}

	@Transient
	public String getDeliverToCompanyName() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getDeliverToCompanyName() : null;
	}

	@Transient
	public void setDeliverToCompanyName(String deliverToCompanyName) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setDeliverToCompanyName(deliverToCompanyName);
	}

	@Transient
	public String getDeliverToCustomerName() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getDeliverToCustomerName() : null;
	}

	@Transient
	public void setDeliverToCustomerName(String deliverToCustomerName) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setDeliverToCustomerName(deliverToCustomerName);
	}

	@Transient
	public String getDeliverToSupplierName() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getDeliverToSupplierName() : null;
	}

	@Transient
	public void setDeliverToSupplierName(String deliverToSupplierName) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setDeliverToSupplierName(deliverToSupplierName);
	}

	@Transient
	public String getDeliverToOther() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getDeliverToOther() : null;
	}

	@Transient
	public void setDeliverToOther(String deliverToOther) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setDeliverToOther(deliverToOther);
	}

	@Transient
	public String getToUserFullName() {
		return deliveryRequestDetail != null ? deliveryRequestDetail.getToUserFullName() : null;
	}

	@Transient
	public void setToUserFullName(String toUserFullName) {
		if (deliveryRequestDetail == null)
			deliveryRequestDetail = new DeliveryRequestDetail();
		deliveryRequestDetail.setToUserFullName(toUserFullName);
	}

}
