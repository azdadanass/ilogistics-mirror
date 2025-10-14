package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ma.azdad.service.PathService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.App;

@Entity

public class TransportationJob extends GenericModel<Integer> implements Serializable {

	private String reference;
	private String ref;
	private Boolean important = false;
	private Priority priority;
	private String comment;
	private Date startDate; // Calculable
	private Date endDate; // Calculable
	private TransportationJobStatus status = TransportationJobStatus.EDITED;
	private TransportationJobAssignmentType assignmentType;
	private Double acceptLeadTime = 12.0;
	private Double startLeadTime = 24.0;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Double plannedStartCost = 0.0;
	private Double plannedItineraryCost = 0.0;
	private Double plannedHandlingCost = 0.0;

	private Date maxAcceptDate;
	private Date maxStartDate;

	private Double firstLatitude;
	private Double firstLongitude;

	private Double plannedStartLatitude;
	private Double plannedStartLongitude;

	private Double startLatitude; // if web start
	private Double startLongitude;
	private Double startDistance;// if web start

	private String qrKey;

	// calculable fields
	private Double plannedEffectiveDistance;
	private Double plannedNonEffectiveDistance;
	private Double plannedMaxVolume;
	private Double plannedMaxWeight;

	private Double startingDistance;
	private Double totalDistanceTravelled;
	private String totalItineraryDuration;
	private Double effectiveTraveledDistance;
	private Double nonEffectiveTraveledDistance;
	private Double itineratyMaxVolume;
	private Double itineratyMaxWeight;

	// timeline
	private Date date1; // edited
	private Date date2; // assigned1
	private Date date3; // assigned2
	private Date date4; // accepted
	private Date date5; // started
	private Date date6; // in_progress
	private Date date7; // completed
	private Date date8; // Acknowledged

	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User user6;
	private User user7;
	private User user8;

	// Costs
	private Double cost = 0.0; // = startCost+itineraryCost+handlingCost
	private Double startCost = 0.0;
	private Double itineraryCost = 0.0;
	private Double handlingCost = 0.0;

	private Double estimatedCost = 0.0;
	private Double estimatedStartCost = 0.0;
	private Double estimatedItineraryCost = 0.0;

	private Double vehiclePrice = 0.0;

	private Transporter transporter;
	private Vehicle vehicle;
	private User driver;

	private List<TransportationJobFile> fileList = new ArrayList<>();
	private List<TransportationJobHistory> historyList = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList = new ArrayList<>();
	private List<Stop> stopList = new ArrayList<>();
	private List<Path> pathList = new ArrayList<>();
	private List<ToNotify> toNotifyList = new ArrayList<>();
	private List<TransportationJobComment> commentList = new ArrayList<>();

	private List<CommentGroup<TransportationJobComment>> commentGroupList;

	public TransportationJob() {
		super();
	}

	// c1
	public TransportationJob(Integer id, String reference, String ref, Priority priority, Date plannedStartDate, Date plannedEndDate, Date startDate, Date endDate, TransportationJobStatus status,
			Double cost, Double estimatedCost, Double firstLatitude, Double firstLongitude, //
			String user1Photo, Integer transporterId, TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName,
			String transporterCompanyName) {
		super(id);
		this.reference = reference;
		this.ref = ref;
		this.priority = priority;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.cost = cost;
		this.estimatedCost = estimatedCost;
		this.firstLatitude = firstLatitude;
		this.firstLongitude = firstLongitude;
		this.setUser1Photo(user1Photo);
		this.setTransporterId(transporterId);
		this.setTransporterType(transporterType);
		this.setTransporterPrivateFirstName(transporterPrivateFirstName);
		this.setTransporterPrivateLastName(transporterPrivateLastName);
		this.setTransporterSupplierName(transporterSupplierName);
		this.setTransporterCompanyName(transporterCompanyName);
	}

