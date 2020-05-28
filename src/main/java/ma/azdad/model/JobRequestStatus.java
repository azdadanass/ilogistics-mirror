package ma.azdad.model;

import ma.azdad.utils.Color;

public enum JobRequestStatus {
	EDITED("Edited", Color.ORANGE),
	REQUESTED("Requested", Color.PINK),
	APPROVED("Approved", Color.GREEN),
	ASSIGNED("Assigned", Color.BLUE),
	ACCEPTED("Accepted", Color.L_BLUE),
	STARTED("Started", Color.BLUE),
	IN_PROGRESS("In Progress", Color.PURPLE),
	COMPLETED("Completed", Color.L_GREEN),
	VALIDATED("Validated", Color.GREEN),
	REJECTED("Rejected", Color.RED),
	CANCELED("Canceled", Color.RED),;

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

	@Override
	public String toString() {
		return this.value;
	}
}