package ma.azdad.model;

public enum InboundType {
	NEW("New", "green", "#69aa46"),
	RETURN("Return From Outbound", "red", "#dd5a43"),
	TRANSFER("Transfer from Outbound", "blue", "#ff851d"),;

	private final String value;
	private final String color;
	private final String colorCode;

	private InboundType(String value, String color, String colorCode) {
		this.value = value;
		this.color = color;
		this.colorCode = colorCode;
	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	public String getColorCode() {
		return colorCode;
	}

	@Override
	public String toString() {
		return this.value;
	}
}