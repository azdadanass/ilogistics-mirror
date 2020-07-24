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
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Transporter;
import ma.azdad.model.TransporterFile;
import ma.azdad.model.TransporterType;
import ma.azdad.model.User;
import ma.azdad.model.Vehicle;
import ma.azdad.service.ExternalResourceService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.ToolService;
import ma.azdad.service.TransporterFileService;
import ma.azdad.service.TransporterHistoryService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.VehicleService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class TransporterView extends GenericViewOld<Transporter> {

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
	protected FileView fileView;

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
			transporter = transporterService.findOne(selectedId);
		else if (isViewPage)
			transporter = transporterService.findOne(selectedId);

	}

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
		vehicleService.delete(vehicle);
		refreshTransporter();
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
			transporterService.delete(transporter);
		return addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private String transporterFileType;
	private Integer transporterFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileView.handleFileUpload(event, getClassName2());
		TransporterFile transporterFile = new TransporterFile(getClassName2(), file, transporterFileType, event.getFile().getFileName(), transporter, sessionView.getUser());
		transporterFileService.save(transporterFile);
		synchronized (TransporterView.class) {
			refreshTransporter();
		}
	}

	public void deleteTransporterFile() {
		transporterFileService.delete(transporterFileId);
		refreshTransporter();
	}

	// GENERIC
	public List<Transporter> findLight() {
		return transporterService.findLight();
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

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
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
