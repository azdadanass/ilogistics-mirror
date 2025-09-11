package ma.azdad.model;

public enum TransporterType {
	SUPPLIER("Supplier", "blue"),
	PRIVATE("Private", "purple"),
	INTERNAL("Internal", "green"),;

	private final String value;
	private final String color;

	private TransporterType(String value, String color) {
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