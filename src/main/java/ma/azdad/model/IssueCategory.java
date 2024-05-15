package ma.azdad.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class IssueCategory extends GenericModel<Integer> {

	private String name;
	private IssueParentType parentType;

	private Project project;

	private List<IssueType> typeList = new ArrayList<IssueType>();

	public boolean filter(String query) {
		return contains(query, name);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	public void addType(IssueType type) {
		type.setCategory(this);
		typeList.add(type);
	}

	public void removeType(IssueType type) {
		type.setCategory(null);
		typeList.remove(type);
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

	@ManyToOne(fetch = FetchType.LAZY)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<IssueType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<IssueType> typeList) {
		this.typeList = typeList;
	}

	@Enumerated(EnumType.STRING)
	public IssueParentType getParentType() {
		return parentType;
	}

	public void setParentType(IssueParentType parentType) {
		this.parentType = parentType;
	}

}
