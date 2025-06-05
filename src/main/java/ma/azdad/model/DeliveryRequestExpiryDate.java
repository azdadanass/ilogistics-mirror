package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity

public class DeliveryRequestExpiryDate extends GenericModel<Integer> implements Serializable {

	private Double quantity;
	private Date expiryDate;
	private StockRow stockRow;

	// tmp
	private Double tmpQuantity;
	private Boolean initial = false;

	public DeliveryRequestExpiryDate() {
		super();
	}
	
	

	public DeliveryRequestExpiryDate(StockRow stockRow) {
		super();
		this.stockRow = stockRow;
	}



	public DeliveryRequestExpiryDate(Double quantity, Date expiryDate) {
		super();
		this.quantity = quantity;
		this.expiryDate = expiryDate;
	}

	public DeliveryRequestExpiryDate(Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberImage, Double quantity, Date expiryDate) {
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberImage(partNumberImage);
		this.setQuantity(quantity);
		this.setExpiryDate(expiryDate);
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

	public DeliveryRequestExpiryDate(Double quantity, Date expiryDate, Integer deliveryRequestId, Integer inboundDeliveryRequestId) {
		this.quantity = quantity;
		this.expiryDate = expiryDate;
		this.setDeliveryRequestId(deliveryRequestId);
		this.setInboundDeliveryRequestId(inboundDeliveryRequestId);
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		// if (!result && name != null)
		// result = name.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public Integer getPartNumberId() {
		return stockRow != null ? stockRow.getPartNumberId() : null;
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setPartNumberId(partNumberId);
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
	public String getPartNumberImage() {
		return stockRow != null ? stockRow.getPartNumberImage() : null;
	}

	@Transient
	public void setPartNumberImage(String partNumberImage) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setPartNumberImage(partNumberImage);
	}

	@Transient
	public Integer getStockRowId() {
		if (stockRow == null)
			return null;
		else
			return stockRow.getId();
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

	@Transient
	public Integer getInboundDeliveryRequestId() {
		return stockRow != null ? stockRow.getInboundDeliveryRequestId() : null;
	}

	@Transient
	public void setInboundDeliveryRequestId(Integer inboundDeliveryRequestId) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setInboundDeliveryRequestId(inboundDeliveryRequestId);
	}

	@Transient
	public String getInboundDeliveryRequestReference() {
		return stockRow != null ? stockRow.getInboundDeliveryRequestReference() : null;
	}

	@Transient
	public void setInboundDeliveryRequestReference(String inboundDeliveryRequestReference) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DeliveryRequestExpiryDate [quantity=" + quantity + ", expiryDate=" + expiryDate + ", dnId=" + getDeliveryRequestId() + "]";
	}

}
