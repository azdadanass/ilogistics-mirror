package ma.azdad.model;

import ma.azdad.utils.Color;

public enum PoIlogisticsStatus {
	PENDING("Pending", Color.ORANGE),
	IN_PROGRESS("In Progress", Color.BLUE),
	COMPLETED("Completed", Color.GREEN),;

	private final String value;
	private final Color color;

	private PoIlogisticsStatus(String value, Color color) {
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