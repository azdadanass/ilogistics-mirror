package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberType;
import ma.azdad.repos.PartNumberTypeRepos;
import ma.azdad.service.PartNumberTypeService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PartNumberTypeView extends GenericView<Integer, PartNumberType, PartNumberTypeRepos, PartNumberTypeService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private PartNumberTypeService partNumberTypeService;

	@Autowired
	private PartNumberCategoryView partNumberCategoryView;

	@Autowired
	private CacheView cacheView;

	private PartNumberType partNumberType = new PartNumberType();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isPage("partNumberConfiguration"))
			if (partNumberCategoryView.getModel() != null)
				initLists(partNumberTypeService.findByCategory(partNumberCategoryView.getModel().getId()));
	}

	public void flushPartNumberType() {
		partNumberTypeService.flush();
	}

	public void refreshPartNumberType() {
		if (partNumberType.getId() != null)
			partNumberType = partNumberTypeService.findOne(partNumberType.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewPartNumberType())
			cacheView.accessDenied();
	}

	public Boolean canViewPartNumberType() {
		return sessionView.getIsAdmin();
	}

	public void initPartNumberType() {
		partNumberType = new PartNumberType();
		partNumberType.setCategory(partNumberCategoryView.getModel());
	}

	// SAVE PARTNUMBERTYPE
	public Boolean canSavePartNumberType() {
		return sessionView.getIsAdmin();
	}

	public void savePartNumberType() {
		if (!canSavePartNumberType())
			return;
		if (!validatePartNumberType())
			return;
		partNumberType = partNumberTypeService.save(partNumberType);
		refreshList();
	}

	public Boolean validatePartNumberType() {
		if (partNumberTypeService.isNameAndCategoryExists(partNumberType.getName(), partNumberType.getCategory().getId(), partNumberType.getId()))
			return FacesContextMessages.ErrorMessages("Name already exists");

		return true;
	}

	// DELETE PARTNUMBERTYPE
	public Boolean canDeletePartNumberType() {
		return sessionView.getIsAdmin();
	}

	public void deletePartNumberType() {
		if (!canDeletePartNumberType())
			return;
		try {
			partNumberTypeService.delete(partNumberType);
			refreshList();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// generic
	public List<PartNumberType> findByCategory(Integer categoryId) {
		return partNumberTypeService.findByCategory(categoryId);
	}

	public List<PartNumberType> findAll() {
		return partNumberTypeService.findAll();
	}

	// GETTERS & SETTERS
	public PartNumberType getPartNumberType() {
		return partNumberType;
	}

	public void setPartNumberType(PartNumberType partNumberType) {
		this.partNumberType = partNumberType;
	}

	public PartNumberType getModel() {
		return model;
	}

	public void setModel(PartNumberType model) {
		this.model = model;
	}
}
