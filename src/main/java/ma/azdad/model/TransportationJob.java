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

@Entity

public class TransportationJob extends GenericModel<Integer> implements Serializable {

	private String comment;
	private Date startDate; // Calculable
	private Date endDate; // Calculable
	private TransportationJobStatus status = TransportationJobStatus.EDITED;
	private TransportationJobAssignmentType assignmentType;
	private Double acceptLeadTime = 12.0;
	private Double startLeadTime = 24.0;

	private Date maxAcceptDate;
	private Date maxStartDate;

	// timeline
	private Date date1; // edited
	private Date date2; // assigned1
	private Date date3; // assigned2
	private Date date4; // accepted
	private Date date5; // started
	private Date date6; // in_progress
	private Date date7; // completed
	private Date date8; // closed

	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User user6;
	private User user7;
	private User user8;

	// Costs
	private Double realCost = 0.0;
	private Double estimatedCost = 0.0; // Calculable
	private Double vehiclePrice = 0.0;

	// gps (first TR latitude/longitude)
	private Double latitude;
	private Double longitude;

	private Transporter transporter;
	private Vehicle vehicle;
	private User driver;

	private List<TransportationJobFile> fileList = new ArrayList<>();
	private List<TransportationJobHistory> historyList = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList = new ArrayList<>();
	private List<Stop> stopList = new ArrayList<>();
	private List<Path> pathList = new ArrayList<>();

	public TransportationJob() {
		super();
	}

	// c1
	public TransportationJob(Integer id, Date startDate, Date endDate, TransportationJobStatus status, Double realCost, Double estimatedCost, Double latitude, Double longitude, //
			Integer transporterId,TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName) {
		super(id);
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.realCost = realCost;
		this.estimatedCost = estimatedCost;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setTransporterId(transporterId);
		this.setTransporterType(transporterType);
		this.setTransporterPrivateFirstName(transporterPrivateFirstName);
		this.setTransporterPrivateLastName(transporterPrivateLastName);
		this.setTransporterSupplierName(transporterSupplierName);
	}

	// c2
	public TransportationJob(Integer id, Date startDate, Date endDate, TransportationJobStatus status, Double realCost, Double estimatedCost, Double latitude, Double longitude, //
			Integer transporterId,TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName, String driverUsername,
			String vehicleMatricule) {
		super(id);
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.realCost = realCost;
		this.estimatedCost = estimatedCost;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public void addHistory(TransportationJobHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(TransportationJobHistory history) {
		history.setParent(null);
		historyList.remove(history);
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
		return contains(query, comment, getTransporterName());
	}

	@Transient
	public String getEstimatedDuration() {
		try {
			return UtilsFunctions.getFormattedDuration(startDate, endDate);
		} catch (Exception e) {
			return null;
		}
	}

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

	public void calculateEstimatedCost() {
		estimatedCost = getEstimatedDistance() * vehiclePrice;
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

			map.put(date1, new Stop(tr.getStartDate(), type1, expected1, site1, warehouse1, this));
			map.put(date2, new Stop(tr.getEndDate(), type2, expected2, site2, warehouse2, this));
		}
		stopList = new ArrayList<>(map.values());
		Collections.sort(stopList);
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

		calculateEstimatedCost();

	}

	// public void updateCalculableFields() {
	// calculateStartDate();
	// calculateEndDate();
	// calculateStatus();
	// }

	public void calculateStatus() {
		// Closed
		if (TransportationJobStatus.CLOSED.equals(status))
			return;

		if (transportationRequestList.isEmpty())
			return;

		// Completed
		Boolean test1 = true, test2 = false;
		for (TransportationRequest transportationRequest : transportationRequestList) {
			if (transportationRequest.getDeliveryDate() == null)
				test1 = false;
			if (transportationRequest.getPickupDate() != null)
				test2 = true;
		}
		if (test1) {
			status = TransportationJobStatus.COMPLETED;
			return;
		}
		if (test2) {
			status = TransportationJobStatus.IN_PROGRESS;
			return;
		}

	}

	@Transient
	public Boolean getExpectedWarning() {
		return getMinExpectedDate() != null && new Date().compareTo(getMinExpectedDate()) > 0;
	}

	@Transient
	public Date getMinExpectedDate() {
		if (Arrays.asList(TransportationJobStatus.COMPLETED, TransportationJobStatus.CLOSED).contains(status))
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

	public Double getRealCost() {
		return realCost;
	}

	public void setRealCost(Double realCost) {
		this.realCost = realCost;
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

}
