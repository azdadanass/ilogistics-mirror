package ma.azdad.model;

import ma.azdad.utils.Color;

public enum PartNumberClass {
	HW("HW", Color.BLUE),
	SW("SW", Color.GREEN),
	SRV("SRV", Color.ORANGE),;

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