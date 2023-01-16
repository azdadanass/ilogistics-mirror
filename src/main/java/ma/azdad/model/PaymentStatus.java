package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum PaymentStatus {
	PENDING("Initiated","Pending", Color.ORANGE),
	SUBMITTED("Submitted","Pending", Color.PINK),
	CONFIRMED("Mapped Invoice","Mapped", Color.BLUE),
	MAPPED("Mapped ifinance","Mapped", Color.GREEN),;

	private final String value;
	private final String oldValue;
	private final Color color;

	private PaymentStatus(String value, String oldValue, Color color) {
		this.value = value;
		this.oldValue = oldValue;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public String getOldValue() {
		return oldValue;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static PaymentStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}