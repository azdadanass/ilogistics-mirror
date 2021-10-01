package ma.azdad.model;

public enum PartNumberStatus {
	ACTIVE("Active"),
	OBSOLETE("Obsolete");

	private final String value;

	private PartNumberStatus(String value) {
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