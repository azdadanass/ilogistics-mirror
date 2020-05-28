package ma.azdad.model;

public enum PartNumberState {
	DRAFT("Draft"), CONFIRMED("Confirmed");

	private final String value;

	private PartNumberState(String value) {
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