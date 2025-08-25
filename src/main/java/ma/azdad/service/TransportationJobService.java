package ma.azdad.service;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.mobile.model.Vehicule;
import ma.azdad.model.DriverLocation;
import ma.azdad.model.Path;
import ma.azdad.model.Role;
import ma.azdad.model.Stop;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobAssignmentType;
import ma.azdad.model.TransportationJobFile;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.TransportationJobItinerary;
import ma.azdad.model.TransportationJobState;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestHistory;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
import ma.azdad.repos.DriverLocationRepo;
import ma.azdad.repos.TransportationJobFileRepos;
import ma.azdad.repos.TransportationJobItineraryRepos;
import ma.azdad.repos.TransportationJobRepos;
import ma.azdad.repos.TransportationRequestRepos;
import ma.azdad.repos.UserRepos;
import ma.azdad.utils.FacesContextMessages;

@Component
public class TransportationJobService extends GenericService<Integer, TransportationJob, TransportationJobRepos> {

	private final TransportationRequestRepos transportationRequestRepos;

	@Autowired
	UserService userService;

	@Autowired
	UserRepos userRepos;
	
	@Autowired
	DriverLocationRepo driverLocationRepo;

	@Autowired
	TransportationJobRepos transportationJobRepos;

	@Autowired
	TransportationJobItineraryRepos transportationJobItineraryRepos;

	@Autowired
	DeliveryRequestService deliveryRequestService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	GoogleGeocodeService googleGeocodeService;

	@Autowired
	TransportationRequestService transportationRequestService;

	@Autowired
	PathService pathService;

	@Autowired
	StopService stopService;

	@Autowired
	TransporterService transporterService;

	@Autowired
	TransportationJobFileRepos transportationJobFileRepos;

	@Autowired
	VehicleService vehicleService;

	TransportationJobService(UserService userService, TransportationRequestRepos transportationRequestRepos) {
		this.userService = userService;
		this.transportationRequestRepos = transportationRequestRepos;
	}

	@Override
	public TransportationJob findOne(Integer id) {
		TransportationJob transportationJob = super.findOne(id);
		Hibernate.initialize(transportationJob.getFileList());
		Hibernate.initialize(transportationJob.getHistoryList());
		Hibernate.initialize(transportationJob.getTransportationRequestList());
		if (transportationJob.getTransportationRequestList() != null)
			for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
				Hibernate.initialize(tr.getHistoryList());
				tr.getDeliveryRequest().getDetailList().forEach(i -> initialize(i.getPacking().getDetailList()));
			}
		Hibernate.initialize(transportationJob.getStopList());
		Hibernate.initialize(transportationJob.getPathList());
		if (transportationJob.getTransporter() != null) {
			Hibernate.initialize(transportationJob.getTransporter().getSupplier());
			Hibernate.initialize(transportationJob.getTransporter().getCompany());
			initialize(transportationJob.getTransporter().getUserList());
		}

