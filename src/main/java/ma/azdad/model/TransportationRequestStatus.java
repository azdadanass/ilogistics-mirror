package ma.azdad.model;

import java.util.stream.Stream;

public enum TransportationRequestStatus {
	EDITED("Edited", "orange", "#e8b110"),
	REQUESTED("Requested", "blue", "#ff851d"),
	APPROVED("Approved", "green", "#69aa46"),
	ASSIGNED("Assigned", "light-blue", "#6fb3e0"),
	PICKEDUP("Picked up", "pink", "#c6699f"),
	DELIVERED("Delivered", "green", "#69aa46"),
	ACKNOWLEDGED("Acknowledged", "purple", "#a069c3"),
	REJECTED("Rejected", "red", "#dd5a43"),
	CANCELED("Canceled", "red", "#dd5a43");

	private final String value;
	private final String color;
	private final String colorCode;

	private TransportationRequestStatus(String value, String color, String colorCode) {
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

	public static TransportationRequestStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}