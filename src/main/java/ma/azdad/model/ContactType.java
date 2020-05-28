package ma.azdad.model;

public enum ContactType {
	INTERNAL("Internal"),
	EXTERNAL("External");

	private final String value;

	private ContactType(String value) {
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