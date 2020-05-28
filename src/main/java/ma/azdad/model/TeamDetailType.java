package ma.azdad.model;

public enum TeamDetailType {
	TEAM_LEADER("Team Leader", "green", "#69aa46"),
	TECHNICIAN("Technician", "blue", "#ff851d"),
	TYPE3("Type3", "blue", "#ff851d"),;

	private final String value;
	private final String color;
	private final String colorCode;

	private TeamDetailType(String value, String color, String colorCode) {
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

	@Override
	public String toString() {
		return this.value;
	}
}