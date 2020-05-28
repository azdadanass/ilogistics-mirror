package ma.azdad.model;

public enum ProjectStatus {
	OPEN("Open"), CLOSED("Closed");
	
	private final String value;

	private ProjectStatus(String value) {
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