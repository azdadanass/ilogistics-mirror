package ma.azdad.utils;

public enum Color {
	BLUE("blue", "#ff851d", "badge badge-primary"),
	L_BLUE("light-blue", "#6fb3e0", "badge badge-primary"),
	GREEN("green", "#69aa46", "badge badge-success"),
	L_GREEN("light-green", "#69aa46", "badge badge-success"),
	RED("red", "#dd5a43", "badge badge-danger"),
	ORANGE("orange", "#e8b110", "badge badge-warning"),
	PURPLE("purple", "#a069c3", "badge badge-purple"),
	PINK("pink", "#c6699f", "badge badge-pink"),

	;

	private final String name;
	private final String colorCode;
	private final String badge;

	private Color(String name, String colorCode, String badge) {
		this.name = name;
		this.colorCode = colorCode;
		this.badge = badge;
	}

	public String getName() {
		return name;
	}

	public String getColorCode() {
		return colorCode;
	}

	public String getBadge() {
		return badge;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
