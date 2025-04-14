package ma.azdad.mobile.model;

import java.util.Date;

import ma.azdad.utils.Public;

public class DeliveryRequestExpiryDate {
	
	private Integer id;
	private Integer stockRowId;
	private String partNumberImage;
	private String partNumberName;
	private String location;
	private Double quantity;
	private Date expiryDate;
	private String status;
	
	
	
	public DeliveryRequestExpiryDate() {
		super();
	}


	public DeliveryRequestExpiryDate(Integer id,Integer stockRowId, String partNumberImage, String partNumberName, String location,
			Double quantity, Date expiryDate,String status) {
		super();
		this.id = id;
		this.stockRowId = stockRowId;
		this.partNumberImage = Public.getPublicUrl(partNumberImage);
		this.partNumberName = partNumberName;
		this.location = location;
		this.quantity = quantity;
		this.expiryDate = expiryDate;
		this.status= status;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPartNumberImage() {
		return partNumberImage;
	}
	public void setPartNumberImage(String partNumberImage) {
		this.partNumberImage = partNumberImage;
	}
	public String getPartNumberName() {
		return partNumberName;
	}
	public void setPartNumberName(String partNumberName) {
		this.partNumberName = partNumberName;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getStockRowId() {
		return stockRowId;
	}


	public void setStockRowId(Integer stockRowId) {
		this.stockRowId = stockRowId;
	}
	
	
	
	
	
	

}
