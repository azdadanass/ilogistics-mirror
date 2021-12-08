package ma.azdad.model;

public enum IssueType {
	WRONG_KEYS("Wrong/Missing keys"),
	NON_ROUTES("Non accessible routes"),
	BAD_WEATHER("Bad weather"),
	NEED_CRANE("Need crane"),
	NEED_AUTHORIZATION("Need authorization"),
	SITE_PROPRIETARY_BLOCKAGE("Site proprietary blockage"),
	SITE_NEIGHBORS_BLOCKAGE("Site neighbors blockage"),
	LACK_OF_SPACE("Lack of space"),
	INADEQUATE_SPACE("Inadequate space"),
	MISSING_TECHNICAL_REQUIREMENT("Missing technical requirement"),
	CIVIL("Civil"),
	POWER("Power/Battery backup"),
	AIR_CONDITIONING("Air conditioning"),
	OTHER("Other");

	private final String value;

	private IssueType(String value) {
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