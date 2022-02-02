package ma.azdad.model;

import ma.azdad.utils.Color;

public enum PoBoqStatus {
	PENDING("Pending", Color.ORANGE),
	IN_PROGRESS("BoQ in Progress", Color.BLUE),
	MAPPED("BoQ Mapped", Color.GREEN),;

	private final String value;
	private final Color color;

	private PoBoqStatus(String value, Color color) {
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