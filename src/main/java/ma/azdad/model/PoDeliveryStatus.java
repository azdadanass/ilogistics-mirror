package ma.azdad.model;

import ma.azdad.utils.Color;

public enum PoDeliveryStatus {
	PENDING("Pending", Color.ORANGE),
	PARTIALLY_DELIVRED("Partially Delivered", Color.BLUE),
	DELIVRED("Delivred", Color.GREEN),;

	private final String value;
	private final Color color;

	private PoDeliveryStatus(String value, Color color) {
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