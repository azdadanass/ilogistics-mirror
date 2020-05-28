package ma.azdad.model;

import java.io.Serializable;

public enum ProjectCrossTypes implements Serializable {
	REVENUE("Revenue"),
	COST("Cost");

	private String value;

	private ProjectCrossTypes(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
