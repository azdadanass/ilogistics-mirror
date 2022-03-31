package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.IssueType;
import ma.azdad.repos.IssueTypeRepos;
import ma.azdad.service.IssueTypeService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class IssueTypeView extends GenericView<Integer, IssueType, IssueTypeRepos, IssueTypeService> {


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
	@Cacheable("issueTypeView.findByCategory")
	public List<IssueType> findByCategory(Integer categoryId) {
		return service.findByCategory(categoryId);
	}

	// getters & setters
	public IssueType getModel() {
		return model;
	}

	public void setModel(IssueType model) {
		this.model = model;
	}

}
