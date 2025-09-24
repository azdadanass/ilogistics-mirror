package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;

import ma.azdad.utils.App;

@Entity

public class TransportationRequest extends GenericModel<Integer> implements Serializable, Comparable<TransportationRequest> {

	private String reference;
	private TransportationRequestStatus status = TransportationRequestStatus.EDITED;

	// USER
	private Date neededPickupDate;
	private Date neededDeliveryDate;

	private Date plannedPickupDate;
	private Date plannedDeliveryDate;

	private Date expectedPickupDate;
	private Date expectedDeliveryDate;

	private Date pickupDate;
	private Date deliveryDate;

	private String qrKey;

	private DeliveryRequest deliveryRequest;

	private ContactType contactType1;
	private ContactType contactType2;
	private User contact1;
	private User contact2;

	private Double estimatedDuration = 0.0;
	private String estimatedDurationText;
	private Double estimatedDistance = 0.0;
	private String estimatedDistanceText;
	private String rejectionReason;

	private Integer plannedPickupDuration;
	private Integer plannedDeliveryDuration;
	private Integer pickupDuration;
	private Integer deliveryDuration;

	private Double realDistance;
	private Double startDistance;

	private Double estimatedCost = 0.0;
	private Double estimatedItineraryCost;
	private Double estimatedStartCost;

	private Double cost = 0.0; // = startCost+itineraryCost+handlingCost
	private Double startCost = 0.0;
	private Double itineraryCost = 0.0;
	private Double handlingCost = 0.0;

	private Double totalAppLinkCost = 0.0; // MAD

	private TransportationRequestPaymentStatus paymentStatus;

	private Transporter transporter;
	private Vehicle vehicle;
	private User driver;

	private TransportationJob transportationJob;

	// TIMELINE
	private Date date1; // Edited
	private Date date2; // Requested
	private Date date3; // Approved
	private Date date4; // Assigned
	private Date date5; // Picked up
	private Date date6; // Delivered
	private Date date7; // Acknowledged
	private Date date8; // Rejected
	private Date date9; // Canceled

	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User user6;
	private User user7;
	private User user8;
	private User user9;

	// TM
	private Integer vehicleId;
	private String driverUsername;
	private Integer deliveryRequestId;
	private String deliveryRequestReference;
	private String deliveryRequestSmsRef;
	private String transporterName;
	private Integer originId;
	private Integer destinationId;
	private Integer warehouseId;
	private String contact1Username;
	private String contact2Username;
	private String approverFullName;
	private String requesterUsername;
	private String requesterFullName;

	private List<TransportationRequestFile> fileList = new ArrayList<>();
	private List<TransportationRequestHistory> historyList = new ArrayList<>();
	private List<Issue> issueList = new ArrayList<>();

	public void clearTimeLine() {
		rejectionReason = null;

		date2 = null;
		date3 = null;
		date4 = null;
		date5 = null;
		date6 = null;
		date7 = null;
		date8 = null;
		date9 = null;

		user3 = null;
		user4 = null;
		user5 = null;
		user6 = null;
		user7 = null;
		user8 = null;
		user9 = null;
	}

	public TransportationRequest() {
		super();
	}

