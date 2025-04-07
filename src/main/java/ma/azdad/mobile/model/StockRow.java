package ma.azdad.mobile.model;

import ma.azdad.model.StockRowState;
import ma.azdad.model.StockRowStatus;
import ma.azdad.utils.Public;

public class StockRow {
	
	private Integer id;
	private String partNumberName;
	private String partNumberImage;
	private Location location;
	private String status;
	private String state;
	private String partNumberDescription;
	private Double quantity;
	private String projectName;
	private String warehouseName;
	private Double inboundQuantity;
	private Double outboundQuantity;
	private String dnReference;
	
	//cm1
	public StockRow(Integer id, String partNumberName, String partNumberImage, ma.azdad.model.Location location, StockRowStatus status,
			StockRowState state, String partNumberDescription, Double quantity, String projectName, String warehouseName,
			Double inboundQuantity, Double outboundQuantity, String dnReference) {
		super();
		this.id = id;
		this.partNumberName = partNumberName;
		this.partNumberImage = Public.getPublicUrl(partNumberImage);
		this.location = location != null
			    ? new Location(location.getId(), location.getName())
			    : null;
		this.status = status!=null ?status.getValue():"";
		this.state = state!=null ?state.getValue():"";
		this.partNumberDescription = partNumberDescription;
		this.quantity = quantity;
		this.projectName = projectName;
		this.warehouseName = warehouseName;
		this.inboundQuantity = inboundQuantity;
		this.outboundQuantity = outboundQuantity;
		this.dnReference = dnReference;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPartNumberDescription() {
		return partNumberDescription;
	}
	public void setPartNumberDescription(String partNumberDescription) {
		this.partNumberDescription = partNumberDescription;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public Double getInboundQuantity() {
		return inboundQuantity;
	}
	public void setInboundQuantity(Double inboundQuantity) {
		this.inboundQuantity = inboundQuantity;
	}
	public Double getOutboundQuantity() {
		return outboundQuantity;
	}
	public void setOutboundQuantity(Double outboundQuantity) {
		this.outboundQuantity = outboundQuantity;
	}
	public String getDnReference() {
		return dnReference;
	}
	public void setDnReference(String dnReference) {
		this.dnReference = dnReference;
	}
	
	
	

	

}
