package ma.azdad.model;

import ma.azdad.utils.Color;

public enum TransportationJobAssignmentType {
	TRANSPORTER("Transporter", Color.BLUE), //
	INTERNAL_DRIVER("Internal Driver", Color.GREEN), //
	EXTERNAL_DRIVER("External Driver", Color.ORANGE),;

	private final String value;
	private final Color color;

	private TransportationJobAssignmentType(String value, Color color) {
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