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
	private TransportationJobStatus status = TransportationJobStatus.NOT_STARTED; // Calculable or closed

	// Costs
	private Double realCost = 0.0;
	private Double estimatedCost = 0.0; // Calculable

	private Double vehiclePrice = 0.0;

	private Transporter transporter;
	private Vehicle vehicle;
	private User driver;

	private List<TransportationJobFile> fileList = new ArrayList<>();
	private List<TransportationJobHistory> historyList = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList = new ArrayList<>();
	private List<Stop> stopList = new ArrayList<>();
	private List<Path> pathList = new ArrayList<>();

	// TM
	private Integer transporterId;
	private Integer vehicleId;
	private String driverUsername;
	private String transporterName;

	public TransportationJob() {
		super();
	}

	public TransportationJob(Integer id, Date startDate, Date endDate, TransportationJobStatus status, Double realCost, Double estimatedCost, String transporterName1, String transporterName2) {
		super(id);
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.realCost = realCost;
		this.estimatedCost = estimatedCost;
		this.transporterName = transporterName2 != null ? transporterName2 : transporterName1;
	}

	public void init() {
		if (transporter != null)
			transporterId = transporter.getId();
		if (vehicle != null)
			vehicleId = vehicle.getId();
		if (driver != null)
			driverUsername = driver.getUsername();

		Collections.sort(transportationRequestList);
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && comment != null)
			result = comment.toLowerCase().contains(query);
		if (!result && transporter != null)
			result = transporter.filter(query);
		if (!result && transporterName != null)
			result = transporterName.toLowerCase().contains(query);
		if (!result && vehicle != null)
			result = vehicle.filter(query);
		if (!result && driver != null)
			result = driver.filter(query);
		return result;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
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
		return transporterId;
	}

	@Transient
	public void setTransporterId(Integer transporterId) {
		this.transporterId = transporterId;
	}

	@Transient
	public Integer getVehicleId() {
		return vehicleId;
	}

	@Transient
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Transient
	public String getDriverUsername() {
		return driverUsername;
	}

	@Transient
	public void setDriverUsername(String driverUsername) {
		this.driverUsername = driverUsername;
	}

	@Transient
	public String getTransporterName() {
		return transporterName;
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
}
