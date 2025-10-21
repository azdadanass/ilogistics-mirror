package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DriverLocation;
import ma.azdad.model.Path;
import ma.azdad.model.Role;
import ma.azdad.model.Stop;
import ma.azdad.model.ToNotify;
import ma.azdad.model.TrCost;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobAssignmentType;
import ma.azdad.model.TransportationJobCapacity;
import ma.azdad.model.TransportationJobComment;
import ma.azdad.model.TransportationJobFile;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.TransportationJobState;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestHistory;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
import ma.azdad.model.Vehicle;
import ma.azdad.repos.TransportationJobCapacityRepos;
import ma.azdad.repos.TransportationJobRepos;
import ma.azdad.service.CapacityService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.DriverLocationService;
import ma.azdad.service.EmailService;
import ma.azdad.service.GoogleGeocodeService;
import ma.azdad.service.MapService;
import ma.azdad.service.PathService;
import ma.azdad.service.RouteAppService;
import ma.azdad.service.SmsService;
import ma.azdad.service.StopService;
import ma.azdad.service.TransportationJobFileService;
import ma.azdad.service.TransportationJobService;
import ma.azdad.service.TransportationRequestHistoryService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.service.VehicleService;
import ma.azdad.utils.FacesContextMessages;
import ma.azdad.utils.Public;

@ManagedBean
@Component
@Scope("view")
public class TransportationJobView extends GenericView<Integer, TransportationJob, TransportationJobRepos, TransportationJobService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected TransportationJobService transportationJobService;

	@Autowired
	CapacityService capacityService;

	@Autowired
	RouteAppService routeAppService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected TransportationJobFileService transportationJobFileService;

	@Autowired
	TransportationJobCapacityRepos transportationJobCapacityRepos;

	@Autowired
	GoogleGeocodeService geocodeService;

	@Autowired
	protected TransportationRequestHistoryService transportationRequestHistoryService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected TransportationRequestService transportationRequestService;

	@Autowired
	protected DeliveryRequestService deliveryRequestService;

	@Autowired
	protected TransporterService transporterService;

	@Autowired
	protected VehicleService vehicleService;

	@Autowired
	protected StopService stopService;

	@Autowired
	protected PathService pathService;

	@Autowired
	protected MapService mapService;

	@Autowired
	protected UserService userService;

//	@Autowired
//	protected OldEmailService emailService;

	@Autowired
	protected EmailService emailService;

	@Autowired
	protected SmsService smsService;

	@Autowired
	protected TransporterView transporterView;

	@Autowired
	protected UserView userView;

	@Autowired
	protected DriverLocationService driverLocationService;

	private TransportationJob transportationJob = new TransportationJob();
	private TransportationJobFile transportationJobFile;

	private List<TransportationRequest> transportationRequestList1 = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList2 = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList3; // selection

	private TransportationRequest transportationRequest;

	private List<Path> optimizedPaths = new ArrayList<>();
	private boolean optimizing = false;

	private int step = 1;

	private int currentStep = 1;
	private static final int STEP_ASSIGNMENT = 1;
	private static final int STEP_SCHEDULE_LOCATION = 2;

	public List<Path> getOptimizedPaths() {
		return optimizedPaths;
	}

	public boolean isOptimizing() {
		return optimizing;
	}

	private MapModel mapModel;
	private Marker marker;
	private Boolean viewPathList = false;

	private static Map<String, List<Integer>> TO_ASSIGN_MAP = new HashMap<String, List<Integer>>();
	private List<TransportationJob> toAssignList = new ArrayList<TransportationJob>();
	private String key;

	private TransportationJobStatus status;
	private TransportationJobState state;

	private Double latitude;
	private Double longitude;

	private TimelineModel timeline1;
	private TimelineModel timeline2;

	private Double mapLatitude;
	private Double mapLongitude;

	private String downloadPath;
	
	private Integer transporterId ;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage || isPage("assignTrList")) {
			transportationJob = transportationJobService.findOne(id);
			transportationJob.init();
			refreshTransportationRequestList();
		} else if (isViewPage) {
			
			transportationJob = transportationJobService.findOne(id);
			transportationJob = transportationJobService.initCalculableFields(transportationJob);
			transportationJob.init();
			refreshMapModel();
			initTimelines();
		} else if (isPage("assignTransportationJob")) {
			toAssignList = service.findByIdList(TO_ASSIGN_MAP.get(key));
			transportationJob = toAssignList.get(0);
			refreshMapModel();
			if (pageIndex == 5) {
				transportationJob.setAssignmentType(TransportationJobAssignmentType.EXTERNAL_DRIVER);
				changeTransportationJobAssignmentTypeListener();
			}
		} else if (isPage("startTransportationJob")) {
			transportationJob = transportationJobService.findOne(id);
			refreshMapModel();
		}

	}

	@Override
	protected void initParameters() {
		super.initParameters();
		status = TransportationJobStatus.get(UtilsFunctions.getIntegerParameter("status"));
		try {
			transporterId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("transporterId"));
		} catch (Exception e) {
			transporterId = null;
		}
		state = TransportationJobState.get(UtilsFunctions.getIntegerParameter("state"));
		key = UtilsFunctions.getParameter("key");
	}

	private void initTimelines() {
		String[] styles = { "aa-timeline-event-info", "aa-timeline-event-success", "aa-timeline-event-warning", "aa-timeline-event-purple", "aa-timeline-event-danger" }; // TODO

		// init timeline1 (Planning Timeline)
		timeline1 = new TimelineModel();
		transportationJob.getTransportationRequestList()
				.forEach(i -> timeline1.add(new TimelineEvent(i, i.getPlannedPickupDate(), i.getPlannedDeliveryDate(), false, null, styles[i.getId() % styles.length])));
		// init timeline2 (Delivery Timeline)
		timeline2 = new TimelineModel();
		transportationJob.getTransportationRequestList().stream().filter(i -> i.getPickupDate() != null && i.getDeliveryDate() != null)
				.forEach(i -> timeline2.add(new TimelineEvent(i, i.getPickupDate(), i.getDeliveryDate(), false, null, styles[i.getId() % styles.length])));
	}

	@Override
	public void refreshList() {
		if (isListPage  )
			if (pageIndex == null)
				list2 = list1 = transportationJobService.find();
			else
				if(transporterId == null) {
					
				switch (pageIndex) {
				case 1:
					if (sessionView.getIsInternalTM()) {
						initLists(transportationJobService.find(state));
						break;
					} else {
						Set<TransportationJob> result = new HashSet<TransportationJob>();
						if (sessionView.getIsDriver())
							result.addAll(service.findByDriver(sessionView.getUsername(), state));
						if (sessionView.getIsExternalTM())
							result.addAll(service.findByTransporter(sessionView.getUser().getTransporterId(), state));
						List<TransportationJob> list = new ArrayList<TransportationJob>(result);
						Collections.sort(list, new Comparator<TransportationJob>() {
							@Override
							public int compare(TransportationJob o1, TransportationJob o2) {
								return o2.getId().compareTo(o1.getId());
							}
						});
						initLists(list);
						break;
					}
				case 4:
					initLists(transportationJobService.findToAssign1(sessionView.getUsername()));
					break;
				case 5:
					initLists(transportationJobService.findToAssign2(sessionView.getUser().getTransporterId()));
					break;
				case 6:
					initLists(transportationJobService.findByDriver(sessionView.getUsername(), status));
					break;
				case 7:
					initLists(transportationJobService.findToAccept(sessionView.getUsername()));
					break;
				case 8:
					initLists(transportationJobService.findToStart(sessionView.getUsername()));
					break;
				case 9:
					// to complete1
					initLists(transportationJobService.findToComplete(sessionView.getUsername()));
					break;
				}
				}else {
					switch (pageIndex) {
			
					case 5:
						initLists(transportationJobService.findToAssign2(transporterId));
						break;
					
					case 7:
						initLists(transportationJobService.findToAcceptByTransporter(transporterId));
						break;
					case 8:
						initLists(transportationJobService.findToStartByTransporter(transporterId));
						break;
					case 9:
						// to complete1
						initLists(transportationJobService.findToCompleteByTransporter(transporterId));
						break;
					}
				}
	}

	public void refreshTransportationJob() {
		transportationJobService.flush();
		transportationJob = transportationJobService.findOne(transportationJob.getId());
		transportationJob.init();
	}

	private void refreshTransportationRequestList() {
		System.out.println("refreshTransportationRequestList");
		transportationRequestList2 = transportationRequestList1 = transportationRequestService.findByNotHavingTransportationJob(TransportationRequestStatus.APPROVED,
				Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
		System.out.println("transportationRequestList2 : " + transportationRequestList2);
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		// if (false)
		// cacheView.accessDenied();
	}

	// M�THODE D'APER�U D'OPTIMISATION
	public void loadOptimizationPreview() {
		optimizing = true;
		optimizedPaths = new ArrayList<>();
		try {
			Integer jobId = (transportationJob != null) ? transportationJob.getId() : null;
			String driverUsername = (transportationJob != null && transportationJob.getDriver() != null) ? transportationJob.getDriver().getUsername() // <-- username (String)
					: null;

			if (jobId == null) {
				FacesContextMessages.ErrorMessages("Job introuvable.");
				return;
			}

			// renvoie juste la liste
			optimizedPaths = routeAppService.buildAndPersistPathsForJob(jobId, driverUsername, 50.0, true, true);
			if (optimizedPaths == null)
				optimizedPaths = new ArrayList<>();
		} catch (Exception e) {
			optimizedPaths = new ArrayList<>();
			FacesContextMessages.ErrorMessages("Optimisation �chou�e : " + e.getMessage());
		} finally {
			optimizing = false;
		}
	}

	// SAVE TRANSPORTATIONJOB
	public Boolean canSave() {
		if (isListPage || isAddPage)
			return sessionView.isTM();
		else if (isViewPage || isEditPage)
			return sessionView.isTM() && !TransportationJobStatus.ACKNOWLEDGED.equals(transportationJob.getStatus());
		return false;
	}

	public String saveNextStep() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		switch (step) {
		case 1:
			System.out.println("step1");
			step++;
			break;
		case 2:
			System.out.println("step1");
			step++;
			break;
		case 3:
			System.out.println("step1");
			step++;
			if (isAddPage)
				initToNotifiyList();
			break;
		case 4:
			System.out.println("step1");
			step++;
			break;
		case 5:
			return save();

		}
		return null;
	}

	private void initToNotifiyList() {
		addToNotify(sessionView.getUser());
	}

	public void savePreviousStep() {
		if (step > 1)
			step--;
	}

	// to notify
	private String toNotifyUserUsername;

	public void addToNotifyItem() {
		System.out.println("addToNotifyItem");
		addToNotify(userService.findOne(toNotifyUserUsername));
		System.out.println(transportationJob.getToNotifyList());
	}

	public void addToNotify(User user) {
		if (transportationJob.getToNotifyList().stream().filter(i -> i.getInternalResource().getUsername().equals(user.getUsername())).count() == 0)
			transportationJob.getToNotifyList().add(new ToNotify(user, transportationJob));
	}

	public void removeToNotifyItem(int index) {
		transportationJob.getToNotifyList().get(index).setDeliveryRequest(null);
		transportationJob.getToNotifyList().remove(index);
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateTransportationJob())
			return null;
