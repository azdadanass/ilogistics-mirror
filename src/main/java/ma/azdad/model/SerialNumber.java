package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity

public class SerialNumber extends GenericBean implements Serializable {

	private String name;

	private DeliveryRequestDetail deliveryRequestDetail;
	private JobRequest jobRequest;

	public SerialNumber() {
		super();
	}

	public SerialNumber(DeliveryRequestDetail deliveryRequestDetail, JobRequest jobRequest) {
		super();
		this.deliveryRequestDetail = deliveryRequestDetail;
		this.jobRequest = jobRequest;
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		return result;
	}

	@Column(length = 500)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public DeliveryRequestDetail getDeliveryRequestDetail() {
		return deliveryRequestDetail;
	}

	public void setDeliveryRequestDetail(DeliveryRequestDetail deliveryRequestDetail) {
		this.deliveryRequestDetail = deliveryRequestDetail;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public JobRequest getJobRequest() {
		return jobRequest;
	}

	public void setJobRequest(JobRequest jobRequest) {
		this.jobRequest = jobRequest;
	}

}
