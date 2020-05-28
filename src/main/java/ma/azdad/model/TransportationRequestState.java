package ma.azdad.model;

public enum TransportationRequestState {
	WAITING("Waiting"), HANDLED("Handled"), DELIVRED("Delivred"), REJECTED("Rejected");

	private final String value;

	private TransportationRequestState(String value) {
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