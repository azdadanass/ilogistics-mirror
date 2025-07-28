package ma.azdad.mobile.model;

import java.util.Date;

import ma.azdad.model.TransportationRequestStatus;


public class TransportationRequest {
	
	private Integer id;
	private String reference;
	private TransportationRequestStatus status = TransportationRequestStatus.EDITED;

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
	
	

	public TransportationRequest() {
		super();
	}

	public TransportationRequest(Integer id, String reference, TransportationRequestStatus status,
			Date neededPickupDate, Date neededDeliveryDate, Date expectedPickupDate, Date pickupDate,
			Date expectedDeliveryDate, Date deliveryDate, String originName, String destinationName, User requester) {
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
		this.requester = requester;
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
	
	
	




}
