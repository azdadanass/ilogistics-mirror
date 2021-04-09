package ma.azdad.utils;

public enum Color {
	GREY("grey", "#777777", "badge", "50C8C8C8"),
	RED("red", "#dd5a43", "badge badge-red", "501400FF"),
	PINK("pink", "#c6699f", "badge badge-pink", "50781EF0"),
	YELLOW("yellow", "#fff014", "badge badge-yellow", "5014F0FF"),
	ORANGE("orange", "#e8b110", "badge badge-orange", "501478FF"),
	PURPLE("purple", "#a069c3", "badge badge-purple", "50782878"),
	L_BLUE("light-blue", "#93cbf9", "badge badge-light-blue", "50F0FA14"),
	BLUE("blue", "#478fca", "badge badge-blue", "50F01414"),
	L_GREEN("light-green", "#b0d877", "badge badge-light-green", "5078E614"),
	GREEN("green", "#69aa46", "badge badge-green", "5014B400"),;

	private final String name;
	private final String colorCode;
	private final String badge;
	private final String kmlColor;

	private Color(String name, String colorCode, String badge, String kmlColor) {
		this.name = name;
		this.colorCode = colorCode;
		this.badge = badge;
		this.kmlColor = kmlColor;
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

	public String getKmlColor() {
		return kmlColor;
	}

	public String getMapIcon() {
		return "http://public.3gcom.ma/img/mapIcon/" + name + ".png";
	}

	@Override
	public String toString() {
		return this.name;
	}

	static public Color get(int ordinal) {
		return Color.values()[ordinal % Color.values().length];
	}
}
