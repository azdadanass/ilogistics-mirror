package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberCategory;
import ma.azdad.service.PartNumberCategoryService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PartNumberCategoryView extends GenericView<PartNumberCategory> {

	@Autowired
	private PartNumberCategoryService partNumberCategoryService;

	@Autowired
	private CacheView cacheView;

	private PartNumberCategory partNumberCategory = new PartNumberCategory();

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

	public void refreshList() {
		if ("/partNumberConfiguration.xhtml".equals(currentPath))
			list2 = list1 = partNumberCategoryService.findAll();
	}

	public void flushPartNumberCategory() {
		partNumberCategoryService.flush();
	}

	public void refreshPartNumberCategory() {
		if (partNumberCategory.getId() != null)
			partNumberCategory = partNumberCategoryService.findOne(partNumberCategory.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		if (!canViewPartNumberCategory())
			cacheView.accessDenied();
	}

	public Boolean canViewPartNumberCategory() {
		return sessionView.getIsAdmin();
	}

	public void initPartNumberCategory() {
		partNumberCategory = new PartNumberCategory();
	}

	// SAVE PARTNUMBERCATEGORY
	public Boolean canSavePartNumberCategory() {
		return sessionView.getIsAdmin();
	}

	public void savePartNumberCategory() {
		if (!canSavePartNumberCategory())
			return;
		if (!validatePartNumberCategory())
			return;
		partNumberCategory = partNumberCategoryService.save(partNumberCategory);
		refreshList();
	}

	public Boolean validatePartNumberCategory() {
		if (partNumberCategoryService.isNameAndIndustryExists(partNumberCategory.getName(), partNumberCategory.getIndustry().getId(), partNumberCategory.getId()))
			return FacesContextMessages.ErrorMessages("Name already exists");

		return true;
	}

	// DELETE PARTNUMBERCATEGORY
	public Boolean canDeletePartNumberCategory() {
		return sessionView.getIsAdmin();
	}

	public void deletePartNumberCategory() {
		if (!canDeletePartNumberCategory())
			return;
		partNumberCategoryService.delete(partNumberCategory);
		refreshList();
	}

	// GENERIC
	public List<PartNumberCategory> findAll() {
		return partNumberCategoryService.findAll();
	}

	public List<PartNumberCategory> findByIndustry(Integer industryId) {
		return partNumberCategoryService.findByIndustry(industryId);
	}

	// GETTERS & SETTERS
	public PartNumberCategory getPartNumberCategory() {
		return partNumberCategory;
	}

	public void setPartNumberCategory(PartNumberCategory partNumberCategory) {
		this.partNumberCategory = partNumberCategory;
	}

}
