package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum InvoiceStatus {
	EDITED("Edited", Color.ORANGE),
	SUBMITTED("Submitted", Color.PINK),
	APPROVED1("Approved L1", Color.GREEN),
	APPROVED2("Approved L2", Color.BLUE),
	IN_PROGRESS("In Progress", Color.L_BLUE),
	PAID("Paid", Color.BLUE),
	REJECTED("Rejected", Color.RED),
	PENDING("Pending", Color.ORANGE), ;

	private final String value;
	private final Color color;

	private InvoiceStatus(String value, Color color) {
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

	public static InvoiceStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}