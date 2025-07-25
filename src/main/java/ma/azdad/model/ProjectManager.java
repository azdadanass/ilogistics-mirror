package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class ProjectManager extends GenericModel<Integer> {

	private ProjectManagerType type;
	private User user;
	private Project project;

	@Transient
	public String getUserUsername(){
		return user!=null?user.getUsername():null;
	}

	@Transient
	public void setUserUsername(String userUsername){
		if(user==null || !userUsername.equals(user.getUsername()))
			user=new User();
		user.setUsername(userUsername);
	}

	// getters & setters

	@Enumerated(EnumType.STRING)
	public ProjectManagerType getType() {
		return type;
	}

	public void setType(ProjectManagerType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
