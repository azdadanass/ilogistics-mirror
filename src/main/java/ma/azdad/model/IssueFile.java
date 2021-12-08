package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IssueFile extends GenericFile<Issue> {

	public IssueFile() {
	}

	public IssueFile(File file, String type, String name, User user) {
		super(file, type, name, user);
	}

	public IssueFile(File file, String type, String name, User user, Issue parent) {
		super(file, type, name, user, parent);
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
