package ma.azdad.model;

public enum TeamType {
	INTERNAL("Internal", "green", "#69aa46"),
	EXTERNAL("External", "blue", "#ff851d"),
	MIXTE("Mixte", "purple", "#a069c3"),;

	private final String value;
	private final String color;
	private final String colorCode;

	private TeamType(String value, String color, String colorCode) {
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