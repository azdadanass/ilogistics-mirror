package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class JobRequestDelivery extends GenericModel<Integer> implements Serializable {

	private DeliveryRequest deliveryRequest;
	private List<JobRequestDeliveryDetail> detailList = new ArrayList<>();

	public JobRequestDelivery(DeliveryRequest deliveryRequest) {
		super();
		this.deliveryRequest = deliveryRequest;
	}

	public String getDeliveryRequestReference() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getReference();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((deliveryRequest == null) ? 0 : deliveryRequest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobRequestDelivery other = (JobRequestDelivery) obj;
		if (deliveryRequest == null) {
			if (other.deliveryRequest != null)
				return false;
		} else if (!deliveryRequest.equals(other.deliveryRequest))
			return false;
		return true;
	}

	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	public List<JobRequestDeliveryDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<JobRequestDeliveryDetail> detailList) {
		this.detailList = detailList;
	}

	@Override
	public String toString() {
		return "JobRequestDelivery [deliveryRequest=" + deliveryRequest.getId() + ", detailList=" + detailList.size() + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