//		transportationJob.setTransporter(transporterService.findOne(transportationJob.getTransporterId()));
//		transportationJob.setVehicle(vehicleService.findOne(transportationJob.getVehicleId()));
//		transportationJob.setDriver(userService.findOne(transportationJob.getDriverUsername()));
//		if (isAddPage) {
//			// tmp validation
//			if (transportationJob.getVehicle().getVehicleType() == null || transportationJob.getVehicle().getVehicleType().getPrice() == null) {
//				FacesContextMessages.ErrorMessages("VehicleType and VehicleType.price should not be null");
//				return null;
//			}
//			transportationJob.setVehiclePrice(transportationJob.getVehicle().getVehicleType().getPrice());
//		}
//
//		if (isEditPage) {
//			transportationJob.calculateEstimatedCost();
//			transportationJobService.calculateTransportationRequestListCosts(transportationJob, true);
//		}
		transportationJob.setDate1(new Date());
		transportationJob.setUser1(sessionView.getUser());
		transportationJob.addHistory(new TransportationJobHistory(isAddPage ? "Created" : "Edited", sessionView.getUser()));
		if (isAddPage)
			transportationJob.setQrKey(UtilsFunctions.generateQrKey());
		transportationJob = transportationJobService.save(transportationJob);
		if (isAddPage) {
			transportationJob.generateReference();
			transportationJobService.save(transportationJob);
		}

		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}

	public Boolean validateTransportationJob() {
		return true;
	}

	public void updateCalculableFields() {
		transportationJobService.updateCalculableFields(transportationJob, true);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
		transportationJob.init();
		// refreshTransportationJob();
	}

	public void updateCalculableFieldsOldddddd() {
		try {
			refreshTransportationJob();
			transportationJob.calculateStartDate();
			transportationJob.calculateEndDate();
			transportationJob.calculateStatus();
			transportationJobService.save(transportationJob);
			// clear path List
			for (Path path : transportationJob.getPathList())
				pathService.delete(path);
			transportationJob.getPathList().clear();
			// clear stop List
			for (Stop stop : transportationJob.getStopList())
				stopService.delete(stop);
			transportationJob.getStopList().clear();
			refreshTransportationJob();
			transportationJob.generateStopList();
			transportationJobService.save(transportationJob);
			refreshTransportationJob();
			transportationJob.generatePathList();
			transportationJob = transportationJobService.save(transportationJob);
			refreshTransportationJob();
			transportationJobService.calculateTransportationRequestListCosts(transportationJob, true);
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	public void updatePathList() {

	}

	public String getFirstTMUsername() {
		if (transportationJob.getTransporter() == null)
			return null;
		return transportationJob.getTransporter().getUserList().stream().map(i -> i.getUsername()).filter(i -> userService.isHavingRole(i, Role.ROLE_ILOGISTICS_TM)).findFirst()
				.orElse(null);
	}

	public String getFirstDriverUsername() {
		if (transportationJob.getTransporter() == null)
			return null;
		return transportationJob.getTransporter().getUserList().stream().map(i -> i.getUsername()).filter(i -> userService.isHavingRole(i, Role.ROLE_ILOGISTICS_DRIVER)).findFirst()
				.orElse(null);
	}

	public void generateStamp() {
		downloadPath = service.generateStamp(transportationJob);
	}

	// assign
	public void initAssign() {
		if ((isViewPage && TransportationJobStatus.ASSIGNED1.equals(transportationJob.getStatus())) || (isListPage && pageIndex == 5))
			transportationJob.setAssignmentType(TransportationJobAssignmentType.EXTERNAL_DRIVER);
		if (!sessionView.getInternal())
			transportationJob.setAssignmentType(TransportationJobAssignmentType.EXTERNAL_DRIVER);
		changeTransportationJobAssignmentTypeListener();
	}

	public String initAssignList() {
		String key = UtilsFunctions.generateKey();
		Integer index = null;
		if (isListPage) {
			TO_ASSIGN_MAP.put(key, list4.stream().map(i -> i.getId()).collect(Collectors.toList()));
			index = pageIndex;
		} else if (isViewPage) {
			TO_ASSIGN_MAP.put(key, Arrays.asList(transportationJob.getId()));
			index = TransportationJobStatus.ASSIGNED1.equals(transportationJob.getStatus()) ? 5 : 4;
		}
		return "assignTransportationJob.xhtml?faces-redirect=true&pageIndex=" + index + "&key=" + key;
	}

	public void changeTransportationJobAssignmentTypeListener() {
		if (transportationJob.getAssignmentType() != null)
			switch (transportationJob.getAssignmentType()) {
			case TRANSPORTER:
				transporterView.initLists(transporterService.findLight(true));
				transporterView.getList2().forEach(u -> {
					u.setCountPendingTr(transportationRequestService.countPendingByTransporter(u.getId()));
					u.setReactivity(transportationJobService.getTransporterReactivity(u.getId()));
					u.setPerformance(transportationJobService.getTransporterPerformance(u.getId()));
					
				});
				transporterView.sort();
				break;
			case INTERNAL_DRIVER:
				userView.initLists(userService.findActiveDriverList(true));
				userView.getList2().forEach(u -> {
					u.setCountPendingTr(transportationRequestService.countPendingByDriver(u.getUsername()));
					u.setReactivity(transportationJobService.getReactivity(u.getUsername()));
					u.setPerformance(transportationJobService.getPerformance(u.getUsername()));
					DriverLocation driverLocation = driverLocationService.getLastLocation(u.getUsername());
					if (driverLocation != null) {
						u.setLatitude(driverLocation.getLatitude());
						u.setLongitude(driverLocation.getLongitude());
						u.setDistance(PathService.getDistance(u.getLatitude(), u.getLongitude(), transportationJob.getFirstLatitude(), transportationJob.getFirstLongitude()));
					}
				});
				userView.sort();
				refreshMapModel();
				break;
			case EXTERNAL_DRIVER:
				switch (transportationJob.getStatus()) {
				case EDITED:
					userView.initLists(userService.findActiveDriverList(false));
					break;
				case ASSIGNED1:
					userView.initLists(userService.findByRoleAndActiveAndTransporter(Role.ROLE_ILOGISTICS_DRIVER, transportationJob.getTransporterId()));
					break;
				default:
					break;
				}
				userView.getList2().forEach(u -> {
					u.setCountPendingTr(transportationRequestService.countPendingByDriver(u.getUsername()));
					u.setReactivity(transportationJobService.getReactivity(u.getUsername()));
					u.setPerformance(transportationJobService.getPerformance(u.getUsername()));
					DriverLocation driverLocation = driverLocationService.getLastLocation(u.getUsername());
					if (driverLocation != null) {
						u.setLatitude(driverLocation.getLatitude());
						u.setLongitude(driverLocation.getLongitude());
						u.setDistance(PathService.getDistance(u.getLatitude(), u.getLongitude(), transportationJob.getFirstLatitude(), transportationJob.getFirstLongitude()));
					}
				});
				userView.sort();
				refreshMapModel();
				break;
			default:
				break;
			}
	}

	public Boolean canAssign() {
		return canAssign(transportationJob);
	}

	public Boolean canAssign(TransportationJob transportationJob) {
		return sessionView.getIsTM() //
				&& !transportationJob.getTransportationRequestList().isEmpty() //
				&& ((TransportationJobStatus.EDITED.equals(transportationJob.getStatus()) //
						&& sessionView.isTheConnectedUser(transportationJob.getUser1())) //
						|| (TransportationJobStatus.ASSIGNED1.equals(transportationJob.getStatus()) //
								&& transportationJob.getTransporter().equals(sessionView.getUser().getTransporter()) //
						));
	}

	private Boolean validateAssign() {
		if (TransportationJobAssignmentType.TRANSPORTER.equals(transportationJob.getAssignmentType()) && transportationJob.getTransporter() == null)
			return FacesContextMessages.ErrorMessages("Transporter should not be null");
		else if (Arrays.asList(TransportationJobAssignmentType.INTERNAL_DRIVER, TransportationJobAssignmentType.EXTERNAL_DRIVER).contains(transportationJob.getAssignmentType())) {
			if (transportationJob.getDriver() == null)
				return FacesContextMessages.ErrorMessages("Driver should not be null");
			if (transportationJob.getVehicle() == null)
				return FacesContextMessages.ErrorMessages("Vehicle should not be null");
		}
		if (transportationJob.getPlannedStartLatitude() == null || transportationJob.getPlannedStartLongitude() == null)
			return FacesContextMessages.ErrorMessages("Start position should not be null");
		Double maxCumulativeWeight = transportationJobCapacityRepos.findMaxCumulativeWeightByTransportationJobIdAndType(transportationJob.getId(), "Planned");
		Double maxCumulativeVolume = transportationJobCapacityRepos.findMaxCumulativeVolumeByTransportationJobIdAndType(transportationJob.getId(), "Planned");
		Double maxVehiculeWeight = vehicleService.findOne(this.transportationJob.getVehicleId()).getMaxWeight();
		Double maxVehiculeVolume = vehicleService.findOne(this.transportationJob.getVehicleId()).getMaxVolume();
		if (maxVehiculeVolume != null && maxCumulativeVolume != null && maxVehiculeVolume < maxCumulativeVolume) {
			return FacesContextMessages.ErrorMessages("This Transportation Job could not be assigned to this vehicle as you will be exceeding the max Volume of the vehicle,"
					+ " Please change the assigned vehicle.");

		}
		if (maxVehiculeWeight != null && maxCumulativeWeight != null && maxVehiculeWeight < maxCumulativeWeight) {
			return FacesContextMessages.ErrorMessages("This Transportation Job could not be assigned to this vehicle as you will be exceeding the max Weight of the vehicle,"
					+ " Please change the assigned vehicle.");

		}
		if (transportationJob.getStartLeadTime() < transportationJob.getAcceptLeadTime()) {
			return FacesContextMessages
					.ErrorMessages("Start Lead Time shouldn't be less than Accept Lead Time.");

		}
		return true;
	}

	public void assign(TransportationJob transportationJob) {
		if (!canAssign(transportationJob))
			return;
		if (this.transportationJob.getStartLeadTime() != null)
			transportationJob.setStartLeadTime(this.transportationJob.getStartLeadTime());
		if (this.transportationJob.getAcceptLeadTime() != null)
			transportationJob.setAcceptLeadTime(this.transportationJob.getAcceptLeadTime());
		if (this.transportationJob.getPlannedStartLatitude() != null)
			transportationJob.setPlannedStartLatitude(this.transportationJob.getPlannedStartLatitude());
		if (this.transportationJob.getPlannedStartLongitude() != null)
			transportationJob.setPlannedStartLongitude(this.transportationJob.getPlannedStartLongitude());
		service.assign(transportationJob, this.transportationJob.getAssignmentType(), this.transportationJob.getTransporterId(), //
				this.transportationJob.getDriverUsername(), this.transportationJob.getVehicleId(), sessionView.getUser());
//		transportationJob.setAssignmentType(this.transportationJob.getAssignmentType());
//
//		switch (transportationJob.getAssignmentType()) {
//		case TRANSPORTER:
//			transportationJob.setStatus(TransportationJobStatus.ASSIGNED1);
//			transportationJob.setDate2(new Date());
//			transportationJob.setUser2(sessionView.getUser());
//			transportationJob.setTransporter(transporterService.findOneLight(this.transportationJob.getTransporterId()));
//			transportationJob.addHistory(
//					new TransportationJobHistory("Assigned", sessionView.getUser(), "Assigned to transporter <b class='blue'>" + transportationJob.getTransporterName() + "</b>"));
//			break;
//		case INTERNAL_DRIVER:
//		case EXTERNAL_DRIVER:
//			transportationJob.setStatus(TransportationJobStatus.ASSIGNED2);
//			transportationJob.setDate3(new Date());
//			transportationJob.setUser3(sessionView.getUser());
//			transportationJob.setDriver(userService.findOneLight(this.transportationJob.getDriverUsername()));
//			transportationJob.addHistory(
//					new TransportationJobHistory("Assigned", sessionView.getUser(), "Assigned to driver <b class='green'>" + transportationJob.getDriverFullName() + "</b>"));
//			break;
//		}
//
//		transportationJob.calculateMaxAcceptTime();
//		transportationJob.calculateMaxStartTime();
//		transportationJob = service.save(transportationJob);
	}

//	public String assign() {
//		if (isViewPage) {
//			assign(transportationJob);
//			return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
//		} else if (pageIndex == 4 || pageIndex == 5) {
//			for (TransportationJob transportationJob : list4)
//				assign(service.findOne(transportationJob.getId()));
//			return addParameters(listPage, "faces-redirect=true", "pageIndex=" + pageIndex);
//		}
//		return null;
//	}

	public String assign() {
		if (!validateAssign())
			return null;

		for (TransportationJob transportationJob : toAssignList)
			assign(service.findOne(transportationJob.getId()));
		return addParameters(listPage, "faces-redirect=true", "pageIndex=" + pageIndex);
	}

	public List<Vehicle> getVehicleSelectionList() {
		return vehicleService.findActiveByDriver(transportationJob.getDriverUsername());
	}

	// unassign
	public Boolean canUnassign() {
		return service.canUnassign(transportationJob, sessionView.getUsername(), sessionView.getRoleList());
	}

	public void unassign() {
		if (!canUnassign())
			return;
		service.unassign(transportationJob, sessionView.getUser());
	}

	// accept
	public Boolean canAccept(TransportationJob transportationJob) {
		return service.canAccept(transportationJob, sessionView.getUsername());
	}

	public Boolean canAccept() {
		return canAccept(transportationJob);
	}

	public void accept(TransportationJob transportationJob) {
		if (!canAccept(transportationJob))
			return;
		service.accept(transportationJob, sessionView.getUser());
	}

	public void accept() {
		if (isViewPage) {
			accept(transportationJob);
			transportationJob = service.findOne(transportationJob.getId());
		} else if (isListPage && isPageIndex(6l, 7l)) {
			for (TransportationJob transportationJob : list4)
				accept(service.findOne(transportationJob.getId()));
			refreshList();
		}
	}

	// decline
	public Boolean canDecline() {
		return canDecline(transportationJob);
	}

	public Boolean canDecline(TransportationJob transportationJob) {
		return service.canDecline(transportationJob, sessionView.getUsername());
	}

	public void decline(TransportationJob transportationJob) {
		if (!canDecline(transportationJob))
			return;
		service.decline(transportationJob, sessionView.getUser());
	}

	public void decline() {
		if (isViewPage) {
			decline(transportationJob);
			transportationJob = service.findOne(transportationJob.getId());
		} else if (isListPage && pageIndex == 6) {
			for (TransportationJob transportationJob : list4)
				decline(service.findOne(transportationJob.getId()));
			refreshList();
		}
	}

	// start

	public Boolean canStart() {
		return service.canStart(transportationJob, sessionView.getUsername());
	}

	private Boolean validateStart() {
		if (transportationJob.getStartLatitude() == null || transportationJob.getStartLongitude() == null)
			return FacesContextMessages.ErrorMessages("Please select the start position from the map");

		if (transportationJob.getTransportationRequestList().stream().filter(i -> i.getExpectedPickupDate() == null).count() > 0)
			return FacesContextMessages.ErrorMessages("Expected Pickup Date should not be null");
		return true;
	}

	public String startTransportationJob() {
		if (!canStart())
			return null;
		if (!validateStart())
			return null;
		service.start(transportationJob, sessionView.getUser());
		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}

	public void onPointSelect(PointSelectEvent event) {
		LatLng latlng = event.getLatLng();
		FacesContextMessages.InfoMessages("Point Selected : " + latlng.getLat() + " " + latlng.getLng());
		transportationJob.setStartLatitude(latlng.getLat());
		transportationJob.setStartLongitude(latlng.getLng());
	}

	// GPS

	public void refreshMapModel() {
		if (isViewPage || isPage("startTransportationJob"))
			mapModel = mapService.generate(transportationJob.getStopList(), viewPathList);
		else if (isPage("assignTransportationJob")) {
			List<Stop> stopList = stopService.findByTransportationJobList(toAssignList.stream().map(i -> i.getId()).collect(Collectors.toList()));
			// init center latitude,longitude
			if (!stopList.isEmpty()) {
				latitude = stopList.get(0).getPlace().getLatitude();
				longitude = stopList.get(0).getPlace().getLongitude();
			}
			mapModel = mapService.generate(stopList, false);
			userView.getList2().stream().filter(i -> i.getLatitude() != null).forEach(i -> {
				Marker marker = new Marker(new LatLng(i.getLatitude(), i.getLongitude()), i.getName());
				marker.setIcon(Public.getPublicResizedImage(i.getPhoto()));
				mapModel.addOverlay(marker);
			});
		}
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		System.out.println("onMarkerSelect " + event.getOverlay());
		marker = (Marker) event.getOverlay();
	}

	// ASSIGN TR List
	public Boolean canAssignTrList() {
		return sessionView.isTM() && !TransportationJobStatus.ACKNOWLEDGED.equals(transportationJob.getStatus());
	}

	public String assignTrList() {
		if (!canAssignTrList())
			return null;
		if (!validateAssignTransportationRequestList())
			return null;
		Date currentDate = new Date();
		for (TransportationRequest tr : transportationRequestList3) {
			Date plannedPickupDate = tr.getPlannedPickupDate();
			Date plannedDeliveryDate = tr.getPlannedDeliveryDate();
			Integer plannedPickupDuration = tr.getPlannedPickupDuration();
			Integer plannedDeliveryDuration = tr.getPlannedDeliveryDuration();

			tr = transportationRequestService.findOne(tr.getId());
			tr.setPlannedPickupDate(plannedPickupDate);
			tr.setPlannedDeliveryDate(plannedDeliveryDate);
			tr.setPlannedPickupDuration(plannedPickupDuration);
			tr.setPlannedDeliveryDuration(plannedDeliveryDuration);
			tr.setStatus(TransportationRequestStatus.ASSIGNED);
			tr.setDate4(currentDate);
			tr.setUser4(sessionView.getUser());
			tr.setTransportationJob(transportationJob);
			tr.addHistory(new TransportationRequestHistory("Assigned", sessionView.getUser(), "Assigned To : " + transportationJob.getReference()));
//			transportationRequestHistoryService.assignedNew(tr, sessionView.getUser());
			if (Arrays.asList(TransportationJobStatus.STARTED, TransportationJobStatus.IN_PROGRESS, TransportationJobStatus.COMPLETED).contains(transportationJob.getStatus())) {
				if (tr.getExpectedPickupDate() == null)
					tr.setExpectedPickupDate(tr.getPlannedPickupDate());
			}
			transportationRequestService.save(tr);
			tr = transportationRequestService.findOne(tr.getId());
			// emailService.transportationRequestNotification(tr);
			// smsService.sendSms(tr);
		}
		refreshTransportationJob();
		service.initCalculableFields(transportationJob);
		updateCalculableFields();
		capacityService.calculatePlannedCapacities(transportationJob.getId());
		

		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}
	

	public Boolean validateAssignTransportationRequestList() {
		for (TransportationRequest tr : transportationRequestList3) {
			if (tr.getPlannedPickupDate() == null || tr.getPlannedDeliveryDate() == null) {
				FacesContextMessages.ErrorMessages("Planned Pickup/Delivery Time should not be null");
				return false;
			}
			if (tr.getPlannedPickupDuration() == null || tr.getPlannedDeliveryDuration() == null) {
				FacesContextMessages.ErrorMessages("Planned Pickup/Delivery Duration should not be null");
				return false;
			}
			if (tr.getPlannedPickupDate().compareTo(tr.getPlannedDeliveryDate()) >= 0) {
				FacesContextMessages.ErrorMessages("Planned Pickup Time should be lower than Planned Delivery Time");
				return false;
			}
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob, transportationRequestList3)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		if (this.transportationJob.getStatus().equals(TransportationJobStatus.ASSIGNED2) || this.transportationJob.getStatus().equals(TransportationJobStatus.ASSIGNED1)) {

			Double maxVehiculeWeight = vehicleService.findOne(this.transportationJob.getVehicleId()).getMaxWeight();
			Double maxVehiculeVolume = vehicleService.findOne(this.transportationJob.getVehicleId()).getMaxVolume();
			// Simulate planned capacities including new requests
			List<TransportationJobCapacity> simulatedList = capacityService.simulatePlannedCapacities(transportationJob.getId(), transportationRequestList3);

			// Find max cumulative weight & volume from the simulated list
			double maxCumulativeWeight = simulatedList.stream().mapToDouble(TransportationJobCapacity::getCumulativeWeight).max().orElse(0d);

			double maxCumulativeVolume = simulatedList.stream().mapToDouble(TransportationJobCapacity::getCumulativeVolume).max().orElse(0d);

			if (maxVehiculeVolume < maxCumulativeVolume) {
				FacesContextMessages.ErrorMessages("This Transportation Job could not be assigned to this vehicle as you will be exceeding the max Volume of the vehicle, "
						+ "Please change the assigned vehicle.");
				return false;
			}

			if (maxVehiculeWeight < maxCumulativeWeight) {
				FacesContextMessages.ErrorMessages("This Transportation Job could not be assigned to this vehicle as you will be exceeding the max Weight of the vehicle, "
						+ "Please change the assigned vehicle.");
				return false;
			}
		}
		return true;
	}

	// REMOVE FROM TR JOB
	public Boolean canRemoveTransportationRequest(TransportationRequest transportationRequest) {
		return sessionView.isTM() && TransportationRequestStatus.ASSIGNED.equals(transportationRequest.getStatus());
	}

	public void removeTransportationRequest() {
		if (!canRemoveTransportationRequest(transportationRequest))
			return;

		transportationRequest.setPlannedPickupDate(null);
		transportationRequest.setPlannedDeliveryDate(null);

		transportationRequest.setExpectedPickupDate(null);
		transportationRequest.setExpectedDeliveryDate(null);

		transportationRequest.setStatus(TransportationRequestStatus.APPROVED);
		transportationRequest.setUser4(null);
		transportationRequest.setDate4(null);
		transportationRequest.setTransportationJob(null);
		transportationRequestService.save(transportationRequest);
		refreshTransportationJob();
		updateCalculableFields();
		capacityService.calculatePlannedCapacities(transportationJob.getId());
	}

	// UPDATE Expected Times
	public Boolean canUpdateExpectedPickupDate(TransportationRequest transportationRequest) {
		return sessionView.isTM() && transportationRequest.getPickupDate() == null;
	}

	public void updateExpectedPickupDate(TransportationRequest transportationRequest) {
		if (!canUpdateExpectedPickupDate(transportationRequest))
			return;
		if (!validateUpdateExpectedPickupDate(transportationRequest))
			return;

		Date expectedPickupDate = transportationRequest.getExpectedPickupDate();
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		transportationRequest.setExpectedPickupDate(expectedPickupDate);
		transportationRequest = transportationRequestService.save(transportationRequest);
		updateCalculableFields();
	}

	public Boolean validateUpdateExpectedPickupDate(TransportationRequest transportationRequest) {
		if (transportationRequest.getExpectedPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) >= 0) {
			FacesContextMessages.ErrorMessages("Expected Pickup Time should be lower than Expected Delivery Time");
			return false;
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	public Boolean canUpdateExpectedDeliveryDate(TransportationRequest transportationRequest) {
		return sessionView.isTM() && transportationRequest.getDeliveryDate() == null;
	}

	public void updateExpectedDeliveryDate(TransportationRequest transportationRequest) {
		if (!canUpdateExpectedDeliveryDate(transportationRequest))
			return;
		if (!validateUpdateExpectedDeliveryDate(transportationRequest))
			return;

		Date expectedDeliveryDate = transportationRequest.getExpectedDeliveryDate();
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		transportationRequest.setExpectedDeliveryDate(expectedDeliveryDate);
		transportationRequest = transportationRequestService.save(transportationRequest);

		updateCalculableFields();
	}

	public Boolean validateUpdateExpectedDeliveryDate(TransportationRequest transportationRequest) {
		if (transportationRequest.getPickupDate() == null) {
			if (transportationRequest.getExpectedPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) >= 0) {
				FacesContextMessages.ErrorMessages("Expected Pick Up Time should be lower than Expected Delivery Time");
				return false;
			}
		} else if (transportationRequest.getPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) >= 0) {
			FacesContextMessages.ErrorMessages("Pick Up Time should be lower than Expected Delivery Time");
			return false;
		}

		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	public Boolean validateCloseTransportationJob(TransportationJob transportationJob) {
		Double sum = 0.0;

		for (TransportationRequest tr : transportationJob.getTransportationRequestList())
			sum += tr.getCost();

		if (UtilsFunctions.compareDoubles(transportationJob.getCost(), sum, 2) != 0)
			return FacesContextMessages.ErrorMessages("Total TR Costs should be equal to Real Cost : " + UtilsFunctions.formatDouble(transportationJob.getCost()));

		return true;
	}

	// RETURN BACK
	public Boolean canReturnBack(TransportationRequestStatus status) {
		return !TransportationJobStatus.ACKNOWLEDGED.equals(transportationJob.getStatus()) && sessionView.isTM()
				&& Arrays.asList(TransportationRequestStatus.PICKEDUP, TransportationRequestStatus.DELIVERED).contains(status);
	}

	public Boolean canReturnBack() {
		return canReturnBack(transportationRequest.getStatus());
	}

	public void returnBack() {
		if (!canReturnBack())
			return;
		if (TransportationRequestStatus.PICKEDUP.equals(transportationRequest.getStatus())) {
			transportationRequest.setStatus(TransportationRequestStatus.ASSIGNED);
			transportationRequest.setDate5(null);
			transportationRequest.setUser5(null);
			transportationRequest.setPickupDate(null);
		} else if (TransportationRequestStatus.DELIVERED.equals(transportationRequest.getStatus())) {
			transportationRequest.setStatus(TransportationRequestStatus.PICKEDUP);
			transportationRequest.setDate6(null);
			transportationRequest.setUser6(null);
			transportationRequest.setDeliveryDate(null);
		}
		transportationRequest = transportationRequestService.save(transportationRequest);
		updateCalculableFields();
	}

	// PICK UP
	public Boolean canPickup(TransportationRequestStatus status) {
		return Arrays.asList(TransportationJobStatus.STARTED, TransportationJobStatus.IN_PROGRESS, TransportationJobStatus.COMPLETED).contains(transportationJob.getStatus()) && //
				TransportationRequestStatus.ASSIGNED.equals(status) && //
				(sessionView.isTheConnectedUser(transportationJob.getDriver()) || //
						(sessionView.getIsInternalTM() && sessionView.isTheConnectedUser(transportationJob.getUser1())));
	}

	public Boolean canPickup() {
		return canPickup(transportationRequest.getStatus());
	}

	public void pickupTransportationRequest() {
		if (!canPickup())
			return;
		if (!validatePickupTransportationRequest())
			return;
		transportationRequest.setStatus(TransportationRequestStatus.PICKEDUP);
		transportationRequest.setDate5(new Date());
		transportationRequest.setUser5(sessionView.getUser());
		transportationRequestHistoryService.pickedupNew(transportationRequest, sessionView.getUser());
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		transportationRequestService.sendNotification(transportationRequest);
		capacityService.pickupVolumeAndWeight(transportationRequest.getId());
//		emailService.transportationRequestNotification(transportationRequest);
		// smsService.sendSms(transportationRequest);

		updateCalculableFields();
	}

	public Boolean validatePickupTransportationRequest() {
		if (transportationRequest.getPickupDate().compareTo(new Date()) > 0) {
			FacesContextMessages.ErrorMessages("Pickup Time should be lower than Current Time");
			return false;
		}
		if (transportationRequest.getPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) > 0) {
			FacesContextMessages.ErrorMessages("Expected Delivery Time should be greather than Pickup Time");
			return false;
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		Double maxCumulativeWeight = transportationJobCapacityRepos.findMaxCumulativeWeightByTransportationJobIdAndType(transportationRequest.getTransportationJob().getId(),
				"Real");
		if (maxCumulativeWeight == null) {
			maxCumulativeWeight = 0d;
		}

		Double maxCumulativeVolume = transportationJobCapacityRepos.findMaxCumulativeVolumeByTransportationJobIdAndType(transportationRequest.getTransportationJob().getId(),
				"Real");
		if (maxCumulativeVolume == null) {
			maxCumulativeVolume = 0d;
		}

		// Vehicle limits
		Double maxVehiculeWeight = vehicleService.findOne(transportationRequest.getTransportationJob().getVehicleId()).getMaxWeight();
		Double maxVehiculeVolume = vehicleService.findOne(transportationRequest.getTransportationJob().getVehicleId()).getMaxVolume();

		// Validation BEFORE inserting pickup
		if (maxCumulativeVolume + (transportationRequest.getVolume() != null ? transportationRequest.getVolume() : 0d) > maxVehiculeVolume) {
			return FacesContextMessages.ErrorMessages("Pickup of this TR could not be completed as you will be exceeding the maximum Volume of the vehicle.");
		}

		if (maxCumulativeWeight + (transportationRequest.getGrossWeight() != null ? transportationRequest.getGrossWeight() : 0d) > maxVehiculeWeight) {
			return FacesContextMessages.ErrorMessages("Pickup of this TR could not be completed as you will be exceeding the maximum Weight of the vehicle.");
		}

		return true;
	}

	// DELIVER
	public Boolean canDeliver(TransportationRequestStatus status) {
		return Arrays.asList(TransportationJobStatus.STARTED, TransportationJobStatus.IN_PROGRESS, TransportationJobStatus.COMPLETED).contains(transportationJob.getStatus()) && //
				TransportationRequestStatus.PICKEDUP.equals(status) && //
				(sessionView.isTheConnectedUser(transportationJob.getDriver()) || //
						(sessionView.getIsInternalTM() && sessionView.isTheConnectedUser(transportationJob.getUser1())));
	}

	public Boolean canDeliver() {
		return canDeliver(transportationRequest.getStatus());
	}

	public void deliverTransportationRequest() {
		if (!canDeliver())
			return;
		if (!validateDeliverTransportationRequest())
			return;
		transportationRequest.setStatus(TransportationRequestStatus.DELIVERED);
		transportationRequest.setDate6(new Date());
		transportationRequest.setUser6(sessionView.getUser());
		transportationRequestHistoryService.delivredNew(transportationRequest, sessionView.getUser());
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		transportationRequestService.sendNotification(transportationRequest);
//		emailService.transportationRequestNotification(transportationRequest);
		smsService.sendSms(transportationRequest);

		updateCalculableFields();
	}

	public Boolean validateDeliverTransportationRequest() {
		if (transportationRequest.getDeliveryDate().compareTo(new Date()) > 0) {
			FacesContextMessages.ErrorMessages("Delivery Time should be lower than Current Time");
			return false;
		}
		if (transportationRequest.getDeliveryDate().compareTo(transportationRequest.getPickupDate()) <= 0) {
			FacesContextMessages.ErrorMessages("Delivery Time should be greather than Pickup Date");
			return false;
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	// DELETE TRANSPORTATIONJOB
	public Boolean canDeleteTransportationJob() {
		return Arrays
				.asList(TransportationJobStatus.EDITED, TransportationJobStatus.ASSIGNED1, TransportationJobStatus.ASSIGNED2, TransportationJobStatus.ACCEPTED,
						TransportationJobStatus.STARTED)
				.contains(transportationJob.getStatus()) //
				&& sessionView.isTM() //
				&& sessionView.isTheConnectedUser(transportationJob.getUser1()) //
				&& transportationJob.getTransportationRequestList().isEmpty();
	}

	public String deleteTransportationJob() {
		if (canDeleteTransportationJob())
			try {
				transportationJobService.delete(transportationJob);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private String transportationJobFileType;
	private Integer transportationJobFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		TransportationJobFile transportationJobFile = new TransportationJobFile(file, transportationJobFileType, event.getFile().getFileName(), sessionView.getUser(),
				transportationJob);
		transportationJobFileService.save(transportationJobFile);
		synchronized (TransportationJobView.class) {
			refreshTransportationJob();
		}
	}

	public void deleteTransportationJobFile() {
		try {
			transportationJobFileService.delete(transportationJobFileId);
			refreshTransportationJob();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	// costs management
	public Boolean canEditCosts() {
		return sessionView.getIsTrPayment() //
				&& Arrays.asList(TransportationJobStatus.ASSIGNED2, TransportationJobStatus.ACCEPTED, TransportationJobStatus.STARTED, TransportationJobStatus.IN_PROGRESS,
						TransportationJobStatus.COMPLETED, TransportationJobStatus.ACKNOWLEDGED).contains(transportationJob.getStatus());
	}

	public void editCosts() {
		if (!canEditCosts())
			return;
		transportationJob.calculateCost();
		calculateTrListCosts();
		transportationJobService.save(transportationJob);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
	}

	public void calculateTrListCosts() {
		if (transportationJob.getTransportationRequestList().stream().anyMatch(tr -> deliveryRequestService.getGrossWeight(tr.getDeliveryRequestId()).equals(0.0))) {
			FacesContextMessages.ErrorMessages("TR gross weight equal to 0 !");
			return;
		}
		if (transportationJob.getTransportationRequestList().size() == 1) {
			TransportationRequest tr = transportationJob.getTransportationRequestList().get(0);
			tr.setStartCost(transportationJob.getStartCost());
			tr.setItineraryCost(transportationJob.getItineraryCost());
			tr.setHandlingCost(transportationJob.getHandlingCost());
			tr.calculateCost();
		} else { // else use proportion
			Boolean useRealDistance = !transportationJob.getTransportationRequestList().stream().anyMatch(tr -> tr.getRealDistance() == null || tr.getRealDistance().equals(0.0));
			Double total = transportationJob.getTransportationRequestList().stream().mapToDouble(tr -> deliveryRequestService.getVolume(tr.getDeliveryRequestId())
					* deliveryRequestService.getGrossWeight(tr.getDeliveryRequestId()) * tr.getDistance(useRealDistance)).sum();
			transportationJob.getTransportationRequestList().forEach(tr -> {
				Double proportion = deliveryRequestService.getVolume(tr.getDeliveryRequestId()) * deliveryRequestService.getGrossWeight(tr.getDeliveryRequestId())
						* tr.getDistance(useRealDistance) / total;
				tr.setStartCost(proportion * transportationJob.getStartCost());
				tr.setItineraryCost(proportion * transportationJob.getItineraryCost());
				tr.setHandlingCost(proportion * transportationJob.getHandlingCost());
				tr.calculateCost();
			});
		}
	}

	public Boolean canRecalculateTrListCosts() {
		return canEditCosts() //
				&& ((UtilsFunctions.compareDoubles(transportationJob.getStartCost(),
						transportationJob.getTransportationRequestList().stream().mapToDouble(i -> i.getStartCost()).sum()) != 0)
						|| (UtilsFunctions.compareDoubles(transportationJob.getItineraryCost(),
								transportationJob.getTransportationRequestList().stream().mapToDouble(i -> i.getItineraryCost()).sum()) != 0)
						|| (UtilsFunctions.compareDoubles(transportationJob.getHandlingCost(),
								transportationJob.getTransportationRequestList().stream().mapToDouble(i -> i.getHandlingCost()).sum()) != 0));
	}

	public void recalculateTrListCosts() {
		if (!canRecalculateTrListCosts())
			return;
		calculateTrListCosts();
		transportationJobService.save(transportationJob);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
	}

//	public void editTrListCosts() {
//		if (!canEditCosts())
//			return;
//		if (!validateEditTrListCosts())
//			return;
//
//		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
//			tr.calculateCost();
//			transportationRequestService.save(tr);
//		}
//
//		editTrListCosts = false;
//	}

//	public Boolean validateEditTrListCosts() {
//
//		if (transportationJob.getTransportationRequestList().stream().filter(i -> i.getStartCost() == null).count() > 0)
//			return FacesContextMessages.ErrorMessages("Start Cost should not be null");
//		if (transportationJob.getTransportationRequestList().stream().filter(i -> i.getItineraryCost() == null).count() > 0)
//			return FacesContextMessages.ErrorMessages("Itinerary Cost should not be null");
//		if (transportationJob.getTransportationRequestList().stream().filter(i -> i.getHandlingCost() == null).count() > 0)
//			return FacesContextMessages.ErrorMessages("Handling Cost should not be null");
//
//		if (UtilsFunctions.compareDoubles(transportationJob.getStartCost(), transportationJob.getTransportationRequestList().stream().mapToDouble(i -> i.getStartCost()).sum()) != 0)
//			return FacesContextMessages.ErrorMessages("Total TR start costs should be equal to TJ start cost");
//		if (UtilsFunctions.compareDoubles(transportationJob.getItineraryCost(), transportationJob.getTransportationRequestList().stream().mapToDouble(i -> i.getItineraryCost()).sum()) != 0)
//			return FacesContextMessages.ErrorMessages("Total TR itinerary costs should be equal to TJ itinerary cost");
//		if (UtilsFunctions.compareDoubles(transportationJob.getHandlingCost(), transportationJob.getTransportationRequestList().stream().mapToDouble(i -> i.getHandlingCost()).sum()) != 0)
//			return FacesContextMessages.ErrorMessages("Total TR handling costs should be equal to TJ handling cost");
//
//		return true;
//	}

	// counts

	public Long countToAssign1() {
		return service.countToAssign1(sessionView.getUsername());
	}

	public Long countToAssign2() {
		return service.countToAssign2(sessionView.getUser().getTransporterId());
	}

	public Long countToAccept() {
		return service.countToAccept(sessionView.getUsername());
	}

	public Long countToStart() {
		return service.countToStart(sessionView.getUsername());
	}

	public Long countToComplete() {
		return service.countToComplete(sessionView.getUsername());
	}

	public Long countTotal() {
		return countToAssign1() + countToAssign2() + countToAccept() + countToStart() + countToComplete();
	}

	// assignt tj 2 steps

	public void updateCoordinates() {
		if (mapLatitude != null && mapLongitude != null) {
			transportationJob.setPlannedStartLatitude(mapLatitude);
			transportationJob.setPlannedStartLongitude(mapLongitude);
		}
	}

	public String goToStep1() {
		this.currentStep = STEP_ASSIGNMENT;
		return null; // Stay on same page
	}

	public String goToStep2() {
		this.currentStep = STEP_SCHEDULE_LOCATION;
		return null;
	}

	public boolean isStep1() {
		return currentStep == STEP_ASSIGNMENT;
	}

	public boolean isStep2() {
		return currentStep == STEP_SCHEDULE_LOCATION;
	}

	public boolean isStepAssignment() {
		return currentStep == STEP_ASSIGNMENT;
	}

	public boolean isStepScheduleLocation() {
		return currentStep == STEP_SCHEDULE_LOCATION;
	}

	public boolean isStep1Incomplete() {
		return !isStep1Complete();
	}

	public boolean isStep1Complete() {
		if (transportationJob == null || transportationJob.getAssignmentType() == null) {
			return false;
		}

		switch (transportationJob.getAssignmentType()) {
		case TRANSPORTER:
			return transportationJob.getTransporter() != null;

		case INTERNAL_DRIVER:
		case EXTERNAL_DRIVER:
			return transportationJob.getDriver() != null && transportationJob.getVehicle() != null;

		default:
			return false;
		}
	}

	public boolean isStep2Incomplete() {
		return !isStep2Complete();
	}

	public boolean isStep2Complete() {
		if (transportationJob == null) {
			return false;
		}

		return transportationJob.getAcceptLeadTime() != null && transportationJob.getStartLeadTime() != null && transportationJob.getPlannedStartLatitude() != null
				&& transportationJob.getPlannedStartLongitude() != null;
	}

	public String getCurrentStepTitle() {
		switch (currentStep) {
		case STEP_ASSIGNMENT:
			return "Assignment Selection";
		case STEP_SCHEDULE_LOCATION:
			return "Schedule & Location";
		default:
			return "Unknown Step";
		}
	}

	public String getCurrentStepDescription() {
		switch (currentStep) {
		case STEP_ASSIGNMENT:
			return "Select assignment type, driver/transporter, and vehicle";
		case STEP_SCHEDULE_LOCATION:
			return "Set lead times and planned start location";
		default:
			return "";
		}
	}

	public void resetToStep1() {
		currentStep = STEP_ASSIGNMENT;
	}

	public String goToStep3() {

		if (!isStep2Incomplete()) {
			this.currentStep = 3;
		} else {

			FacesContextMessages.ErrorMessages("Start position should not be null");
		}
		return null;
	}
	
	//COSTS 
	  public  List<TrCost> getCostList() {
	        List<TrCost> list = new ArrayList<>();
	        List<TransportationRequest> trs = transportationJob.getTransportationRequestList();
	        for (TransportationRequest transportationRequest : trs) {
	        	 list.add(new TrCost(transportationRequest.getReference(), transportationRequest.getPickupDate(), transportationRequest.getDeliveryDate(), 
	 	        		transportationRequest.getStartCost(), transportationRequest.getItineraryCost(), transportationRequest.getHandlingCost()));
			}
	       
	        return list;
	    }

	// comments
	private TransportationJobComment comment = new TransportationJobComment();

	public Boolean canAddComment() {
		return true;
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		transportationJob.addComment(comment);
		transportationJob = service.saveAndRefresh(transportationJob);
	}

	public Boolean canDeleteComment(TransportationJobComment comment) {
		return sessionView.isTheConnectedUser(comment.getUser());
	}

	public void deleteComment() {
		if (!canDeleteComment(comment))
			return;
		transportationJob.removeComment(comment);
		transportationJob = service.saveAndRefresh(transportationJob);
	}
	
	public Date getNow() {
	    return new Date();
	}


	// google adress
	public String getStartAddress() {
		Double lat = transportationJob.getPlannedStartLatitude() != null ? transportationJob.getPlannedStartLatitude() : transportationJob.getFirstLatitude();
		Double lng = transportationJob.getPlannedStartLongitude() != null ? transportationJob.getPlannedStartLongitude() : transportationJob.getFirstLongitude();

		String latlng = lat + "," + lng;
		return geocodeService.getAddress(latlng);
	}

	public String getStartAddress2() {
		Double lat = transportationJob.getStartLatitude();
		Double lng = transportationJob.getStartLongitude();

		if (lat == null || lng == null) {
			return "No coordinates";
		}

		String latlng = lat + "," + lng;
		return geocodeService.getAddress(latlng);
	}

	// GETTERS & SETTERS

	public TransportationJobService getTransportationJobService() {
		return transportationJobService;
	}

	public void setTransportationJobService(TransportationJobService transportationJobService) {
		this.transportationJobService = transportationJobService;
	}

	public TransportationJob getTransportationJob() {
		return transportationJob;
	}

	public void setTransportationJob(TransportationJob transportationJob) {
		this.transportationJob = transportationJob;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public TransportationJobFileService getTransportationJobFileService() {
		return transportationJobFileService;
	}

	public void setTransportationJobFileService(TransportationJobFileService transportationJobFileService) {
		this.transportationJobFileService = transportationJobFileService;
	}

	public String getTransportationJobFileType() {
		return transportationJobFileType;
	}

	public void setTransportationJobFileType(String transportationJobFileType) {
		this.transportationJobFileType = transportationJobFileType;
	}

	public Integer getTransportationJobFileId() {
		return transportationJobFileId;
	}

	public void setTransportationJobFileId(Integer transportationJobFileId) {
		this.transportationJobFileId = transportationJobFileId;
	}

	public TransportationJobFile getTransportationJobFile() {
		return transportationJobFile;
	}

	public void setTransportationJobFile(TransportationJobFile transportationJobFile) {
		this.transportationJobFile = transportationJobFile;
	}

	public List<TransportationRequest> getTransportationRequestList1() {
		return transportationRequestList1;
	}

	public void setTransportationRequestList1(List<TransportationRequest> transportationRequestList1) {
		this.transportationRequestList1 = transportationRequestList1;
	}

	public List<TransportationRequest> getTransportationRequestList2() {
		return transportationRequestList2;
	}

	public void setTransportationRequestList2(List<TransportationRequest> transportationRequestList2) {
		this.transportationRequestList2 = transportationRequestList2;
	}

	public List<TransportationRequest> getTransportationRequestList3() {
		return transportationRequestList3;
	}

	public void setTransportationRequestList3(List<TransportationRequest> transportationRequestList3) {
		this.transportationRequestList3 = transportationRequestList3;
	}

	public TransportationRequest getTransportationRequest() {
		return transportationRequest;
	}

	public void setTransportationRequest(TransportationRequest transportationRequest) {
		this.transportationRequest = transportationRequest;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public Boolean getViewPathList() {
		return viewPathList;
	}

	public void setViewPathList(Boolean viewPathList) {
		this.viewPathList = viewPathList;
	}

//	public Boolean getEditTrListCosts() {
//		return editTrListCosts;
//	}
//
//	public void setEditTrListCosts(Boolean editTrListCosts) {
//		this.editTrListCosts = editTrListCosts;
//	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public TimelineModel getTimeline1() {
		return timeline1;
	}

	public void setTimeline1(TimelineModel timeline1) {
		this.timeline1 = timeline1;
	}

	public TimelineModel getTimeline2() {
		return timeline2;
	}

	public void setTimeline2(TimelineModel timeline2) {
		this.timeline2 = timeline2;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getToNotifyUserUsername() {
		return toNotifyUserUsername;
	}

	public void setToNotifyUserUsername(String toNotifyUserUsername) {
		this.toNotifyUserUsername = toNotifyUserUsername;
	}

	public TransportationJobState getState() {
		return state;
	}

	public void setState(TransportationJobState state) {
		this.state = state;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

	public Double getMapLatitude() {
		return mapLatitude;
	}

	public void setMapLatitude(Double mapLatitude) {
		this.mapLatitude = mapLatitude;
	}

	public Double getMapLongitude() {
		return mapLongitude;
	}

	public void setMapLongitude(Double mapLongitude) {
		this.mapLongitude = mapLongitude;
	}

	public TransportationJobComment getComment() {
		return comment;
	}

	public void setComment(TransportationJobComment comment) {
		this.comment = comment;
	}

	public Integer getTransporterId() {
		return transporterId;
	}

	public void setTransporterId(Integer transporterId) {
		this.transporterId = transporterId;
	}
	
	

}
