package ma.azdad.mobile.model;

import ma.azdad.utils.Public;

public class DeliveryRequestSerialNumber {
	
	private Integer id;
	private String packingDetail;
	private Integer packingNumero;
	private String status;
	private String location;
	private String partNumber;
	private String serialNumber;
	private String partNumberImage;
	private String snType;
	private Double longitude;
	private Double latitude;
	private String entryMode;
	
	public DeliveryRequestSerialNumber() {
		super();
	}

	
	
	public DeliveryRequestSerialNumber(Integer id, String packingDetail, String partNumber, String serialNumber,String image,String snType,
			Integer packingNumero,String status,String location,Double longitude,Double latitude,String entryMode) {
		super();
		this.id = id;
		this.packingDetail = packingDetail;
		this.partNumber = partNumber;
		this.serialNumber = serialNumber;
		this.partNumberImage = Public.getPublicUrl(image);
		this.snType = snType;
		this.packingNumero = packingNumero;
		this.status = status;
		this.location = location;
		this.longitude = longitude;
		this.latitude = latitude;
		this.entryMode = entryMode;

	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPackingDetail() {
		return packingDetail;
	}
	public void setPackingDetail(String packingDetail) {
		this.packingDetail = packingDetail;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getPartNumberImage() {
		return partNumberImage;
	}

	public void setPartNumberImage(String partNumberImage) {
		this.partNumberImage = partNumberImage;
	}


	public String getSnType() {
		return snType;
	}


	public void setSnType(String snType) {
		this.snType = snType;
	}


	public Integer getPackingNumero() {
		return packingNumero;
	}


	public void setPackingNumero(Integer packingNumero) {
		this.packingNumero = packingNumero;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}



	public Double getLongitude() {
		return longitude;
	}



	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}



	public Double getLatitude() {
		return latitude;
	}



	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}



	public String getEntryMode() {
		return entryMode;
	}



	public void setEntryMode(String entryMode) {
		this.entryMode = entryMode;
	}
	
	
	
	
	
	
	
	
	

}
