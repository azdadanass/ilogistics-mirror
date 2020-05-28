package ma.azdad.model;

public enum TransportationRequestPaymentStatus {
	PENDING("Pending", "orange"),
	PAYMENT_IN_PROGRESS("Payment In Progress", "blue"),
	PAYMENT_PENDING("Payement Pending", "purple"),
	PAYMENT_CONFIRMED("Payement Confirmed", "green"),;

	private final String value;
	private final String color;

	private TransportationRequestPaymentStatus(String value, String color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	//	public static void main(String[] args) {
	//		int i = 0;
	//		for (TransportationRequestPaymentStatus c : TransportationRequestPaymentStatus.values()) {
	//			System.out.println((i++) + "-" + c.getValue());
	//		}
	//	}

	@Override
	public String toString() {
		return this.value;
	}
}