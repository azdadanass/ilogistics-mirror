package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class JobRequestDeliveryDetail extends GenericBean implements Serializable {

	private Double quantity = 0.0;
	private Double receivedQuantity = 0.0;
	private Double installedQuantity = 0.0;
	private Double additionalQuantity = 0.0;
	private Boolean isSerialNumberRequired = false;

	private JobRequest jobRequest;
	private DeliveryRequestDetail deliveryRequestDetail;

	//TMP
	private Integer tmpDeliveryRequestDetailId;
	private String tmpPartNumberName;
	private String tmpPartNumberDescription;
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

	public JobRequestDeliveryDetail(Double installedQuantity, Boolean isSerialNumberRequired, Integer tmpDeliveryRequestDetailId, String tmpPartNumberName, String tmpPartNumberDescription,
			Integer referenceNumber, DeliveryRequestType deliveryRequestType, Integer tmpJobRequestId, String tmpSiteName, String tmpTeamName) {
		super();
		this.installedQuantity = installedQuantity;
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.tmpDeliveryRequestDetailId = tmpDeliveryRequestDetailId;
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestReference = "DN" + (deliveryRequestType.ordinal() + 1) + String.format("%05d", referenceNumber);
		this.tmpJobRequestId = tmpJobRequestId;
		this.tmpJobRequestReference = "JR" + String.format("%05d", tmpJobRequestId);
		this.tmpSiteName = tmpSiteName;
		this.tmpTeamName = tmpTeamName;
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

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && jobRequest != null)
			result = jobRequest.filter(query);
		if (!result && tmpPartNumberName != null)
			result = tmpPartNumberName.toLowerCase().contains(query);
		if (!result && tmpPartNumberDescription != null)
			result = tmpPartNumberDescription.toLowerCase().contains(query);
		if (!result && tmpDeliveryRequestReference != null)
			result = tmpDeliveryRequestReference.toLowerCase().contains(query);
		if (!result && tmpJobRequestReference != null)
			result = tmpJobRequestReference.toLowerCase().contains(query);
		if (!result && tmpSiteName != null)
			result = tmpSiteName.toLowerCase().contains(query);
		if (!result && tmpTeamName != null)
			result = tmpTeamName.toLowerCase().contains(query);
		if (!result && tmpSerialNumber != null)
			result = tmpSerialNumber.toLowerCase().contains(query);
		return result;
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

}
