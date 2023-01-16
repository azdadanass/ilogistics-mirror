package ma.azdad.model;

import java.util.Arrays;

import ma.azdad.utils.Color;

public enum PaymentMode {
	CHQ("CHQ", Color.ORANGE, true), VIRM("VIRM", Color.BLUE, false), PRLV("PRLV", Color.PURPLE, false), CASH("CASH", Color.GREEN, false),;

	private final String value;
	private final Color color;
	private final Boolean manual;

	private PaymentMode(String value, Color color, Boolean manual) {
		this.value = value;
		this.color = color;
		this.manual = manual;
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

	public static PaymentMode get(Integer ordinal) {
		try {
			return PaymentMode.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}

	public Boolean getManual() {
		return manual;
	}

	public static PaymentMode getByValue(String name) {
		return Arrays.stream(PaymentMode.values()).filter(i -> name.equalsIgnoreCase(i.getValue())).findFirst().get();
	}
}