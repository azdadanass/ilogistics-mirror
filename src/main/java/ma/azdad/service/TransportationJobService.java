package ma.azdad.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ma.azdad.mobile.model.Vehicule;
import ma.azdad.model.DriverLocation;
import ma.azdad.model.Path;
import ma.azdad.model.Role;
import ma.azdad.model.Stop;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobAssignmentType;
import ma.azdad.model.TransportationJobCapacity;
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
import ma.azdad.repos.PathRepos;
import ma.azdad.repos.StopRepos;
import ma.azdad.repos.TransportationJobCapacityRepos;
import ma.azdad.repos.TransportationJobFileRepos;
import ma.azdad.repos.TransportationJobItineraryRepos;
import ma.azdad.repos.TransportationJobRepos;
import ma.azdad.repos.TransportationRequestRepos;
import ma.azdad.repos.UserRepos;
import ma.azdad.utils.App;
import ma.azdad.utils.FacesContextMessages;
import ma.azdad.utils.LatLng;
import ma.azdad.utils.Mail;
import ma.azdad.utils.TemplateType;

@Component
public class TransportationJobService extends GenericService<Integer, TransportationJob, TransportationJobRepos> {

	@Value("#{'${spring.profiles.active}'.replaceAll('-dev','')}")
	private String erp;

	private final TransportationRequestRepos transportationRequestRepos;

	@Autowired
	UserService userService;

	@Autowired
	UserRepos userRepos;

	@Autowired
	CapacityService capacityService;

	@Autowired
	DriverLocationRepo driverLocationRepo;

	@Autowired
	TransportationJobCapacityRepos transportationJobCapacityRepos;

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
	StopRepos stopRepos;

	@Autowired
	TransporterService transporterService;

	@Autowired
	TransportationJobFileRepos transportationJobFileRepos;

	@Autowired
	VehicleService vehicleService;

	@Autowired
	EmailService emailService;

	TransportationJobService(UserService userService, TransportationRequestRepos transportationRequestRepos) {
		this.userService = userService;
		this.transportationRequestRepos = transportationRequestRepos;
	}

	@Override
	public TransportationJob findOne(Integer id) {
		TransportationJob transportationJob = super.findOne(id);
		Hibernate.initialize(transportationJob.getFileList());
		Hibernate.initialize(transportationJob.getHistoryList());
		Hibernate.initialize(transportationJob.getCommentList());
		Hibernate.initialize(transportationJob.getTransportationRequestList());
		Hibernate.initialize(transportationJob.getToNotifyList());
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
		if (transportationJob.getVehicle() != null)
			if (transportationJob.getVehicle().getBrandType() != null)
				initialize(transportationJob.getVehicle().getBrandType().getBrand());

		Hibernate.initialize(transportationJob.getVehicle());
		Hibernate.initialize(transportationJob.getDriver());
		return transportationJob;
	}

