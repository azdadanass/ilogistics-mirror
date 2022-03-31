package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import ma.azdad.utils.App;

@Entity
public class IssueCategory extends GenericModel<Integer> {

	private String name;
	private App app;

	public boolean filter(String query) {
		return contains(query, name);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
	
	


}