	// c1
	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status, Date neededPickupDate, Date neededDeliveryDate, Date plannedPickupDate, Date plannedDeliveryDate,
			Date expectedPickupDate, Date expectedDeliveryDate, Date pickupDate, Date deliveryDate, //
			Integer deliveryRequestId, String deliveryRequestReference, String deliveryRequestSmsRef, DeliveryRequestType deliveryRequestType, Integer numberOfItems,Double netWeight,Double grossWeight,Double volume,// 
			String requesterUsername, String requesterFullName,
			String originName, String destinationName, String warehouseName, //
			TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName) {
		super(id);
		this.reference = reference;
		this.status = status;
		this.neededPickupDate = neededPickupDate;
		this.neededDeliveryDate = neededDeliveryDate;
		this.plannedPickupDate = plannedPickupDate;
		this.plannedDeliveryDate = plannedDeliveryDate;
		this.expectedPickupDate = expectedPickupDate;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.pickupDate = pickupDate;
		this.deliveryDate = deliveryDate;
		this.deliveryRequestId = deliveryRequestId;
		this.deliveryRequestReference = deliveryRequestReference;
		this.deliveryRequestSmsRef = deliveryRequestSmsRef;
		this.setDeliveryRequestType(deliveryRequestType);
		this.setNumberOfItems(numberOfItems);
		this.setNetWeight(netWeight);
		this.setGrossWeight(grossWeight);
		this.setVolume(volume);
		this.requesterUsername = requesterUsername;
		this.requesterFullName = requesterFullName;
		this.setOriginName(originName);
		this.setDestinationName(destinationName);
		this.setWarehouseName(warehouseName);

		this.setTransporterType(transporterType);
		this.setTransporterPrivateFirstName(transporterPrivateFirstName);
		this.setTransporterPrivateLastName(transporterPrivateLastName);
		this.setTransporterSupplierName(transporterSupplierName);
	}

	// c2
	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status, Date pickupDate, Date deliveryDate, //
			DeliveryRequestType deliveryRequestType, String originName, String destinationName, String warehouseName) {
		super(id);
		this.reference = reference;
		this.status = status;
		this.pickupDate = pickupDate;
		this.deliveryDate = deliveryDate;
		this.setDeliveryRequestType(deliveryRequestType);
		this.setOriginName(originName);
		this.setDestinationName(destinationName);
		this.setWarehouseName(warehouseName);
	}

	// select1
	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status, Integer deliveryRequestId, String deliveryRequestReference, DeliveryRequestType deliveryRequestType,
			String deliveryRequestSmsRef, String requesterUsername, String requesterFullName, //
			Date neededPickupDate, Date neededDeliveryDate, Date deliveryDate, String originName, String destinationName, String warehouseName, //
			TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName, //
			String approverFullName, Double cost, Double totalAppLinkCost, TransportationRequestPaymentStatus paymentStatus, String destinationProjectName) {
		super(id);
		this.reference = reference;
		this.status = status;

		this.setDeliveryRequestId(deliveryRequestId);
		this.deliveryRequestReference = deliveryRequestReference;
		this.setDeliveryRequestType(deliveryRequestType);
		this.deliveryRequestSmsRef = deliveryRequestSmsRef;
		this.requesterUsername = requesterUsername;
		this.requesterFullName = requesterFullName;

		this.neededPickupDate = neededPickupDate;
		this.neededDeliveryDate = neededDeliveryDate;
		this.deliveryDate = deliveryDate;
		this.setOriginName(originName);
		this.setDestinationName(destinationName);
		this.setWarehouseName(warehouseName);

		this.setTransporterType(transporterType);
		this.setTransporterPrivateFirstName(transporterPrivateFirstName);
		this.setTransporterPrivateLastName(transporterPrivateLastName);
		this.setTransporterSupplierName(transporterSupplierName);

		this.cost = cost;
		this.totalAppLinkCost = totalAppLinkCost;
		this.paymentStatus = paymentStatus;
		this.approverFullName = approverFullName;
		this.setDestinationProjectName(destinationProjectName);
	}

	// select3
	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status, String deliveryRequestReference, DeliveryRequestType deliveryRequestType,
			String deliveryRequestSmsRef, String requesterUsername, String requesterFullName //
			, Date neededPickupDate, Date neededDeliveryDate, String originName, String destinationName, String warehouseName, //
			TransporterType transporterType, String transporterPrivateFirstName, String transporterPrivateLastName, String transporterSupplierName, //
			Integer originId, Integer destinationId, Integer warehouseId) {
		super(id);
		this.reference = reference;
		this.status = status;

		this.deliveryRequestReference = deliveryRequestReference;
		this.setDeliveryRequestType(deliveryRequestType);
		this.deliveryRequestSmsRef = deliveryRequestSmsRef;
		this.requesterUsername = requesterUsername;
		this.requesterFullName = requesterFullName;

		this.neededPickupDate = neededPickupDate;
		this.neededDeliveryDate = neededDeliveryDate;
		this.setOriginName(originName);
		this.setDestinationName(destinationName);
		this.setWarehouseName(warehouseName);

		this.setTransporterType(transporterType);
		this.setTransporterPrivateFirstName(transporterPrivateFirstName);
		this.setTransporterPrivateLastName(transporterPrivateLastName);
		this.setTransporterSupplierName(transporterSupplierName);

		this.originId = originId;
		this.destinationId = destinationId;
		this.warehouseId = warehouseId;
	}

	public void calculateCost() {
		this.cost = this.startCost + this.itineraryCost + this.handlingCost;
	}

	@Transient
	public String getNumero() {
		return getIdStr();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public Double getDistance() {
		return ObjectUtils.firstNonNull(realDistance, estimatedDistance, 0.0);
	}

	@Transient
	public int getState() {
		switch (status) {
		case EDITED:
		case REQUESTED:
		case APPROVED:
		case ASSIGNED:
			return 0;
		case PICKEDUP:
			return 1;
		case DELIVERED:
		case ACKNOWLEDGED:
			return 2;
		default:
			return -1;
		}
	}

	@Transient
	public Double getRemainder() {
		try {
			return cost - totalAppLinkCost;
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public String getDestinationProjectName() {
		return deliveryRequest != null ? deliveryRequest.getDestinationProjectName() : null;
	}

	@Transient
	public void setDestinationProjectName(String destinationProjectName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDestinationProjectName(destinationProjectName);
	}

	@Transient
	public Boolean getExpectedToStart() {
		return pickupDate == null;
	}

	@Transient
	public Boolean getExpectedToEnd() {
		return deliveryDate == null;
	}

	@Transient
	public Date getStartDate() {
		return ObjectUtils.firstNonNull(pickupDate, expectedPickupDate, plannedPickupDate, neededPickupDate);
	}

	@Transient
	public String getStartDatePrefix() {
		if (pickupDate != null)
			return "";
		if (expectedPickupDate != null)
			return "Expected on ";
		if (plannedPickupDate != null)
			return "Planned on ";
		return "Needed on ";
	}

	@Transient
	public Integer getPickupDuration2() {
		if (pickupDuration != null)
			return pickupDuration;
		return plannedPickupDuration;
	}

	@Transient
	public Integer getDeliveryDuration2() {
		if (deliveryDuration != null)
			return deliveryDuration;
		return plannedDeliveryDuration;
	}

	@Transient
	public Date getEndDate() {
		return ObjectUtils.firstNonNull(deliveryDate, expectedDeliveryDate, plannedDeliveryDate, neededDeliveryDate);
	}

	@Transient
	public String getEndDatePrefix() {
		if (deliveryDate != null)
			return "";
		if (expectedDeliveryDate != null)
			return "Expected on ";
		if (plannedDeliveryDate != null)
			return "Planned on ";
		return "Needed on ";
	}

	@Transient
	public String getOriginName() {
		return deliveryRequest != null ? deliveryRequest.getOriginName() : null;
	}

	@Transient
	public void setOriginName(String originName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setOriginName(originName);
	}

	@Transient
	public String getDestinationName() {
		return deliveryRequest != null ? deliveryRequest.getDestinationName() : null;
	}

	@Transient
	public void setDestinationName(String destinationName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setDestinationName(destinationName);
	}

	@Transient
	public String getWarehouseName() {
		return deliveryRequest != null ? deliveryRequest.getWarehouseName() : null;
	}

	@Transient
	public void setWarehouseName(String warehouseName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setWarehouseName(warehouseName);
	}

	@Transient
	public String getOriginContactName() {
		try {
			return !deliveryRequest.getIsOutbound() ? deliveryRequest.getOrigin().getContact().getFullName() : deliveryRequest.getWarehouse().getFirstManager().getFullName();
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public String getDestinationContactName() {
		try {
			return !deliveryRequest.getIsInbound() ? deliveryRequest.getDestination().getContact().getFullName() : deliveryRequest.getWarehouse().getFirstManager().getFullName();
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public String getOriginContactPhone() {
		try {
			return !deliveryRequest.getIsOutbound() ? deliveryRequest.getOrigin().getContact().getPhone() : deliveryRequest.getWarehouse().getPhone();
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public String getDestinationContactPhone() {
		try {
			return !deliveryRequest.getIsInbound() ? deliveryRequest.getDestination().getContact().getPhone() : deliveryRequest.getWarehouse().getPhone();
		} catch (Exception e) {
			return null;
		}
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

	public void generateReference() {
		this.reference = "TR" + (deliveryRequest.getType().ordinal() + 1) + String.format("%05d", deliveryRequest.getReferenceNumber());
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void init() {
		if (deliveryRequest != null)
			deliveryRequestId = deliveryRequest.getId();
		if (vehicle != null)
			vehicleId = vehicle.getId();
		if (driver != null)
			driverUsername = driver.getUsername();
		if (contact1 != null)
			contact1Username = contact1.getUsername();
		if (contact2 != null)
			contact2Username = contact2.getUsername();
	}

	public void addHistory(TransportationRequestHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(TransportationRequestHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getDeliveryRequestReference(), getReference(), getOriginName(), getDestinationName(), transporterName);
	}

	@Transient
	public Boolean getIsInbound() {
		return DeliveryRequestType.INBOUND.equals(getDeliveryRequestType());
	}

	@Transient
	public Boolean getIsOutbound() {
		return DeliveryRequestType.OUTBOUND.equals(getDeliveryRequestType());
	}

	@Transient
	public Boolean getIsXbound() {
		return DeliveryRequestType.XBOUND.equals(getDeliveryRequestType());
	}

	@Transient
	public String getOriginValue() {
		if (deliveryRequest.getIsOutbound())
			return deliveryRequest.getWarehouse().getValue();
		else
			return deliveryRequest.getOrigin().getValue();
	}

	@Transient
	public GenericPlace getOriginPlace() {
		if (deliveryRequest.getIsOutbound())
			return deliveryRequest.getWarehouse();
		else
			return deliveryRequest.getOrigin();
	}

	@Transient
	public GenericPlace getDestinationPlace() {
		if (deliveryRequest.getIsInbound())
			return deliveryRequest.getWarehouse();
		else
			return deliveryRequest.getDestination();
	}

	@Transient
	public String getDestinationValue() {
		if (deliveryRequest.getIsInbound())
			return deliveryRequest.getWarehouse().getValue();
		else
			return deliveryRequest.getDestination() != null ? deliveryRequest.getDestination().getValue() : null;
	}

	public Date getNeededPickupDate() {
		return neededPickupDate;
	}

	public void setNeededPickupDate(Date neededPickupDate) {
		this.neededPickupDate = neededPickupDate;
	}

	public Date getNeededDeliveryDate() {
		return neededDeliveryDate;
	}

	public void setNeededDeliveryDate(Date neededDeliveryDate) {
		this.neededDeliveryDate = neededDeliveryDate;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(unique = true)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getContact1() {
		return contact1;
	}

	public void setContact1(User contact1) {
		this.contact1 = contact1;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getContact2() {
		return contact2;
	}

	public void setContact2(User contact2) {
		this.contact2 = contact2;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public Date getExpectedPickupDate() {
		return expectedPickupDate;
	}

	public void setExpectedPickupDate(Date expectedPickupDate) {
		this.expectedPickupDate = expectedPickupDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<TransportationRequestFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<TransportationRequestFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TransportationRequestHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<TransportationRequestHistory> historyList) {
		this.historyList = historyList;
	}

	@Transient
	public Integer getDeliveryRequestId() {
		if (deliveryRequestId != null)
			return deliveryRequestId;
		if (deliveryRequest != null)
			return deliveryRequest.getId();
		return null;
	}

	@Transient
	public void setDeliveryRequestId(Integer deliveryRequestId) {
		this.deliveryRequestId = deliveryRequestId;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public TransportationRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TransportationRequestStatus status) {
		this.status = status;
	}

	public Double getEstimatedDuration() {
		return estimatedDuration;
	}

	public void setEstimatedDuration(Double estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}

	public String getEstimatedDurationText() {
		return estimatedDurationText;
	}

	public void setEstimatedDurationText(String estimatedDurationText) {
		this.estimatedDurationText = estimatedDurationText;
	}

	public Double getEstimatedDistance() {
		return estimatedDistance;
	}

	public void setEstimatedDistance(Double estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
	}

	public String getEstimatedDistanceText() {
		return estimatedDistanceText;
	}

	public void setEstimatedDistanceText(String estimatedDistanceText) {
		this.estimatedDistanceText = estimatedDistanceText;
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

	public Date getDate9() {
		return date9;
	}

	public void setDate9(Date date9) {
		this.date9 = date9;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser6() {
		return user6;
	}

	public void setUser6(User user6) {
		this.user6 = user6;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser7() {
		return user7;
	}

	public void setUser7(User user7) {
		this.user7 = user7;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser8() {
		return user8;
	}

	public void setUser8(User user8) {
		this.user8 = user8;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser9() {
		return user9;
	}

	public void setUser9(User user9) {
		this.user9 = user9;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public TransportationJob getTransportationJob() {
		return transportationJob;
	}

	public void setTransportationJob(TransportationJob transportationJob) {
		this.transportationJob = transportationJob;
	}

	@Column(columnDefinition = "TEXT")
	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
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

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public Date getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Transient
	public Integer getOriginId() {
		return originId != null ? originId : deliveryRequest != null && deliveryRequest.getOrigin() != null ? deliveryRequest.getOrigin().getId() : null;
	}

	@Transient
	public Integer getDestinationId() {
		return destinationId != null ? destinationId : deliveryRequest != null && deliveryRequest.getDestination() != null ? deliveryRequest.getDestination().getId() : null;
	}

	@Transient
	public Integer getWarehouseId() {
		return warehouseId != null ? warehouseId : deliveryRequest != null && deliveryRequest.getWarehouse() != null ? deliveryRequest.getWarehouse().getId() : null;
	}

	@Override
	public int compareTo(TransportationRequest o) {
		if (getStartDate() == null)
			return -1;
		if (o.getStartDate() == null)
			return 1;
		return o.getStartDate().compareTo(getStartDate());
	}

	public Double getTotalAppLinkCost() {
		return totalAppLinkCost;
	}

	public void setTotalAppLinkCost(Double totalAppLinkCost) {
		this.totalAppLinkCost = totalAppLinkCost;
	}

	public Double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(Double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Enumerated(EnumType.STRING)
	public ContactType getContactType1() {
		return contactType1;
	}

	public void setContactType1(ContactType contactType1) {
		this.contactType1 = contactType1;
	}

	@Enumerated(EnumType.STRING)
	public ContactType getContactType2() {
		return contactType2;
	}

	public void setContactType2(ContactType contactType2) {
		this.contactType2 = contactType2;
	}

	@Transient
	public String getContact1Username() {
		return contact1Username;
	}

	@Transient
	public void setContact1Username(String contact1Username) {
		this.contact1Username = contact1Username;
	}

	@Transient
	public String getContact2Username() {
		return contact2Username;
	}

	@Transient
	public void setContact2Username(String contact2Username) {
		this.contact2Username = contact2Username;
	}

	@Enumerated(EnumType.STRING)
	public TransportationRequestPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(TransportationRequestPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Transient
	public Priority getPriority() {
		if (deliveryRequest != null)
			return deliveryRequest.getPriority();
		return null;
	}

	@Transient
	public Boolean getImportant() {
		if (deliveryRequest != null)
			return deliveryRequest.getImportant();
		return null;
	}

	@Transient
	public String getDeliveryRequestReference() {
		if (deliveryRequestReference != null)
			return deliveryRequestReference;
		if (deliveryRequest != null)
			return deliveryRequest.getReference();
		return null;
	}

	@Transient
	public DeliveryRequestType getDeliveryRequestType() {
		return deliveryRequest != null ? deliveryRequest.getType() : null;
	}

	@Transient
	public void setDeliveryRequestType(DeliveryRequestType deliveryRequestType) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setType(deliveryRequestType);
	}

	@Transient
	public String getProjectName() {
		if (deliveryRequest != null)
			return deliveryRequest.getProject().getName();
		return null;
	}

	@Transient
	public String getSmsRef() {
		if (deliveryRequest != null)
			return deliveryRequest.getSmsRef();
		return null;
	}

	@Transient
	public Integer getNumberOfItems() {
		return deliveryRequest != null ? deliveryRequest.getNumberOfItems() : null;
	}

	@Transient
	public void setNumberOfItems(Integer deliveryRequestNumberOfItems) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setNumberOfItems(deliveryRequestNumberOfItems);
	}

	@Transient
	public Double getNetWeight() {
		return deliveryRequest != null ? deliveryRequest.getNetWeight() : null;
	}

	@Transient
	public void setNetWeight(Double deliveryRequestNetWeight) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setNetWeight(deliveryRequestNetWeight);
	}

	@Transient
	public Double getGrossWeight() {
		return deliveryRequest != null ? deliveryRequest.getGrossWeight() : null;
	}

	@Transient
	public void setGrossWeight(Double deliveryRequestGrossWeight) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setGrossWeight(deliveryRequestGrossWeight);
	}

	@Transient
	public Double getVolume() {
		return deliveryRequest != null ? deliveryRequest.getVolume() : null;
	}

	@Transient
	public void setVolume(Double deliveryRequestVolume) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setVolume(deliveryRequestVolume);
	}

	@Transient
	public String getDriverName() {
		if (transportationJob != null)
			return transportationJob.getDriver().getFullName();
		return null;
	}

	@Transient
	public String getDriverFullName() {
		if (transportationJob != null)
			return transportationJob.getDriver().getFullName();
		return null;
	}

	@Transient
	public String getDriverPhone() {
		if (transportationJob != null)
			return transportationJob.getDriver().getPhone();
		return null;
	}

	@Transient
	public String getVehicleMatricule() {
		if (transportationJob != null)
			return transportationJob.getVehicle().getCorrectMatricule();
		return null;
	}

	@Transient
	public String getTransportationJobNumero() {
		if (transportationJob != null)
			return transportationJob.getIdStr();
		return null;
	}

	@Transient
	public TransportationJobStatus getTransportationJobStatus() {
		if (transportationJob != null)
			return transportationJob.getStatus();
		return null;
	}

	@Transient
	public String getApproverFullName() {
		return approverFullName;
	}

	@Transient
	public void setApproverFullName(String approverFullName) {
		this.approverFullName = approverFullName;
	}

	@Transient
	public String getDeliveryRequestSmsRef() {
		if (deliveryRequestSmsRef != null)
			return deliveryRequestSmsRef;
		if (deliveryRequest != null)
			return deliveryRequest.getSmsRef();
		return null;
	}

	@Transient
	public void setDeliveryRequestSmsRef(String deliveryRequestSmsRef) {
		this.deliveryRequestSmsRef = deliveryRequestSmsRef;
	}

	@Transient
	public void setDeliveryRequestReference(String deliveryRequestReference) {
		this.deliveryRequestReference = deliveryRequestReference;
	}

	@Transient
	public String getRequesterUsername() {
		if (requesterUsername != null)
			return requesterUsername;
		if (deliveryRequest != null && deliveryRequest.getRequester() != null)
			return deliveryRequest.getRequester().getUsername();
		return null;
	}

	@Transient
	public void setRequesterUsername(String requesterUsername) {
		this.requesterUsername = requesterUsername;
	}

	@Transient
	public String getRequesterPhoto() {
		return deliveryRequest != null ? deliveryRequest.getRequesterPhoto() : null;
	}

	@Transient
	public void setRequesterPhoto(String requesterPhoto) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setRequesterPhoto(requesterPhoto);
	}

	@Transient
	public String getRequesterFullName() {
		if (requesterFullName != null)
			return requesterFullName;
		if (deliveryRequest != null && deliveryRequest.getRequester() != null)
			return deliveryRequest.getRequester().getFullName();
		return null;
	}

	@Transient
	public void setRequesterFullName(String requesterFullName) {
		this.requesterFullName = requesterFullName;
	}

	public Date getPlannedPickupDate() {
		return plannedPickupDate;
	}

	public void setPlannedPickupDate(Date plannedPickupDate) {
		this.plannedPickupDate = plannedPickupDate;
	}

	public Date getPlannedDeliveryDate() {
		return plannedDeliveryDate;
	}

	public void setPlannedDeliveryDate(Date plannedDeliveryDate) {
		this.plannedDeliveryDate = plannedDeliveryDate;
	}

	@Override
	public String toString() {
		return reference;
	}

	public String getQrKey() {
		return qrKey;
	}

	public void setQrKey(String qrKey) {
		this.qrKey = qrKey;
	}

	@Transient
	public String getQrImageLink() {
		return App.QR.getLink() + "/img/tr/" + id + "/" + qrKey;
	}

	@Transient
	public String getQrLink() {
		return App.QR.getLink() + "/tr/" + id + "/" + qrKey;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryRequest", cascade = CascadeType.ALL)
	public List<Issue> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Issue> issueList) {
		this.issueList = issueList;
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

	public Double getRealDistance() {
		return realDistance;
	}

	public void setRealDistance(Double realDistance) {
		this.realDistance = realDistance;
	}

	public Double getStartDistance() {
		return startDistance;
	}

	public void setStartDistance(Double startDistance) {
		this.startDistance = startDistance;
	}

	public Integer getPlannedPickupDuration() {
		return plannedPickupDuration;
	}

	public void setPlannedPickupDuration(Integer plannedPickupDuration) {
		this.plannedPickupDuration = plannedPickupDuration;
	}

	public Integer getPlannedDeliveryDuration() {
		return plannedDeliveryDuration;
	}

	public void setPlannedDeliveryDuration(Integer plannedDeliveryDuration) {
		this.plannedDeliveryDuration = plannedDeliveryDuration;
	}

	public Integer getDeliveryDuration() {
		return deliveryDuration;
	}

	public void setDeliveryDuration(Integer deliveryDuration) {
		this.deliveryDuration = deliveryDuration;
	}

	public Integer getPickupDuration() {
		return pickupDuration;
	}

	public void setPickupDuration(Integer pickupDuration) {
		this.pickupDuration = pickupDuration;
	}

}
