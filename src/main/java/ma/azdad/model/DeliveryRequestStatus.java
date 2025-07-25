package ma.azdad.model;

import java.util.stream.Stream;

public enum DeliveryRequestStatus {
	EDITED("Edited", "orange", "#e8b110"),
	REQUESTED("Requested", "pink", "#c6699f"),
	APPROVED1("Approved L1", "green", "#69aa46"),
	APPROVED2("Approved L2", "green", "#69aa46"),
	DELIVRED("Delivred", "blue", "#ff851d"),
	PARTIALLY_DELIVRED("Partially Delivered", "light-blue", "#6fb3e0"),
	ACKNOWLEDGED("Acknowledged", "purple", "#a069c3"),
	REJECTED("Rejected", "red", "#dd5a43"),
	CANCELED("Canceled", "red", "#dd5a43"),;

	private final String value;
	private final String color;
	private final String colorCode;

	private DeliveryRequestStatus(String value, String color, String colorCode) {
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

	public String getBadge() {
		return "badge badge-" + color;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static DeliveryRequestStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}