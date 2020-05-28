package ma.azdad.model;

public enum UserDataStatus {
	ACTIVE("Active");

	private final String value;

	private UserDataStatus(String value) {
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