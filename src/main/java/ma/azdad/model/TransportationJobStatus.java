package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum TransportationJobStatus {
	//new workflow
	EDITED("Edited", Color.ORANGE), // date1
	ASSIGNED1("Assigned To Transporter", Color.PINK), // date2
	ASSIGNED2("Assigned", Color.PURPLE), // date3
	ACCEPTED("Accepted", Color.L_BLUE), // date4
	STARTED("Started", Color.BLUE), // date5
	IN_PROGRESS("In Progress", Color.ORANGE), //date6
	COMPLETED("Completed", Color.L_GREEN),//date7
	CLOSED("Closed",Color.GREY),//date8
	NOT_STARTED("Not Started", Color.ORANGE); // to remove

	private final String value;
	private final Color color;

	private TransportationJobStatus(String value, Color color) {
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

	public static TransportationJobStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}
