package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Vehicle;
import ma.azdad.model.VehicleFile;
import ma.azdad.repos.VehicleRepos;
import ma.azdad.service.VehicleFileService;
import ma.azdad.service.VehicleService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class VehicleView extends GenericView<Integer, Vehicle, VehicleRepos, VehicleService> {

	@Autowired
	protected VehicleService vehicleService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected VehicleFileService vehicleFileService;

	@Autowired
	protected FileView fileView;

	private Vehicle vehicle = new Vehicle();
	private VehicleFile vehicleFile;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			vehicle = vehicleService.findOne(id);
		else if (isViewPage)
			vehicle = vehicleService.findOne(id);

	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = vehicleService.findAll();
	}

	public void refreshVehicle() {
		vehicleService.flush();
		vehicle = vehicleService.findOne(vehicle.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (false)
			cacheView.accessDenied();
	}

	// SAVE VEHICLE
	public Boolean canSaveVehicle() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveVehicle() {
		if (!canSaveVehicle())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateVehicle())
			return null;
		vehicle = vehicleService.save(vehicle);
		return addParameters(viewPage, "faces-redirect=true", "id=" + vehicle.getId());
	}

	public Boolean validateVehicle() {
		return true;
	}

	// DELETE VEHICLE
	public Boolean canDeleteVehicle() {
		return sessionView.isTheConnectedUser(vehicle.getTransporter().getUser());
	}

	public String deleteVehicle() {
		if (canDeleteVehicle())
			try {
				vehicleService.delete(vehicle);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private String vehicleFileType;
	private Integer vehicleFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileView.handleFileUpload(event, getClassName2());
		VehicleFile vehicleFile = new VehicleFile(getClassName2(), file, vehicleFileType, event.getFile().getFileName(), sessionView.getUser(), vehicle);
		vehicleFileService.save(vehicleFile);
		synchronized (VehicleView.class) {
			refreshVehicle();
		}
	}

	public void deleteVehicleFile() {
		try {
			vehicleFileService.delete(vehicleFileId);
			refreshVehicle();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// GENERIC
	public List<Vehicle> findLightByTransporter(Integer transporterId) {
		return vehicleService.findLightByTransporter(transporterId);
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

	public VehicleService getVehicleService() {
		return vehicleService;
	}

	public void setVehicleService(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public VehicleFileService getVehicleFileService() {
		return vehicleFileService;
	}

	public void setVehicleFileService(VehicleFileService vehicleFileService) {
		this.vehicleFileService = vehicleFileService;
	}

	public String getVehicleFileType() {
		return vehicleFileType;
	}

	public void setVehicleFileType(String vehicleFileType) {
		this.vehicleFileType = vehicleFileType;
	}

	public Integer getVehicleFileId() {
		return vehicleFileId;
	}

	public void setVehicleFileId(Integer vehicleFileId) {
		this.vehicleFileId = vehicleFileId;
	}

	public VehicleFile getVehicleFile() {
		return vehicleFile;
	}

	public void setVehicleFile(VehicleFile vehicleFile) {
		this.vehicleFile = vehicleFile;
	}

}
