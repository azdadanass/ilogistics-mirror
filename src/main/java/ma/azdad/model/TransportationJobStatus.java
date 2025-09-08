package ma.azdad.model;

import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;

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
	ACKNOWLEDGED("Acknowledged", Color.GREEN);

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
	
	static public TransportationJobStatus get(Integer ordinal) {
		if(ObjectUtils.equals(ordinal, -1))
			return null;
		try {
			return TransportationJobStatus.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}

	public static TransportationJobStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}
