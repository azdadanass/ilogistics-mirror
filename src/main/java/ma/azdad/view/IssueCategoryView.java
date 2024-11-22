package ma.azdad.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Issue;
import ma.azdad.model.IssueCategory;
import ma.azdad.model.IssueParentType;
import ma.azdad.model.IssueType;
import ma.azdad.repos.IssueCategoryRepos;
import ma.azdad.service.IssueCategoryService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class IssueCategoryView extends GenericView<Integer, IssueCategory, IssueCategoryRepos, IssueCategoryService> {

	@Autowired
	SessionView sessionView;

	@Autowired
	ProjectView projectView;

	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isPage("viewProject"))
			initLists(service.findByProject(id));
	}

	// save
	public Boolean canSave() {
		if (getIsAddPage() || getIsListPage())
			return true;
		if (getIsEditPage() || getIsViewPage())
			return true;
		return false;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// init default template
	public Boolean canInitDefaultTemplate() {
		return sessionView.getIsAdmin() && list1.isEmpty();
	}

	public void initDefaultTemplate() {
		if (!canInitDefaultTemplate())
			return;
		service.addDefaultIssueCategory(id);
		refreshList();
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

	// issueCategory list
	public Boolean editIssueCategoryList = false;

	public Boolean canEditIssueCategoryList() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && !editIssueCategoryList;
	}

	public Boolean canAddIssueCategory() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && editIssueCategoryList;
	}

	public void addIssueCategory() {
		if (!canAddIssueCategory())
			return;
		list1.add(new IssueCategory());
	}

	public Boolean canSaveIssueCategoryList() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && editIssueCategoryList;
	}

	public Boolean validateSaveIssueCategoryList() {
		Set<String> nameKeySet = new HashSet<String>();
		for (IssueCategory issueCategory : list1) {
			if (StringUtils.isBlank(issueCategory.getName()))
				return FacesContextMessages.ErrorMessages("Name should not be null");
			if (!nameKeySet.add(issueCategory.getName()))
				return FacesContextMessages.ErrorMessages("Duplicate Name !");
		}
		return true;
	}

	public void saveIssueCategoryList() {
		if (!canSaveIssueCategoryList() || !validateSaveIssueCategoryList())
			return;
		list1.forEach(i -> {
			i.setProject(projectView.getProject());
			service.save(i);
		});
		editIssueCategoryList = false;
		refreshList();
	}

	public Boolean canDeleteIssueCategory() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && sessionView.getInternal();
	}

	public void deleteIssueCategory(IssueCategory issueCategory) throws DataIntegrityViolationException, Exception {
		if (!canDeleteIssueCategory())
			return;
		if (issueCategory.getId() != null)
			service.delete(issueCategory);
		list1.remove(issueCategory);
	}

	// issueType list
	private Boolean editIssueTypeList = false;

	public Boolean canEditIssueTypeList() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && !editIssueTypeList;
	}

	public Boolean canAddIssueType() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && editIssueTypeList;
	}

	public void addIssueType() {
		if (!canAddIssueType())
			return;
		model.addType(new IssueType());
	}

	public Boolean canSaveIssueTypeList() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && editIssueTypeList;
	}

	public Boolean validateSaveIssueTypeList() {
		Set<String> nameKeySet = new HashSet<String>();
		for (IssueType issueType : model.getTypeList()) {
			if (StringUtils.isBlank(issueType.getName()))
				return FacesContextMessages.ErrorMessages("Name should not be null");
			if (!nameKeySet.add(issueType.getName()))
				return FacesContextMessages.ErrorMessages("Duplicate Name !");
		}
		return true;
	}

	public void saveIssueTypeList() {
		if (!canSaveIssueTypeList() || !validateSaveIssueTypeList())
			return;
		model = service.saveAndRefresh(model);
		editIssueTypeList = false;
	}

	public Boolean canDeleteIssueType() {
		return sessionView.isTheConnectedUser(projectView.getProject().getManager()) && sessionView.getInternal();
	}

	public void deleteIssueType(IssueType issueType) {
		if (!canDeleteIssueType())
			return;
		model.removeType(issueType);
		model = service.saveAndRefresh(model);
	}

	// generic
	public List<IssueCategory> findByProjectAndParenType(Integer projectId, IssueParentType parentType) {
		return service.findByProjectAndParenType(projectId, parentType);
	}

	@Cacheable("issueCategoryView.findByProjectAndParenType")
	public List<IssueCategory> findByProjectAndParenType(Issue issue) {
		return service.findByProjectAndParenType(issue.getDeliveryRequest().getProject().getId(), IssueParentType.DN);
	}

	// getters & setters
	public IssueCategory getModel() {
		return model;
	}

	public void setModel(IssueCategory model) {
		this.model = model;
	}

	public Boolean getEditIssueCategoryList() {
		return editIssueCategoryList;
	}

	public void setEditIssueCategoryList(Boolean editIssueCategoryList) {
		this.editIssueCategoryList = editIssueCategoryList;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public ProjectView getProjectView() {
		return projectView;
	}

	public void setProjectView(ProjectView projectView) {
		this.projectView = projectView;
	}

	public Boolean getEditIssueTypeList() {
		return editIssueTypeList;
	}

	public void setEditIssueTypeList(Boolean editIssueTypeList) {
		this.editIssueTypeList = editIssueTypeList;
	}

}
