package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IssueComment extends GenericComment<Issue> {

	public IssueComment() {
	}

	public IssueComment(String title, User user) {
		super(title, user);
	}

	public IssueComment(String title, User user, Issue parent) {
		super(title, user, parent);
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

}
