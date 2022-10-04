package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Transporter;
import ma.azdad.model.TransporterFile;
import ma.azdad.model.TransporterType;
import ma.azdad.model.User;
import ma.azdad.model.Vehicle;
import ma.azdad.repos.TransporterRepos;
import ma.azdad.service.ExternalResourceService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.ToolService;
import ma.azdad.service.TransporterFileService;
import ma.azdad.service.TransporterHistoryService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.VehicleService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class TransporterView extends GenericView<Integer, Transporter, TransporterRepos, TransporterService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected TransporterService transporterService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected SupplierService supplierService;

	@Autowired
	protected VehicleService vehicleService;

	@Autowired
	protected ToolService toolService;

	@Autowired
	protected TransporterFileService transporterFileService;

	@Autowired
	protected TransporterHistoryService transporterHistoryService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected ExternalResourceService externalResourceService;

	private Transporter transporter = new Transporter();
	private TransporterFile transporterFile;

	private Vehicle vehicle;
	private User driver;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			transporter = transporterService.findOne(id);
		else if (isViewPage)
			transporter = transporterService.findOne(id);

	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = transporterService.findLight();
	}

	public void refreshTransporter() {
		transporterService.flush();
		transporter = transporterService.findOne(transporter.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		Boolean test = false;
		if (test)
			cacheView.accessDenied();
	}

	// Vehicle MANAGEMENT
	public void initVehicle() {
		vehicle = new Vehicle(transporter);
	}

	public Boolean canSaveVehicle() {
		return sessionView.isTheConnectedUser(transporter.getUser());
	}

	public void saveVehicle() {
		if (!canSaveVehicle())
			return;
		if (vehicle.getFromMyTools())
			vehicle.setTool(toolService.findOne(vehicle.getToolId()));
		vehicleService.save(vehicle);
		refreshTransporter();
		RequestContext.getCurrentInstance().execute("PF('addEditVehicleDlg').hide()");
	}

	public Boolean canDeleteVehicle() {
		return sessionView.isTheConnectedUser(transporter.getUser());
	}

	public void deleteVehicle() {
		if (!canDeleteVehicle())
			return;
		try {
			vehicleService.delete(vehicle);
			refreshTransporter();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	// Driver MANAGEMENT
	public void addDriver() {
		if (!transporter.getDriverList().contains(driver)) {
			driver.setTransporter(transporter);
			transporter.getDriverList().add(driver);
			transporterService.save(transporter);
		}
	}

	// SAVE TRANSPORTER
	public Boolean canSaveTransporter() {
		if (isListPage || isAddPage)
			return sessionView.isTM();
		else if (isViewPage || isEditPage)
			return sessionView.isTheConnectedUser(transporter.getUser());
		return false;
	}

	public String saveTransporter() {
		if (!canSaveTransporter())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateTransporter())
			return null;
		transporter.setUser(sessionView.getUser());
		if (TransporterType.SUPPLIER.equals(transporter.getType()))
			transporter.setSupplier(supplierService.findOne(transporter.getSupplierId()));
		else
			transporter.setSupplier(null);
		transporter = transporterService.save(transporter);

		if (!isEditPage)
			transporterHistoryService.created(transporter);
		else
			transporterHistoryService.edited(transporter);

		return addParameters(viewPage, "faces-redirect=true", "id=" + transporter.getId());
	}

	public Boolean validateTransporter() {
		return true;
	}

	// DELETE TRANSPORTER
	public Boolean canDeleteTransporter() {
		return sessionView.isTheConnectedUser(transporter.getUser());
	}

	public String deleteTransporter() {
		if (canDeleteTransporter())
			try {
				transporterService.delete(transporter);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private String transporterFileType;
	private Integer transporterFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		TransporterFile transporterFile = new TransporterFile(file, transporterFileType, event.getFile().getFileName(), sessionView.getUser(), transporter);
		transporterFileService.save(transporterFile);
		synchronized (TransporterView.class) {
			refreshTransporter();
		}
	}

	public void deleteTransporterFile() {
		try {
			transporterFileService.delete(transporterFileId);
			refreshTransporter();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	// generic
	public List<Transporter> findLight() {
		return transporterService.findLight();
	}

	// GETTERS & SETTERS




	public TransporterService getTransporterService() {
		return transporterService;
	}

	public void setTransporterService(TransporterService transporterService) {
		this.transporterService = transporterService;
	}

	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public TransporterFileService getTransporterFileService() {
		return transporterFileService;
	}

	public void setTransporterFileService(TransporterFileService transporterFileService) {
		this.transporterFileService = transporterFileService;
	}

	public String getTransporterFileType() {
		return transporterFileType;
	}

	public void setTransporterFileType(String transporterFileType) {
		this.transporterFileType = transporterFileType;
	}

	public Integer getTransporterFileId() {
		return transporterFileId;
	}

	public void setTransporterFileId(Integer transporterFileId) {
		this.transporterFileId = transporterFileId;
	}

	public TransporterFile getTransporterFile() {
		return transporterFile;
	}

	public void setTransporterFile(TransporterFile transporterFile) {
		this.transporterFile = transporterFile;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

}
