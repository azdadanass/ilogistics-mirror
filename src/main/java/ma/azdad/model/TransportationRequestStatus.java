package ma.azdad.model;

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

	//	public static void main(String[] args) {
	//		int i = 0;
	//		for (TransportationRequestStatus c : TransportationRequestStatus.values()) {
	//			System.out.println((i++) + "-" + c.getValue());
	//		}
	//	}

	@Override
	public String toString() {
		return this.value;
	}
}