package ma.azdad.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Assignment;
import ma.azdad.model.AssignmentDetail;
import ma.azdad.model.Project;
import ma.azdad.service.AssignmentDetailService;
import ma.azdad.service.AssignmentService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class AssignmentView extends GenericView<Assignment> {

	@Autowired
	protected AssignmentService assignmentService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected ProjectView projectView;

	@Autowired
	protected ProjectService projectService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected FileView fileView;

	@Autowired
	AssignmentDetailService assignmentDetailService;

	private Assignment assignment = new Assignment();
	private DualListModel<Project> projectDualList;

	private Boolean assignator = true;
	private Boolean showDetailList = false;
	private Boolean active = true;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isAddPage) {
			assignment = new Assignment(sessionView.getUser());
			projectDualList = new DualListModel<>(projectView.findLightByManagerAndOpen(), new ArrayList<Project>());
		} else if (isEditPage) {
			assignment = assignmentService.findOne(selectedId);
			assignment.init();
			List<Project> target = projectService.findLightByAssignment(assignment.getId());
			List<Project> source = projectView.findLightByManagerAndOpen();
			source.removeAll(target);
			projectDualList = new DualListModel<>(source, target);
		} else if (isViewPage)
			assignment = assignmentService.findOne(selectedId);
	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = assignmentService.find(sessionView.getUsername(), assignator, active);
	}

	public void refreshAssignment() {
		assignmentService.flush();
		assignment = assignmentService.findOne(assignment.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		if (isViewPage && !sessionView.isTheConnectedUser(assignment.getAssignator()))
			cacheView.accessDenied();

	}

	// SAVE DELEGATION
	public Boolean canSaveAssignment() {
		if (isListPage || isAddPage)
			return sessionView.isPM();
		else if (isViewPage || isEditPage)
			return sessionView.isTheConnectedUser(assignment.getAssignator().getUsername());
		return false;
	}

	public String saveAssignment() {
		if (!canSaveAssignment())
			return listPage;
		if (!validateAssignment())
			return null;
		assignment.setCreationDate(new Date());
		assignment.setUser(userService.findOne(assignment.getUserUsername()));
		for (AssignmentDetail assignmentDetail : assignment.getDetailList())
			assignmentDetailService.delete(assignmentDetail);
		assignment.getDetailList().clear();
		for (Project project : projectDualList.getTarget())
			assignment.getDetailList().add(new AssignmentDetail(projectService.findOne(project.getId()), assignment));
		assignment = assignmentService.save(assignment);
		return addParameters(viewPage, "faces-redirect=true", "id=" + assignment.getId());
	}

	private Boolean validateAssignment() {
		if (assignment.getStartDate().compareTo(assignment.getEndDate()) > 0) {
			FacesContextMessages.ErrorMessages("Start Date should be lower than End Date");
			return false;
		}
		if (projectDualList.getTarget().isEmpty()) {
			FacesContextMessages.ErrorMessages("Project Target List should not be null");
			return false;
		}
		return true;
	}

	// DELETE DELEGATION
	public Boolean canDeleteAssignment() {
		return sessionView.isTheConnectedUser(assignment.getUser().getUsername());
	}

	public String deleteAssignment() {
		if (canDeleteAssignment())
			assignmentService.delete(assignment);
		return listPage;
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public AssignmentService getAssignmentService() {
		return assignmentService;
	}

	public void setAssignmentService(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public DualListModel<Project> getProjectDualList() {
		return projectDualList;
	}

	public void setProjectDualList(DualListModel<Project> projectDualList) {
		this.projectDualList = projectDualList;
	}

	public Boolean getAssignator() {
		return assignator;
	}

	public void setAssignator(Boolean assignator) {
		this.assignator = assignator;
	}

	public Boolean getShowDetailList() {
		return showDetailList;
	}

	public void setShowDetailList(Boolean showDetailList) {
		this.showDetailList = showDetailList;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
