package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum JobRequestStatus {
	EDITED("Edited", Color.ORANGE), // date1
	REQUESTED("Requested", Color.PINK), // date2
	APPROVED1("Approved L1", Color.GREEN), // date3
	APPROVED2("Approved L2", Color.GREEN), // date12
	ASSIGNED1("Assigned To Supplier", Color.PINK), // date11
	ASSIGNED2("Assigned", Color.PURPLE), // date4
	ACCEPTED("Accepted", Color.L_BLUE), // date5
	STARTED("Started", Color.BLUE), // date6
	IN_PROGRESS("In Progress", Color.ORANGE),
	COMPLETED("Completed", Color.L_GREEN), // date7
	VALIDATED("Validated", Color.GREEN), // date8
	REJECTED("Rejected", Color.RED), // date9
	CANCELED("Canceled", Color.RED),;// date10

	private final String value;
	private final Color color;

	private JobRequestStatus(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	public String getBadge() {
		return "badge badge-" + color;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static JobRequestStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}