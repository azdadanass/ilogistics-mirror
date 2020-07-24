package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberType;
import ma.azdad.service.PartNumberTypeService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PartNumberTypeView extends GenericViewOld<PartNumberType> {

	@Autowired
	private PartNumberTypeService partNumberTypeService;

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

	public void refreshList() {
		if ("/partNumberConfiguration.xhtml".equals(currentPath))
			list2 = list1 = partNumberTypeService.findAll();
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
	public void redirect() {
		if (!canViewPartNumberType())
			cacheView.accessDenied();
	}

	public Boolean canViewPartNumberType() {
		return sessionView.getIsAdmin();
	}

	public void initPartNumberType() {
		partNumberType = new PartNumberType();
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
		partNumberTypeService.delete(partNumberType);
		refreshList();
	}

	// GENERIC
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

}
