package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Role;
import ma.azdad.model.Transporter;
import ma.azdad.model.TransporterFile;
import ma.azdad.model.TransporterType;
import ma.azdad.model.User;
import ma.azdad.model.Vehicle;
import ma.azdad.repos.TransporterRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.ExternalResourceService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.ToolService;
import ma.azdad.service.TransportationJobService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.TransporterFileService;
import ma.azdad.service.TransporterHistoryService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.service.VehicleBrandTypeService;
import ma.azdad.service.VehicleService;
import ma.azdad.utils.Color;
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
	protected CompanyService companyService;

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

	@Autowired
	protected UserService userService;

	@Autowired
	protected VehicleBrandTypeService vehicleBrandTypeService;
	
	@Autowired
	private TransportationJobService transportationJobService;

	@Autowired
	private TransportationRequestService transportationRequestService;

	private Long countTjToAssign = 0l;
	private Long countTjToAccept = 0l;
	private Long countTjToStart = 0l;
	private Long countTjToComplete = 0l;
	private Long countTrToAssign = 0l;
	private Long countTrToPickup = 0l;
	private Long countTrToDeliver = 0l;
	private Long countTrToAcknowledge = 0l;

	private Long acceptPerfomance = 0l;
	private Long startPerfomance = 0l;
	private Long completePerfomance = 0l;

	private Transporter transporter = new Transporter();
	private TransporterFile transporterFile;

	private Vehicle vehicle;
	private User user;
	private Boolean active = true;
	private String sortBy = "Pending TR";

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			transporter = transporterService.findOne(id);
		else if (isViewPage) {
			transporter = transporterService.findOne(id);
			this.countTjToAssign = transportationJobService.countToAssign2(id);
			this.countTjToAccept = transportationJobService.countToAcceptByTransporter(id);
			this.countTjToStart = transportationJobService.countToStartByTransporter(id);
			this.countTjToComplete = transportationJobService.countToCompleteByTransporter(id);
			this.countTrToAssign = transportationRequestService.countToAssignByTransporter(id);
			this.countTrToPickup = transportationRequestService.countToPickupByTransporter(id);
			this.countTrToDeliver = transportationRequestService.countToDeliverByTransporter(id);
			this.countTrToAcknowledge = transportationRequestService.countToAcknowledgeByTransporter(id);
			
			this.acceptPerfomance = transportationJobService.getAcceptPerformanceByTransporter(id);
			this.startPerfomance = transportationJobService.getStartPerformanceByTransporter(id);
			this.completePerfomance = transportationJobService.getCompletePerformanceByTransporter(id);}

	}
	


	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = transporterService.findLight(active);

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

	public void sort() {
		System.out.println("sort");
		Collections.sort(list2, new Comparator<Transporter>() {
			@Override
			public int compare(Transporter o1, Transporter o2) {
				switch (sortBy) {
				case "Pending TR":
					if (o1.getCountPendingTr() == null)
						return -1;
					if (o2.getCountPendingTr() == null)
						return 1;
					return o1.getCountPendingTr().compareTo(o2.getCountPendingTr());
				case "Reactivity":
					return o1.getReactivity().compareTo(o2.getReactivity());
				case "Performance":
					return o1.getPerformance().compareTo(o2.getPerformance());
				}
				return 1;
			}
		});
		Collections.reverse(list2);

	}
	
	//Performance
	
	public String getColor(Long percentage) {
		if (percentage == null)
			return null;
		if (percentage >= 80)
			return Color.GREEN.getColorCode();
		if (percentage >= 50)
			return Color.L_BLUE.getColorCode();
		if (percentage >= 30)
			return Color.ORANGE.getColorCode();
		return Color.RED.getColorCode();
	}
	
	public Long getReactivity() {
		return transportationJobService.getTransporterReactivity(id);
	}
	
	

	public Long getPerformance() {
		return transportationJobService.getTransporterPerformance(id);
	}

	// Vehicle MANAGEMENT
	public void initVehicle() {
		vehicle = new Vehicle(transporter);
	}

	public Boolean canSaveVehicle() {
		return sessionView.isInternalTrAdmin()
				|| (sessionView.getIsExternalTrAdmin() && transporter.equals(sessionView.getUser().getTransporter()));
	}

	public void saveVehicle() {
		if (!canSaveVehicle())
			return;
		if (vehicle.getFromMyTools())
			vehicle.setTool(toolService.findOne(vehicle.getToolId()));

		vehicle.setBrandType(vehicleBrandTypeService.findOneLight(vehicle.getBrandTypeId()));
		vehicleService.save(vehicle);
		refreshTransporter();
		RequestContext.getCurrentInstance().execute("PF('addEditVehicleDlg').hide()");
	}

	public Boolean canDeleteVehicle() {
		return canSaveVehicle();
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
	public Boolean canAddUser() {
		return sessionView.isInternalTrAdmin()
				|| (sessionView.getIsExternalTrAdmin() && transporter.equals(sessionView.getUser().getTransporter()));
	}

	public void addUser() {
		if (!transporter.getUserList().contains(user)) {
			user = userService.findOneLight(user.getUsername());
			user.setTransporter(transporter);
			transporter.getUserList().add(user);
			transporterService.save(transporter);
			transporter = transporterService.findOne(transporter.getId());
		}
	}

	public List<User> getUserSelectionList() {
		switch (transporter.getType()) {
		case INTERNAL:
			return userService.findActiveAndHaveAnyRole(
					Arrays.asList(Role.ROLE_ILOGISTICS_DRIVER, Role.ROLE_ILOGISTICS_TM), true);
		case PRIVATE:
			return userService.findActiveAndHaveAnyRole(
					Arrays.asList(Role.ROLE_ILOGISTICS_DRIVER, Role.ROLE_ILOGISTICS_TM), false);
		case SUPPLIER:
			return userService.findActiveAndHaveAnyRoleAndSupplier(
					Arrays.asList(Role.ROLE_ILOGISTICS_DRIVER, Role.ROLE_ILOGISTICS_TM), transporter.getSupplierId());
		default:
			return new ArrayList<User>();
		}
	}

	// SAVE TRANSPORTER
	public Boolean canSaveTransporter() {
		return sessionView.isInternalTrAdmin();
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

		if (TransporterType.INTERNAL.equals(transporter.getType()))
			transporter.setCompany(companyService.findOne(transporter.getCompanyId()));
		else
			transporter.setCompany(null);

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

	public void formatPrivateFirstName() {
		transporter.setPrivateFirstName(UtilsFunctions.formatName(transporter.getPrivateFirstName()));
	}

	public void formaPrivatetLastName() {
		transporter.setPrivateLastName(UtilsFunctions.formatName(transporter.getPrivateLastName()));
	}

	public void formatPrivateCin() {
		transporter
				.setPrivateCin(UtilsFunctions.cleanString(transporter.getPrivateCin()).replace(" ", "").toUpperCase());
	}

	public void formatPrivateEmail() {
		transporter.setPrivateEmail(
				UtilsFunctions.cleanString(transporter.getPrivateEmail()).replace(" ", "").toLowerCase());
	}

	// toggle status
	public Boolean canToggle() {
		return sessionView.isInternalTrAdmin();
	}

	public void toggle() {
		if (!canToggle())
			return;
		transporter.setActive(!transporter.getActive());
		transporter = service.save(transporter);
		transporter = service.findOne(transporter.getId());
	}

	// DELETE TRANSPORTER
	public Boolean canDeleteTransporter() {
		return sessionView.isInternalTrAdmin();
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
		TransporterFile transporterFile = new TransporterFile(file, transporterFileType, event.getFile().getFileName(),
				sessionView.getUser(), transporter);
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
		return transporterService.findLight(true);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Long getCountTjToAssign() {
		return countTjToAssign;
	}

	public void setCountTjToAssign(Long countTjToAssign) {
		this.countTjToAssign = countTjToAssign;
	}

	public Long getCountTjToAccept() {
		return countTjToAccept;
	}

	public void setCountTjToAccept(Long countTjToAccept) {
		this.countTjToAccept = countTjToAccept;
	}

	public Long getCountTjToStart() {
		return countTjToStart;
	}

	public void setCountTjToStart(Long countTjToStart) {
		this.countTjToStart = countTjToStart;
	}

	public Long getCountTjToComplete() {
		return countTjToComplete;
	}

	public void setCountTjToComplete(Long countTjToComplete) {
		this.countTjToComplete = countTjToComplete;
	}

	public Long getCountTrToAssign() {
		return countTrToAssign;
	}

	public void setCountTrToAssign(Long countTrToAssign) {
		this.countTrToAssign = countTrToAssign;
	}

	public Long getCountTrToPickup() {
		return countTrToPickup;
	}

	public void setCountTrToPickup(Long countTrToPickup) {
		this.countTrToPickup = countTrToPickup;
	}

	public Long getCountTrToDeliver() {
		return countTrToDeliver;
	}

	public void setCountTrToDeliver(Long countTrToDeliver) {
		this.countTrToDeliver = countTrToDeliver;
	}

	public Long getCountTrToAcknowledge() {
		return countTrToAcknowledge;
	}

	public void setCountTrToAcknowledge(Long countTrToAcknowledge) {
		this.countTrToAcknowledge = countTrToAcknowledge;
	}

	public Long getAcceptPerfomance() {
		return acceptPerfomance;
	}

	public void setAcceptPerfomance(Long acceptPerfomance) {
		this.acceptPerfomance = acceptPerfomance;
	}

	public Long getStartPerfomance() {
		return startPerfomance;
	}

	public void setStartPerfomance(Long startPerfomance) {
		this.startPerfomance = startPerfomance;
	}

	public Long getCompletePerfomance() {
		return completePerfomance;
	}

	public void setCompletePerfomance(Long completePerfomance) {
		this.completePerfomance = completePerfomance;
	}
	
	

}