		Hibernate.initialize(transportationJob.getVehicle());
		Hibernate.initialize(transportationJob.getDriver());
		return transportationJob;
	}

	public List<TransportationJob> find() {
		return repos.find();
	}

	@Cacheable(value = "transportationJobService.findByUser1")
	public List<TransportationJob> findByUser1(String user1Username, TransportationJobState state) {
		if (state == null)
			return repos.findByUser1(user1Username);
		return repos.findByUser1(user1Username, state.getStatusList());
	}

	@Cacheable(value = "transportationJobService.findToAccept")
	public List<TransportationJob> findToAccept(String username) {
		return repos.findToAccept(username);
	}

	@Cacheable(value = "transportationJobService.countToAccept")
	public Long countToAccept(String username) {
		return repos.countToAccept(username);
	}

	@Cacheable(value = "transportationJobService.findToStart")
	public List<TransportationJob> findToStart(String username) {
		return repos.findToStart(username);
	}

	@Cacheable(value = "transportationJobService.countToStart")
	public Long countToStart(String username) {
		return repos.countToStart(username);
	}

	@Cacheable(value = "transportationJobService.findToComplete")
	public List<TransportationJob> findToComplete(String username) {
		return repos.findToComplete(username);
	}

	@Cacheable(value = "transportationJobService.countToComplete")
	public Long countToComplete(String username) {
		return repos.countToComplete(username);
	}

	@Cacheable(value = "transportationJobService.findByUser1AndStatus")
	public List<TransportationJob> findByUser1AndStatus(String user1Username, TransportationJobStatus transportationJobStatus) {
		return repos.findByUser1AndStatus(user1Username, transportationJobStatus);
	}

	@Cacheable(value = "transportationJobService.countByUser1AndStatus")
	public Long countByUser1AndStatus(String user1Username, TransportationJobStatus transportationJobStatus) {
		return repos.countByUser1AndStatus(user1Username, transportationJobStatus);
	}

	@Cacheable(value = "transportationJobService.findByUser1AndStatus")
	public List<TransportationJob> findByUser1AndStatus(String user1Username, List<TransportationJobStatus> transportationJobStatusList) {
		return repos.findByUser1AndStatus(user1Username, transportationJobStatusList);
	}

	@Cacheable(value = "transportationJobService.countByUser1AndStatus")
	public Long countByUser1AndStatus(String user1Username, List<TransportationJobStatus> transportationJobStatusList) {
		return repos.countByUser1AndStatus(user1Username, transportationJobStatusList);
	}

	@Cacheable(value = "transportationJobService.findToAssign1")
	public List<TransportationJob> findToAssign1(String user1Username) {
		return repos.findToAssign1(user1Username);
	}

	@Cacheable(value = "transportationJobService.countToAssign1")
	public Long countToAssign1(String user1Username) {
		return ObjectUtils.firstNonNull(repos.countToAssign1(user1Username), 0l);
	}

	@Cacheable(value = "transportationJobService.findToAssign2")
	public List<TransportationJob> findToAssign2(Integer transporterId) {
		return repos.findToAssign2(transporterId);
	}

	@Cacheable(value = "transportationJobService.countToAssign2")
	public Long countToAssign2(Integer transporterId) {
		return ObjectUtils.firstNonNull(repos.countToAssign2(transporterId), 0l);
	}

	@Cacheable(value = "transportationJobService.findByDriver")
	public List<TransportationJob> findByDriver(String driverUsername, TransportationJobStatus status) {
		if (status == null)
			return repos.findByDriver(driverUsername);
		return repos.findByDriver(driverUsername, status);
	}

	@Cacheable(value = "transportationJobService.findByDriver")
	public List<TransportationJob> findByDriver(String driverUsername, TransportationJobState state) {
		if (state == null)
			return repos.findByDriver(driverUsername);
		return repos.findByDriver(driverUsername, state.getStatusList());
	}

	@Cacheable(value = "transportationJobService.findByTransporter")
	public List<TransportationJob> findByTransporter(Integer transporterId, TransportationJobState state) {
		if (state == null)
			return repos.findByTransporter(transporterId);
		return repos.findByTransporter(transporterId, state.getStatusList());
	}

	@Cacheable(value = "transportationJobService.countByDriverAndStatus")
	public Long countByDriver(String driverUsername, TransportationJobStatus status) {
		return ObjectUtils.firstNonNull(repos.countByDriver(driverUsername, status), 0l);
	}

	public List<TransportationJob> findByIdList(List<Integer> id) {
		return repos.findByIdList(id);
	}

	public Map<String, Integer> getMapDateAndPlace(TransportationJob transportationJob) {
		Map<String, Integer> result = new HashMap<>(); // positive:site, negative:warehouse
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			String startDateStr = UtilsFunctions.getFormattedDateTime(tr.getStartDate());
			String endDateStr = UtilsFunctions.getFormattedDateTime(tr.getEndDate());
			result.put(startDateStr, tr.getIsOutbound() ? -tr.getWarehouseId() : tr.getOriginId());
			result.put(endDateStr, tr.getIsInbound() ? -tr.getWarehouseId() : tr.getDestinationId());
		}
		return result;
	}

	public Boolean validateTransportationRequestListDates(TransportationJob transportationJob, List<TransportationRequest> newList) {
		// positive:site, negative:warehouse
		Map<String, Integer> mapDateAndPlace = getMapDateAndPlace(transportationJob);
		for (TransportationRequest tr : newList) {
			String startDateStr = UtilsFunctions.getFormattedDateTime(tr.getStartDate());
			String endDateStr = UtilsFunctions.getFormattedDateTime(tr.getEndDate());
			Integer old;
			old = mapDateAndPlace.put(startDateStr, tr.getIsOutbound() ? -tr.getWarehouseId() : tr.getOriginId());
			if (old != null && !old.equals(mapDateAndPlace.get(startDateStr)))
				return false;
			old = mapDateAndPlace.put(endDateStr, tr.getIsInbound() ? -tr.getWarehouseId() : tr.getDestinationId());
			if (old != null && !old.equals(mapDateAndPlace.get(endDateStr)))
				return false;
		}
		return true;
	}

	public Boolean validateTransportationRequestListDates(TransportationJob transportationJob) {
		Map<String, Integer> mapDateAndPlace = new HashMap<>();
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			String startDateStr = UtilsFunctions.getFormattedDateTime(tr.getStartDate());
			String endDateStr = UtilsFunctions.getFormattedDateTime(tr.getEndDate());
			Integer old;
			old = mapDateAndPlace.put(startDateStr, tr.getIsOutbound() ? -tr.getWarehouseId() : tr.getOriginId());
			if (old != null && !old.equals(mapDateAndPlace.get(startDateStr)))
				return false;
			old = mapDateAndPlace.put(endDateStr, tr.getIsInbound() ? -tr.getWarehouseId() : tr.getDestinationId());
			if (old != null && !old.equals(mapDateAndPlace.get(endDateStr)))
				return false;
		}
		return true;
	}

	public List<TransportationJob> find(TransportationJobStatus status) {
		return repos.find(status);
	}

	public List<TransportationJob> find(List<TransportationJobStatus> status) {
		return repos.find(status);
	}

	public List<TransportationJob> find(TransportationJobState state) {
		if (state == null)
			return repos.find();
		return repos.find(state.getStatusList());
	}

	public void calculateTransportationRequestListCosts(TransportationJob transportationJob, Boolean setCost) {
		Double total1 = 0.0, total2 = 0.0;
		Boolean test = true;
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			Double grossWeight = deliveryRequestService.getGrossWeight(tr.getDeliveryRequest().getId());
			Double estimatedDistance = tr.getEstimatedDistance();
			total1 += grossWeight * estimatedDistance;
			total2 += estimatedDistance;
			if (grossWeight.equals(0.0))
				test = false;
		}

		if (total2.equals(0.0))
			return;

		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			Double grossWeight = deliveryRequestService.getGrossWeight(tr.getDeliveryRequest().getId());
			Double estimatedDistance = tr.getEstimatedDistance();
			tr.setEstimatedCost(transportationJob.getEstimatedCost() * (test ? estimatedDistance * grossWeight / total1 : estimatedDistance / total2));
			if (setCost)
				tr.setCost(transportationJob.getRealCost() * (test ? estimatedDistance * grossWeight / total1 : estimatedDistance / total2));
			transportationRequestService.save(tr);
		}
	}

	public void updateCalculableFields(TransportationJob transportationJob, Boolean setCost) {
		try {
			transportationJob.init();
			transportationJob.calculateStartDate();
			transportationJob.calculateEndDate();
			transportationJob.calculateStatus();
			for (Stop stop : transportationJob.getStopList())
				stopService.delete(stop);
			for (Path path : transportationJob.getPathList())
				pathService.delete(path);
			transportationJob.getPathList().clear();
			transportationJob.getStopList().clear();
			transportationJob.generateStopList();
			transportationJob.generatePathList();
			calculateTransportationRequestListCosts(transportationJob, setCost);
			save(transportationJob);
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	@Transactional
	public void correctExistingTransportationRequestList() {
		List<TransportationRequest> list = transportationRequestService
				.findByNotHavingTransportationJob(Arrays.asList(TransportationRequestStatus.PICKEDUP, TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED));
		for (TransportationRequest transportationRequest : list) {
			TransportationJob tj = new TransportationJob();
			tj.setTransporter(transportationRequest.getTransporter());
			tj.setDriver(transportationRequest.getDriver());
			tj.setVehicle(transportationRequest.getVehicle());
			tj.setVehiclePrice(transportationRequest.getVehicle().getVehicleType().getPrice());
			tj.addHistory(new TransportationJobHistory("Created", userRepos.findById("a.azdad").get()));
			tj = save(tj);
			transportationRequestService.calculateEstimatedDistanceAndDuration(transportationRequest);
			transportationRequest.setTransportationJob(tj);
			transportationRequest = transportationRequestService.save(transportationRequest);
			tj = findOne(tj.getId());
			tj.getTransportationRequestList().add(transportationRequest);
			System.out.println(tj.getTransportationRequestList());
			updateCalculableFields(tj, true);
		}

	}

	public void generateReferenceScript() {
		repos.findAll().forEach(i -> {
			i.generateReference();
			save(i);
		});
	}

	// workflow

	public void assign(TransportationJob transportationJob, TransportationJobAssignmentType assignmentType, Integer transporterId, String driverUsername, Integer vehicleId, User connectedUser) {
		transportationJob.setAssignmentType(assignmentType);

		switch (transportationJob.getAssignmentType()) {
		case TRANSPORTER:
			transportationJob.setStatus(TransportationJobStatus.ASSIGNED1);
			transportationJob.setDate2(new Date());
			transportationJob.setUser2(connectedUser);
			transportationJob.setTransporter(transporterService.findOneLight(transporterId));
			transportationJob.addHistory(new TransportationJobHistory("Assigned", connectedUser, "Assigned to transporter <b class='blue'>" + transportationJob.getTransporterName() + "</b>"));
			transportationJob.getTransportationRequestList().forEach(i -> {
				i.setTransporter(transportationJob.getTransporter());
				i.addHistory(new TransportationRequestHistory("Assigned", connectedUser, "Assigned to transporter <b class='blue'>" + transportationJob.getTransporterName() + "</b>"));
			});
			break;
		case INTERNAL_DRIVER:
		case EXTERNAL_DRIVER:
			transportationJob.setStatus(TransportationJobStatus.ASSIGNED2);
			transportationJob.setDate3(new Date());
			transportationJob.setUser3(connectedUser);
			transportationJob.setDriver(userService.findOneLight(driverUsername));
			transportationJob.setVehicle(vehicleService.findOneLight(vehicleId));
			transportationJob.setTransporter(transporterService.findOneLight(transportationJob.getDriver().getTransporterId()));
			transportationJob.addHistory(new TransportationJobHistory("Assigned", connectedUser, "Assigned to driver <b class='green'>" + transportationJob.getDriverFullName() + "</b>"));
			transportationJob.getTransportationRequestList().forEach(i -> {
				i.setDriver(transportationJob.getDriver());
				i.setVehicle(transportationJob.getVehicle());
				i.setTransporter(transportationJob.getTransporter());
				i.addHistory(new TransportationRequestHistory("Assigned", connectedUser, "Assigned to driver <b class='green'>" + transportationJob.getDriverFullName() + "</b>"));
			});
			break;
		}

		transportationJob.calculateMaxAcceptTime();
		transportationJob.calculateMaxStartTime();
		save(transportationJob);
	}

	public Boolean canAccept(TransportationJob transportationJob, String connectedUserUsername) {
		return TransportationJobStatus.ASSIGNED2.equals(transportationJob.getStatus()) //
				&& (connectedUserUsername.equalsIgnoreCase(transportationJob.getDriverUsername()) //
						|| connectedUserUsername.equalsIgnoreCase(transportationJob.getUser1Username()));
	}

	public void accept(TransportationJob transportationJob, User connectedUser) {
		transportationJob.setStatus(TransportationJobStatus.ACCEPTED);
		transportationJob.setDate4(new Date());
		transportationJob.setUser4(connectedUser);
		transportationJob.addHistory(new TransportationJobHistory("Accepted", connectedUser));
		save(transportationJob);
	}

	public Boolean canDecline(TransportationJob transportationJob, String connectedUserUsername) {
		return canAccept(transportationJob, connectedUserUsername);
	}

	public void decline(TransportationJob transportationJob, User connectedUser) {
		transportationJob.setStatus(TransportationJobStatus.EDITED);
		transportationJob.setDate2(null);
		transportationJob.setUser2(null);
		transportationJob.setDate3(null);
		transportationJob.setUser3(null);
		transportationJob.addHistory(new TransportationJobHistory("Declined", connectedUser));
		save(transportationJob);
	}

	public Boolean canStart(TransportationJob transportationJob, String connectedUserUsername) {
		return TransportationJobStatus.ACCEPTED.equals(transportationJob.getStatus()) //
				&& (connectedUserUsername.equalsIgnoreCase(transportationJob.getDriverUsername()) //
						|| connectedUserUsername.equalsIgnoreCase(transportationJob.getUser1Username()));
	}

	public void start(TransportationJob transportationJob, User connectedUser) {
		transportationJob.setStatus(TransportationJobStatus.STARTED);
		transportationJob.setDate5(new Date());
		transportationJob.setUser5(connectedUser);
		transportationJob.addHistory(new TransportationJobHistory("Started", connectedUser));
		save(transportationJob);
	}

	private Boolean isTM(List<Role> roleList) {
		return roleList.contains(Role.ROLE_ILOGISTICS_TM);
	}

	public Boolean canUnassign(TransportationJob transportationJob, String connectedUserUsername, List<Role> roleList) {
		return Arrays.asList(TransportationJobStatus.ASSIGNED1, TransportationJobStatus.ASSIGNED2).contains(transportationJob.getStatus()) //
				&& isTM(roleList) //
				&& connectedUserUsername.equalsIgnoreCase(transportationJob.getUser1().getUsername());
	}

	public void unassign(TransportationJob transportationJob, User connectedUser) {
		transportationJob.setStatus(TransportationJobStatus.EDITED);
		transportationJob.setDate2(null);
		transportationJob.setUser2(null);
		transportationJob.setDate3(null);
		transportationJob.setUser3(null);
		transportationJob.addHistory(new TransportationJobHistory("Unassign", connectedUser));
		transportationJob.getTransportationRequestList().forEach(i -> i.addHistory(new TransportationRequestHistory("Unassign", connectedUser)));
		save(transportationJob);
	}

	// reactivity & performance

	public Long getAcceptPerformance(String username) {
		Long countAcceptedWithinDeadLine = ObjectUtils.firstNonNull(repos.countAcceptedWithinDeadLineByDriver(username), 0l);
		Long countAccepted = ObjectUtils.firstNonNull(repos.countAcceptedByDriver(username), 0l);
		if (countAccepted == 0)
			return 0l;
		return countAcceptedWithinDeadLine * 100 / countAccepted;
	}

	public Long getStartPerformance(String username) {
		Long countStartedWithinDeadLine = ObjectUtils.firstNonNull(repos.countStartedWithinDeadLineByDriver(username), 0l);
		Long countStarted = ObjectUtils.firstNonNull(repos.countStartedByDriver(username), 0l);
		if (countStarted == 0)
			return 0l;
		return countStartedWithinDeadLine * 100 / countStarted;
	}

	public Long getReactivity(String username) {
		return getReactivity(getAcceptPerformance(username), getStartPerformance(username));
	}

	public Long getReactivity(Long acceptPerfomance, Long startPerfomance) {
		return (acceptPerfomance + startPerfomance) / 2;
	}

	// mobile
	public List<ma.azdad.mobile.model.TransportationJob> findMobile(TransportationJobStatus status) {
		List<TransportationJob> list = repos.findMobile(status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			System.out.println("driver : " + tj.getDriver());
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByUser1Mobile(List<TransportationJobStatus> status, String username) {
		List<TransportationJob> list = repos.findByUser1Mobile(username, status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByUser1Mobile(String username) {
		List<TransportationJob> list = repos.findByUser1Mobile(username);
		System.out.println("tj size" + list.size());
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByUser1MobileByStatus(Integer state, String username) {
		switch (state) {
		case 0:
			return findByUser1Mobile(Arrays.asList(TransportationJobStatus.EDITED, TransportationJobStatus.ASSIGNED1, TransportationJobStatus.ASSIGNED2, TransportationJobStatus.ACCEPTED), username);
		case 1:
			return findByUser1Mobile(Arrays.asList(TransportationJobStatus.IN_PROGRESS, TransportationJobStatus.STARTED), username);
		case 2:
			return findByUser1Mobile(Arrays.asList(TransportationJobStatus.COMPLETED), username);
		case 3:
			return findByUser1Mobile(username);

		default:
			return new ArrayList<>();
		}
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByDriverMobile(List<TransportationJobStatus> status, String username) {
		List<TransportationJob> list = repos.findByDriverMobile(username, status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByDriverMobile(String username) {
		List<TransportationJob> list = repos.findByDriverMobile(username);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByDriverMobileByStatus(Integer state, String username) {
		switch (state) {
		case 0:
			return findByDriverMobile(Arrays.asList(TransportationJobStatus.ASSIGNED2, TransportationJobStatus.ACCEPTED), username);
		case 1:
			return findByDriverMobile(Arrays.asList(TransportationJobStatus.IN_PROGRESS, TransportationJobStatus.STARTED), username);
		case 2:
			return findByDriverMobile(Arrays.asList(TransportationJobStatus.COMPLETED), username);

		default:
			return new ArrayList<>();
		}
	}

	public List<ma.azdad.mobile.model.Stop> findTjStopsMobile(Integer id) {
		TransportationJob tj = findOne(id);
		List<Stop> stops = tj.getStopList();
		List<ma.azdad.mobile.model.Stop> stopsMobile = new ArrayList<>();

		for (Stop stop : stops) {
			stopsMobile.add(new ma.azdad.mobile.model.Stop(stop.getId(), stop.getDate(), stop.getPlaceName(), stop.getType().getValue()));
		}
		return stopsMobile;
	}

	public ma.azdad.mobile.model.TransportationJob findOneMobile(Integer id) {
		TransportationJob tj = findOne(id);
		ma.azdad.mobile.model.TransportationJob tjMobile = new ma.azdad.mobile.model.TransportationJob(id, tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
				tj.getVehiclePrice(), tj.getVehicleMatricule(), toMobileUser2(tj.getDriver()));
		List<ma.azdad.mobile.model.TransportationRequest> trList = new ArrayList<>();
		if (tj.getEstimatedDistance() != null) {
			tjMobile.setEstimatedDistanceText(tj.getEstimatedDistance().toString() + " Km");
		}

		if (Arrays.asList(TransportationJobStatus.COMPLETED, TransportationJobStatus.ACKNOWLEDGED).contains(tj.getStatus())) {

			List<TransportationJobItinerary> list = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(id);

			String realDist = "0 Km";
			if (list != null && !list.isEmpty()) {
				TransportationJobItinerary lastPoint = list.get(list.size() - 1);
				realDist = String.valueOf(lastPoint.getCumulativeDistance()) + " Km";
				tjMobile.setRealDistanceText(realDist);
			}
		}
		double totalGrossWeight = 0.0;
		double totalVolume = 0.0;
		int totalItems = 0;
		for (TransportationRequest transportationRequest : tj.getTransportationRequestList()) {
			if (transportationRequest.getGrossWeight() != null) {
				totalGrossWeight += transportationRequest.getGrossWeight();
			}
			if (transportationRequest.getVolume() != null) {
				totalVolume += transportationRequest.getVolume();
			}
			if (transportationRequest.getNumberOfItems() != null) {
				totalItems += transportationRequest.getNumberOfItems();
			}
			trList.add(new ma.azdad.mobile.model.TransportationRequest(transportationRequest.getId(), transportationRequest.getReference(), transportationRequest.getStatus(),
					transportationRequest.getNeededPickupDate(), transportationRequest.getNeededDeliveryDate(), transportationRequest.getExpectedPickupDate(), transportationRequest.getPickupDate(),
					transportationRequest.getExpectedDeliveryDate(), transportationRequest.getDeliveryDate(), transportationRequest.getOriginName(), transportationRequest.getDestinationName()));
		}
		tjMobile.setGrossWeight(totalGrossWeight);
		tjMobile.setVolume(totalVolume);
		tjMobile.setNumberOfItems(totalItems);
		if (tj.getVehicle() != null) {
			tjMobile.setVehicule(new Vehicule(tj.getVehicleId(), tj.getVehicleTypeName(), tj.getVehicleMatricule()));
		}

		if (tj.getUser1() != null) {
			tjMobile.setUser1(toMobileUser(tj.getUser1()));
			tjMobile.setDate1(tj.getDate1());
		}
		if (tj.getUser2() != null) {
			tjMobile.setUser2(toMobileUser(tj.getUser2()));
			tjMobile.setDate2(tj.getDate2());
		}
		if (tj.getUser3() != null) {
			tjMobile.setUser3(toMobileUser(tj.getUser3()));
			tjMobile.setDate3(tj.getDate3());
		}
		if (tj.getUser4() != null) {
			tjMobile.setUser4(toMobileUser(tj.getUser4()));
			tjMobile.setDate4(tj.getDate4());
		}
		if (tj.getUser5() != null) {
			tjMobile.setUser5(toMobileUser(tj.getUser5()));
			tjMobile.setDate5(tj.getDate5());
		}
		if (tj.getUser6() != null) {
			tjMobile.setUser6(toMobileUser(tj.getUser6()));
			tjMobile.setDate6(tj.getDate6());
		}
		if (tj.getUser7() != null) {
			tjMobile.setUser7(toMobileUser(tj.getUser7()));
			tjMobile.setDate7(tj.getDate7());
		}

		tjMobile.getTransportationRequestList().addAll(trList);
		tjMobile.setHistoryList(repos.findHistoryListMobile(id));

		return tjMobile;
	}

	private ma.azdad.mobile.model.User toMobileUser(User user) {
		return new ma.azdad.mobile.model.User(user.getUsername(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPhoto(), user.getEmail(), user.getJob());
	}

	private ma.azdad.mobile.model.User toMobileUser2(User user) {
		return new ma.azdad.mobile.model.User(user.getUsername(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPhoto(), user.getEmail(), user.getCin(), user.getPhone());
	}

	public void handleFileUpload(FileUploadEvent event, User user, Integer id, String fileType) throws IOException {
		TransportationJob job = findOne(id);

		File file = fileUploadService.handleFileUploadMobile(event, "transportationJob");
		TransportationJobFile modelFile = new TransportationJobFile(file, fileType, event.getFile().getFileName(), user);

		modelFile.setParent(job);
		transportationJobFileRepos.save(modelFile);
		job = saveAndRefresh(job);

	}

	public void deleteFile(Integer idDn, Integer idFile) {
		TransportationJob job = findOne(idDn);
		TransportationJobFile file = transportationJobFileRepos.findById(idFile).get();

		try {
			transportationJobFileRepos.delete(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<TransportationJobFile> i = job.getFileList().iterator();
		while (i.hasNext()) {
			TransportationJobFile current = i.next();
			if (current.getId().equals(file.getId())) {
				job.getFileList().remove(current);
				break;
			}
		}
		job = saveAndRefresh(job);
	}

	public List<ma.azdad.mobile.model.TransportationJobFile> findTjAttachments(Integer id) {
		TransportationJob job = findOne(id);
		List<ma.azdad.mobile.model.TransportationJobFile> list = new ArrayList<>();
		for (TransportationJobFile dnFile : job.getFileList()) {
			list.add(new ma.azdad.mobile.model.TransportationJobFile(dnFile.getId(), dnFile.getDate(), dnFile.getLink(), dnFile.getExtension(), dnFile.getType(), dnFile.getSize(), dnFile.getName()));

		}
		return list;
	}

	public String getContentTypeFromUrl(String url) {
		// You can enhance this with more comprehensive detection
		url = url.toLowerCase();
		if (url.contains(".png"))
			return "image/png";
		if (url.contains(".gif"))
			return "image/gif";
		if (url.contains(".webp"))
			return "image/webp";
		return "image/jpeg"; // default
	}

	public void updateDriverLocation(Double lat, Double lng, ma.azdad.mobile.model.User us) {
		List<DriverLocation> locations = driverLocationRepo.findByUser(us.getUsername());
		User user = userService.findByUsername(us.getUsername());
		// il reste check de profile driver !!!!!!!!!!!!!!!!!!!!
		if (user.getIsDriver())
			if (locations == null || locations.isEmpty()) {
				DriverLocation userLocation = new DriverLocation(new Date(), lat, lng, user);
				userLocation = driverLocationRepo.save(userLocation);
				// hadi hiya li at3merlina les info de user si nexiste pas f base donnés
				googleGeocodeService.updateGoogleGeocodeDataAsync(userLocation);

			} else {
				DriverLocation lastLocation = locations.get(locations.size() - 1);
				double distanceKm = haversineDistance(lastLocation.getLatitude(), lastLocation.getLongitude(), lat, lng);
				if (distanceKm >= 5.0) {
					lastLocation.setLatitude(lat);
					lastLocation.setLongitude(lng);
					// hadi adir m�j les cordonnées si user exist déja et distance > 5km
					googleGeocodeService.updateGoogleGeocodeDataAsync(lastLocation);

				} else {
					System.out.println("Distance < 5km � skipping update");
				}
			}
	}

	public void updateJobItinerary(Double lat, Double lng, ma.azdad.mobile.model.User us) {

		User user = userService.findByUsername(us.getUsername());
		if (user.getIsDriver()) {
			List<TransportationJob> jobs = transportationJobRepos.find(Arrays.asList(TransportationJobStatus.STARTED, TransportationJobStatus.IN_PROGRESS), us.getUsername());
			for (TransportationJob transportationJob : jobs) {
				List<TransportationJobItinerary> locations = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(transportationJob.getId());
				if (!locations.isEmpty()) {
					TransportationJobItinerary lastLocation = locations.get(locations.size() - 1);
					double distanceKm = haversineDistance(lastLocation.getLatitude(), lastLocation.getLongitude(), lat, lng);
					if (distanceKm >= 5.0) {
						List<TransportationRequest> list = transportationRequestRepos.findLightByJob(transportationJob.getId());
						TransportationJobItinerary location = new TransportationJobItinerary(new Date(), lat, lng, transportationJob, transportationJob.getStatus());
						if (!list.isEmpty()) {
							for (TransportationRequest req : list) {
								if (Arrays.asList(TransportationRequestStatus.PICKEDUP).contains(req.getStatus())) {
									location.setTransportationRequest(req);
									location.setTransportationRequestStatus(req.getStatus());
								}
							}
						}
						Double distance = PathService.getDistance(lastLocation.getLatitude(), lastLocation.getLongitude(), lat, lng);
						location.setCumulativeDistance(distance + location.getCumulativeDistance());
						location.setDistanceFromPrevious(distance);
						transportationJobItineraryRepos.save(location);

					} else {
						System.out.println("Distance < 5km � skipping update");
					}
				} else {
					List<TransportationRequest> list = transportationRequestRepos.findLightByJob(transportationJob.getId());
					TransportationJobItinerary location = new TransportationJobItinerary(new Date(), lat, lng, transportationJob, transportationJob.getStatus());
					if (!list.isEmpty()) {
						for (TransportationRequest req : list) {
							if (Arrays.asList(TransportationRequestStatus.PICKEDUP).contains(req.getStatus())) {
								location.setTransportationRequest(req);
								location.setTransportationRequestStatus(req.getStatus());
							}
						}
					}
					transportationJobItineraryRepos.save(location);
				}
			}

		}

	}

	public Double calculateRealProductiveDist(Integer jobId) {
		List<TransportationJobItinerary> orderedItineraries = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(jobId);

		double realProductive = 0d;
		boolean carryingGoods = false;

		for (TransportationJobItinerary point : orderedItineraries) {
			double dist = point.getDistanceFromPrevious();

			// Check if state changes
			if (point.getTransportationRequestStatus() == TransportationRequestStatus.PICKEDUP) {
				carryingGoods = true;
			}
			if (point.getTransportationRequestStatus() == TransportationRequestStatus.DELIVERED) {
				carryingGoods = false;
			}

			// Add distance only when carrying goods
			if (carryingGoods) {
				realProductive += dist;
			}
		}
		return realProductive;
	}

	public Double calculateRealNonProductiveDist(Integer jobId) {
		List<TransportationJobItinerary> orderedItineraries = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(jobId);

		double realNonProductive = 0d;
		boolean carryingGoods = false;

		for (TransportationJobItinerary point : orderedItineraries) {
			double dist = point.getDistanceFromPrevious();

			// Check if state changes
			if (point.getTransportationRequestStatus() == TransportationRequestStatus.PICKEDUP) {
				carryingGoods = true;
			}
			if (point.getTransportationRequestStatus() == TransportationRequestStatus.DELIVERED) {
				carryingGoods = false;
			}

			// Add distance only when NOT carrying goods
			if (!carryingGoods) {
				realNonProductive += dist;
			}
		}
		return realNonProductive;
	}

	public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
		final int EARTH_RADIUS_KM = 6371;

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS_KM * c;
	}

}
