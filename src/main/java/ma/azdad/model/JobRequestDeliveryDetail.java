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
	private DeliveryRequest deliveryRequest;
	private PartNumber partNumber;

	// TMP
	private String tmpSerialNumber;

	public JobRequestDeliveryDetail() {
		super();
	}

	public JobRequestDeliveryDetail(Double installedQuantity, String tmpSerialNumber, String tmpPartNumberName, String tmpPartNumberDescription, String tmpDeliveryRequestReference,
			Integer tmpJobRequestId, String tmpJobRequestReference, String tmpSiteName, String tmpTeamName) {
		super();
		this.installedQuantity = installedQuantity;
		this.tmpSerialNumber = tmpSerialNumber;
		this.setPartNumberName(tmpPartNumberName);
		this.setPartNumberDescription(tmpPartNumberDescription);
		this.setDeliveryRequestReference(tmpDeliveryRequestReference);
		this.setJobRequestId(tmpJobRequestId);
		this.setJobRequestReference(tmpJobRequestReference);
		this.setSiteName(tmpSiteName);
		this.setTeamName(tmpTeamName);
	}

	// c1
	public JobRequestDeliveryDetail(Double installedQuantity, Boolean isSerialNumberRequired, //
			Integer tmpPartNumberId, String tmpPartNumberName, String tmpPartNumberImage, //
			String tmpPartNumberDescription, Integer tmpDeliveryRequestId, String deliveryRequestReference, DeliveryRequestType deliveryRequestType, //
			Integer tmpJobRequestId, String tmpJobRequestReference, String tmpSiteName, String tmpTeamName, //
			CompanyType deliverToCompanyType, String deliverToCompanyName, String deliverToCustomerName, String deliverToSupplierName, String toUserFullName) {
		super();
		this.installedQuantity = installedQuantity;
		this.isSerialNumberRequired = isSerialNumberRequired;

		this.setPartNumberId(tmpPartNumberId);
		this.setPartNumberName(tmpPartNumberName);
		this.setPartNumberImage(tmpPartNumberImage);
		this.setPartNumberDescription(tmpPartNumberDescription);
		this.setDeliveryRequestId(tmpDeliveryRequestId);
		this.setDeliveryRequestReference(deliveryRequestReference);
		this.setJobRequestId(tmpJobRequestId);
		this.setJobRequestReference(tmpJobRequestReference);
		this.setSiteName(tmpSiteName);
		this.setTeamName(tmpTeamName);

		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setToUserFullName(toUserFullName);
	}

	public JobRequestDeliveryDetail(Double quantity, Boolean isSerialNumberRequired, JobRequest jobRequest, DeliveryRequest deliveryRequest, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.jobRequest = jobRequest;
		this.deliveryRequest = deliveryRequest;
		this.partNumber = partNumber;
		this.receivedQuantity = quantity;
		this.installedQuantity = quantity;
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getPartNumberName(), getPartNumberDescription(), getDeliveryRequestReference(), //
				getJobRequestReference(), getSiteName(), tmpSerialNumber, getTeamName(), //
				getDeliverToCompanyType() != null ? getDeliverToCompanyType().getValue() : null, getDeliverToCompanyName(), getDeliverToCustomerName(), getDeliverToSupplierName(),
				getToUserFullName());
	}

	@Transient
	public Integer getJobRequestId() {
		return jobRequest != null ? jobRequest.getId() : null;
	}

	@Transient
	public void setJobRequestId(Integer jobRequestId) {
		if (jobRequest == null || !jobRequestId.equals(jobRequest.getId()))
			jobRequest = new JobRequest();
		jobRequest.setId(jobRequestId);
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
	public String getDeliveryRequestReference() {
		return deliveryRequest != null ? deliveryRequest.getReference() : null;
	}

	@Transient
	public void setDeliveryRequestReference(String deliveryRequestReference) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setReference(deliveryRequestReference);
	}

	@Transient
	public String getPartNumberName() {
		return partNumber != null ? partNumber.getName() : null;
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}

	@Transient
	public String getPartNumberImage() {
		return partNumber != null ? partNumber.getImage() : null;
	}

	@Transient
	public void setPartNumberImage(String partNumberImage) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setImage(partNumberImage);
	}

	@Transient
	public String getPartNumberDescription() {
		return partNumber != null ? partNumber.getDescription() : null;
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
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
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
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
	public String getTmpSerialNumber() {
		return tmpSerialNumber;
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
	public String getToUserFullName() {
		return deliveryRequest != null ? deliveryRequest.getToUserFullName() : null;
	}

	@Transient
	public void setToUserFullName(String toUserFullName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setToUserFullName(toUserFullName);
	}

	@Transient
	public String getJobRequestReference() {
		return jobRequest != null ? jobRequest.getReference() : null;
	}

	@Transient
	public void setJobRequestReference(String jobRequestReference) {
		if (jobRequest == null)
			jobRequest = new JobRequest();
		jobRequest.setReference(jobRequestReference);
	}

	@Transient
	public String getSiteName() {
		return jobRequest != null ? jobRequest.getSiteName() : null;
	}

	@Transient
	public void setSiteName(String siteName) {
		if (jobRequest == null)
			jobRequest = new JobRequest();
		jobRequest.setSiteName(siteName);
	}

	@Transient
	public String getTeamName() {
		return jobRequest != null ? jobRequest.getTeamName() : null;
	}

	@Transient
	public void setTeamName(String teamName) {
		if (jobRequest == null)
			jobRequest = new JobRequest();
		jobRequest.setTeamName(teamName);
	}

}
