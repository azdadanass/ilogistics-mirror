package ma.azdad.mobile.model;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.StockRowStatus;
import ma.azdad.utils.Public;

public class DeliveryRequestDetail {
	private Integer id;
	private Double quantity = 0.0;
	private Boolean isSerialNumberRequired = false;
	private Boolean expirable = false;
	private Integer packingId;
	private String packingName;
	private Double packingQuantity = 0.0;
	private Integer deliveryRequestId;
	private String partNumberName;
	private String partNumberImage;
	private Double remainingQuantity;
    private Double tmpDeliveredQuantity;
    private Double tmpRemainingQuantity;

	
	//Extra Informations
	private String location;
	private String location2;
	private String status;
	private String partNumberDescription;
	private Boolean unit;
	private String unitType;
	private String brandName;
	private String industryName;
	private String categoryName;
	private String typeName;
	private Double grossWeight;
	private Double volume;
	private String status2;
	

	

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
	public DeliveryRequestDetail(Integer id, Double quantity, Boolean isSerialNumberRequired,Boolean expirable,
			String packingName,Integer packingId, Double packingQuantity, String partNumberName, String partNumberImage, Double remainingQuantity
			,String location,StockRowStatus status,String partNumberDescription, Boolean unit, String unitType, String brandName,
			String industryName, String categoryName, String typeName, Double grossWeight, Double volume,StockRowStatus status2,String location2) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.location = location;
		this.location2 = location2;
		this.status = status!=null ?status.getValue():"";
		this.isSerialNumberRequired = isSerialNumberRequired;
		this.expirable = expirable;
		this.packingName = packingName;
		this.packingId =packingId;
		this.packingQuantity = remainingQuantity;
		this.partNumberName = partNumberName;
		this.partNumberImage = Public.getPublicUrl(partNumberImage);
		this.remainingQuantity = remainingQuantity;
		this.tmpQuantity = remainingQuantity;
		this.partNumberDescription = partNumberDescription;
		this.unit = unit;
		this.unitType = unitType;
		this.brandName = brandName;
		this.industryName = industryName;
		this.categoryName = categoryName;
		this.typeName = typeName;
		this.grossWeight = grossWeight;
		this.volume = volume;
		this.status2 = status2!=null ?status2.getValue():"";
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


	public String getStatus2() {
		return status2;
	}


	public void setStatus2(String status2) {
		this.status2 = status2;
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
	
	

	public Integer getPackingId() {
		return packingId;
	}


	public void setPackingId(Integer packingId) {
		this.packingId = packingId;
	}
	
	

	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Boolean getExpirable() {
		return expirable;
	}


	public void setExpirable(Boolean expirable) {
		this.expirable = expirable;
	}
	
	
	public String getPartNumberDescription() {
		return partNumberDescription;
	}


	public void setPartNumberDescription(String partNumberDescription) {
		this.partNumberDescription = partNumberDescription;
	}


	public Boolean getUnit() {
		return unit;
	}


	public void setUnit(Boolean unit) {
		this.unit = unit;
	}


	public String getUnitType() {
		return unitType;
	}


	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}


	public String getBrandName() {
		return brandName;
	}


	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getIndustryName() {
		return industryName;
	}


	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	
	
	public String getLocation2() {
		return location2;
	}


	public void setLocation2(String location2) {
		this.location2 = location2;
	}
	
	
	public Double getTmpDeliveredQuantity() {
		return tmpDeliveredQuantity;
	}


	public void setTmpDeliveredQuantity(Double tmpDeliveredQuantity) {
		this.tmpDeliveredQuantity = tmpDeliveredQuantity;
	}


	public Double getTmpRemainingQuantity() {
		return tmpRemainingQuantity;
	}


	public void setTmpRemainingQuantity(Double tmpRemainingQuantity) {
		this.tmpRemainingQuantity = tmpRemainingQuantity;
	}


	@Override
	public String toString() {
		return "JobRequestDeliveryDetail [id=" + id + ", quantity=" + quantity ;
	}

}
