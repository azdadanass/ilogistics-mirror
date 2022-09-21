package ma.azdad.model;

import java.io.Serializable;

import ma.azdad.utils.Color;

public enum PoStatus implements Serializable {
	EDITED("Edited", Color.ORANGE),
	APPROVED1("Approval in Progress (Buyer)", Color.L_GREEN),
	APPROVED2("Approval in Progress (PM)", Color.L_GREEN),
	APPROVED3("Approval in Progress (LoB MGR)", Color.L_GREEN),
	APPROVED4("Approved", Color.GREEN),
	SUBMITTED("Submitted", Color.BLUE),
	IN_PROGRESS("In Progress", Color.PINK),
	RECEIVING("Receiving", Color.PINK),
	RECEIVED("Received", Color.PURPLE),
	OPEN("Open", Color.ORANGE),
	CLOSED("Closed", Color.GREY),
	REJECTED("Rejected", Color.RED);

	private final String value;
	private final Color color;

	private PoStatus(String value,Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

}
