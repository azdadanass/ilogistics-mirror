package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestExpiryDate extends GenericBeanOld implements Serializable {

	private Double quantity;
	private Date expiryDate;
	private StockRow stockRow;

	// tmp
	private Double tmpQuantity;
	private Boolean initial = false;

	public DeliveryRequestExpiryDate() {
		super();
	}

	public DeliveryRequestExpiryDate(StockRow stockRow, Boolean initial) {
		super();
		Boolean isInbound = stockRow.getDeliveryRequest().getIsInbound();
		this.stockRow = stockRow;
		this.quantity = (isInbound ? 1 : -1) * stockRow.getQuantity();
		this.tmpQuantity = (isInbound ? 1 : -1) * stockRow.getQuantity();
		this.initial = initial;
	}

	public DeliveryRequestExpiryDate(StockRow stockRow, Boolean initial, Date expiryDate) {
		this(stockRow, initial);
		this.expiryDate = expiryDate;
	}

	public DeliveryRequestExpiryDate(StockRow stockRow, Double quantity, Boolean initial) {
		super();
		this.stockRow = stockRow;
		this.quantity = quantity;
		this.tmpQuantity = quantity;
		this.initial = initial;
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		//		if (!result && name != null)
		//			result = name.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public Integer getStockRowId() {
		if (stockRow == null)
			return null;
		else
			return stockRow.getId();
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Temporal(TemporalType.DATE)
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public StockRow getStockRow() {
		return stockRow;
	}

	public void setStockRow(StockRow stockRow) {
		this.stockRow = stockRow;
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
}
