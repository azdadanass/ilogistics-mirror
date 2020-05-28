package ma.azdad.model;

public enum Priority {
	MINOR("Minor", "green", "#69aa46"),
	MEDIUM("Medium", "blue", "#ff851d"),
	HIGH("High", "orange", "#e8b110"),
	CRITICAL("Critical", "red", "#dd5a43");

	private final String value;
	private final String color;
	private final String colorCode;

	private Priority(String value, String color, String colorCode) {
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