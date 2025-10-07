package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class StockRowDetail extends GenericModel<Integer> {

	private Double quantity;
	private StockRow stockRow;
	private PackingDetail packingDetail;
	private ZoneHeight zoneHeight;

	// only in inbound
	private Double usedQuantity; // total outbound quantity from this detail

	// only in outbound
	private StockRow inboundStockRow;

	// TMP
	private Double tmpQuantity;
	private Boolean initial = false;

	public StockRowDetail() {
		super();
	}

	public StockRowDetail(Integer id, Double quantity, PackingDetail packingDetail, Double usedQuantity, //
			String partNumberName, String partNumberDescription, Integer deliveryRequestId, String deliveryRequestReference

	) {
		super(id);
		this.quantity = quantity;
		this.packingDetail = packingDetail;
		this.usedQuantity = usedQuantity;
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setDeliveryRequestId(deliveryRequestId);
		this.setDeliveryRequestReference(deliveryRequestReference);
	}

//	public StockRowDetail(Double quantity, PackingDetail packingDetail) {
//		super();
//		this.quantity = quantity;
//		this.packingDetail = packingDetail;
//	}

	// inbound constructor
	public StockRowDetail(Double quantity, Double tmpQuantity, Boolean initial, StockRow stockRow, PackingDetail packingDetail) {
		super();
		this.quantity = quantity;
		this.tmpQuantity = tmpQuantity;
		this.initial = initial;
		this.stockRow = stockRow;
		this.packingDetail = packingDetail;
		this.usedQuantity = 0.0;
	}

	// outbound constructor
	public StockRowDetail(Double quantity, StockRow stockRow, StockRow InboundStockRow, PackingDetail packingDetail, ZoneHeight zoneHeight) {
		super();
		this.quantity = quantity;
		this.stockRow = stockRow;
		this.inboundStockRow = InboundStockRow;
		this.packingDetail = packingDetail;
		this.zoneHeight = zoneHeight;
	}

	public boolean filter(String query) {
		return contains(query, getIdStr());
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public StockRow getStockRow() {
		return stockRow;
	}

	public void setStockRow(StockRow stockRow) {
		this.stockRow = stockRow;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public StockRow getInboundStockRow() {
		return inboundStockRow;
	}

	public void setInboundStockRow(StockRow inboundStockRow) {
		this.inboundStockRow = inboundStockRow;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PackingDetail getPackingDetail() {
		return packingDetail;
	}

	public void setPackingDetail(PackingDetail packingDetail) {
		this.packingDetail = packingDetail;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ZoneHeight getZoneHeight() {
		return zoneHeight;
	}

	public void setZoneHeight(ZoneHeight zoneHeight) {
		this.zoneHeight = zoneHeight;
	}

	@Transient
	public Double getTmpQuantity() {
		return tmpQuantity;
	}

	@Transient
	public void setTmpQuantity(Double tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	@Transient
	public Boolean getInitial() {
		return initial;
	}

	@Transient
	public void setInitial(Boolean initial) {
		this.initial = initial;
	}

	@Override
	public String toString() {
		return "{\"quantity\":\"" + quantity + "\", \"usedQuantity\":\"" + usedQuantity + "\", \"tmpQuantity\":\"" + tmpQuantity + "\", \"initial\":\"" + initial + "\"}";
	}

	@Transient
	public Integer getZoneHeightId() {
		return zoneHeight != null ? zoneHeight.getId() : null;
	}

	@Transient
	public void setZoneHeightId(Integer zoneHeightId) {
		if (zoneHeightId == null) {
			zoneHeight = null;
			return;
		}

		if (zoneHeight == null || !zoneHeightId.equals(zoneHeight.getId()))
			zoneHeight = new ZoneHeight();
		zoneHeight.setId(zoneHeightId);
	}

	public Double getUsedQuantity() {
		return usedQuantity;
	}

	public void setUsedQuantity(Double usedQuantity) {
		this.usedQuantity = usedQuantity;
	}

	@Transient
	public Double getRemainingQuantity() {
		return this.quantity - this.usedQuantity;
	}

	@Transient
	public String getPartNumberName() {
		return stockRow != null ? stockRow.getPartNumberName() : null;
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setPartNumberName(partNumberName);
	}

	@Transient
	public String getPartNumberDescription() {
		return stockRow != null ? stockRow.getPartNumberDescription() : null;
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setPartNumberDescription(partNumberDescription);
	}

	@Transient
	public String getPackingDetailName() {
		return packingDetail != null ? packingDetail.getName() : null;
	}

	@Transient
	public void setPackingDetailName(String packingDetailName) {
		if (packingDetail == null)
			packingDetail = new PackingDetail();
		packingDetail.setName(packingDetailName);
	}

	@Transient
	public String getPackingDetailType() {
		return packingDetail != null ? packingDetail.getType() : null;
	}

	@Transient
	public void setPackingDetailType(String packingDetailType) {
		if (packingDetail == null)
			packingDetail = new PackingDetail();
		packingDetail.setType(packingDetailType);
	}

	@Transient
	public String getDeliveryRequestReference() {
		return stockRow != null ? stockRow.getDeliveryRequestReference() : null;
	}

	@Transient
	public void setDeliveryRequestReference(String deliveryRequestReference) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliveryRequestReference(deliveryRequestReference);
	}

	@Transient
	public Integer getDeliveryRequestId() {
		return stockRow != null ? stockRow.getDeliveryRequestId() : null;
	}

	@Transient
	public void setDeliveryRequestId(Integer deliveryRequestId) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliveryRequestId(deliveryRequestId);
	}

}
