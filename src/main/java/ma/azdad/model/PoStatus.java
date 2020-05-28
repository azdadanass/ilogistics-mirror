package ma.azdad.model;

import java.io.Serializable;

public enum PoStatus implements Serializable {
	EDITED("Edited"),
	APPROVED1("Approval in Progress (Buyer)"),
	APPROVED2("Approval in Progress (PM)"),
	APPROVED3("Approval in Progress (LoB MGR)"),
	APPROVED4("Approved"),
	SUBMITTED("Submitted"),
	INPROGRESS("In Progress"),
	RECEIVING("Receiving"),
	RECEIVED("Received"),
	OPEN("Open"),
	CLOSED("Closed"),
	REJECTED("Rejected");

	private String value;

	private PoStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
