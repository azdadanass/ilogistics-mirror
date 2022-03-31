package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.IssueCategory;
import ma.azdad.repos.IssueCategoryRepos;
import ma.azdad.service.IssueCategoryService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class IssueCategoryView extends GenericView<Integer, IssueCategory, IssueCategoryRepos, IssueCategoryService> {


	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
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

	// generic
	@Cacheable("issueCategoryView.findByApp")
	public List<IssueCategory> findByApp() {
		return service.findByApp();
	}

	// getters & setters
	public IssueCategory getModel() {
		return model;
	}

	public void setModel(IssueCategory model) {
		this.model = model;
	}

}


