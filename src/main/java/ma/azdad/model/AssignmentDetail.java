package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class AssignmentDetail extends GenericBean implements Serializable {

	private Project project;
	private Assignment assignment;

	public AssignmentDetail(Integer id, Date startDate, Date endDate, String assignatorPhoto, String assignatorFullName, String userPhoto, String userFullName, String projectName) {
		super(id);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setAssignatorPhoto(assignatorPhoto);
		this.setAssignatorFullName(assignatorFullName);
		this.setUserPhoto(userPhoto);
		this.setUserFullName(userFullName);
		this.setProjectName(projectName);
	}

	public AssignmentDetail() {
		super();
	}

	public AssignmentDetail(Project project, Assignment assignment) {
		super();
		this.project = project;
		this.assignment = assignment;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

	@Transient
	public Date getStartDate() {
		return assignment == null ? null : assignment.getStartDate();
	}

	@Transient
	public void setStartDate(Date startDate) {
		if (assignment == null)
			assignment = new Assignment();
		assignment.setStartDate(startDate);
	}

	@Transient
	public Date getEndDate() {
		return assignment == null ? null : assignment.getEndDate();
	}

	@Transient
	public void setEndDate(Date endDate) {
		if (assignment == null)
			assignment = new Assignment();
		assignment.setEndDate(endDate);
	}

	@Transient
	public String getAssignatorFullName() {
		return assignment == null ? null : assignment.getAssignatorFullName();
	}

	@Transient
	public void setAssignatorFullName(String assignatorFullName) {
		if (assignment == null)
			assignment = new Assignment();
		assignment.setAssignatorFullName(assignatorFullName);
	}

	@Transient
	public String getAssignatorPhoto() {
		return assignment == null ? null : assignment.getAssignatorPhoto();
	}

	@Transient
	public void setAssignatorPhoto(String assignatorPhoto) {
		if (assignment == null)
			assignment = new Assignment();
		assignment.setAssignatorPhoto(assignatorPhoto);
	}

	@Transient
	public String getUserFullName() {
		return assignment == null ? null : assignment.getUserFullName();
	}

	@Transient
	public void setUserFullName(String userFullName) {
		if (assignment == null)
			assignment = new Assignment();
		assignment.setUserFullName(userFullName);
	}

	@Transient
	public String getUserPhoto() {
		return assignment == null ? null : assignment.getUserPhoto();
	}

	@Transient
	public void setUserPhoto(String userPhoto) {
		if (assignment == null)
			assignment = new Assignment();
		assignment.setUserPhoto(userPhoto);
	}

	@Transient
	public String getProjectName() {
		return project == null ? null : project.getName();
	}

	@Transient
	public void setProjectName(String projectName) {
		if (project == null)
			project = new Project();
		project.setName(projectName);
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

}
