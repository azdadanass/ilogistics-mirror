package ma.azdad.model;

import java.io.Serializable;

public enum PoTypes implements Serializable {
	SUPPLIER("Supplier"),
	Customer("Customer");

	private String value;

	private PoTypes(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
