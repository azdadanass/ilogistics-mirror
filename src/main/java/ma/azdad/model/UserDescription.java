package ma.azdad.model;

import ma.azdad.utils.Color;

public class UserDescription {
	private String description;
	private User user;

	public UserDescription(String description, User user) {
		super();
		this.description = description;
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStyleClass() {
		switch (description) {
		case "Requester":
			return Color.ORANGE.getBadge();
		case "Assignator":
			return Color.BLUE.getBadge();
		case "Confirmator":
			return Color.GREEN.getBadge();
		case "Validator":
			return Color.GREEN.getBadge();
		case "Project Manager":
			return Color.PURPLE.getBadge();
		case "Line of Business Manager":
			return Color.PINK.getBadge();
		default:
			return null;
		}
	}

}