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
	private Integer packingId;
	private Integer detailId;
	private Double packingQty;
	private Boolean isAdded;
	private Location location;
	
	
	public HardwareStatusData(Integer id, String partNumber, Double quantity, String packing,
			Double packingQty,Integer packingId,Integer detailId) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.quantity = quantity;
		this.packing = packing;
		this.packingId = packingId;
		this.detailId = detailId;
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
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Integer getPackingId() {
		return packingId;
	}
	public void setPackingId(Integer packingId) {
		this.packingId = packingId;
	}
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	
	
	
	
	
	
	

}
