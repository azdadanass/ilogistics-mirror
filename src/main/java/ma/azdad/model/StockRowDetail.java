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

	// TMP
	private Double tmpQuantity;
	private Boolean initial = false;

	public StockRowDetail() {
		super();
	}

//	public StockRowDetail(Double quantity, PackingDetail packingDetail) {
//		super();
//		this.quantity = quantity;
//		this.packingDetail = packingDetail;
//	}

	public StockRowDetail(Double quantity, Double tmpQuantity, Boolean initial, StockRow stockRow, PackingDetail packingDetail) {
		super();
		this.quantity = quantity;
		this.tmpQuantity = tmpQuantity;
		this.initial = initial;
		this.stockRow = stockRow;
		this.packingDetail = packingDetail;
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
		return "{\"quantity\":\"" + quantity + "\", \"tmpQuantity\":\"" + tmpQuantity + "\", \"initial\":\"" + initial + "\"}";
	}

}
