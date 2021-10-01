package ma.azdad.model;

public enum WarehouseStatus {
	ACTIVE("Active"),
	NOTACTIVE("Not Active");

	private final String value;

	private WarehouseStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}