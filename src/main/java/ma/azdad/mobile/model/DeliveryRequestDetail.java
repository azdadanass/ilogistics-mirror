package ma.azdad.mobile.model;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.utils.Public;

public class DeliveryRequestDetail {
	private Integer id;
	private Double quantity = 0.0;
	private Boolean isSerialNumberRequired = false;
	private String packingName;
	private Double packingQuantity = 0.0;
	private Integer deliveryRequestId;
	private String partNumberName;
	private String partNumberImage;
	private Double remainingQuantity;

	// TMP
	private Double tmpQuantity;
	
	public DeliveryRequestDetail() {
    }


	// c2
	public DeliveryRequestDetail(Integer id, Double quantity, Boolean isSerialNumberRequired,
			String packingName, Double packingQuantity, String partNumberName, String partNumberImage) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.packingName = packingName;
		this.packingQuantity = packingQuantity;
		this.partNumberName = partNumberName;
		this.partNumberImage = Public.getPublicUrl(partNumberImage);
	}
	
	// c1
	public DeliveryRequestDetail(Integer id, Double quantity, Boolean isSerialNumberRequired,
			String packingName, Double packingQuantity, String partNumberName, String partNumberImage, Double remainingQuantity) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.packingName = packingName;
		this.packingQuantity = packingQuantity;
		this.partNumberName = partNumberName;
		this.partNumberImage = Public.getPublicUrl(partNumberImage);
		this.remainingQuantity = remainingQuantity;
		this.tmpQuantity = remainingQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}


	public Boolean getIsSerialNumberRequired() {
		return isSerialNumberRequired;
	}

	public void setIsSerialNumberRequired(Boolean isSerialNumberRequired) {
		this.isSerialNumberRequired = isSerialNumberRequired;
	}


	public Integer getDeliveryRequestId() {
		return deliveryRequestId;
	}

	public void setDeliveryRequestId(Integer deliveryRequestId) {
		this.deliveryRequestId = deliveryRequestId;
	}


	public String getPartNumberName() {
		return partNumberName;
	}

	public void setPartNumberName(String partNumberName) {
		this.partNumberName = partNumberName;
	}


	public String getPartNumberImage() {
		return partNumberImage;
	}

	public void setPartNumberImage(String partNumberImage) {
		this.partNumberImage = partNumberImage;
	}

	public String getPackingName() {
		return packingName;
	}

	public void setPackingName(String packingName) {
		this.packingName = packingName;
	}

	public Double getPackingQuantity() {
		return packingQuantity;
	}

	public void setPackingQuantity(Double packingQuantity) {
		this.packingQuantity = packingQuantity;
	}
	
	

	public Double getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(Double remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public Double getTmpQuantity() {
		return tmpQuantity;
	}

	public void setTmpQuantity(Double tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	@Override
	public String toString() {
		return "JobRequestDeliveryDetail [id=" + id + ", quantity=" + quantity ;
	}

}
