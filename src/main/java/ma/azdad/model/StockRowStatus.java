package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum StockRowStatus {
	BRAND_NEW("Brand New", Color.GREEN),
	USED("Used", Color.GREEN),
	HARDWARE_FAULT("Hardware Fault", Color.RED),
	WATER_DAMAGE("Water damage", Color.PURPLE),
	PHYSICAL_DAMAGE("Physical damage", Color.GREY),
	FIRE_DAMAGE("Fire damage", Color.ORANGE),
	OTHER("Other", Color.GREY);

	private final String value;
	private final Color color;

	private StockRowStatus(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	

	public Color getColor() {
		return color;
	}
	
	public static StockRowStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.value;
	}
}
