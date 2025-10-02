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

}
