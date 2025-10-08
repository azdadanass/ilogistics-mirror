package ma.azdad.model;

public enum StopType {
	PICKUP("Pick Up", "blue"),
	STOP("Stop", "purple"),
	DELIVERY("Delivery", "green"),
	DELIVERY_AND_PICKUP("Delivery & Pick Up", "purple"),;

	private final String value;
	private final String color;

	private StopType(String value, String color) {
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