package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Path;
import ma.azdad.model.Role;
import ma.azdad.model.Stop;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobAssignmentType;
import ma.azdad.model.TransportationJobFile;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.TransportationJobState;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestPaymentStatus;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.Vehicle;
import ma.azdad.repos.TransportationJobRepos;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.MapService;
import ma.azdad.service.OldEmailService;
import ma.azdad.service.PathService;
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
	protected CacheView cacheView;

	@Autowired
	protected TransportationJobFileService transportationJobFileService;

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

	@Autowired
	protected OldEmailService emailService;

	@Autowired
	protected SmsService smsService;

	@Autowired
	protected TransporterView transporterView;

	@Autowired
	protected UserView userView;

	private TransportationJob transportationJob = new TransportationJob();
	private TransportationJobFile transportationJobFile;

	private List<TransportationRequest> transportationRequestList1 = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList2 = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList3; // selection

	private TransportationRequest transportationRequest;

	private MapModel mapModel;
	private Marker marker;
	private Boolean viewPathList = false;
	private Boolean editCosts = false;

	private static Map<String, List<Integer>> TO_ASSIGN_MAP = new HashMap<String, List<Integer>>();
	private List<TransportationJob> toAssignList = new ArrayList<TransportationJob>();
	private String key;

	private TransportationJobStatus status;
	private TransportationJobState state;

	private Double latitude;
	private Double longitude;

	private TimelineModel timeline1;
	private TimelineModel timeline2;

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
		} else if (isPage("startTransportationJob"))
			transportationJob = transportationJobService.findOne(id);

	}

	@Override
	protected void initParameters() {
		super.initParameters();
		status = TransportationJobStatus.get(UtilsFunctions.getIntegerParameter("status"));
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
		if (isListPage)
			if (pageIndex == null)
				list2 = list1 = transportationJobService.find();
			else
				switch (pageIndex) {
				case 1:
					list2 = list1 = transportationJobService.findByUser1(sessionView.getUsername(), state);
					break;
				case 4:
					initLists(transportationJobService.findToAssign1(sessionView.getUsername()));
					break;
				case 5:
					initLists(transportationJobService.findToAssign2(sessionView.getUser().getTransporterId()));
					break;
				case 6:
					initLists(transportationJobService.findByDriver(sessionView.getUsername(), status));
					break;
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

	// SAVE TRANSPORTATIONJOB
	public Boolean canSave() {
		if (isListPage || isAddPage)
			return sessionView.isTM();
		else if (isViewPage || isEditPage)
			return sessionView.isTM() && !TransportationJobStatus.CLOSED.equals(transportationJob.getStatus());
		return false;
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
				break;
			case INTERNAL_DRIVER:
				userView.initLists(userService.findActiveDriverList(true));
//				teamView.getList1().forEach(t -> {
//					t.setCountTotalJr(countByTeamAndProject(t.getId(), jobRequest.getProjectId()));
//					t.setCountPendingJr(countPendingByTeamAndProject(t.getId(), jobRequest.getProjectId()));
//					if (t.getTeamLeaderLatitude() != null)
//						t.setDistance(UtilsFunctions.calculateDistance(t.getTeamLeaderLatitude(), t.getTeamLeaderLongitude(), jobRequest.getSiteLatitude(), jobRequest.getSiteLongitude()));
//
//				});
				refreshMapModel();
				break;
			case EXTERNAL_DRIVER:
				switch (transportationJob.getStatus()) {
				case EDITED:
					userView.initLists(userService.findActiveDriverList(false));
					break;
				case ASSIGNED1:
					System.out.println("init user list ROLE_ILOGISTICS_DRIVER : "
							+ userService.findByRoleAndActiveAndTransporter(Role.ROLE_ILOGISTICS_DRIVER, transportationJob.getTransporterId()));
					System.out.println("transportationJob.getTransporterId() : " + transportationJob.getTransporterId());
					userView.initLists(userService.findByRoleAndActiveAndTransporter(Role.ROLE_ILOGISTICS_DRIVER, transportationJob.getTransporterId()));
					break;
				default:
					break;
				}

//				teamView.getList1().forEach(t -> {
//					t.setCountTotalJr(countByTeamAndProject(t.getId(), jobRequest.getProjectId()));
//					t.setCountPendingJr(countPendingByTeamAndProject(t.getId(), jobRequest.getProjectId()));
//					if (t.getTeamLeaderLatitude() != null)
//						t.setDistance(UtilsFunctions.calculateDistance(t.getTeamLeaderLatitude(), t.getTeamLeaderLongitude(), jobRequest.getSiteLatitude(), jobRequest.getSiteLongitude()));
//
//				});
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
		return true;
	}

	public void assign(TransportationJob transportationJob) {
		if (!canAssign(transportationJob))
			return;
		if (this.transportationJob.getStartLeadTime() != null)
			transportationJob.setStartLeadTime(this.transportationJob.getStartLeadTime());
		if (this.transportationJob.getAcceptLeadTime() != null)
			transportationJob.setAcceptLeadTime(this.transportationJob.getAcceptLeadTime());

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
		} else if (isListPage && pageIndex == 6) {
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

		System.out.println("validateStart : ");
		if (transportationJob.getTransportationRequestList().stream().filter(i -> i.getExpectedPickupDate() == null).count() > 0)
			return FacesContextMessages.ErrorMessages("Expected Pickup Date should not be null");

		System.out.println("aaaaaaaaaaaaa");

		return true;
	}

	public String start() {
		if (!canStart())
			return null;
		if (!validateStart())
			return null;
		service.start(transportationJob, sessionView.getUser());
		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}

	// GPS

	public void refreshMapModel() {
		if (isViewPage)
			mapModel = mapService.generate(transportationJob.getStopList(), viewPathList);
		if (isPage("assignTransportationJob")) {
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
		return sessionView.isTM() && !TransportationJobStatus.CLOSED.equals(transportationJob.getStatus());
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
			tr = transportationRequestService.findOne(tr.getId());
			tr.setPlannedPickupDate(plannedPickupDate);
			tr.setPlannedDeliveryDate(plannedDeliveryDate);
			tr.setStatus(TransportationRequestStatus.ASSIGNED);
			tr.setDate4(currentDate);
			tr.setUser4(sessionView.getUser());
			tr.setTransportationJob(transportationJob);
			transportationRequestHistoryService.assignedNew(tr, sessionView.getUser());
			transportationRequestService.save(tr);
			tr = transportationRequestService.findOne(tr.getId());
			// emailService.transportationRequestNotification(tr);
			// smsService.sendSms(tr);
		}

		refreshTransportationJob();
		updateCalculableFields();

		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}

	public Boolean validateAssignTransportationRequestList() {
		for (TransportationRequest tr : transportationRequestList3) {
			if (tr.getPlannedPickupDate() == null || tr.getPlannedDeliveryDate() == null) {
				FacesContextMessages.ErrorMessages("Planned Pickup/Delivery Time should not be null");
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
		return true;
	}

	// REMOVE FROM TR JOB
	public Boolean canRemoveTransportationRequest(TransportationRequest transportationRequest) {
		return sessionView.isTM() && TransportationRequestStatus.ASSIGNED.equals(transportationRequest.getStatus());
	}

	public void removeTransportationRequest() {
		if (!canRemoveTransportationRequest(transportationRequest))
			return;
		transportationRequest.setStatus(TransportationRequestStatus.APPROVED);
		transportationRequest.setUser4(null);
		transportationRequest.setDate4(null);
		transportationRequest.setTransportationJob(null);
		transportationRequestService.save(transportationRequest);
		refreshTransportationJob();
		updateCalculableFields();
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

	// CLOSE TJ
	public Boolean canCloseTransportationJob() {
		return canCloseTransportationJob(transportationJob);
	}

	public Boolean canCloseTransportationJob(TransportationJob transportationJob) {
		return sessionView.isTM() && TransportationJobStatus.COMPLETED.equals(transportationJob.getStatus());
	}

	public void closeTransportationJob(TransportationJob transportationJob) {
		if (!canCloseTransportationJob(transportationJob))
			return;
		if (!validateCloseTransportationJob(transportationJob))
			return;
		transportationJob.setStatus(TransportationJobStatus.CLOSED);
		transportationJob.addHistory(new TransportationJobHistory(TransportationJobStatus.CLOSED.getValue(), sessionView.getUser()));
		transportationJob = transportationJobService.save(transportationJob);
		transportationJob = transportationJobService.findOne(transportationJob.getId());

	}

	public Boolean validateCloseTransportationJob(TransportationJob transportationJob) {
		Double sum = 0.0;

		for (TransportationRequest tr : transportationJob.getTransportationRequestList())
			sum += tr.getCost();

		if (UtilsFunctions.compareDoubles(transportationJob.getRealCost(), sum, 2) != 0)
			return FacesContextMessages.ErrorMessages("Total TR COsts should be equal to Real Cost : " + UtilsFunctions.formatDouble(transportationJob.getRealCost()));

		return true;
	}

	// OPEN TJ
	public Boolean canOpenTransportationJob() {
		return canOpenTransportationJob(transportationJob);
	}

	public Boolean canOpenTransportationJob(TransportationJob transportationJob) {
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			transportationRequestService.updatePaymentStatus(tr.getId());
			tr = transportationRequestService.findOne(tr.getId());
			if (tr.getPaymentStatus() != null && !TransportationRequestPaymentStatus.PENDING.equals(tr.getPaymentStatus()))
				return false;
		}

		return sessionView.isTM() && TransportationJobStatus.CLOSED.equals(transportationJob.getStatus());
	}

	public void openTransportationJob(TransportationJob transportationJob) {
		if (!canOpenTransportationJob(transportationJob))
			return;
		transportationJob.setStatus(TransportationJobStatus.COMPLETED);
		transportationJob.addHistory(new TransportationJobHistory("Re-open", sessionView.getUser()));
		transportationJob = transportationJobService.save(transportationJob);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
	}

	public void openTransportationJob() {
		openTransportationJob(transportationJob);
	}

	// private void calculateTransportationRequestListCosts(TransportationJob
	// transportationJob) {
	// Double total1 = 0.0, total2 = 0.0;
	// Boolean test = true;
	// for (TransportationRequest tr :
	// transportationJob.getTransportationRequestList()) {
	// Double grossWeight =
	// deliveryRequestService.getGrossWeight(tr.getDeliveryRequest().getId());
	// Double estimatedDistance = tr.getEstimatedDistance();
	// total1 += grossWeight * estimatedDistance;
	// total2 += estimatedDistance;
	// if (grossWeight.equals(0.0))
	// test = false;
	// }
	// for (TransportationRequest tr :
	// transportationJob.getTransportationRequestList()) {
	// Double grossWeight =
	// deliveryRequestService.getGrossWeight(tr.getDeliveryRequest().getId());
	// Double estimatedDistance = tr.getEstimatedDistance();
	// tr.setEstimatedCost(transportationJob.getEstimatedCost() * (test ?
	// estimatedDistance * grossWeight / total1 : estimatedDistance / total2));
	// tr.setCost(transportationJob.getRealCost() * (test ? estimatedDistance *
	// grossWeight / total1 : estimatedDistance / total2));
	// transportationRequestService.save(tr);
	// }
	// }

	public void closeTransportationJob() {
		if (isViewPage) {
			closeTransportationJob(transportationJob);
			refreshTransportationJob();

		} else if (pageIndex == 1) {
			for (TransportationJob transportationJob : list4)
				closeTransportationJob(transportationJobService.findOne(transportationJob.getId()));
			refreshList();
		}
	}

	// RETURN BACK
	public Boolean canReturnBack(TransportationRequestStatus status) {
		return !TransportationJobStatus.CLOSED.equals(transportationJob.getStatus()) && sessionView.isTM()
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
		return TransportationRequestStatus.ASSIGNED.equals(status) && //
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
		emailService.transportationRequestNotification(transportationRequest);
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
		return true;
	}

	// DELIVER
	public Boolean canDeliver(TransportationRequestStatus status) {
		return TransportationRequestStatus.PICKEDUP.equals(status) && //
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
		emailService.transportationRequestNotification(transportationRequest);
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
		return TransportationJobStatus.EDITED.equals(transportationJob.getStatus()) //
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

	// Edit TR List Costs
	public Boolean canEditTransportationRequestListCosts() {
		return sessionView.isTM() && TransportationJobStatus.COMPLETED.equals(transportationJob.getStatus());
	}

	public void editTransportationRequestListCosts() {
		if (!canAssignTrList())
			return;
		if (!validateEditTransportationRequestListCosts())
			return;

		for (TransportationRequest tr : transportationJob.getTransportationRequestList())
			transportationRequestService.save(tr);

		editCosts = false;
	}

	public Boolean validateEditTransportationRequestListCosts() {
		Double sum = 0.0;
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			if (tr.getCost() == null)
				return FacesContextMessages.ErrorMessages("TR Cost should not be null");
			sum += tr.getCost();
		}

		if (UtilsFunctions.compareDoubles(transportationJob.getRealCost(), sum, 2) != 0)
			return FacesContextMessages.ErrorMessages("Total Costs should be equal to Real Cost : " + UtilsFunctions.formatDouble(transportationJob.getRealCost()));

		return true;
	}

	// counts

	public Long countToAssign1() {
		return service.countToAssign1(sessionView.getUsername());
	}

	public Long countToAssign2() {
		return service.countToAssign2(sessionView.getUser().getTransporterId());
	}

	public Long countToAccept() {
		return service.countByDriver(sessionView.getUsername(), TransportationJobStatus.ASSIGNED2);
	}

	public Long countToStart() {
		return service.countByDriver(sessionView.getUsername(), TransportationJobStatus.ACCEPTED);
	}

	public Long countTotal() {
		return countToAssign1() + countToAssign2() + countToAccept() + countToStart();
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

	public Boolean getEditCosts() {
		return editCosts;
	}

	public void setEditCosts(Boolean editCosts) {
		this.editCosts = editCosts;
	}

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

}
