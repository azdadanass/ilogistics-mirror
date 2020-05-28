package ma.azdad.model;

public enum DeliverToType {
	USER("Internal Resource"),
	EXTERNAL("External Resource");

	private final String value;

	private DeliverToType(String value) {
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