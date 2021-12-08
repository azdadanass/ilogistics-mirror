package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum IssueStatus {
	RAISED("Raised", Color.ORANGE),
	CONFIRMED("Confirmed", Color.BLUE),
	ASSIGNED("Assigned", Color.PINK),
	RESOLVED("Resolved", Color.GREEN),
	ACKNOWLEDGED("Acknowledged", Color.PURPLE),
	NOT_RESOLVED("Not Resolved", Color.RED),
	REJECTED("Rejected", Color.GREY);

	private final String value;
	private final Color color;

	private IssueStatus(String value, Color color) {
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

	public static IssueStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}