	// c2
	public TransportationJob(Integer id, String reference, String ref, Priority priority, Date plannedStartDate, Date plannedEndDate, Date startDate, Date endDate, TransportationJobStatus status,
			Double cost, Double estimatedCost, Double firstLatitude, Double firstLongitude, //
			String user1Photo, Integer transporterId, TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName,
			String driverUsername, String vehicleMatricule) {
		super(id);
		this.reference = reference;
		this.ref = ref;
		this.priority = priority;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.cost = cost;
		this.estimatedCost = estimatedCost;
		this.firstLatitude = firstLatitude;
		this.firstLongitude = firstLongitude;
		this.setUser1Photo(user1Photo);
		this.setTransporterId(transporterId);
		this.setTransporterType(transporterType);
		this.setTransporterPrivateFirstName(transporterPrivateFirstName);
		this.setTransporterPrivateLastName(transporterPrivateLastName);
		this.setTransporterSupplierName(transporterSupplierName);
		this.setDriverUsername(driverUsername);
		this.setVehicleMatricule(vehicleMatricule);
	}

	public void init() {
		Collections.sort(transportationRequestList);
	}

	public void calculateCost() {
		this.cost = this.startCost + this.itineraryCost + this.handlingCost;
	}

	public void calculateEstimatedCost() {
		this.estimatedCost = this.estimatedStartCost + this.estimatedItineraryCost;
	}

