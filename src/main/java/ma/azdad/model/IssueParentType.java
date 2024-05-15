package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum IssueParentType {
	JR("JR", Color.ORANGE),
	TICKET("Ticket", Color.PINK),
	DN("DN", Color.GREEN);

	private final String value;
	private final Color color;

	private IssueParentType(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	public String getBadge() {
		return "badge badge-" + color;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static IssueParentType getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}