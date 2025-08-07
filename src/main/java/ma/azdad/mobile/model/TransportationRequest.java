package ma.azdad.mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ma.azdad.model.Priority;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.mobile.model.User;

public class TransportationRequest {

	private Integer id;
	private String reference;
	private TransportationRequestStatus status ;
	private Priority priority;
	private Integer numberOfItems;
	private Double grossWeight;
	private Double volume;
	
	private String destinationOwner;
	private String destinationPhone;
	private String destinationAddress;
	private String destinationPhoto;
	private Double destinationLatitude = 33.966171;
	private Double destinationLongitude = -6.8678663;
	private String originOwner;
	private String originPhone;
	private String originAddress;
	private String originPhoto;
	private Double originLatitude = 33.966171;
	private Double originLongitude = -6.8678663;

	


	// USER
	private Date neededPickupDate;
	private Date neededDeliveryDate;
	private Date expectedPickupDate;
	private Date pickupDate;
	private Date expectedDeliveryDate;
	private Date deliveryDate;

	private String originName;
	private String destinationName;

	private User requester;

	// TIMELINE

	private Date date4; // Assigned
	private Date date5; // Picked up
	private Date date6; // Delivered
	private Date date7; // Acknowledged

	private User user4;
	private User user5;
	private User user6;
	private User user7;
	
	
	private List<ma.azdad.mobile.model.TransportationRequestFile> fileList = new ArrayList<>();
	private List<ma.azdad.mobile.model.TransportationRequestHistory> historyList = new ArrayList<>();

	public TransportationRequest() {
		super();
	}

	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status,
			Date neededPickupDate, Date neededDeliveryDate, Date expectedPickupDate, Date pickupDate,
			Date expectedDeliveryDate, Date deliveryDate, String originName, String destinationName) {
		super();
		this.id = id;
		this.reference = reference;
		this.status = status;
		this.neededPickupDate = neededPickupDate;
		this.neededDeliveryDate = neededDeliveryDate;
		this.expectedPickupDate = expectedPickupDate;
		this.pickupDate = pickupDate;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.deliveryDate = deliveryDate;
		this.originName = originName;
		this.destinationName = destinationName;
	}
	
	

	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status, Priority priority,
			 Integer numberOfItems, Double grossWeight, Double volume, Date neededPickupDate,
			Date neededDeliveryDate, Date expectedPickupDate, Date pickupDate, Date expectedDeliveryDate,
			Date deliveryDate) {
		super();
		this.id = id;
		this.reference = reference;
		this.status = status;
		this.priority = priority;
		this.numberOfItems = numberOfItems;
		this.grossWeight = grossWeight;
		this.volume = volume;
		this.neededPickupDate = neededPickupDate;
		this.neededDeliveryDate = neededDeliveryDate;
		this.expectedPickupDate = expectedPickupDate;
		this.pickupDate = pickupDate;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.deliveryDate = deliveryDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public TransportationRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TransportationRequestStatus status) {
		this.status = status;
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

	public Date getExpectedPickupDate() {
		return expectedPickupDate;
	}

	public void setExpectedPickupDate(Date expectedPickupDate) {
		this.expectedPickupDate = expectedPickupDate;
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

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	
	public Integer getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(Integer numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
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

	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	public User getUser6() {
		return user6;
	}

	public void setUser6(User user6) {
		this.user6 = user6;
	}

	public User getUser7() {
		return user7;
	}

	public void setUser7(User user7) {
		this.user7 = user7;
	}

	

	public String getDestinationOwner() {
		return destinationOwner;
	}

	public void setDestinationOwner(String destinationOwner) {
		this.destinationOwner = destinationOwner;
	}

	public String getDestinationPhone() {
		return destinationPhone;
	}

	public void setDestinationPhone(String destinationPhone) {
		this.destinationPhone = destinationPhone;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getDestinationPhoto() {
		return destinationPhoto;
	}

	public void setDestinationPhoto(String destinationPhoto) {
		this.destinationPhoto = destinationPhoto;
	}

	public Double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public Double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	
	public String getOriginOwner() {
		return originOwner;
	}

	public void setOriginOwner(String originOwner) {
		this.originOwner = originOwner;
	}

	public String getOriginPhone() {
		return originPhone;
	}

	public void setOriginPhone(String originPhone) {
		this.originPhone = originPhone;
	}

	public String getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}

	public String getOriginPhoto() {
		return originPhoto;
	}

	public void setOriginPhoto(String originPhoto) {
		this.originPhoto = originPhoto;
	}

	public Double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(Double originLatitude) {
		this.originLatitude = originLatitude;
	}

	public Double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(Double originLongitude) {
		this.originLongitude = originLongitude;
	}

	public List<ma.azdad.mobile.model.TransportationRequestFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ma.azdad.mobile.model.TransportationRequestFile> fileList) {
		this.fileList = fileList;
	}

	public List<ma.azdad.mobile.model.TransportationRequestHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<ma.azdad.mobile.model.TransportationRequestHistory> historyList) {
		this.historyList = historyList;
	}
	
	
	
	

	
}
