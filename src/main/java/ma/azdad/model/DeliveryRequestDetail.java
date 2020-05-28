package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestDetail extends GenericBean implements Serializable {

	private Double quantity = 0.0;
	private Double unitCost = 0.0;
	private Double unitPrice = 0.0;

	private PartNumber partNumber;
	private DeliveryRequest deliveryRequest;

	// outbound case
	private StockRowStatus status;
	private String originNumber;
	private DeliveryRequest inboundDeliveryRequest;
	private Packing packing;

	private Double remainingQuantity;

	// TMP
	private Double tmpQuantity;
	private String tmpPartNumberName;
	private String tmpPartNumberDescription;
	private Integer tmpDeliveryRequestId;
	private DeliveryRequestType tmpDeliveryRequestType;
	private Integer tmpDeliveryRequestTypeReferenceNumber;
	private String tmpDeliveryRequestReference;
	private Double tmpUsedQuantity;
	private String tmpDeliverToFullName;
	private Date tmpDeliveryRequestDeliveryDate;
	private String tmpProjectName;

	public DeliveryRequestDetail() {
		super();
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.packing = packing;
	}

	public DeliveryRequestDetail(Integer id, String tmpPartNumberName, String tmpPartNumberDescription, Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType, Integer tmpDeliveryRequestTypeReferenceNumber, Double unitCost, Date tmpDeliveryRequestDeliveryDate, String tmpProjectName) {
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestTypeReferenceNumber = tmpDeliveryRequestTypeReferenceNumber;
		this.tmpDeliveryRequestReference = "DN" + (tmpDeliveryRequestType.ordinal() + 1) + String.format("%05d", tmpDeliveryRequestTypeReferenceNumber);
		this.unitCost = unitCost;
		this.tmpDeliveryRequestDeliveryDate = tmpDeliveryRequestDeliveryDate;
		this.tmpProjectName = tmpProjectName;
	}

	public DeliveryRequestDetail(Integer id, String tmpPartNumberName, String tmpPartNumberDescription, Integer tmpDeliveryRequestId, DeliveryRequestType tmpDeliveryRequestType, Integer tmpDeliveryRequestTypeReferenceNumber, Double quantity, Double tmpUsedQuantity, String toUserFullName) {
		super(id);
		this.tmpPartNumberName = tmpPartNumberName;
		this.tmpPartNumberDescription = tmpPartNumberDescription;
		this.tmpDeliveryRequestId = tmpDeliveryRequestId;
		this.tmpDeliveryRequestType = tmpDeliveryRequestType;
		this.tmpDeliveryRequestTypeReferenceNumber = tmpDeliveryRequestTypeReferenceNumber;
		this.tmpDeliveryRequestReference = "DN" + (tmpDeliveryRequestType.ordinal() + 1) + String.format("%05d", tmpDeliveryRequestTypeReferenceNumber);
		this.quantity = quantity;
		this.tmpUsedQuantity = tmpUsedQuantity;
		this.tmpDeliverToFullName = toUserFullName;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest, StockRowStatus status, String originNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Packing packing) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
		this.status = status;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost, Packing packing) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
		this.packing = packing;
	}

	public DeliveryRequestDetail(Double remainingQuantity, StockRowStatus status, String originNumber, PartNumber partNumber, DeliveryRequest inboundDeliveryRequest, Double unitCost) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.status = status;
		this.originNumber = originNumber;
		this.partNumber = partNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
		this.unitCost = unitCost;
	}

	public DeliveryRequestDetail(Double remainingQuantity, PartNumber partNumber, Double unitCost) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
		this.unitCost = unitCost;
	}

	public DeliveryRequestDetail(Double remainingQuantity, PartNumber partNumber, Double unitCost, Double unitPrice) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
	}

	public DeliveryRequestDetail(Double remainingQuantity, PartNumber partNumber, Double unitCost, Double unitPrice, Packing packing) {
		super();
		this.remainingQuantity = remainingQuantity;
		this.partNumber = partNumber;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
		this.packing = packing;
	}

	@Override // if id == null compare by key
	public boolean equals(Object obj) {
		if (id != null)
			return super.equals(obj);
		try {
			return getKey().equals(((DeliveryRequestDetail) obj).getKey());
		} catch (Exception e) {
			return false;
		}
	}

	@Transient
	public Integer getDeliveryRequestId() {
		if (deliveryRequest == null)
			return null;
		return deliveryRequest.getId();
	}

	@Transient
	public String getPartNumberName() {
		if (partNumber == null)
			return null;
		return partNumber.getName();
	}

	@Transient
	public String getPartNumberDescription() {
		if (partNumber == null)
			return null;
		return partNumber.getDescription();
	}

	public void init() {
	}

	public DeliveryRequestDetail(DeliveryRequest deliveryRequest) {
		super();
		this.deliveryRequest = deliveryRequest;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, DeliveryRequest deliveryRequest) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.deliveryRequest = deliveryRequest;
	}

	public DeliveryRequestDetail(Double quantity, PartNumber partNumber, StockRowStatus status, String originNumber, DeliveryRequest inboundDeliveryRequest) {
		super();
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.status = status;
		this.originNumber = originNumber;
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	@Transient
	public Double getNetWeight() {
		if (partNumber != null)
			return quantity * partNumber.getNetWeight();
		return 0.0;
	}

	@Transient
	public Double getGrossWeight() {
		if (packing != null)
			return (quantity / packing.getQuantity()) * packing.getGrossWeight();
		return 0.0;
	}

	@Transient
	public Double getVolume() {
		if (packing != null)
			return (quantity / packing.getQuantity()) * packing.getVolume();
		return 0.0;
	}

	@Transient
	public Double getTotalCost() {
		try {
			return quantity * unitCost;
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Transient
	public Double getTotalPrice() {
		try {
			return quantity * unitPrice;
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Transient
	public String getKey() {
		return StockRow.getKey(status, originNumber, partNumber.getId(), inboundDeliveryRequest);
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && originNumber != null)
			result = originNumber.toLowerCase().contains(query);
		if (!result && inboundDeliveryRequest != null)
			result = inboundDeliveryRequest.getReference().toLowerCase().contains(query);
		if (!result && partNumber != null)
			result = partNumber.filter(query);
		if (!result && tmpPartNumberName != null)
			result = tmpPartNumberName.toLowerCase().contains(query);
		if (!result && tmpPartNumberDescription != null)
			result = tmpPartNumberDescription.toLowerCase().contains(query);
		if (!result && tmpDeliveryRequestReference != null)
			result = tmpDeliveryRequestReference.toLowerCase().contains(query);
		if (!result && tmpProjectName != null)
			result = tmpProjectName.toLowerCase().contains(query);

		return result;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	public Double getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(Double remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	@Transient
	public Double getTmpQuantity() {
		return tmpQuantity;
	}

	@Transient
	public void setTmpQuantity(Double tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	public StockRowStatus getStatus() {
		return status;
	}

	public void setStatus(StockRowStatus status) {
		this.status = status;
	}

	public String getOriginNumber() {
		return originNumber;
	}

	public void setOriginNumber(String originNumber) {
		this.originNumber = originNumber;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public DeliveryRequest getInboundDeliveryRequest() {
		return inboundDeliveryRequest;
	}

	public void setInboundDeliveryRequest(DeliveryRequest inboundDeliveryRequest) {
		this.inboundDeliveryRequest = inboundDeliveryRequest;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Transient
	public String getTmpPartNumberName() {
		return tmpPartNumberName;
	}

	@Transient
	public String getTmpPartNumberDescription() {
		return tmpPartNumberDescription;
	}

	@Transient
	public Integer getTmpDeliveryRequestId() {
		return tmpDeliveryRequestId;
	}

	@Transient
	public DeliveryRequestType getTmpDeliveryRequestType() {
		return tmpDeliveryRequestType;
	}

	@Transient
	public Integer getTmpDeliveryRequestTypeReferenceNumber() {
		return tmpDeliveryRequestTypeReferenceNumber;
	}

	@Transient
	public String getTmpDeliveryRequestReference() {
		return tmpDeliveryRequestReference;
	}

	@Transient
	public Double getTmpUsedQuantity() {
		return tmpUsedQuantity;
	}

	@Transient
	public String getTmpDeliverToFullName() {
		return tmpDeliverToFullName;
	}

	@Transient
	public Date getTmpDeliveryRequestDeliveryDate() {
		return tmpDeliveryRequestDeliveryDate;
	}

	@Transient
	public String getTmpProjectName() {
		return tmpProjectName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	@Override
	public String toString() {
		return "DRD [quantity=" + quantity + ", partNumber=" + partNumber.getName() + ", unitCost=" + unitCost + "]\n";
	}

}
