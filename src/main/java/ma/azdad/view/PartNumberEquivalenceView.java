package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberEquivalence;
import ma.azdad.model.PartNumberEquivalenceDetail;
import ma.azdad.repos.PartNumberEquivalenceRepos;
import ma.azdad.service.PartNumberEquivalenceService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PartNumberEquivalenceView extends GenericView<Integer, PartNumberEquivalence, PartNumberEquivalenceRepos, PartNumberEquivalenceService> {

	@Autowired
	private PartNumberEquivalenceService partNumberEquivalenceService;

	@Autowired
	private PartNumberService partNumberService;

	@Autowired
	private CacheView cacheView;

	private PartNumberEquivalence partNumberEquivalence = new PartNumberEquivalence();

	private Integer partNumberId;

	private List<Integer> toDeleteDetailList = new ArrayList<>();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isAddPage)
			partNumberEquivalence.setPartNumber(partNumberService.findOne(partNumberId));
		else if (isEditPage)
			partNumberEquivalence = partNumberEquivalenceService.findOne(id);
		else if (isViewPage)
			partNumberEquivalence = partNumberEquivalenceService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		partNumberId = UtilsFunctions.getIntegerParameter("partNumberId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = partNumberEquivalenceService.findAll();
	}

	public void flushPartNumberEquivalence() {
		partNumberEquivalenceService.flush();
	}

	public void refreshPartNumberEquivalence() {
		if (partNumberEquivalence.getId() != null)
			partNumberEquivalence = partNumberEquivalenceService.findOne(partNumberEquivalence.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewPartNumberEquivalence())
			cacheView.accessDenied();
	}

	public Boolean canViewPartNumberEquivalence() {
		return true;
	}

	// SAVE PARTNUMBEREQUIVALENCE
	public Boolean canSavePartNumberEquivalence() {
		if (isListPage || isAddPage)
			return sessionView.isSE();
		else if (isViewPage || isEditPage)
			return false;
		return false;
	}

	public String savePartNumberEquivalence() {
		if (!canSavePartNumberEquivalence())
			return addParameters(listPage, "faces-redirect=true");
		if (!validatePartNumberEquivalence())
			return null;

		partNumberEquivalence.calculateFileds();
		partNumberEquivalence = partNumberEquivalenceService.save(partNumberEquivalence);

		return addParameters("viewPartNumber.xhtml", "faces-redirect=true", "id=" + partNumberEquivalence.getPartNumber().getId());
	}

	public Boolean validatePartNumberEquivalence() {
		if (partNumberEquivalence.getDetailList().isEmpty())
			return FacesContextMessages.ErrorMessages("List should not be null");
		if (partNumberEquivalence.getDetailList().stream().filter(i -> i.getPartNumber() == null || i.getQuantity() < 1).count() >= 1)
			return FacesContextMessages.ErrorMessages("Part Number should not be null and quantities should be greather than 1");
		if (partNumberEquivalence.getDetailList().stream().mapToInt(i -> i.getPartNumber().getId()).distinct().count() < partNumberEquivalence.getDetailList().size())
			return FacesContextMessages.ErrorMessages("Duplicate Part Number !");
		if (partNumberEquivalence.getDetailList().stream().filter(i -> i.getPartNumber().equals(partNumberEquivalence.getPartNumber())).count() > 0)
			return FacesContextMessages.ErrorMessages("Part Number should not be function of itself !");

		if (partNumberEquivalence.getDetailList().size() == 1)
			if (partNumberEquivalence.getPartNumber().getEquivalenceList().stream().filter(i -> i.getDetailList().size() == 1 && i.getDetailList().get(0).getPartNumber().equals(partNumberEquivalence.getDetailList().get(0).getPartNumber())).count() > 0)
				return FacesContextMessages.ErrorMessages("Part Number " + partNumberEquivalence.getDetailList().get(0).getPartNumber().getName() + " Already Used !");

		return true;
	}

	// TOGGLE STATUS
	public Boolean canToggleStatus() {
		return sessionView.isSE();
	}

	public void toggleStatus() {
		if (!canToggleStatus())
			return;
		partNumberEquivalence.setActive(!partNumberEquivalence.getActive());
		partNumberEquivalenceService.save(partNumberEquivalence);
		refreshPartNumberEquivalence();
	}

	// DELETE PARTNUMBEREQUIVALENCE
	public Boolean canDeletePartNumberEquivalence() {
		return sessionView.isSE();
	}

	public String deletePartNumberEquivalence() {
		if (canDeletePartNumberEquivalence())
			try {
				partNumberEquivalenceService.delete(partNumberEquivalence);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// Detail management
	public Boolean canAddDetail() {
		return isAddPage && sessionView.isSE();
	}

	public void addDetail() {
		if (canAddDetail())
			partNumberEquivalence.getDetailList().add(new PartNumberEquivalenceDetail(partNumberEquivalence));
	}

	public Boolean canRemoveDetail() {
		return sessionView.isSE();
	}

	public void removeDetail(int index) {
		if (canRemoveDetail()) {
			Integer id = partNumberEquivalence.getDetailList().get(index).getId();
			if (id != null)
				toDeleteDetailList.add(id);
			partNumberEquivalence.getDetailList().remove(index);
		}
	}

	// GETTERS & SETTERS
	public PartNumberEquivalence getPartNumberEquivalence() {
		return partNumberEquivalence;
	}

	public void setPartNumberEquivalence(PartNumberEquivalence partNumberEquivalence) {
		this.partNumberEquivalence = partNumberEquivalence;
	}

}
