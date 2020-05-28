package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestSerialNumber extends GenericBean implements Serializable {

	private Integer packingNumero;
	private String serialNumber;
	private String box;
	private PackingDetail packingDetail;
	private StockRow inboundStockRow;
	private DeliveryRequest outboundDeliveryRequest;

	// tmp
	private PartNumber tmpPartNumber;
	private DeliveryRequest tmpInboundDeliveryRequest;
	private Location tmpLocation;
	private StockRowStatus tmpStockRowStatus;

	public void init() {
		if (inboundStockRow != null) {
			tmpPartNumber = inboundStockRow.getPartNumber();
			tmpInboundDeliveryRequest = inboundStockRow.getDeliveryRequest();
		}
			
	}

	public DeliveryRequestSerialNumber() {
		super();
	}

	public DeliveryRequestSerialNumber(Integer packingNumero, PackingDetail packingDetail, StockRow inboundStockRow) {
		super();
		this.packingNumero = packingNumero;
		this.packingDetail = packingDetail;
		this.inboundStockRow = inboundStockRow;
		this.tmpPartNumber = inboundStockRow.getPartNumber();
	}

	public DeliveryRequestSerialNumber(Integer packingNumero, PackingDetail packingDetail, DeliveryRequest outboundDeliveryRequest, StockRow outboundStockRow) {
		super();
		this.packingNumero = packingNumero;
		this.packingDetail = packingDetail;
		this.outboundDeliveryRequest = outboundDeliveryRequest; // = outboundStockRow.getDeliveryRequest
		this.tmpPartNumber = packingDetail.getParent().getPartNumber();
		this.tmpInboundDeliveryRequest = outboundStockRow.getInboundDeliveryRequest();
		this.tmpLocation = outboundStockRow.getLocation();
		this.tmpStockRowStatus = outboundStockRow.getStatus();
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && serialNumber != null)
			result = serialNumber.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public Integer getTmpPartNumberId() {
		if (tmpPartNumber == null)
			return null;
		return tmpPartNumber.getId();
	}

	@Transient
	public Integer getInboundStockRowId() {
		if (inboundStockRow == null)
			return null;
		return inboundStockRow.getId();
	}

	@Transient
	public Integer getNotNullInboundStockRowId() {
		if (inboundStockRow == null || inboundStockRow.getId() == null)
			return 0;
		return inboundStockRow.getId();
	}

	@Transient
	public Integer getNotNullId() {
		if (id == null)
			return 0;
		return id;
	}

	@Transient
	public String getKey1() {
		return inboundStockRow.getKey() + ";" + packingDetail.getId();
	}

	@Column(columnDefinition = "TEXT")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PackingDetail getPackingDetail() {
		return packingDetail;
	}

	public void setPackingDetail(PackingDetail packingDetail) {
		this.packingDetail = packingDetail;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public StockRow getInboundStockRow() {
		return inboundStockRow;
	}

	public void setInboundStockRow(StockRow inboundStockRow) {
		this.inboundStockRow = inboundStockRow;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public DeliveryRequest getOutboundDeliveryRequest() {
		return outboundDeliveryRequest;
	}

	public void setOutboundDeliveryRequest(DeliveryRequest outboundDeliveryRequest) {
		this.outboundDeliveryRequest = outboundDeliveryRequest;
	}

	public Integer getPackingNumero() {
		return packingNumero;
	}

	public void setPackingNumero(Integer packingNumero) {
		this.packingNumero = packingNumero;
	}

	@Transient
	public PartNumber getTmpPartNumber() {
		return tmpPartNumber;
	}

	@Transient
	public void setTmpPartNumber(PartNumber tmpPartNumber) {
		this.tmpPartNumber = tmpPartNumber;
	}

	@Transient
	public DeliveryRequest getTmpInboundDeliveryRequest() {
		return tmpInboundDeliveryRequest;
	}

	@Transient
	public void setTmpInboundDeliveryRequest(DeliveryRequest tmpInboundDeliveryRequest) {
		this.tmpInboundDeliveryRequest = tmpInboundDeliveryRequest;
	}

	@Transient
	public Location getTmpLocation() {
		return tmpLocation;
	}

	@Transient
	public StockRowStatus getTmpStockRowStatus() {
		return tmpStockRowStatus;
	}

}
