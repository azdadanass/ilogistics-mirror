package ma.azdad.model;

import java.util.stream.Stream;

public enum Severity {
	LOW("Low", "green", "#69aa46"),
	MEDIUM("Medium", "blue", "#ff851d"),
	HIGH("High", "orange", "#e8b110"),
	CRITICAL("Critical", "red", "#dd5a43");

	private final String value;
	private final String color;
	private final String colorCode;

	private Severity(String value, String color, String colorCode) {
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
	
	public static Severity getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.value;
	}

}