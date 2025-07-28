package ma.azdad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.JobRequest;
import ma.azdad.model.Path;
import ma.azdad.model.Stop;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
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
			initialize(transportationJob.getTransporter().getUserList());
		}
			
		Hibernate.initialize(transportationJob.getVehicle());
		Hibernate.initialize(transportationJob.getDriver());
		return transportationJob;
	}

	public List<TransportationJob> find() {
		return repos.find();
	}
	
	public List<TransportationJob> findToAssign1(String user1Username){
		return repos.findToAssign1(user1Username);
	}

	public List<TransportationJob> findToAssign2(String user1Username){
		return repos.findToAssign2(user1Username);
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
			return findMobile(TransportationJobStatus.NOT_STARTED);
		case 1:
			return findMobile(TransportationJobStatus.IN_PROGRESS);
		case 2:
			return findMobile(Arrays.asList(TransportationJobStatus.COMPLETED, TransportationJobStatus.CLOSED));
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
