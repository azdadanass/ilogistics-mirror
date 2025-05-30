package ma.azdad.model;

public enum OutboundType {
	NORMAL("Normal", "green", "#69aa46"),
	TRANSFER("Transfer", "blue", "#dd5a43"),
	PLANNED_RETURN("Planned Return", "orange", "#ff851d"),;

	private final String value;
	private final String color;
	private final String colorCode;

	private OutboundType(String value, String color, String colorCode) {
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