	public void addHistory(TransportationJobHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(TransportationJobHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(TransportationJobComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(TransportationJobComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<TransportationJobComment>> map = new HashMap<>();
		for (TransportationJobComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<TransportationJobComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	public void calculateMaxAcceptTime() {
		maxAcceptDate = null;
		if (getAssignDate() != null)
			maxAcceptDate = UtilsFunctions.addMinutesToDate(getAssignDate(), (int) (acceptLeadTime * 60));
	}

	@Transient
	public Date getAssignDate() {
		return date2 != null ? date2 : date3;
	}

	public void calculateMaxStartTime() {
		maxStartDate = null;
		Date assignDate = getAssignDate();
		if (assignDate != null)
			maxStartDate = UtilsFunctions.addMinutesToDate(assignDate, (int) (startLeadTime * 60));

	}

	@Override
	public boolean filter(String query) {
		return contains(query, reference, comment, getTransporterName());
	}

	@Transient
	public Double getGrossWeight() {
		return transportationRequestList.stream().filter(i -> i.getGrossWeight() != null).mapToDouble(i -> i.getGrossWeight()).sum();
	}

	@Transient
	public Double getNetWeight() {
		return transportationRequestList.stream().filter(i -> i.getNetWeight() != null).mapToDouble(i -> i.getGrossWeight()).sum();
	}

	@Transient
	public Double getVolume() {
		return transportationRequestList.stream().filter(i -> i.getVolume() != null).mapToDouble(i -> i.getGrossWeight()).sum();
	}

	@Transient
	public Integer getNumberOfItems() {
		return transportationRequestList.stream().filter(i -> i.getNumberOfItems() != null).mapToInt(i -> i.getNumberOfItems()).sum();
	}

	@Transient
	public String getPlannedStartDuration() {
		if (getStopList() != null && !getStopList().isEmpty()) {

			Double startLat = (plannedStartLatitude != null) ? plannedStartLatitude : getFirstLatitude();
			Double startLng = (plannedStartLongitude != null) ? plannedStartLongitude : getFirstLongitude();

			Double stopLat = getStopList().get(0).getSite() != null ? getStopList().get(0).getSite().getLatitude() : getStopList().get(0).getWarehouse().getLatitude();

			Double stopLng = getStopList().get(0).getSite() != null ? getStopList().get(0).getSite().getLongitude() : getStopList().get(0).getWarehouse().getLongitude();

			return PathService.getDuration(startLat, startLng, stopLat, stopLng);
		}

		return "";
	}

	@Transient
	public String getEstimatedDuration() {
		if (getStopList() != null && getStopList().size() > 1) {
			try {
				// first stop
				Double firstLat = getStopList().get(0).getSite() != null ? getStopList().get(0).getSite().getLatitude() : getStopList().get(0).getWarehouse().getLatitude();

				Double firstLng = getStopList().get(0).getSite() != null ? getStopList().get(0).getSite().getLongitude() : getStopList().get(0).getWarehouse().getLongitude();

				// last stop
				int lastIndex = getStopList().size() - 1;
				Double lastLat = getStopList().get(lastIndex).getSite() != null ? getStopList().get(lastIndex).getSite().getLatitude() : getStopList().get(lastIndex).getWarehouse().getLatitude();

				Double lastLng = getStopList().get(lastIndex).getSite() != null ? getStopList().get(lastIndex).getSite().getLongitude() : getStopList().get(lastIndex).getWarehouse().getLongitude();

				// use path service to compute duration
				return PathService.getDuration(firstLat, firstLng, lastLat, lastLng);
			} catch (Exception e) {
				return null;
			}
		}

		return "";
	}

	// Distance MANAGEMENT

	@Transient
	public Double getEstimatedDistance() {
		Double result = 0.0;
		for (Path path : pathList)
			if (path.getEstimatedDistance() != null)
				result += path.getEstimatedDistance();
		return result;
	}

	@Transient
	public Double getCurrentDistance() {
		Double result = 0.0;
		for (Path path : pathList)
			if (path.getEstimatedDistance() != null && !path.getFrom().getExpected() && !path.getTo().getExpected())
				result += path.getEstimatedDistance();
		return result;
	}

	@Transient
	public Double getCurrentDistancePercentage() {
		try {
			return getCurrentDistance() / getEstimatedDistance();
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public Double getPlannedStartingDistance() {
		if (getStopList() != null && !getStopList().isEmpty()) {
			Double startLat = (plannedStartLatitude != null) ? plannedStartLatitude : getFirstLatitude();
			Double startLng = (plannedStartLongitude != null) ? plannedStartLongitude : getFirstLongitude();
			Double stopLat = getStopList().get(0).getSite() != null ? getStopList().get(0).getSite().getLatitude() : getStopList().get(0).getWarehouse().getLatitude();
			Double stopLng = getStopList().get(0).getSite() != null ? getStopList().get(0).getSite().getLongitude() : getStopList().get(0).getWarehouse().getLongitude();
			return PathService.getDistance(startLat, startLng, stopLat, stopLng);
		}
		return 0d;
	}

	@Transient
	public Double getPlannedTotalDistance() {

		return getPlannedStartingDistance() + getEstimatedDistance();
	}

	@Transient
	public Double getCurrentVsPlannedDistPercentage() {

		return getTotalDistanceTravelled() != null ? getTotalDistanceTravelled() : 0d / getPlannedTotalDistance();
	}

	@Transient
	public Double getCurrentVsPlannedDurationPercentage() {
		try {
			if (plannedStartDate != null && plannedEndDate != null && totalItineraryDuration != null) {
				// planned duration (plannedStart + estimatedDuration)
				long plannedDurationMinutes = UtilsFunctions.getDateDiffInMinutes(plannedStartDate, plannedEndDate);
				long itineraryDurationMinutes = UtilsFunctions.parseDurationStringToMinutes(totalItineraryDuration);

				if (plannedDurationMinutes > 0) {
					return (double) itineraryDurationMinutes / plannedDurationMinutes;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	@Transient
	public Double getPlannedNonEffectiveDistance() {
		return plannedNonEffectiveDistance;
	}

	@Transient
	public void setPlannedNonEffectiveDistance(Double plannedNonEffectiveDistance) {
		this.plannedNonEffectiveDistance = plannedNonEffectiveDistance;
	}

	@Transient
	public Double getPlannedEffectiveDistance() {
		return plannedEffectiveDistance;
	}

	@Transient
	public void setPlannedEffectiveDistance(Double plannedEffectiveDistance) {
		this.plannedEffectiveDistance = plannedEffectiveDistance;
	}

	@Transient
	public Integer gettotalPlannedStops() {
		return stopList.size();
	}

	@Transient
	public Integer getTotalStopsDuration() {
		if (getStopList() == null || getStopList().isEmpty()) {
			return 0;
		}

		return getStopList().stream().map(Stop::getDuration).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
	}

	@Transient
	public Double getPlannedMaxVolume() {
		return plannedMaxVolume;
	}

	@Transient
	public void setPlannedMaxVolume(Double plannedMaxVolume) {
		this.plannedMaxVolume = plannedMaxVolume;
	}

	@Transient
	public Double getPlannedMaxWeight() {
		return plannedMaxWeight;
	}

	@Transient
	public Double getStartingDistance() {
		return startingDistance;
	}

	@Transient
	public void setStartingDistance(Double startingDistance) {
		this.startingDistance = startingDistance;
	}

	@Transient
	public void setPlannedMaxWeight(Double plannedMaxWeight) {
		this.plannedMaxWeight = plannedMaxWeight;
	}

	@Transient
	public Double getTotalDistanceTravelled() {
		return totalDistanceTravelled;
	}

	@Transient
	public void setTotalDistanceTravelled(Double totalDistanceTravelled) {
		this.totalDistanceTravelled = totalDistanceTravelled;
	}

	@Transient
	public String getTotalItineraryDuration() {
		return totalItineraryDuration;
	}

	@Transient
	public void setTotalItineraryDuration(String totalItineraryDuration) {
		this.totalItineraryDuration = totalItineraryDuration;
	}

	@Transient
	public Double getEffectiveTraveledDistance() {
		return effectiveTraveledDistance;
	}

	@Transient
	public void setEffectiveTraveledDistance(Double effectiveTraveledDistance) {
		this.effectiveTraveledDistance = effectiveTraveledDistance;
	}

	@Transient
	public Double getNonEffectiveTraveledDistance() {
		return nonEffectiveTraveledDistance;
	}

	@Transient
	public void setNonEffectiveTraveledDistance(Double nonEffectiveTraveledDistance) {
		this.nonEffectiveTraveledDistance = nonEffectiveTraveledDistance;
	}

	@Transient
	public Double getItineratyMaxVolume() {
		return itineratyMaxVolume;
	}

	@Transient
	public void setItineratyMaxVolume(Double itineratyMaxVolume) {
		this.itineratyMaxVolume = itineratyMaxVolume;
	}

	@Transient
	public Double getItineratyMaxWeight() {
		return itineratyMaxWeight;
	}

	@Transient
	public void setItineratyMaxWeight(Double itineratyMaxWeight) {
		this.itineratyMaxWeight = itineratyMaxWeight;
	}

	public void calculateEstimatedStartCost() {
		try {
			this.estimatedStartCost = getPlannedStartingDistance() * 0.5 * vehiclePrice;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		calculateEstimatedCost();
	}

	public void calculateEstimatedItineraryCost() {
//		estimatedItineraryCost = getEstimatedDistance() * vehiclePrice; // old
		try {
			this.estimatedItineraryCost = (plannedEffectiveDistance + 0.5 * (plannedNonEffectiveDistance - getPlannedStartingDistance())) * vehiclePrice;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		calculateEstimatedCost();
	}

	@Transient
	public Double getPaidCost() {
		Double result = 0.0;
		for (TransportationRequest transportationRequest : transportationRequestList)
			result += transportationRequest.getTotalAppLinkCost();
		return result;
	}

	// STOP & PATH MANAGEMENT
	@Transient
	public String getFirstStopValue() {
		if (stopList.isEmpty())
			return null;
		return stopList.get(0).getPlace().getValue();
	}

	public void generateStopList() {
		Map<String, Stop> map = new HashMap<>();
		Map<Date, Integer> map2 = new HashMap<>();
		for (TransportationRequest tr : transportationRequestList) {
			Stop s1 = map.get(tr.getStartDate());
			Stop s2 = map.get(tr.getEndDate());

			StopType type1 = s1 == null || StopType.PICKUP.equals(s1.getType()) ? StopType.PICKUP : StopType.DELIVERY_AND_PICKUP;
			StopType type2 = s2 == null || StopType.DELIVERY.equals(s1.getType()) ? StopType.DELIVERY : StopType.DELIVERY_AND_PICKUP;

			Boolean expected1 = s1 == null ? tr.getExpectedToStart() : tr.getExpectedToStart() && s1.getExpected();
			Boolean expected2 = s2 == null ? tr.getExpectedToEnd() : tr.getExpectedToEnd() && s2.getExpected();

			Site site1 = tr.getIsOutbound() ? null : tr.getDeliveryRequest().getOrigin();
			Site site2 = tr.getIsInbound() ? null : tr.getDeliveryRequest().getDestination();

			Warehouse warehouse1 = tr.getIsInbound() ? null : tr.getDeliveryRequest().getWarehouse();
			Warehouse warehouse2 = tr.getIsOutbound() ? null : tr.getDeliveryRequest().getWarehouse();

			String date1 = UtilsFunctions.getFormattedDateTime(tr.getStartDate());
			String date2 = UtilsFunctions.getFormattedDateTime(tr.getEndDate());
			System.out.println("tr " + tr.getOriginName());
			System.out.println("tr " + tr.getDestinationName());

			// ----- PICKUP STOP -----
			s1 = new Stop(tr.getStartDate(), type1, expected1, site1, warehouse1, this);
			s1.setDuration(tr.getPickupDuration2() != null ? tr.getPickupDuration2() : 0);
			Integer siteId = map2.get(tr.getStartDate());
			if (siteId != null && siteId.equals(s1.getPlace().id())) {
				// filter requests that match by destination or warehouse
				int totalDuration = transportationRequestList.stream().filter(req -> {
					Integer destId = (req.getDeliveryRequest().getOrigin() != null) ? req.getDeliveryRequest().getOrigin().getId()
							: (req.getDeliveryRequest().getWarehouse() != null ? req.getDeliveryRequest().getWarehouse().getId() : null);
					return destId != null && destId.equals(siteId);
				}).mapToInt(req -> {
					Integer d = req.getPickupDuration2();
					return d != null ? d : 0;
				}).sum();

				// set summed duration
				s1.setDuration(totalDuration);
			}
			map.put(date1, s1);
			map2.put(tr.getStartDate(), s1.getPlace().id());

			// ----- DELIVERY STOP -----
			s2 = new Stop(tr.getEndDate(), type2, expected2, site2, warehouse2, this);
			s2.setDuration(tr.getDeliveryDuration2() != null ? tr.getDeliveryDuration2() : 0);

			Integer siteId2 = map2.get(tr.getEndDate());
			if (siteId2 != null && siteId2.equals(s2.getPlace().id())) {
				// filter requests that match by destination or warehouse
				int totalDuration = transportationRequestList.stream().filter(req -> {
					Integer destId = (req.getDeliveryRequest().getDestination() != null) ? req.getDeliveryRequest().getDestination().getId()
							: (req.getDeliveryRequest().getWarehouse() != null ? req.getDeliveryRequest().getWarehouse().getId() : null);
					return destId != null && destId.equals(siteId2);
				}).mapToInt(req -> {
					Integer d = req.getDeliveryDuration2();
					return d != null ? d : 0;
				}).sum();

				// set summed duration
				s2.setDuration(totalDuration);
			}
			map.put(date2, s2);
			map2.put(tr.getEndDate(), s2.getPlace().id());

		}
		stopList.clear();
		for (Stop stop : map.values()) {
			stop.setTransportationJob(this); // extra safety in case ctor didn’t set it
			stopList.add(stop);
		}
		Collections.sort(stopList);
		if (!stopList.isEmpty()) {
			firstLatitude = stopList.get(0).getPlace().getLatitude();
			firstLongitude = stopList.get(0).getPlace().getLongitude();
		}
	}

	public void generatePathList() {
		pathList = new ArrayList<>();
		for (int i = 0; i < stopList.size() - 1; i++) {
			Stop from = stopList.get(i);
			Stop to = stopList.get(i + 1);
			if (from.getPlaceValue().equals(to.getPlaceValue()))
				continue;
			Path path = PathService.getNewPath(from.getPlace(), to.getPlace());
			path.setFrom(from);
			path.setTo(to);
			path.setTransportationJob(this);
			pathList.add(path);
		}

		calculateEstimatedItineraryCost();

	}

	@Transient
	public Boolean getExpectedWarning() {
		return getMinExpectedDate() != null && new Date().compareTo(getMinExpectedDate()) > 0;
	}

	@Transient
	public Date getMinExpectedDate() {
		if (Arrays.asList(TransportationJobStatus.COMPLETED, TransportationJobStatus.ACKNOWLEDGED).contains(status))
			return null;
		try {
			Date minDate = null;
			for (Stop stop : stopList)
				if (stop.getExpected()) {
					if (minDate == null)
						minDate = stop.getDate();
					minDate = UtilsFunctions.getMinDate(minDate, stop.getDate());
				}
			return minDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transient
	public Boolean getExpectedToStart() {
		for (TransportationRequest tr : transportationRequestList)
			if (tr.getStartDate().equals(startDate) && tr.getPickupDate() != null)
				return false;
		return true;
	}

	@Transient
	public Boolean getExpectedToEnd() {
		for (TransportationRequest tr : transportationRequestList)
			if (tr.getEndDate().equals(endDate) && tr.getDeliveryDate() != null)
				return false;
		return true;
	}

	public void calculateStartDate() {
		if (transportationRequestList.isEmpty()) {
			startDate = null;
			return;
		}
		Iterator<TransportationRequest> it = transportationRequestList.iterator();
		startDate = it.next().getStartDate();
		while (it.hasNext())
			startDate = UtilsFunctions.getMinDate(startDate, it.next().getStartDate());
	}

	public void calculateEndDate() {
		if (transportationRequestList.isEmpty()) {
			endDate = null;
			return;
		}
		Iterator<TransportationRequest> it = transportationRequestList.iterator();
		endDate = it.next().getEndDate();
		while (it.hasNext())
			endDate = UtilsFunctions.getMaxDate(endDate, it.next().getEndDate());
	}

	public void calculateStatus() {
		// check if completed or < started
		if (date5 == null || date8 != null || transportationRequestList.isEmpty())
			return;

		long countPicked = transportationRequestList.stream().filter(i -> i.getPickupDate() != null).count();
		long countDelivered = transportationRequestList.stream().filter(i -> i.getDeliveryDate() != null).count();
		long countTotal = transportationRequestList.size();

		if (countTotal == countDelivered) {
			status = TransportationJobStatus.COMPLETED;
			if (date7 == null) {
				date7 = new Date();
				user7 = driver;
			}
		} else if (countPicked > 0) {
			status = TransportationJobStatus.IN_PROGRESS;
			if (date6 == null) {
				date6 = new Date();
				user6 = driver;
			}
			date7 = null;
			user7 = null;
			date8 = null;
//			user8 = null;
		}

		if (transportationRequestList.stream().filter(i -> !TransportationRequestStatus.ACKNOWLEDGED.equals(i.getStatus())).count() == 0) {
			status = TransportationJobStatus.ACKNOWLEDGED;
			if (date8 == null)
				date8 = new Date();
//			user8 = driver;
		}

	}

	public void generateReference() {
		reference = "TJ" + String.format("%06d", id);
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(Double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<TransportationJobFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<TransportationJobFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TransportationJobHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<TransportationJobHistory> historyList) {
		this.historyList = historyList;
	}

	@Column(columnDefinition = "TEXT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public TransportationJobStatus getStatus() {
		return status;
	}

	public void setStatus(TransportationJobStatus status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transportationJob", cascade = CascadeType.ALL)
	public List<TransportationRequest> getTransportationRequestList() {
		return transportationRequestList;
	}

	public void setTransportationRequestList(List<TransportationRequest> transportationRequestList) {
		this.transportationRequestList = transportationRequestList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transportationJob", cascade = CascadeType.ALL)
	public List<Stop> getStopList() {
		return stopList;
	}

	public void setStopList(List<Stop> stopList) {
		this.stopList = stopList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transportationJob", cascade = CascadeType.ALL)
	public List<Path> getPathList() {
		return pathList;
	}

	public void setPathList(List<Path> pathList) {
		this.pathList = pathList;
	}

	@Transient
	public Integer getTransporterId() {
		return transporter != null ? transporter.getId() : null;
	}

	@Transient
	public void setTransporterId(Integer transporterId) {
		if (transporter == null || !transporterId.equals(transporter.getId()))
			transporter = new Transporter();
		transporter.setId(transporterId);
	}

	@Transient
	public String getTransporterName() {
		return transporter != null ? transporter.getName() : null;
	}

	@Transient
	public TransporterType getTransporterType() {
		return transporter != null ? transporter.getType() : null;
	}

	@Transient
	public void setTransporterType(TransporterType transporterType) {
		if (transporter == null)
			transporter = new Transporter();
		transporter.setType(transporterType);
	}

	@Transient
	public String getTransporterPrivateFirstName() {
		return transporter != null ? transporter.getPrivateFirstName() : null;
	}

	@Transient
	public void setTransporterPrivateFirstName(String transporterPrivateFirstName) {
		if (transporter == null)
			transporter = new Transporter();
		transporter.setPrivateFirstName(transporterPrivateFirstName);
	}

	@Transient
	public String getTransporterPrivateLastName() {
		return transporter != null ? transporter.getPrivateLastName() : null;
	}

	@Transient
	public void setTransporterPrivateLastName(String transporterPrivateLastName) {
		if (transporter == null)
			transporter = new Transporter();
		transporter.setPrivateLastName(transporterPrivateLastName);
	}

	@Transient
	public String getTransporterSupplierName() {
		return transporter != null ? transporter.getSupplierName() : null;
	}

	@Transient
	public void setTransporterSupplierName(String transporterSupplierName) {
		if (transporter == null)
			transporter = new Transporter();
		transporter.setSupplierName(transporterSupplierName);
	}

	@Transient
	public String getTransporterCompanyName() {
		return transporter != null ? transporter.getCompanyName() : null;
	}

	@Transient
	public void setTransporterCompanyName(String transporterCompanyName) {
		if (transporter == null)
			transporter = new Transporter();
		transporter.setCompanyName(transporterCompanyName);
	}

	@Transient
	public String getUser1Username() {
		return user1 != null ? user1.getUsername() : null;
	}

	@Transient
	public void setUser1Username(String user1Username) {
		if (user1 == null || !user1Username.equals(user1.getUsername()))
			user1 = new User();
		user1.setUsername(user1Username);
	}

	@Transient
	public String getUser1Photo() {
		return user1 != null ? user1.getPhoto() : null;
	}

	@Transient
	public void setUser1Photo(String user1Photo) {
		if (user1 == null)
			user1 = new User();
		user1.setPhoto(user1Photo);
	}

	@Transient
	public String getDriverUsername() {
		return driver != null ? driver.getUsername() : null;
	}

	@Transient
	public void setDriverUsername(String driverUsername) {
		if (driver == null || !driverUsername.equals(driver.getUsername()))
			driver = new User();
		driver.setUsername(driverUsername);
	}

	@Transient
	public String getDriverFullName() {
		return driver != null ? driver.getFullName() : null;
	}

	@Transient
	public void setDriverFullName(String driverFullName) {
		if (driver == null)
			driver = new User();
		driver.setFullName(driverFullName);
	}

	@Transient
	public String getVehicleMatricule() {
		return vehicle != null ? vehicle.getMatricule() : null;
	}

	@Transient
	public void setVehicleMatricule(String vehicleMatricule) {
		if (vehicle == null)
			vehicle = new Vehicle();
		vehicle.setMatricule(vehicleMatricule);
	}

	@Transient
	public String getVehicleTypeName() {
		return vehicle != null ? vehicle.getTypeName() : null;
	}

	@Transient
	public void setVehicleTypeName(String vehicleTypeName) {
		if (vehicle == null)
			vehicle = new Vehicle();
		vehicle.setTypeName(vehicleTypeName);
	}

	@Transient
	public Integer getVehicleId() {
		return vehicle != null ? vehicle.getId() : null;
	}

	@Transient
	public void setVehicleId(Integer vehicleId) {
		if (vehicle == null || !vehicleId.equals(vehicle.getId()))
			vehicle = new Vehicle();
		vehicle.setId(vehicleId);
	}

	public Double getVehiclePrice() {
		return vehiclePrice;
	}

	public void setVehiclePrice(Double vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(Date date3) {
		this.date3 = date3;
	}

	public Date getDate4() {
		return date4;
	}

	public void setDate4(Date date4) {
		this.date4 = date4;
	}

	public Date getDate5() {
		return date5;
	}

	public void setDate5(Date date5) {
		this.date5 = date5;
	}

	public Date getDate6() {
		return date6;
	}

	public void setDate6(Date date6) {
		this.date6 = date6;
	}

	public Date getDate7() {
		return date7;
	}

	public void setDate7(Date date7) {
		this.date7 = date7;
	}

	public Date getDate8() {
		return date8;
	}

	public void setDate8(Date date8) {
		this.date8 = date8;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser6() {
		return user6;
	}

	public void setUser6(User user6) {
		this.user6 = user6;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser7() {
		return user7;
	}

	public void setUser7(User user7) {
		this.user7 = user7;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser8() {
		return user8;
	}

	public void setUser8(User user8) {
		this.user8 = user8;
	}

	@Enumerated(EnumType.STRING)
	public TransportationJobAssignmentType getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(TransportationJobAssignmentType assignmentType) {
		this.assignmentType = assignmentType;
	}

	public Double getAcceptLeadTime() {
		return acceptLeadTime;
	}

	public void setAcceptLeadTime(Double acceptLeadTime) {
		this.acceptLeadTime = acceptLeadTime;
	}

	public Double getStartLeadTime() {
		return startLeadTime;
	}

	public void setStartLeadTime(Double startLeadTime) {
		this.startLeadTime = startLeadTime;
	}

	public Date getMaxAcceptDate() {
		return maxAcceptDate;
	}

	public void setMaxAcceptDate(Date maxAcceptDate) {
		this.maxAcceptDate = maxAcceptDate;
	}

	public Date getMaxStartDate() {
		return maxStartDate;
	}

	public void setMaxStartDate(Date maxStartDate) {
		this.maxStartDate = maxStartDate;
	}

	public Double getFirstLatitude() {
		return firstLatitude;
	}

	public void setFirstLatitude(Double firstLatitude) {
		this.firstLatitude = firstLatitude;
	}

	public Double getFirstLongitude() {
		return firstLongitude;
	}

	public void setFirstLongitude(Double firstLongitude) {
		this.firstLongitude = firstLongitude;
	}

	public String getQrKey() {
		return qrKey;
	}

	public void setQrKey(String qrKey) {
		this.qrKey = qrKey;
	}

	@Transient
	public String getQrImageLink() {
		return App.QR.getLink() + "/img/tj/" + id + "/" + qrKey;
	}

	@Transient
	public String getQrLink() {
		return App.QR.getLink() + "/tj/" + id + "/" + qrKey;
	}

	public Double getStartCost() {
		return startCost;
	}

	public void setStartCost(Double startCost) {
		this.startCost = startCost;
	}

	public Double getItineraryCost() {
		return itineraryCost;
	}

	public void setItineraryCost(Double itineraryCost) {
		this.itineraryCost = itineraryCost;
	}

	public Double getHandlingCost() {
		return handlingCost;
	}

	public void setHandlingCost(Double handlingCost) {
		this.handlingCost = handlingCost;
	}

	public Double getEstimatedItineraryCost() {
		return estimatedItineraryCost;
	}

	public void setEstimatedItineraryCost(Double estimatedItineraryCost) {
		this.estimatedItineraryCost = estimatedItineraryCost;
	}

	public Double getEstimatedStartCost() {
		return estimatedStartCost;
	}

	public void setEstimatedStartCost(Double estimatedStartCost) {
		this.estimatedStartCost = estimatedStartCost;
	}

	public Double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(Double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public Double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(Double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Boolean getImportant() {
		return important;
	}

	public void setImportant(Boolean important) {
		this.important = important;
	}

	@Enumerated(EnumType.STRING)
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public Double getPlannedStartCost() {
		return plannedStartCost;
	}

	public void setPlannedStartCost(Double plannedStartCost) {
		this.plannedStartCost = plannedStartCost;
	}

	public Double getPlannedItineraryCost() {
		return plannedItineraryCost;
	}

	public void setPlannedItineraryCost(Double plannedItineraryCost) {
		this.plannedItineraryCost = plannedItineraryCost;
	}

	public Double getPlannedHandlingCost() {
		return plannedHandlingCost;
	}

	public void setPlannedHandlingCost(Double plannedHandlingCost) {
		this.plannedHandlingCost = plannedHandlingCost;
	}

	public Double getStartDistance() {
		return startDistance;
	}

	public void setStartDistance(Double startDistance) {
		this.startDistance = startDistance;
	}

	public Double getPlannedStartLatitude() {
		return plannedStartLatitude;
	}

	public void setPlannedStartLatitude(Double plannedStartLatitude) {
		this.plannedStartLatitude = plannedStartLatitude;
	}

	public Double getPlannedStartLongitude() {
		return plannedStartLongitude;
	}

	public void setPlannedStartLongitude(Double plannedStartLongitude) {
		this.plannedStartLongitude = plannedStartLongitude;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transportationJob", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ToNotify> getToNotifyList() {
		return toNotifyList;
	}

	public void setToNotifyList(List<ToNotify> toNotifyList) {
		this.toNotifyList = toNotifyList;
	}

	@Transient
	public List<CommentGroup<TransportationJobComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<TransportationJobComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TransportationJobComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<TransportationJobComment> commentList) {
		this.commentList = commentList;
	}

}
