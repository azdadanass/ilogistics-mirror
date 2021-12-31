package ma.azdad.model;

import ma.azdad.utils.Color;

public enum PartNumberClass {
	HW("Hardware", Color.BLUE), SW("Software", Color.GREEN), SRV("Service", Color.ORANGE),;

	private final String value;
	private final Color color;

	private PartNumberClass(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return this.value;
	}

}