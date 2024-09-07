package ma.azdad.mobile.model;

import java.util.Date;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;

public class DeliveryRequest {

	private Integer id;
	private String reference;
	private DeliveryRequestType type;
	private Date neededDeliveryDate;
	private InboundType inboundType;
	private DeliveryRequestStatus status;
	private Boolean isForReturn = false;
	private Boolean isForTransfer = false;

	private String requesterFullName;
	private Integer projectId;
	private String projectName;
	private Integer destinationProjectId;
	private String destinationProjectName;
	private Integer warehouseId;
	private String warehouseName;
	private Integer destinationId;
	private String destinationName;
	private Integer originId;
	private String originName;

	public DeliveryRequest() {
		super();
	}

	public DeliveryRequest(Integer id, String reference, DeliveryRequestType type, Date neededDeliveryDate, InboundType inboundType, DeliveryRequestStatus status,
			Boolean isForReturn, Boolean isForTransfer, String requesterFullName, Integer projectId, String projectName, Integer destinationProjectId,
			String destinationProjectName, Integer warehouseId, String warehouseName, Integer destinationId, String destinationName, Integer originId, String originName) {
		super();
		this.id = id;
		this.reference = reference;
		this.type = type;
		this.neededDeliveryDate = neededDeliveryDate;
		this.inboundType = inboundType;
		this.status = status;
		this.isForReturn = isForReturn;
		this.isForTransfer = isForTransfer;
		this.requesterFullName = requesterFullName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.destinationProjectId = destinationProjectId;
		this.destinationProjectName = destinationProjectName;
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.destinationId = destinationId;
		this.destinationName = destinationName;
		this.originId = originId;
		this.originName = originName;
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

	public DeliveryRequestType getType() {
		return type;
	}

	public void setType(DeliveryRequestType type) {
		this.type = type;
	}

	public Date getNeededDeliveryDate() {
		return neededDeliveryDate;
	}

	public void setNeededDeliveryDate(Date neededDeliveryDate) {
		this.neededDeliveryDate = neededDeliveryDate;
	}

	public InboundType getInboundType() {
		return inboundType;
	}

	public void setInboundType(InboundType inboundType) {
		this.inboundType = inboundType;
	}

	public DeliveryRequestStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryRequestStatus status) {
		this.status = status;
	}

	public Boolean getIsForReturn() {
		return isForReturn;
	}

	public void setIsForReturn(Boolean isForReturn) {
		this.isForReturn = isForReturn;
	}

	public Boolean getIsForTransfer() {
		return isForTransfer;
	}

	public void setIsForTransfer(Boolean isForTransfer) {
		this.isForTransfer = isForTransfer;
	}

	public String getRequesterFullName() {
		return requesterFullName;
	}

	public void setRequesterFullName(String requesterFullName) {
		this.requesterFullName = requesterFullName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getDestinationProjectId() {
		return destinationProjectId;
	}

	public void setDestinationProjectId(Integer destinationProjectId) {
		this.destinationProjectId = destinationProjectId;
	}

	public String getDestinationProjectName() {
		return destinationProjectName;
	}

	public void setDestinationProjectName(String destinationProjectName) {
		this.destinationProjectName = destinationProjectName;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public Integer getOriginId() {
		return originId;
	}

	public void setOriginId(Integer originId) {
		this.originId = originId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

}
