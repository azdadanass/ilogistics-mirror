package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.model.ProjectStatus;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.User;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class ProjectView {

	@Autowired
	protected ProjectService projectService;

	@Autowired
	private SessionView sessionView;

	@Autowired
	private UserService userService;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private CacheView cacheView;

	private String currentPath;
	private Integer selectedId;
	protected List<Project> list1 = new ArrayList<>();
	protected List<Project> list2 = new ArrayList<>();
	protected List<Project> list3;
	protected List<Project> list4;
	private String searchBean = "";

	private Project project = new Project();

	@PostConstruct
	public void init() {
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		selectedId = UtilsFunctions.getIntegerParameter("id");
		refreshList();
		if ("/partNumberThresholdList.xhtml".equals(currentPath))
			project = projectService.findOne(selectedId);
	}

	public void redirect() {
		if (!sessionView.isTheConnectedUser(project.getManager().getUsername()))
			cacheView.accessDenied();
	}

	public void refreshList() {
		if ("/viewUser.xhtml".equals(currentPath)) {
			List<Project> list = projectService.findLightByResource(sessionView.getUsername());
			Integer customerId = userService.findCustomerId(selectedId);
			if (customerId == null)
				list2 = list1 = list;
			else
				list2 = list1 = list.stream().filter(project -> customerId.equals(project.getTmpCustomerId())).collect(Collectors.toList());
		} else if ("/projectList.xhtml".equals(currentPath))
			list2 = list1 = projectService.findLightByManager(sessionView.getUsername());
	}

	public void setProject(Integer projectId) {
		project = projectService.findOne(projectId);
	}

	private void filterBean(String query) {
		list3 = null;
		List<Project> list = new ArrayList<>();
		query = query.toLowerCase().trim();
		for (Project bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

//	public Boolean canEditCustomerWarehousing() {
//		return project.getCustomerWarehousing() deliveryRequestService.countByProject(project.getId()) == 0;
//	}

//	public Boolean canEditCustomerStockManagement() {
//		return project.getCustomerWarehousing();
//	}

	public Boolean canEdit() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername());
	}

	public void edit() {
		if (!canEdit())
			return;
		project = projectService.save(project);
		refreshList();
	}

	// GENERIC
	public List<Project> findLightOpen() {
		return projectService.findLight(ProjectStatus.OPEN.getValue());
	}

	public List<Project> findLight() {
		return projectService.findLight();
	}

	public List<Project> findLightByManagerAndOpen(String managerUsername) {
		return projectService.findLightByManagerAndOpen(managerUsername);
	}

	public List<Project> findLightByManagerAndOpen() {
		return findLightByManagerAndOpen(sessionView.getUsername());

	}

	public Boolean isStockProject(Integer projectId) {
		return ProjectTypes.STOCK.getValue().equals(projectService.getType(projectId));
	}

	public List<Project> findLightByResource(String username, DeliveryRequestType deliveryRequestType) {
		if (deliveryRequestType == null)
			return null;
		switch (deliveryRequestType) {
		case INBOUND:
			return projectService.findInboundProjectList(username);
		case XBOUND:
			return projectService.findXboundProjectList(username);
		case OUTBOUND:
			return projectService.findOutboundProjectList(username);
		default:
			return null;
		}
	}

	public List<Project> findLightByResource(DeliveryRequestType deliveryRequestType) {
		return findLightByResource(sessionView.getUsername(), deliveryRequestType);
	}

	public User findFirstManagerByType(Integer projectId, ProjectManagerType managerType) {
		return projectService.findFirstManagerByType(projectId, managerType);
	}

	public User findFirstHardwareManager(Integer projectId) {
		return findFirstManagerByType(projectId, ProjectManagerType.HARDWARE_MANAGER);
	}

	// GETTERS & SETTERS
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public Integer getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Integer selectedId) {
		this.selectedId = selectedId;
	}

	public List<Project> getList1() {
		return list1;
	}

	public void setList1(List<Project> list1) {
		this.list1 = list1;
	}

	public List<Project> getList2() {
		return list2;
	}

	public void setList2(List<Project> list2) {
		this.list2 = list2;
	}

	public List<Project> getList3() {
		return list3;
	}

	public void setList3(List<Project> list3) {
		this.list3 = list3;
	}

	public List<Project> getList4() {
		return list4;
	}

	public void setList4(List<Project> list4) {
		this.list4 = list4;
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

}