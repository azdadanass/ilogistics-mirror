package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryRequestState {
	WAITING("Waiting", Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)),
	PARTIALLY_DELIVRED("Partially Delivered", Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED)),
	DELIVRED("Delivred", Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)),
	REJECTED("Rejected", Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));

	private final String value;
	private final List<DeliveryRequestStatus> statusList;

	private DeliveryRequestState(String value, List<DeliveryRequestStatus> statusList) {
		this.value = value;
		this.statusList = statusList;
	}

	public String getValue() {
		return value;
	}

	public List<DeliveryRequestStatus> getStatusList() {
		return statusList;
	}

	@Override
	public String toString() {
		return this.value;
	}
}