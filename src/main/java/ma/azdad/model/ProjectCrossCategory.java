package ma.azdad.model;

public enum ProjectCrossCategory {
	PROJECT_TO_PROJECT("Project To Project", "blue"),
	SINGLE_PROJECT("Single Project", "green"),
	SHIFT("Cost / Revenue Shift", "purple"),;

	private final String value;
	private final String color;

	private ProjectCrossCategory(String value, String color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return this.value;
	}
}