package ma.azdad.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import ma.azdad.model.ProjectManager;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.model.ProjectStatus;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.User;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

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
	private CacheView cacheView;

	private String currentPath;
	private Integer id;
	protected List<Project> list1 = new ArrayList<>();
	protected List<Project> list2 = new ArrayList<>();
	protected List<Project> list3;
	protected List<Project> list4;
	private String searchBean = "";
	private String status = "Open";

	private Project project = new Project();

	@PostConstruct
	public void init() {
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		id = UtilsFunctions.getIntegerParameter("id");
		refreshList();
		if ("/viewProject.xhtml".equals(currentPath))
			project = projectService.findOne(id);
	}

	public void redirect() {
		if (!sessionView.isTheConnectedUser(project.getManager().getUsername()))
			cacheView.accessDenied();
	}

	public void refreshList() {
		if ("/viewUser.xhtml".equals(currentPath)) {
			List<Project> list = projectService.findLightByResource(sessionView.getUsername(), cacheView.getDelegatedProjectList());
			Integer customerId = userService.findCustomerId(id);
			if (customerId == null)
				list2 = list1 = list;
			else
				list2 = list1 = list.stream().filter(project -> customerId.equals(project.getTmpCustomerId())).collect(Collectors.toList());
		} else if ("/projectList.xhtml".equals(currentPath))
			list2 = list1 = projectService.findLightByManager(sessionView.getUsername(), status);
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

	// managers
	public Boolean editManagerList = false;

	public Boolean canEditManagerList() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && !editManagerList;
	}

	public Boolean canAddManager() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && editManagerList;
	}

	public void addManager() {
		if (!canAddManager())
			return;
		project.addManager(new ProjectManager());
	}

	public Boolean canSaveManagerList() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && editManagerList;
	}

	public Boolean validateSaveManagerList() {
		Set<String> keySet = new HashSet<String>();
		for (ProjectManager manager : project.getManagerList()) {
			if (manager.getUserUsername() == null || manager.getUserUsername().isEmpty())
				return FacesContextMessages.ErrorMessages("User should not be null");
			if (manager.getType() == null)
				return FacesContextMessages.ErrorMessages("Type should not be null");
			if (!keySet.add(manager.getType().ordinal() + manager.getUserUsername()))
				return FacesContextMessages.ErrorMessages("Duplicate Line !");
		}

		return true;
	}

	public void saveManagerList() {
		if (!canSaveManagerList() || !validateSaveManagerList())
			return;
		project.getManagerList().forEach(i -> i.setUser(userService.findOneLight(i.getUserUsername())));
		projectService.save(project);
		project = projectService.findOne(project.getId());
		
		
		System.out.println(project.getManagerFullName());
		editManagerList = false;
	}

	public Boolean canDeleteManager() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername());
	}

	public void deleteManager(ProjectManager manager) {
		if (!canDeleteManager())
			return;
		project.removeManager(manager);
		projectService.save(project);
		project = projectService.findOne(project.getId());
	}

	// inplace
	public Boolean canEditInplace() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername());
	}

	public void updateApproximativeStoragePeriod() {
		if (!canEditInplace())
			return;
		projectService.updateApproximativeStoragePeriod(project.getId(), project.getApproximativeStoragePeriod());
	}

	public void updatePreferredWarehouse() {
		if (!canEditInplace())
			return;
		projectService.updatePreferredWarehouse(project.getId(), project.getPreferredWarehouseId());

		// clear preferredLocation
		project.setPreferredLocation(null);
		projectService.updatePreferredLocation(project.getId(), null);
	}

	public void updatePreferredLocation() {
		if (!canEditInplace())
			return;
		projectService.updatePreferredLocation(project.getId(), project.getPreferredLocationId());
	}

	public void updateCustomerWarehousing() {
		if (!canEditInplace())
			return;
		projectService.updateCustomerWarehousing(project.getId(), project.getCustomerWarehousing());
		if (!project.getCustomerWarehousing() && project.getCustomerStockManagement()) {
			project.setCustomerStockManagement(false);
			projectService.updateCustomerStockManagement(project.getId(), project.getCustomerStockManagement());
		}
	}

	public void updateCustomerStockManagement() {
		if (!canEditInplace())
			return;
		projectService.updateCustomerStockManagement(project.getId(), project.getCustomerStockManagement());
		if (project.getCustomerStockManagement() && !project.getCustomerWarehousing()) {
			project.setCustomerWarehousing(true);
			projectService.updateCustomerWarehousing(project.getId(), project.getCustomerWarehousing());
		}
	}

	public void updateCompanyWarehousing() {
		if (!canEditInplace())
			return;
		projectService.updateCompanyWarehousing(project.getId(), project.getCompanyWarehousing());
		if (!project.getCompanyWarehousing() && project.getCompanyStockManagement()) {
			project.setCompanyStockManagement(false);
			projectService.updateCompanyStockManagement(project.getId(), project.getCompanyStockManagement());
		}
	}

	public void updateCompanyStockManagement() {
		if (!canEditInplace())
			return;
		projectService.updateCompanyStockManagement(project.getId(), project.getCompanyStockManagement());
		if (project.getCompanyStockManagement() && !project.getCompanyWarehousing()) {
			project.setCompanyWarehousing(true);
			projectService.updateCompanyWarehousing(project.getId(), project.getCompanyWarehousing());
		}
	}

	public void updateSupplierWarehousing() {
		if (!canEditInplace())
			return;
		projectService.updateSupplierWarehousing(project.getId(), project.getSupplierWarehousing());
		if (!project.getSupplierWarehousing() && project.getSupplierStockManagement()) {
			project.setSupplierStockManagement(false);
			projectService.updateSupplierStockManagement(project.getId(), project.getSupplierStockManagement());
		}
	}

	public void updateSupplierStockManagement() {
		if (!canEditInplace())
			return;
		projectService.updateSupplierStockManagement(project.getId(), project.getSupplierStockManagement());
		if (project.getSupplierStockManagement() && !project.getSupplierWarehousing()) {
			project.setSupplierWarehousing(true);
			projectService.updateSupplierWarehousing(project.getId(), project.getSupplierWarehousing());
		}
	}

	public void updateSdm() {
		if (!canEditInplace())
			return;
		projectService.updateSdm(project.getId(), project.getSdm());
		if (project.getSdm()) {
			project.setIsm(false);
			projectService.updateIsm(project.getId(), false);
		}
	}

	public void updateIsm() {
		if (!canEditInplace())
			return;
		projectService.updateIsm(project.getId(), project.getIsm());
		if (project.getIsm()) {
			project.setSdm(false);
			projectService.updateSdm(project.getId(), false);
		}
	}

	// generic
	public List<Project> findLightOpen() {
		return projectService.findLight(ProjectStatus.OPEN.getValue());
	}

	public List<Project> findLight() {
		return projectService.findLight();
	}

	public List<Project> findLight(Collection<Integer> idList) {
		return projectService.findLight(idList);
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

	public Boolean isCustomerProject(Integer projectId) {
		return projectService.isCustomerProject(projectId);
	}

	public List<Project> findLightByResource(String username, DeliveryRequestType deliveryRequestType) {
		if (deliveryRequestType == null)
			return null;
		switch (deliveryRequestType) {
		case INBOUND:
			return projectService.findInboundProjectList(username, cacheView.getDelegatedProjectList());
		case XBOUND:
			return projectService.findXboundProjectList(username, cacheView.getDelegatedProjectList());
		case OUTBOUND:
			return projectService.findOutboundProjectList(username, cacheView.getDelegatedProjectList());
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

	public List<Project> findProjectListHavingIssues() {
		return projectService.findProjectListHavingIssues(sessionView.getUsername(), cacheView.getAllProjectList(), cacheView.getDelegatedLobIdList());
	}

	public Boolean getSdm(Integer id) {
		return projectService.getSdm(id);
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getEditManagerList() {
		return editManagerList;
	}

	public void setEditManagerList(Boolean editManagerList) {
		this.editManagerList = editManagerList;
	}

}