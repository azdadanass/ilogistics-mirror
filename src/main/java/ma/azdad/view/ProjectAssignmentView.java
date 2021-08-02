package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.ProjectAssignment;
import ma.azdad.model.ProjectAssignmentType;
import ma.azdad.repos.ProjectAssignmentRepos;
import ma.azdad.service.ProjectAssignmentService;
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
		if (parentId != null)
			model.setParent(service.findOne(parentId));
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
						initLists(service.find(cacheView.getAssignedProjectList(), cacheView.getDelegatedProjectList(), type));
					else
						initLists(service.findBySupplierAndProjectList(sessionView.getUser().getSupplierId(), cacheView.getAssignedProjectList()));
					break;
				case INTERNAL:
				case INTERNAL_TEAM:
					initLists(service.find(cacheView.getAssignedProjectList(), cacheView.getDelegatedProjectList(), type));
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

	// save
	public Boolean canSave() {
		if (getIsAddPage() || getIsListPage())
			return sessionView.getIsUser() || sessionView.getIsPM();
		if (getIsEditPage() || getIsViewPage())
			return true;
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
		default:
			break;
		}

		model = service.save(model);
		return addParameters(listPage, "faces-redirect=true");
	}

	public Boolean validate() {
		if (model.getStartDate().compareTo(model.getEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Date should be lower than End Date");
		if (service.isOverlap(model))
			return FacesContextMessages.ErrorMessages("Overlap Problem");

		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
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
