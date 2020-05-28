package ma.azdad.model;

import java.io.Serializable;

public enum ExpensepaymentStatus implements Serializable {
	REQUESTED("Requested"),
	APPROVED("Approuved"),
	PAID("Paid"),
	ACKNOWLEDGED("Acknowledged"),
	REJECTED("Rejected"),
	ALL("All");

	private String value;

	private ExpensepaymentStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
