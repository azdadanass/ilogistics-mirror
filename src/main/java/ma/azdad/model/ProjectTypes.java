package ma.azdad.model;

import java.io.Serializable;

public enum ProjectTypes implements Serializable {
	SALES("Sales"),
	SUPPORT("Support"),
	LOB("Lob"),
	DELIVERY("Delivery"),
	TPSR("TPSR"),
	CROSSCHARGE("Cross Charge"),
	STOCK("Stock"),;

	private String value;

	private ProjectTypes(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}