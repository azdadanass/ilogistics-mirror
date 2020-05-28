package ma.azdad.model;

public enum DeliveryRequestState {
	WAITING("Waiting"),
	PARTIALLY_DELIVRED("Partially Delivered"),
	DELIVRED("Delivred"),
	REJECTED("Rejected");

	private final String value;

	private DeliveryRequestState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}