	public TransportationJob initCalculableFields(TransportationJob transportationJob) {
		List<TransportationJobCapacity> simulatedList = capacityService.simulatePlannedCapacities(transportationJob.getId(), transportationJob.getTransportationRequestList());
		List<TransportationJobItinerary> list = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(transportationJob.getId());
		Double maxCumulativeWeight = transportationJobCapacityRepos.findMaxCumulativeWeightByTransportationJobIdAndType(transportationJob.getId(), "Real");
		Double maxCumulativeVolume = transportationJobCapacityRepos.findMaxCumulativeVolumeByTransportationJobIdAndType(transportationJob.getId(), "Real");
		if (!list.isEmpty()) {
			TransportationJobItinerary firstPoint = list.get(0);
			TransportationJobItinerary lastPoint = list.get(list.size() - 1);
			transportationJob.setTotalDistanceTravelled(lastPoint.getCumulativeDistance());
			transportationJob.setTotalItineraryDuration(UtilsFunctions.getDateDifferenceDaysHoursMinutes(firstPoint.getTimestamp(), lastPoint.getTimestamp()));
		} else {
			transportationJob.setTotalDistanceTravelled(0d);
			transportationJob.setTotalItineraryDuration(null);
		}

		transportationJob.setPlannedEffectiveDistance(calculateEstimatedProductiveDist(transportationJob.getId()));
		transportationJob.setPlannedNonEffectiveDistance(calculateEstimatedNonProductiveDist(transportationJob.getId()));
		transportationJob.setPlannedMaxVolume(simulatedList.stream().mapToDouble(TransportationJobCapacity::getCumulativeVolume).max().orElse(0d));
		transportationJob.setPlannedMaxWeight(simulatedList.stream().mapToDouble(TransportationJobCapacity::getCumulativeWeight).max().orElse(0d));
		transportationJob.setStartingDistance(realStartDistance(transportationJob.getId()));

		transportationJob.setEffectiveTraveledDistance(calculateRealProductiveDist(transportationJob.getId()));
		transportationJob.setNonEffectiveTraveledDistance(calculateRealNonProductiveDist(transportationJob.getId()));
		transportationJob.setItineratyMaxVolume(maxCumulativeVolume != null ? maxCumulativeVolume : 0d);
		transportationJob.setItineratyMaxWeight(maxCumulativeWeight != null ? maxCumulativeWeight : 0d);

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
				tr.setCost(transportationJob.getCost() * (test ? estimatedDistance * grossWeight / total1 : estimatedDistance / total2));
			transportationRequestService.save(tr);
		}
	}

	public void updateCalculableFields(TransportationJob transportationJob, Boolean setCost) {
		try {
			System.out.println("i'm in the calculate = " + transportationJob.getStopList().size() );

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
			System.out.println("Stops in DB = " + transportationJob.getStopList().size() );
			TransportationJob tj =save(transportationJob);
			System.out.println("Stops in DB = " + tj.getStopList().size() );
			               
		}  catch (Exception e) {
		    e.printStackTrace(); 
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
			transportationJob.setVehiclePrice(transportationJob.getVehicle().getVehicleType().getPrice());
			transportationJob.setTransporter(transporterService.findOneLight(transportationJob.getDriver().getTransporterId()));
			transportationJob.calculateEstimatedStartCost();
			transportationJob.calculateEstimatedItineraryCost();
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

		sendNotification(transportationJob);
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
		sendNotification(transportationJob);
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
		sendNotification(transportationJob);
	}

	public void startMobile(Integer id, String username, Double lat, Double lng) {
		User user = userService.findByUsername(username);
		TransportationJob transportationJob = findOne(id);
		transportationJob.setStartLongitude(lng);
		transportationJob.setStartLatitude(lat);
		start(transportationJob, user);
		TransportationJobItinerary tItinerary = new TransportationJobItinerary(new Date(), lat, lng, transportationJob, TransportationJobStatus.STARTED);
		transportationJobItineraryRepos.save(tItinerary);
	}

	public void calculateAndSaveTrStartDistance(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return;
		}

		requests.sort(Comparator.comparing(tr -> tr.getPickupDate() != null ? tr.getPickupDate() : tr.getExpectedPickupDate(), Comparator.nullsLast(Comparator.naturalOrder())));
		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(requestId)) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;

		// --- Current pickup coordinates ---
		double toLat = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLatitude() : request.getDeliveryRequest().getWarehouse().getLatitude();
		double toLng = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLongitude() : request.getDeliveryRequest().getWarehouse().getLongitude();

		double startDistance;

		if (index == 0) {
			// First TR — from job start
			double fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
			double fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();

			startDistance = PathService.getDistance(fromLat, fromLng, toLat, toLng);

			// Save into job
			job.setStartDistance(startDistance);
			save(job);
		} else {
			// Not the first one — check previous request
			TransportationRequest prev = requests.get(index - 1);

			// Default: from prev delivery point
			double fromLat = prev.getDeliveryRequest().getDestination() != null ? prev.getDeliveryRequest().getDestination().getLatitude() : prev.getDeliveryRequest().getWarehouse().getLatitude();
			double fromLng = prev.getDeliveryRequest().getDestination() != null ? prev.getDeliveryRequest().getDestination().getLongitude() : prev.getDeliveryRequest().getWarehouse().getLongitude();

			// Handle cases
			if (prev.getDeliveryDate() != null && request.getPickupDate() != null && prev.getDeliveryDate().after(request.getPickupDate())) {
				// Previous delivery happens later — fall back to job start
				fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
				fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
			} else if (Double.compare(fromLat, toLat) == 0 && Double.compare(fromLng, toLng) == 0) {
				// Same place — no travel needed
				startDistance = 0d;
				request.setStartDistance(startDistance);
				transportationRequestService.save(request);
				return;
			}

			startDistance = PathService.getDistance(fromLat, fromLng, toLat, toLng);
		}

		// Save into request
		request.setStartDistance(startDistance);
		transportationRequestService.save(request);
	}

	public LatLng getTrStartPosition(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return null;
		}

		// Sort requests by pickup date
		requests.sort(Comparator.comparing(tr -> tr.getPickupDate() != null ? tr.getPickupDate() : tr.getExpectedPickupDate(), Comparator.nullsLast(Comparator.naturalOrder())));
		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(request.getId())) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			return null; // request not found in list
		}

		Double fromLat;
		Double fromLng;

		if (index == 0) {
			// ✅ First TR → from job start or fallback
			fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
			fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
		} else {
			TransportationRequest prev = requests.get(index - 1);

			// --- Default from previous TR delivery
			Double prevLat = null, prevLng = null;
			if (prev.getDeliveryRequest() != null) {
				if (prev.getDeliveryRequest().getDestination() != null) {
					prevLat = prev.getDeliveryRequest().getDestination().getLatitude();
					prevLng = prev.getDeliveryRequest().getDestination().getLongitude();
				} else if (prev.getDeliveryRequest().getWarehouse() != null) {
					prevLat = prev.getDeliveryRequest().getWarehouse().getLatitude();
					prevLng = prev.getDeliveryRequest().getWarehouse().getLongitude();
				}
			}

			// --- Current pickup point
			Double pickupLat = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLatitude() : request.getDeliveryRequest().getWarehouse().getLatitude();
			Double pickupLng = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLongitude() : request.getDeliveryRequest().getWarehouse().getLongitude();

			if (prev.getDeliveryDate() != null && request.getPickupDate() != null && prev.getDeliveryDate().after(request.getPickupDate())) {
				fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
				fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
			} else if (prevLat != null && prevLng != null && Double.compare(prevLat, pickupLat) == 0 && Double.compare(prevLng, pickupLng) == 0) {
				fromLat = pickupLat;
				fromLng = pickupLng;
			} else {
				fromLat = prevLat;
				fromLng = prevLng;
			}
		}

		return (fromLat != null && fromLng != null) ? new LatLng(fromLat, fromLng) : null;
	}

	public LatLng getPlannedTrStartPosition(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return null;
		}

	    // Sort requests by expected pickup date
	    requests.sort(Comparator.comparing(
	            tr -> tr.getExpectedPickupDate() != null ? tr.getExpectedPickupDate() : tr.getNeededPickupDate(),
	            Comparator.nullsLast(Comparator.naturalOrder())
	    ));

		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(request.getId())) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			return null; // request not found in list
		}

		Double fromLat;
		Double fromLng;

		if (index == 0) {
			// ✅ First TR → use planned job start or fallback
			fromLat = job.getPlannedStartLatitude() != null ? job.getPlannedStartLatitude() : job.getFirstLatitude();
			fromLng = job.getPlannedStartLongitude() != null ? job.getPlannedStartLongitude() : job.getFirstLongitude();
		} else {
			TransportationRequest prev = requests.get(index - 1);

			// --- Default from previous TR planned delivery
			Double prevLat = null, prevLng = null;
			if (prev.getDeliveryRequest() != null) {
				if (prev.getDeliveryRequest().getDestination() != null) {
					prevLat = prev.getDeliveryRequest().getDestination().getLatitude();
					prevLng = prev.getDeliveryRequest().getDestination().getLongitude();
				} else if (prev.getDeliveryRequest().getWarehouse() != null) {
					prevLat = prev.getDeliveryRequest().getWarehouse().getLatitude();
					prevLng = prev.getDeliveryRequest().getWarehouse().getLongitude();
				}
			}

			// --- Current planned pickup
			Double pickupLat = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLatitude() : request.getDeliveryRequest().getWarehouse().getLatitude();
			Double pickupLng = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLongitude() : request.getDeliveryRequest().getWarehouse().getLongitude();

			if (prev.getExpectedDeliveryDate() != null && request.getExpectedPickupDate() != null && prev.getExpectedDeliveryDate().after(request.getExpectedPickupDate())) {
				fromLat = job.getPlannedStartLatitude() != null ? job.getPlannedStartLatitude() : job.getFirstLatitude();
				fromLng = job.getPlannedStartLongitude() != null ? job.getPlannedStartLongitude() : job.getFirstLongitude();
			} else if (prevLat != null && prevLng != null && Double.compare(prevLat, pickupLat) == 0 && Double.compare(prevLng, pickupLng) == 0) {
				fromLat = pickupLat;
				fromLng = pickupLng;
			} else {
				fromLat = prevLat;
				fromLng = prevLng;
			}
		}

		return (fromLat != null && fromLng != null) ? new LatLng(fromLat, fromLng) : null;
	}

	public Double calculateTrStartDistance(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return null;
		}

		// Sort by pickup date
		requests.sort(Comparator.comparing(tr -> tr.getPickupDate() != null ? tr.getPickupDate() : tr.getExpectedPickupDate(), Comparator.nullsLast(Comparator.naturalOrder())));
		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(requestId)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null; // request not found
		}

		// --- Target pickup point
		double toLat = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLatitude() : request.getDeliveryRequest().getWarehouse().getLatitude();

		double toLng = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLongitude() : request.getDeliveryRequest().getWarehouse().getLongitude();

		double startDistance;

		if (index == 0) {
			// ✅ First TR → from job start
			double fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
			double fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
			startDistance = PathService.getDistance(fromLat, fromLng, toLat, toLng);
		} else {
			TransportationRequest prev = requests.get(index - 1);

			// --- Previous delivery point
			Double prevLat = prev.getDeliveryRequest().getDestination() != null ? prev.getDeliveryRequest().getDestination().getLatitude() : prev.getDeliveryRequest().getWarehouse().getLatitude();
			Double prevLng = prev.getDeliveryRequest().getDestination() != null ? prev.getDeliveryRequest().getDestination().getLongitude() : prev.getDeliveryRequest().getWarehouse().getLongitude();

			if (prev.getDeliveryDate() != null && request.getPickupDate() != null && prev.getDeliveryDate().after(request.getPickupDate())) {
				double fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
				double fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
				startDistance = PathService.getDistance(fromLat, fromLng, toLat, toLng);
			} else if (Double.compare(prevLat, toLat) == 0 && Double.compare(prevLng, toLng) == 0) {
				startDistance = 0d;
			} else {
				startDistance = PathService.getDistance(prevLat, prevLng, toLat, toLng);
			}
		}

		return startDistance;
	}

	public Double calculatePlannedTrStartDistance(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return null;
		}

	    // Sort by expected pickup date
	    requests.sort(Comparator.comparing(
	            tr -> tr.getExpectedPickupDate() != null ? tr.getExpectedPickupDate() : tr.getNeededPickupDate(),
	            Comparator.nullsLast(Comparator.naturalOrder())
	    ));

		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(requestId)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null; // request not found
		}

		// --- Target planned pickup point
		double toLat = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLatitude() : request.getDeliveryRequest().getWarehouse().getLatitude();

		double toLng = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLongitude() : request.getDeliveryRequest().getWarehouse().getLongitude();

		double startDistance;

		if (index == 0) {
			// ✅ First TR → from planned job start
			double fromLat = job.getPlannedStartLatitude() != null ? job.getPlannedStartLatitude() : job.getFirstLatitude();
			double fromLng = job.getPlannedStartLongitude() != null ? job.getPlannedStartLongitude() : job.getFirstLongitude();
			startDistance = PathService.getDistance(fromLat, fromLng, toLat, toLng);
		} else {
			TransportationRequest prev = requests.get(index - 1);

			// --- Previous planned delivery point
			Double prevLat = prev.getDeliveryRequest().getDestination() != null ? prev.getDeliveryRequest().getDestination().getLatitude() : prev.getDeliveryRequest().getWarehouse().getLatitude();
			Double prevLng = prev.getDeliveryRequest().getDestination() != null ? prev.getDeliveryRequest().getDestination().getLongitude() : prev.getDeliveryRequest().getWarehouse().getLongitude();

			if (prev.getExpectedDeliveryDate() != null && request.getExpectedPickupDate() != null && prev.getExpectedDeliveryDate().after(request.getExpectedPickupDate())) {
				// planned job start
				double fromLat = job.getPlannedStartLatitude() != null ? job.getPlannedStartLatitude() : job.getFirstLatitude();
				double fromLng = job.getPlannedStartLongitude() != null ? job.getPlannedStartLongitude() : job.getFirstLongitude();
				startDistance = PathService.getDistance(fromLat, fromLng, toLat, toLng);
			} else if (Double.compare(prevLat, toLat) == 0 && Double.compare(prevLng, toLng) == 0) {
				startDistance = 0d;
			} else {
				startDistance = PathService.getDistance(prevLat, prevLng, toLat, toLng);
			}
		}

		return startDistance;
	}

	public String calculatePlannedTrStartDuration(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return null;
		}

	    // Sort by expected pickup date
	    requests.sort(Comparator.comparing(
	            tr -> tr.getExpectedPickupDate() != null ? tr.getExpectedPickupDate() : tr.getNeededPickupDate(),
	            Comparator.nullsLast(Comparator.naturalOrder())
	    ));

		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(requestId)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null; // request not found
		}

		Date trPickup = request.getExpectedPickupDate();
		String startDuration;

		if (index == 0) {
			// First TR → from planned job start
			Date jobStart = job.getPlannedStartDate();
			startDuration = UtilsFunctions.getDateDifferenceDaysHoursMinutes(jobStart, trPickup);
		} else {
			// Other TRs → normally from previous TR delivery
			TransportationRequest prev = requests.get(index - 1);
			Date prevDelivery = prev.getExpectedDeliveryDate();

			if (prevDelivery != null && !prevDelivery.after(trPickup)) {
				// prev delivered before or exactly at current pickup → OK
				startDuration = UtilsFunctions.getDateDifferenceDaysHoursMinutes(prevDelivery, trPickup);
			} else {
				// prev delivery happens after this pickup → fallback to job start
				Date jobStart = job.getPlannedStartDate();
				startDuration = UtilsFunctions.getDateDifferenceDaysHoursMinutes(jobStart, trPickup);
			}
		}

		return startDuration;
	}

	public String calculateTrStartDuration(Integer jobId, Integer requestId) {
		TransportationJob job = findOne(jobId);
		TransportationRequest request = transportationRequestService.findOne(requestId);

		List<TransportationRequest> requests = job.getTransportationRequestList();
		if (requests == null || requests.isEmpty() || request == null) {
			return null;
		}

		// Sort by actual pickup date
		// Sort requests by pickupDate, falling back to expectedPickupDate if pickupDate
		// is null
		requests.sort(Comparator.comparing(tr -> tr.getPickupDate() != null ? tr.getPickupDate() : tr.getExpectedPickupDate(), Comparator.nullsLast(Comparator.naturalOrder())));

		int index = -1;
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getId().equals(requestId)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null; // request not found
		}

		double toLat = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLatitude() : request.getDeliveryRequest().getWarehouse().getLatitude();

		double toLng = request.getDeliveryRequest().getOrigin() != null ? request.getDeliveryRequest().getOrigin().getLongitude() : request.getDeliveryRequest().getWarehouse().getLongitude();

		String startDuration;

		if (index == 0) {
			// First TR → from job start
			double fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
			double fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
			startDuration = PathService.getDuration(fromLat, fromLng, toLat, toLng);
		} else {
			// Search backwards for the last TR that was delivered before this pickup
			Date thisPickup = request.getPickupDate();
			TransportationRequest validPrev = null;

			for (int i = index - 1; i >= 0; i--) {
				TransportationRequest candidate = requests.get(i);
				Date candidateDelivery = candidate.getDeliveryDate();
				if (candidateDelivery != null && !candidateDelivery.after(thisPickup)) {
					validPrev = candidate;
					break;
				}
			}

			double fromLat, fromLng;

			if (validPrev != null) {
				if (validPrev.getDeliveryRequest().getDestination() != null) {
					fromLat = validPrev.getDeliveryRequest().getDestination().getLatitude();
					fromLng = validPrev.getDeliveryRequest().getDestination().getLongitude();
				} else {
					fromLat = validPrev.getDeliveryRequest().getWarehouse().getLatitude();
					fromLng = validPrev.getDeliveryRequest().getWarehouse().getLongitude();
				}
			} else {
				// no valid previous TR → fallback to job start
				fromLat = job.getStartLatitude() != null ? job.getStartLatitude() : job.getFirstLatitude();
				fromLng = job.getStartLongitude() != null ? job.getStartLongitude() : job.getFirstLongitude();
			}

			startDuration = PathService.getDuration(fromLat, fromLng, toLat, toLng);
		}

		return startDuration;
	}

	public Double calculateTrDistance(Integer id) {
		List<TransportationJobItinerary> list = transportationJobItineraryRepos.findByTransportationRequestIdOrderByTimestampAsc(id);

		if (list == null || list.size() < 2) {
			return 0d; // no distance if fewer than 2 points
		}

		double first = list.get(0).getCumulativeDistance();
		double last = list.get(list.size() - 1).getCumulativeDistance();

		return last - first;
	}

	public String calculateTrDuration(Integer id) {
		List<TransportationJobItinerary> list = transportationJobItineraryRepos.findByTransportationRequestIdOrderByTimestampAsc(id);

		if (list == null || list.size() < 2) {
			return "0m"; // no duration if fewer than 2 points
		}

		Date first = list.get(0).getTimestamp();
		Date last = list.get(list.size() - 1).getTimestamp();

		if (first == null || last == null) {
			return null; // safeguard if timestamps are missing
		}

		return UtilsFunctions.getDateDifferenceDaysHoursMinutes(first, last);
	}

	public Long calculateTrDurationMilli(Integer id) {
		List<TransportationJobItinerary> list = transportationJobItineraryRepos.findByTransportationRequestIdOrderByTimestampAsc(id);

		if (list == null || list.size() < 2) {
			return 0L; // no duration if fewer than 2 points
		}

		Date first = list.get(0).getTimestamp();
		Date last = list.get(list.size() - 1).getTimestamp();

		if (first == null || last == null) {
			return null; // safeguard if timestamps are missing
		}

		return UtilsFunctions.getDateDifference(first, last);
	}

	public Double getEstimatedDeliveryDistance(Integer jobId) {
		TransportationJob job = findOne(jobId);

		if (job == null || job.getPathList() == null || job.getPathList().isEmpty()) {
			return 0.0;
		}

		return job.getPathList().stream().map(Path::getEstimatedDistance).filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
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
		transportationJob.setTransporter(null);
		transportationJob.setDriver(null);
		transportationJob.setVehicle(null);
		transportationJob.setVehiclePrice(0.0);
		transportationJob.addHistory(new TransportationJobHistory("Unassign", connectedUser));
		transportationJob.getTransportationRequestList().forEach(i -> {
			i.setExpectedPickupDate(null);
			i.setExpectedDeliveryDate(null);
			i.addHistory(new TransportationRequestHistory("Unassign", connectedUser));
		});
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

	public void generateQrKeyScript() {
		repos.findWithoutQrKey().forEach(i -> {
			i.setQrKey(UtilsFunctions.generateQrKey());
			save(i);
		});
	}

	public String generateStamp(TransportationJob transportationJob) {
		String downloadPath = "temp/stamp_" + transportationJob.getReference() + ".pdf";
		try {
			Document document = new Document(new RectangleReadOnly(284, 171), 5, 5, 5, 5); // 100mm * 60mm
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
			float[] pointColumnWidths = { 158F, 300F };
			PdfPTable table1 = new PdfPTable(pointColumnWidths);
			table1.setTotalWidth(290);
			table1.setLockedWidth(true);
			PdfPCell cell1, cell2;
			Phrase phrase;
			Paragraph paragraph;
			PdfPTable table2 = new PdfPTable(2);

			PdfWriter.getInstance(document, new FileOutputStream(UtilsFunctions.path() + downloadPath));

			document.open();

			paragraph = new Paragraph(transportationJob.getReference() + " |   " + transportationJob.getStatus().getValue(), titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10f);
			document.add(paragraph);

			// qrcode Cell
			BarcodeQRCode barcodeQrcode = new BarcodeQRCode(App.QR.getLink() + "/tj/" + transportationJob.getId() + "/" + transportationJob.getQrKey(), 100, 100, null);
			Image qrcodeImage = barcodeQrcode.getImage();
			qrcodeImage.scaleToFit(95, 95);
			// qrcodeImage.scalePercent(100);
			Image logo = null;
			if ("gcom".equals(erp))
				logo = Image.getInstance(UtilsFunctions.path() + "resources/pdf/gcom.png");
			else if ("orange".equals(erp))
				logo = Image.getInstance(UtilsFunctions.path() + "resources/pdf/orange.png");
			logo.scaleToFit(50, 60);
			logo.setAlignment(Element.ALIGN_CENTER);
			cell1 = new PdfPCell();
			cell1.setBorder(Rectangle.NO_BORDER);
			cell1.addElement(qrcodeImage);
			cell1.addElement(logo);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell1);

			phrase = new Phrase();
			phrase.add(new Chunk("# Of Items : ", boldFont));
			phrase.add(new Chunk(String.valueOf(transportationJob.getNumberOfItems()), normalFont));
			phrase.add(new Chunk("\nTransporter : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(ObjectUtils.firstNonNull(transportationJob.getTransporterName(), ""), 70), normalFont));
			phrase.add(new Chunk("\nDriver : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(ObjectUtils.firstNonNull(transportationJob.getDriverFullName(), ""), 25), normalFont));
			phrase.add(new Chunk("\nVehicle : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(ObjectUtils.firstNonNull(transportationJob.getVehicleMatricule(), ""), 25), normalFont));
			cell1 = new PdfPCell();
			cell1.setPadding(3f);
			cell1.setLeading(0, 1f);
			cell1.addElement(phrase);

			phrase = new Phrase();
			phrase.add(new Chunk("Gross Weight\n", boldFont));
			phrase.add(new Chunk(UtilsFunctions.formatDouble(transportationJob.getGrossWeight()) + " Kg", normalFont));
			cell2 = new PdfPCell();
			cell2.setBorder(0);
			cell2.addElement(phrase);
			table2.addCell(cell2);
			phrase = new Phrase();
			phrase.add(new Chunk("Volume\n", boldFont));
			phrase.add(new Chunk(UtilsFunctions.formatDouble(transportationJob.getVolume()) + " m3", normalFont));
			cell2 = new PdfPCell();
			cell2.setBorder(0);
			cell2.addElement(phrase);
			table2.addCell(cell2);
			cell1.addElement(table2);
			cell1.setBorder(Rectangle.LEFT);
			table1.addCell(cell1);
			document.add(table1);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadPath;
	}

	// notification
	public void sendNotification(TransportationJob transportationJob) {
		User toUser = transportationJob.getUser1();
		String subject = transportationJob.getReference() + " has been " + transportationJob.getStatus().getValue();
		Mail mail = new Mail(toUser.getEmail(), subject, "transportationJob.html", TemplateType.HTML);
		mail.addParameter("transportationJob", transportationJob);
		mail.addParameter("toUser", toUser);
		emailService.generateAndSend(mail);
	}

	// mobile
	public List<ma.azdad.mobile.model.TransportationJob> findMobile(TransportationJobStatus status) {
		List<TransportationJob> list = repos.findMobile(status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			ma.azdad.mobile.model.User user = null;
			if (tj.getDriverUsername() != null) {
				user = toMobileUser(userService.findByUsernameLight(tj.getDriverUsername()));
			}
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getCost(), tj.getEstimatedCost(), user,
					transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByInternalTmMobile(List<TransportationJobStatus> status) {
		List<TransportationJob> list = repos.findByInternalTmMobile(status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			ma.azdad.mobile.model.User user = null;
			if (tj.getDriverUsername() != null) {
				user = toMobileUser(userService.findByUsernameLight(tj.getDriverUsername()));
			}
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getCost(), tj.getEstimatedCost(), user,
					transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findMobile() {
		List<TransportationJob> list = repos.findMobile();
		System.out.println("tj size" + list.size());
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			ma.azdad.mobile.model.User user = null;
			if (tj.getDriverUsername() != null) {
				user = toMobileUser(userService.findByUsernameLight(tj.getDriverUsername()));
			}
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getCost(), tj.getEstimatedCost(), user,
					transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByInternalTmByStatus(Integer state) {
		switch (state) {
		case 0:
			return findByInternalTmMobile(Arrays.asList(TransportationJobStatus.EDITED, TransportationJobStatus.ASSIGNED1, TransportationJobStatus.ASSIGNED2, TransportationJobStatus.ACCEPTED));
		case 1:
			return findByInternalTmMobile(Arrays.asList(TransportationJobStatus.IN_PROGRESS, TransportationJobStatus.STARTED));
		case 2:
			return findByInternalTmMobile(Arrays.asList(TransportationJobStatus.COMPLETED));
		case 3:
			return findMobile();

		default:
			return new ArrayList<>();
		}
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByDriverMobile(List<TransportationJobStatus> status, String username) {
		List<TransportationJob> list = repos.findByDriverMobile(username, status);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {
			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getCost(), tj.getEstimatedCost(),
					toMobileUser(userService.findByUsernameLight(tj.getDriverUsername())), transportationRequestRepos.countByTransportationJob(tj), tj.getVehicleMatricule()));
		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationJob> findByDriverMobile(String username) {
		List<TransportationJob> list = repos.findByDriverMobile(username);
		List<ma.azdad.mobile.model.TransportationJob> mbList = new ArrayList<>();
		for (TransportationJob tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationJob(tj.getId(), tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getCost(), tj.getEstimatedCost(),
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
		ma.azdad.mobile.model.TransportationJob tjMobile = new ma.azdad.mobile.model.TransportationJob(id, tj.getStartDate(), tj.getEndDate(), tj.getStatus(), tj.getCost(), tj.getEstimatedCost(),
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
				// hadi hiya li at3merlina les info de user si nexiste pas f base donnÃƒÆ’Ã‚Â©s
				googleGeocodeService.updateGoogleGeocodeDataAsync(userLocation);

			} else {
				DriverLocation lastLocation = locations.get(locations.size() - 1);
				double distanceKm = PathService.getDistance(lastLocation.getLatitude(), lastLocation.getLongitude(), lat, lng);
				if (distanceKm >= 5.0 || new Date().getTime() > lastLocation.getDate().getTime()) {
					lastLocation.setLatitude(lat);
					lastLocation.setLongitude(lng);
					// hadi adir mÃƒÂ¯Ã‚Â¿Ã‚Â½j les cordonnÃƒÆ’Ã‚Â©es si user exist dÃƒÆ’Ã‚Â©ja et
					// distance > 5km
					googleGeocodeService.updateGoogleGeocodeDataAsync(lastLocation);

				} else {
					System.out.println("Distance < 5km or Last Update < 3 days  skipping update");
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
						System.out.println("Distance < 5km ÃƒÂ¯Ã‚Â¿Ã‚Â½ skipping update");
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

	public Double plannedStartDistance(TransportationJob job) {

		if (job.getStopList() != null && !job.getStopList().isEmpty()) {
			return PathService.getDistance(job.getFirstLatitude(), job.getFirstLongitude(),
					job.getStopList().get(0).getSite() != null ? job.getStopList().get(0).getSite().getLatitude() : job.getStopList().get(0).getWarehouse().getLatitude(),
					job.getStopList().get(0).getSite() != null ? job.getStopList().get(0).getSite().getLongitude() : job.getStopList().get(0).getWarehouse().getLongitude());
		}

		return 0d;
	}

	public Double realStartDistance(Integer jobId) {
		List<TransportationJobItinerary> pickup = transportationJobItineraryRepos.findByTransportationJobIdAndTransportationRequestStatus(jobId, TransportationRequestStatus.PICKEDUP);

		if (pickup != null && !pickup.isEmpty()) {
			return pickup.get(0).getCumulativeDistance();
		}

		return 0d;
	}

	public Double ongoingDistance(Integer jobId) {
		List<TransportationJobItinerary> delivery = transportationJobItineraryRepos.findByTransportationJobIdAndTransportationRequestStatus(jobId, TransportationRequestStatus.DELIVERED);

		if (delivery != null && !delivery.isEmpty()) {
			Double lastCumulative = delivery.get(delivery.size() - 1).getCumulativeDistance();
			return lastCumulative - realStartDistance(jobId);
		}

		return 0d;
	}

	public Double calculateRealProductiveDist(Integer jobId) {
		List<TransportationJobItinerary> orderedItineraries = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(jobId);

		double realProductive = 0d;
		int loadCount = 0; // number of active loads still onboard

		for (TransportationJobItinerary point : orderedItineraries) {
			double dist = point.getDistanceFromPrevious();

			// Count distance only if carrying goods
			if (loadCount > 0) {
				realProductive += dist;
			}

			// Update load count based on request status
			if (point.getTransportationRequestStatus() == TransportationRequestStatus.PICKEDUP) {
				loadCount++;
			} else if (point.getTransportationRequestStatus() == TransportationRequestStatus.DELIVERED) {
				if (loadCount > 0)
					loadCount--;
			}
		}

		return realProductive;
	}

	public Double calculateRealNonProductiveDist(Integer jobId) {
		List<TransportationJobItinerary> orderedItineraries = transportationJobItineraryRepos.findByTransportationJobIdOrderByTimestampAsc(jobId);

		double realNonProductive = 0d;
		int loadCount = 0; // number of active loads still onboard

		for (TransportationJobItinerary point : orderedItineraries) {
			double dist = point.getDistanceFromPrevious();

			// Count distance only if NOT carrying goods
			if (loadCount == 0) {
				realNonProductive += dist;
			}

			// Update load count based on request status
			if (point.getTransportationRequestStatus() == TransportationRequestStatus.PICKEDUP) {
				loadCount++;
			} else if (point.getTransportationRequestStatus() == TransportationRequestStatus.DELIVERED) {
				if (loadCount > 0)
					loadCount--;
			}
		}

		return realNonProductive;
	}

	public Double calculateEstimatedProductiveDist(Integer jobId) {
		List<Stop> stops = stopRepos.findByTransportationJobIdOrderByDateAsc(jobId);

		double estimatedProd = 0d;
		int loadCount = 0; // track how many pickups are still active

		Double lastLat = null;
		Double lastLng = null;

		for (Stop stop : stops) {
			double lat;
			double lng;

			if (stop.getSite() != null) {
				lat = stop.getSite().getLatitude();
				lng = stop.getSite().getLongitude();
			} else if (stop.getWarehouse() != null) {
				lat = stop.getWarehouse().getLatitude();
				lng = stop.getWarehouse().getLongitude();
			} else {
				lat = 0.0;
				lng = 0.0;
			}

			if (lastLat != null && lastLng != null) {
				double dist = PathService.getDistance(lastLat, lastLng, lat, lng);

				if (loadCount > 0) { // productive distance only if carrying goods
					estimatedProd += dist;
				}
			}

			// Update load count based on stop type
			switch (stop.getType()) {
			case PICKUP:
				loadCount++;
				break;
			case DELIVERY:
				if (loadCount > 0)
					loadCount--;
				break;
			case DELIVERY_AND_PICKUP:
				if (loadCount > 0)
					loadCount--; // first deliver
				loadCount++; // then pickup again
				break;
			}

			lastLat = lat;
			lastLng = lng;
		}

		return estimatedProd;
	}

	public Double calculateEstimatedNonProductiveDist(Integer jobId) {
		List<Stop> stops = stopRepos.findByTransportationJobIdOrderByDateAsc(jobId);

		double estimatedNonProd = 0d;
		int loadCount = 0; // track active pickups still onboard

		Double lastLat = null;
		Double lastLng = null;

		for (Stop stop : stops) {
			double lat;
			double lng;

			if (stop.getSite() != null) {
				lat = stop.getSite().getLatitude();
				lng = stop.getSite().getLongitude();
			} else if (stop.getWarehouse() != null) {
				lat = stop.getWarehouse().getLatitude();
				lng = stop.getWarehouse().getLongitude();
			} else {
				lat = 0.0;
				lng = 0.0;
			}

			if (lastLat != null && lastLng != null) {
				double dist = PathService.getDistance(lastLat, lastLng, lat, lng);

				if (loadCount == 0) { // only count as non-productive if carrying nothing
					estimatedNonProd += dist;
				}
			}

			// Update load count based on stop type
			switch (stop.getType()) {
			case PICKUP:
				loadCount++;
				break;
			case DELIVERY:
				if (loadCount > 0)
					loadCount--;
				break;
			case DELIVERY_AND_PICKUP:
				if (loadCount > 0)
					loadCount--; // deliver first
				loadCount++; // then pickup again
				break;
			}

			lastLat = lat;
			lastLng = lng;
		}

		return estimatedNonProd;
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
