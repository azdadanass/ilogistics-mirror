package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.User;
import ma.azdad.model.UserVehicle;
import ma.azdad.model.Vehicle;
import ma.azdad.model.VehicleFile;
import ma.azdad.repos.VehicleRepos;
import ma.azdad.service.UserService;
import ma.azdad.service.VehicleFileService;
import ma.azdad.service.VehicleService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class VehicleView extends GenericView<Integer, Vehicle, VehicleRepos, VehicleService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected VehicleService vehicleService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected VehicleFileService vehicleFileService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected UserService userService;

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
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		VehicleFile vehicleFile = new VehicleFile(file, vehicleFileType, event.getFile().getFileName(), sessionView.getUser(), vehicle);
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

	// user list
	private Boolean editUserList = false;
	
	public Boolean canEditUserList() {
		return !editUserList && sessionView.getIsTrAdmin();
	}

	public Boolean canAddUser() {
		return editUserList && sessionView.getIsTrAdmin();
	}

	public void addUser() {
		if (canAddUser())
			vehicle.addUser(new UserVehicle());
	}

	public Boolean canDeleteUser() {
		return editUserList && sessionView.getIsTrAdmin();
	}

	public void deleteUser(UserVehicle userVehicle) {
		if (canDeleteUser())
			vehicle.removeUser(userVehicle);
	}

	public Boolean canSaveUserList() {
		return editUserList && sessionView.getIsTrAdmin();
	}

	private Boolean validateUserList() {
		if (vehicle.getUserList().stream().filter(i -> i.getUserUsername() == null).count() > 0)
			return FacesContextMessages.ErrorMessages("Driver should not be null");
		if (vehicle.getUserList().stream().map(i->i.getUser()).distinct().count() < vehicle.getUserList().size())
			return FacesContextMessages.ErrorMessages("Duplicate driver !");

		return true;
	}

	public void saveUserList() {
		if (!canSaveUserList())
			return;
		if (!validateUserList())
			return;
		vehicle.getUserList().forEach(i -> i.setUser(userService.findOneLight(i.getUserUsername())));
		service.save(vehicle);
		vehicle = service.findOne(vehicle.getId());
		editUserList = false;
	}

	public List<User> getUserSelectionList() {
		return vehicle.getTransporter().getUserList().stream().filter(i -> i.getActive()).collect(Collectors.toList());
	}

	// generic
	public List<Vehicle> findLightByTransporter(Integer transporterId) {
		return vehicleService.findLightByTransporter(transporterId);
	}

	// GETTERS & SETTERS

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

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
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

	public Boolean getEditUserList() {
		return editUserList;
	}

	public void setEditUserList(Boolean editUserList) {
		this.editUserList = editUserList;
	}

}
