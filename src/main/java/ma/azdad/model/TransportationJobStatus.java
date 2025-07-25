package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum TransportationJobStatus {
	//new workflow
	EDITED("Edited", Color.ORANGE), 
	ASSIGNED1("Assigned To Transporter", Color.PINK), 
	ASSIGNED2("Assigned", Color.PURPLE), 
	ACCEPTED("Accepted", Color.L_BLUE), 
	STARTED("Started", Color.BLUE), 
	IN_PROGRESS("In Progress", Color.ORANGE), 
	COMPLETED("Completed", Color.L_GREEN),
	CLOSED("Closed",Color.GREY),
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
