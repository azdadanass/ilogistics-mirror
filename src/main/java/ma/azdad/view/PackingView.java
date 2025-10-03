package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Packing;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.PartNumber;
import ma.azdad.repos.PackingRepos;
import ma.azdad.service.DeliveryRequestSerialNumberService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.PackingService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PackingView extends GenericView<Integer, Packing, PackingRepos, PackingService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private PackingService packingService;

	@Autowired
	private PartNumberService partNumberService;
	
	@Autowired
	private DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;
	
	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private CacheView cacheView;

	private Packing packing = new Packing();

	private Integer partNumberId;

	private Boolean showPackingDetail = false;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
		if (isAddPage)
			packing.setPartNumber(partNumberService.findOne(partNumberId));
		else if (isEditPage)
			packing = packingService.findOne(id);
		else if (isViewPage)
			packing = packingService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		partNumberId = UtilsFunctions.getIntegerParameter("partNumberId");
	}

	@Override
	public void refreshList() {
		if ("/viewPartNumber.xhtml".equals(currentPath))
			list2 = list1 = packingService.findByPartNumber(id);
	}

	public void flushPacking() {
		packingService.flush();
	}

	public void refreshPacking() {
		System.out.println("refreshPacking");
		if (packing.getId() != null)
			packing = packingService.findOne(packing.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewPacking())
			cacheView.accessDenied();
	}

	public Boolean canViewPacking() {
		return true;
	}

	// SAVE PACKING
	public static Boolean canAddPacking(PartNumber partNumber, SessionView sessionView) {
		return sessionView.isTheConnectedUser(partNumber.getUser());
	}

	public Boolean canAddPacking() {
		return canAddPacking(packing.getPartNumber(), sessionView);
	}

	public void addDetail() {
		packing.addDetail(new PackingDetail());
	}

	public void removeDetail(int index) {
		PackingDetail packingDetail = packing.getDetailList().get(index);
		if (packingDetail.getId() != null)
			packing.removeDetail(packingDetail);
		else
			packing.getDetailList().remove(index);
	}

	public Boolean canSavePacking() {
		return canAddPacking();
	}

	public String savePacking() {
		if (!canSavePacking())
			return addParameters(listPage, "faces-redirect=true");
		if (!validatePacking())
			return null;
		packing.calculateFields();
		packing = packingService.save(packing);
		partNumberService.updateHasPacking(packing.getPartNumber().getId());
		
		List<Integer> associatedDeliveryRequestList = deliveryRequestService.findAssociatedDeliveryRequestListWithPacking(packing.getId());
		deliveryRequestService.updateNumberOfItems(associatedDeliveryRequestList);
		deliveryRequestService.updateNetWeight(associatedDeliveryRequestList);
		deliveryRequestService.updateGrossWeight(associatedDeliveryRequestList);
		deliveryRequestService.updateVolume(associatedDeliveryRequestList);
		
		return addParameters("/viewPartNumber.xhtm", "faces-redirect=true", "id=" + packing.getPartNumber().getId());
	}

	public Boolean validatePacking() {
		if (packingService.isNameExists(packing.getPartNumber().getId(), packing.getName(), packing.getId()))
			return FacesContextMessages.ErrorMessages("Name already exists");
		if (packing.getDetailList().isEmpty())
			return FacesContextMessages.ErrorMessages("Detail List should not be null");
		if(packing.getDetailList().stream().anyMatch(i->StringUtils.isBlank(i.getName())))
			return FacesContextMessages.ErrorMessages("Detail Name should not be null");
		if(packing.getDetailList().stream().anyMatch(i->i.getStorageFactor()==null))
			return FacesContextMessages.ErrorMessages("Storage Factor should not be null");
		if (packing.getDetailList().stream().filter(i -> i.getType() == null || i.getType().isEmpty() || i.getQuantity() == null).count() > 0)
			return FacesContextMessages.ErrorMessages("Type / Quantity should not be null");
		if (packing.getDetailList().stream().filter(i -> i.getHasSerialnumber()).mapToDouble(i -> i.getQuantity()).sum() > 10.0)
			return FacesContextMessages.ErrorMessages("Number of packing details with SN should not exceed 10");
		if(packing.getDetailList().stream().filter(i -> !i.getHasSerialnumber() && deliveryRequestSerialNumberService.countByPackingDetail(i.getId())>0).count()>0)
			return FacesContextMessages.ErrorMessages("You cannot change the Packing Detail 'SN' field as it has already been used in a Delivery Note (DN).");

		return true;
	}

	// DELETE PACKING
	public Boolean canDeletePacking() {
		return sessionView.isTheConnectedUser(partNumberService.findOne(packing.getPartNumber().getId()).getUser());
	}

	public void deletePacking() {
		if (canDeletePacking())
			try {
				packingService.delete(packing);
				partNumberService.updateHasPacking(packing.getPartNumber().getId());
				refreshList();
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
			}
	}

	// TOGGLE STATUS
	public Boolean canToggleStatus() {
		return sessionView.isTheConnectedUser(partNumberService.findOne(packing.getPartNumber().getId()).getUser());
	}

	public void toggleStatus() {
		if (!canToggleStatus())
			return;
		packingService.updateActive(packing.getId(), !packing.getActive());
		refreshList();
	}

	// INPLACE EDIT NAME
	public Boolean canUpdateName(PartNumber partNumber) {
		return sessionView.isTheConnectedUser(partNumber.getUser());
	}

	public void updateName(Integer id, String name) {
		packingService.updateName(id, name);
		refreshList();
	}

	// INPLACE EDIT Packing DETAIL
	private Boolean editMode = false;

	public Boolean canEdit() {
		if (packing.getPartNumber() != null && packing.getPartNumber().getUser() != null)
			return sessionView.isTheConnectedUser(packing.getPartNumber().getUser()) && !editMode;
		return false;
	}

	public Boolean canAddDetail() {
		if (packing.getPartNumber() != null && packing.getPartNumber().getUser() != null)
			return sessionView.isTheConnectedUser(packing.getPartNumber().getUser()) && editMode;
		return false;
	}

	// GETTERS & SETTERS
	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	public Boolean getShowPackingDetail() {
		return showPackingDetail;
	}

	public void setShowPackingDetail(Boolean showPackingDetail) {
		this.showPackingDetail = showPackingDetail;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

}
