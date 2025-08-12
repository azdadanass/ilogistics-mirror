package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

public enum TransportationRequestState {
	WAITING("Waiting", Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED, TransportationRequestStatus.APPROVED)),
	HANDLED("Handled", Arrays.asList(TransportationRequestStatus.ASSIGNED, TransportationRequestStatus.PICKEDUP)),
	DELIVRED("Delivred", Arrays.asList(TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED)),
	REJECTED("Rejected", Arrays.asList(TransportationRequestStatus.REJECTED, TransportationRequestStatus.CANCELED)),
	
	PENDING("Pending", Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED, TransportationRequestStatus.APPROVED)),
	ASSIGNED("Assigned", Arrays.asList(TransportationRequestStatus.ASSIGNED)),
	IN_PROGRESS("In Progress", Arrays.asList(TransportationRequestStatus.PICKEDUP)),
	COMPLETED("Completed", Arrays.asList(TransportationRequestStatus.DELIVERED,TransportationRequestStatus.ACKNOWLEDGED)),
	
	;

	private final String value;
	private final List<TransportationRequestStatus> statusList;

	private TransportationRequestState(String value, List<TransportationRequestStatus> statusList) {
		this.value = value;
		this.statusList = statusList;
	}

	public String getValue() {
		return value;
	}

	public List<TransportationRequestStatus> getStatusList() {
		return statusList;
	}

	@Override
	public String toString() {
		return this.value;
	}
}