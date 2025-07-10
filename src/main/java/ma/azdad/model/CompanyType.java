package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum CompanyType {
	COMPANY("Company", Color.ORANGE),
	CUSTOMER("Customer", Color.GREEN),
	SUPPLIER("Supplier", Color.BLUE),
	OTHER("Other", Color.GREY),
	CONSULTANT("Consultant", Color.PINK),;

	private final String value;
	private final Color color;

	private CompanyType(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}
	
	public static CompanyType getByValue(String value) {
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