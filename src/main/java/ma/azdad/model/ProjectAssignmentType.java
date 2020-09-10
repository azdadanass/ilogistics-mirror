package ma.azdad.model;

import ma.azdad.utils.Color;

public enum ProjectAssignmentType {
	INTERNAL("Internal", Color.BLUE),
	SUPPLIER("Supplier", Color.PURPLE),
	TEAM("Team", Color.GREEN),
	EXTERNAL_PM("External PM", Color.ORANGE),;

	private final String value;
	private final Color color;

	private ProjectAssignmentType(String value, Color color) {
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
