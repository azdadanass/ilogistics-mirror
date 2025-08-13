package ma.azdad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Path;
import ma.azdad.model.Role;
import ma.azdad.model.Stop;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobAssignmentType;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.TransportationJobState;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestHistory;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
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
	DeliveryRequestService deliveryRequestService;

	@Autowired
	TransportationRequestService transportationRequestService;

	@Autowired
	PathService pathService;

	@Autowired
	StopService stopService;

	@Autowired
	TransporterService transporterService;

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
			for (TransportationRequest tr : transportationJob.getTransportationRequestList())
				Hibernate.initialize(tr.getHistoryList());
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
	public List<TransportationJob> findToAccept(String username){
		return repos.findToAccept(username);
	}
	
	@Cacheable(value = "transportationJobService.countToAccept")
	public Long countToAccept(String username){
		return repos.countToAccept(username);
	}
	
	@Cacheable(value = "transportationJobService.findToStart")
	public List<TransportationJob> findToStart(String username){
		return repos.findToStart(username);
	}
	
	@Cacheable(value = "transportationJobService.countToStart")
	public Long countToStart(String username){
		return repos.countToStart(username);
	}
	
	@Cacheable(value = "transportationJobService.findToComplete")
	public List<TransportationJob> findToComplete(String username){
		return repos.findToComplete(username);
	}
	
	@Cacheable(value = "transportationJobService.countToComplete")
	public Long countToComplete(String username){
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
			transportationJob.getTransportationRequestList().forEach(
					i -> i.addHistory(new TransportationRequestHistory("Assigned", connectedUser, "Assigned to transporter <b class='blue'>" + transportationJob.getTransporterName() + "</b>")));
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
			transportationJob.getTransportationRequestList()
					.forEach(i -> i.addHistory(new TransportationRequestHistory("Assigned", connectedUser, "Assigned to driver <b class='green'>" + transportationJob.getDriverFullName() + "</b>")));
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

	public List<ma.azdad.mobile.model.TransportationJob> findMobile(List<TransportationJobStatus> status) {
		List<TransportationJob> list = repos.findMobile(status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findMobile() {
		List<TransportationJob> list = repos.findMobile();
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getRealCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findMobileByStatus(Integer state) {
		switch (state) {
		case 0:
			return findMobile(TransportationJobStatus.EDITED);
		case 1:
			return findMobile(TransportationJobStatus.IN_PROGRESS);
		case 2:
			return findMobile(Arrays.asList(TransportationJobStatus.COMPLETED, TransportationJobStatus.ACKNOWLEDGED));
		case 3:
			return findMobile();

		default:
			return new ArrayList<>();
		}
	}

	private ma.azdad.mobile.model.User toMobileUser(User user) {
		return new ma.azdad.mobile.model.User(user.getUsername(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPhoto(), user.getEmail());
	}

}
