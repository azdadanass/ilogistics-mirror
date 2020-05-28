package ma.azdad.model;

public enum StockRowStatus {
	NORMAL("Normal", "green"),
	FAULTY("Faulty", "red"),
	WATER_DAMAGE("Water damage", "purple"),
	PHYSICAL_DAMAGE("Physical damage", "grey"),
	FIRE_DAMAGE("Fire damage", "orange");

	private final String value;
	private final String color;

	private StockRowStatus(String value, String color) {
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
