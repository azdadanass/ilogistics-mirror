package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.VehicleType;
import ma.azdad.repos.VehicleTypeRepos;
import ma.azdad.service.VehicleTypeHistoryService;
import ma.azdad.service.VehicleTypeService;

@ManagedBean
@Component
@Scope("view")
public class VehicleTypeView extends GenericView<Integer, VehicleType, VehicleTypeRepos, VehicleTypeService> {

	@Autowired
	protected VehicleTypeService vehicleTypeService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	private VehicleTypeHistoryService vehicleTypeHistoryService;

	private VehicleType vehicleType = new VehicleType();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			vehicleType = vehicleTypeService.findOne(id);
		else if (isViewPage)
			vehicleType = vehicleTypeService.findOne(id);

	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = vehicleTypeService.findAll();
	}

	public void refreshVehicleType() {
		vehicleTypeService.flush();
		vehicleType = vehicleTypeService.findOne(vehicleType.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		// if (false)
		// cacheView.accessDenied();
	}

	// SAVE VEHICLETYPE
	public Boolean canSaveVehicleType() {
		if (isListPage || isAddPage)
			return sessionView.isTM();
		else if (isViewPage || isEditPage)
			return sessionView.isTM();
		return false;
	}

	public String saveVehicleType() {
		if (!canSaveVehicleType())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateVehicleType())
			return null;
		vehicleType = vehicleTypeService.save(vehicleType);

		if (!isEditPage)
			vehicleTypeHistoryService.created(vehicleType, sessionView.getUser());
		else
			vehicleTypeHistoryService.edited(vehicleType, sessionView.getUser());

		return addParameters(viewPage, "faces-redirect=true", "id=" + vehicleType.getId());
	}

	public Boolean validateVehicleType() {
		return true;
	}

	// DELETE VEHICLETYPE
	public Boolean canDeleteVehicleType() {
		return sessionView.isTM();
	}

	public String deleteVehicleType() {
		if (canDeleteVehicleType())
			try {
				vehicleTypeService.delete(vehicleType);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return addParameters(listPage, "faces-redirect=true");
	}

	// GENERIC
	public List<VehicleType> findAll() {
		return vehicleTypeService.findAll();
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public VehicleTypeService getVehicleTypeService() {
		return vehicleTypeService;
	}

	public void setVehicleTypeService(VehicleTypeService vehicleTypeService) {
		this.vehicleTypeService = vehicleTypeService;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

}
