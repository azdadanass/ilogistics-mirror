package ma.azdad.mobile.model;

import java.util.List;

import ma.azdad.model.StockRowState;
import ma.azdad.model.StockRowStatus;

public class HardwareStatusData {
	
	private Integer id;
	private String partNumber;
	private Double quantity;
	private String status;
	private String packing;
	private Double packingQty;
	private Boolean isAdded;
	private String location;
	
	
	public HardwareStatusData(Integer id, String partNumber, Double quantity, String packing,
			Double packingQty) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.quantity = quantity;
		this.packing = packing;
		this.packingQty = packingQty;
	
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public Double getPackingQty() {
		return packingQty;
	}
	public void setPackingQty(Double packingQty) {
		this.packingQty = packingQty;
	}
	public Boolean getIsAdded() {
		return isAdded;
	}
	public void setIsAdded(Boolean isAdded) {
		this.isAdded = isAdded;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	

}
