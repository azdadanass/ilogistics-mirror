package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectAssignment;
import ma.azdad.model.ProjectAssignmentType;
import ma.azdad.repos.ProjectAssignmentRepos;
import ma.azdad.service.CustomerService;
import ma.azdad.service.ProjectAssignmentService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.TeamService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class ProjectAssignmentView extends GenericView<Integer, ProjectAssignment, ProjectAssignmentRepos, ProjectAssignmentService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	CacheView cacheView;

	@Autowired
	UserService userService;

	@Autowired
	TeamService teamService;

	@Autowired
	SupplierService supplierService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ProjectService projectService;

	private ProjectAssignmentType type;

	private Integer parentId;
	private ProjectAssignment parent;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void viewPage() {
		super.viewPage();
	}

	@Override
	protected void addPage() {
		super.addPage();
		if (parentId != null) {
			model.setParent(service.findOne(parentId));
			model.setProject(model.getParent().getProject());
		}

	}

	@Override
	protected void initParameters() {
		super.initParameters();
		parentId = UtilsFunctions.getIntegerParameter("parentId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			if (type == null)
				initLists(service.findByUser(sessionView.getUsername()));
			else {
				switch (type) {
				case SUPPLIER:
					if (sessionView.getInternal())
						initLists(service.find(cacheView.getUserProjectList(), cacheView.getDelegatedProjectList(), type));
					else
						initLists(service.findBySupplierAndProjectList(sessionView.getUser().getSupplierId(), cacheView.getUserProjectList()));
					break;
				case CUSTOMER:
					if (sessionView.getInternal())
						initLists(service.find(cacheView.getUserProjectList(), cacheView.getDelegatedProjectList(), type));
					else
						initLists(service.findByCustomerAndProjectList(sessionView.getUser().getCustomerId(), cacheView.getUserProjectList()));
					break;
				case INTERNAL:
				case INTERNAL_TEAM:
					initLists(service.find(cacheView.getUserProjectList(), cacheView.getDelegatedProjectList(), type));
					break;
//				case INTERNAL_TEAM:
//					initLists(service.findInternalTeamsAssignement(cacheView.getUserProjectList(), cacheView.getDelegatedProjectList()));
//					break;
				default:
					break;
				}
			}
		else if (isViewPage)
			initLists(service.findByParentAndType(id, type));
	}

	public List<Project> getProjectList() {
		if (model.getType() == null)
			return null;

		if (ProjectAssignmentType.CUSTOMER.equals(model.getType()))
			return model.getCustomerId() != null ? projectService.findLightByIdListAndCustomer(cacheView.getAllProjectList(), model.getCustomerId()) : null;
		else
			return projectService.findLight(cacheView.getAllProjectList());

	}

	// save
	public Boolean canSave() {
		if (getIsAddPage())
			return (sessionView.getInternal() && (sessionView.getIsUser() || sessionView.getIsPm())) //
					|| (sessionView.getIsExternalPm() && model.getParent().getSupplierId().equals(sessionView.getUser().getSupplierId())) //
					|| (sessionView.getIsExternalPm() && model.getParent().getCustomerId().equals(sessionView.getUser().getCustomerId()));
		if (getIsListPage())
			return (sessionView.getIsUser() || sessionView.getIsPm()) && sessionView.getInternal();
		if (getIsEditPage() || getIsViewPage())
			return sessionView.isTheConnectedUser(model.getProject().getManager()) || cacheView.getDelegatedProjectList().contains(model.getProjectId());
		return false;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		switch (model.getType()) {
		case INTERNAL:
		case EXTERNAL_PM:
			model.setUser(userService.findOneLight(model.getUserUsername()));
			break;
		case INTERNAL_TEAM:
		case EXTERNAL_TEAM:
			model.setTeam(teamService.findOneLight(model.getTeamId()));
			break;
		case SUPPLIER:
			model.setSupplier(supplierService.findOne(model.getSupplierId()));
			break;
		case CUSTOMER:
			model.setCustomer(customerService.findOne(model.getCustomerId()));
			break;
		default:
			break;
		}

		model = service.save(model);
		return addParameters(listPage, "faces-redirect=true");
	}

	public Boolean validate() {
		if (model.getStartDate().compareTo(model.getEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Date should be lower than End Date");

		System.out.println("service --->" + service);

		if (service.isOverlap(model))
			return FacesContextMessages.ErrorMessages("Overlap Problem");

		if (model.getParent() != null)
			if (model.getStartDate().compareTo(model.getParent().getStartDate()) < 0 || model.getEndDate().compareTo(model.getParent().getEndDate()) > 0)
				return FacesContextMessages.ErrorMessages("Start/end dates should be between supplier assignement start/end dates");

		return true;
	}

	// delete
	public Boolean canDelete() {
		return false;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// getters & setters
	public ProjectAssignment getModel() {
		return model;
	}

	public void setModel(ProjectAssignment model) {
		this.model = model;
	}

	public ProjectAssignmentType getType() {
		return type;
	}

	public void setType(ProjectAssignmentType type) {
		this.type = type;
	}

	public CacheView getCacheView() {
		return cacheView;
	}

	public void setCacheView(CacheView cacheView) {
		this.cacheView = cacheView;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TeamService getTeamService() {
		return teamService;
	}

	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public ProjectAssignment getParent() {
		return parent;
	}

	public void setParent(ProjectAssignment parent) {
		this.parent = parent;
	}

}
