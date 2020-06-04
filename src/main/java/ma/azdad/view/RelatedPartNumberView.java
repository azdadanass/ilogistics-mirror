package ma.azdad.view;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumber;
import ma.azdad.model.RelatedPartNumber;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.RelatedPartNumberService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class RelatedPartNumberView extends GenericView<RelatedPartNumber> {

	@Autowired
	private RelatedPartNumberService relatedPartNumberService;

	@Autowired
	private PartNumberService partNumberService;

	@Autowired
	private CacheView cacheView;

	private PartNumber partNumber;
	private RelatedPartNumber relatedPartNumber = new RelatedPartNumber();
	private Boolean editMode = false;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
		if (isEditPage)
			relatedPartNumber = relatedPartNumberService.findOne(selectedId);
		else if (isViewPage)
			relatedPartNumber = relatedPartNumberService.findOne(selectedId);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		if ("/viewPartNumber.xhtml".equals(currentPath)) {
			partNumber = partNumberService.findOne(selectedId);
			list1 = relatedPartNumberService.findByPartNumber(selectedId);
			list1.forEach(i -> i.setTmpPartNumber(!i.getPartNumber1().getId().equals(selectedId) ? i.getPartNumber1() : i.getPartNumber2()));
		}

	}

	public void flushRelatedPartNumber() {
		relatedPartNumberService.flush();
	}

	public void refreshRelatedPartNumber() {
		if (relatedPartNumber.getId() != null)
			relatedPartNumber = relatedPartNumberService.findOne(relatedPartNumber.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		if (!canViewRelatedPartNumber())
			cacheView.accessDenied();
	}

	public Boolean canViewRelatedPartNumber() {
		return true;
	}

	// SAVE RELATEDPARTNUMBER
	public Boolean canSaveRelatedPartNumber() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveRelatedPartNumber() {
		if (!canSaveRelatedPartNumber())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateRelatedPartNumber())
			return null;
		relatedPartNumber = relatedPartNumberService.save(relatedPartNumber);

		return addParameters(viewPage, "faces-redirect=true", "id=" + relatedPartNumber.getId());
	}

	public Boolean validateRelatedPartNumber() {
		return true;
	}

	// DELETE RELATEDPARTNUMBER
	public Boolean canDeleteRelatedPartNumber() {
		return true;
	}

	public String deleteRelatedPartNumber() {
		if (canDeleteRelatedPartNumber())
			relatedPartNumberService.delete(relatedPartNumber);
		return addParameters(listPage, "faces-redirect=true");
	}

	// EDIT MODE
	public Boolean canEdit() {
		return sessionView.isSE() && !editMode;
	}

	public Boolean canAddRow() {
		return sessionView.isSE() && editMode;
	}

	public void addRow() {
		if (!canAddRow())
			return;
		list1.add(new RelatedPartNumber(partNumber));
	}

	private Boolean validate() {
		Set<Integer> pnIdList = new HashSet<Integer>();

		for (RelatedPartNumber relatedPartNumber : list1) {
			if (relatedPartNumber.getTmpPartNumber() == null)
				return FacesContextMessages.ErrorMessages("Part Number should not be null");
			if (relatedPartNumber.getTmpPartNumber().getId().equals(partNumber.getId()))
				return FacesContextMessages.ErrorMessages("Related Part Number should be different than current one");
			pnIdList.add(relatedPartNumber.getTmpPartNumber().getId());
		}

		if (pnIdList.size() < list1.size())
			return FacesContextMessages.ErrorMessages("Duplicate part number !");

		return true;
	}

	public void save() {
		if (!canAddRow())
			return;
		if (!validate())
			return;

		for (RelatedPartNumber relatedPartNumber : list1) {
			relatedPartNumber.setPartNumber1(relatedPartNumber.getIsPartNumber1() ? relatedPartNumber.getTmpPartNumber() : partNumber);
			relatedPartNumber.setPartNumber2(!relatedPartNumber.getIsPartNumber1() ? relatedPartNumber.getTmpPartNumber() : partNumber);
			relatedPartNumberService.save(relatedPartNumber);
		}

		refreshList();
		editMode = false;
	}

	// GETTERS & SETTERS
	public RelatedPartNumber getRelatedPartNumber() {
		return relatedPartNumber;
	}

	public void setRelatedPartNumber(RelatedPartNumber relatedPartNumber) {
		this.relatedPartNumber = relatedPartNumber;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

}