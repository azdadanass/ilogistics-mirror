package ma.azdad.model;

import ma.azdad.utils.Color;

public enum WorkingTime {
	WT1("24h / 7 days", Color.ORANGE), //
	WT2("Working Hours / Workings days", Color.GREEN);

	private final String value;
	private final Color color;

	private WorkingTime(String value, Color color) {
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