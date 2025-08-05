package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import ma.azdad.utils.Color;

@Entity
public class IssueHistory extends GenericHistory<Issue> {

	public IssueHistory() {
	}

	public IssueHistory(String status, User user) {
		super(status, user);
	}

	public IssueHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public IssueHistory(String status, User user, String description, Issue parent) {
		super(status, user, description, parent);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	@Transient
	public String getStatusStyleClass() {
		switch (status) {
		case "Created":
			return Color.ORANGE.getBadge();
		case "Handover":
			return Color.PURPLE.getBadge();
		default:
			try {
				return IssueStatus.getByValue(status).getColor().getBadge();
			} catch (Exception e) {
				return "badge";
			}
		}

	}
	
	

}
