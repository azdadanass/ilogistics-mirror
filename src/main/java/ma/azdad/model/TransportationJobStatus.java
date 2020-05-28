package ma.azdad.model;

public enum TransportationJobStatus {
	NOT_STARTED("Not Started", "orange"),
	IN_PROGRESS("In Progress", "blue"),
	COMPLETED("Completed", "green"),
	CLOSED("Closed", "grey"),;

	private final String value;
	private final String color;

	private TransportationJobStatus(String value, String color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
