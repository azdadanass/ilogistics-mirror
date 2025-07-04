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

import ma.azdad.service.UtilsFunctions;

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
	
	
	// c1
	public DeliveryRequestExpiryDate(Double quantity, Date expiryDate,//
			String partNumberName, String partNumberDescription,String partNumberBrandName,String projectName,//
			String deliveryRequestReference,Date deliveryRequestDate4, //
			CompanyType deliverToCompanyType,String deliverToCompanyName,String deliverToCustomerName,String deliverToSupplierName,String deliverToOther,//
			String destinationName,String destinationProjectCustomerName,String destinationProjectName,String endCustomerName, //
			String poNumero,String  toUserFullName,String warehouseName
			) {
		this.quantity = quantity;
		this.expiryDate = expiryDate;
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setProjectName(projectName);
		this.setDeliveryRequestReference(deliveryRequestReference);
		this.setDeliveryRequestDate4(deliveryRequestDate4);
		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setDeliverToOther(deliverToOther);
		this.setDestinationName(destinationName);
		this.setDestinationProjectCustomerName(destinationProjectCustomerName);
		this.setDestinationProjectName(destinationProjectName);
		this.setEndCustomerName(endCustomerName);
		this.setPoNumero(poNumero);
		this.setToUserFullName(toUserFullName);
		this.setWarehouseName(warehouseName);
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
	public String getDeliveryRequestReference(){
		return stockRow!=null?stockRow.getDeliveryRequestReference():null;
	}

	@Transient
	public void setDeliveryRequestReference(String deliveryRequestReference){
		if(stockRow==null)
			stockRow=new StockRow();
		stockRow.setDeliveryRequestReference(deliveryRequestReference);
	}
	
	@Transient
	public String getProjectName() {
		if (stockRow == null)
			return null;
		return stockRow.getProjectName();
	}

	@Transient
	public void setProjectName(String ProjectName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setProjectName(ProjectName);
	}
	
	@Transient
	public String getDeliverTo() {
		if (getDeliverToCompanyType() == null)
			return null;
		return getDeliverToCompanyType().getValue() + " / " + getDeliverToEntityName();
	}
	
	@Transient
	public String getDeliverToEntityName() {
		if (getDeliverToCompanyType() == null)
			return null;
		switch (getDeliverToCompanyType()) {
		case COMPANY:
			return getDeliverToCompanyName();
		case CUSTOMER:
			return getDeliverToCustomerName();
		case SUPPLIER:
			return getDeliverToSupplierName();
		default:
			return getDeliverToOther();
		}
	}
	
	@Transient
	public CompanyType getDeliverToCompanyType() {
		return stockRow != null ? stockRow.getDeliverToCompanyType() : null;
	}

	@Transient
	public void setDeliverToCompanyType(CompanyType deliverToCompanyType) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliverToCompanyType(deliverToCompanyType);
	}

	@Transient
	public String getDeliverToCompanyName() {
		return stockRow != null ? stockRow.getDeliverToCompanyName() : null;
	}

	@Transient
	public void setDeliverToCompanyName(String deliverToCompanyName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliverToCompanyName(deliverToCompanyName);
	}

	@Transient
	public String getDeliverToCustomerName() {
		return stockRow != null ? stockRow.getDeliverToCustomerName() : null;
	}

	@Transient
	public void setDeliverToCustomerName(String deliverToCustomerName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliverToCustomerName(deliverToCustomerName);
	}

	@Transient
	public String getDeliverToSupplierName() {
		return stockRow != null ? stockRow.getDeliverToSupplierName() : null;
	}

	@Transient
	public void setDeliverToSupplierName(String deliverToSupplierName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliverToSupplierName(deliverToSupplierName);
	}

	@Transient
	public String getDeliverToOther() {
		return stockRow != null ? stockRow.getDeliverToOther() : null;
	}

	@Transient
	public void setDeliverToOther(String deliverToOther) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliverToOther(deliverToOther);
	}
	
	@Transient
	public String getToUserFullName() {
		if (stockRow == null)
			return null;
		return stockRow.getToUserFullName();
	}

	@Transient
	public void setToUserFullName(String toUserFullName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setToUserFullName(toUserFullName);
	}
	
	@Transient
	public String getDestinationProjectCustomerName() {
		if (stockRow == null)
			return null;
		return stockRow.getDestinationProjectCustomerName();
	}

	@Transient
	public void setDestinationProjectCustomerName(String destinationProjectCustomerName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDestinationProjectCustomerName(destinationProjectCustomerName);
	}
	
	@Transient
	public String getPoNumero() {
		return stockRow != null ? stockRow.getPoNumero() : null;
	}

	@Transient
	public void setPoNumero(String poNumero) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setPoNumero(poNumero);
	}
	
	@Transient
	public String getDestinationProjectName() {
		if (stockRow == null)
			return null;
		return stockRow.getDestinationProjectName();
	}

	@Transient
	public void setDestinationProjectName(String destinationProjectName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDestinationProjectName(destinationProjectName);
	}
	
	@Transient
	public String getDestinationName() {
		if (stockRow == null)
			return null;
		return stockRow.getDestinationName();
	}

	@Transient
	public void setDestinationName(String destinationName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDestinationName(destinationName);
	}
	
	@Transient
	public Date getDeliveryRequestDate4() {
		if (stockRow == null)
			return null;
		return stockRow.getDeliveryRequestDate4();
	}

	@Transient
	public void setDeliveryRequestDate4(Date stockRowDate4) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setDeliveryRequestDate4(stockRowDate4);
	}

	@Transient
	public String getDeliveryYear() {
		if (getDeliveryRequestDate4() == null)
			return null;
		return String.valueOf(UtilsFunctions.getYear(getDeliveryRequestDate4()));
	}

	@Transient
	public String getDeliveryMonthAndYear() {
		if (getDeliveryRequestDate4() == null)
			return null;
		return String.valueOf(UtilsFunctions.getMonth(getDeliveryRequestDate4()) + "/" + UtilsFunctions.getYear(getDeliveryRequestDate4()));
	}
	
	@Transient
	public String getPartNumberBrandName() {
		return stockRow == null ? null : stockRow.getPartNumberBrandName();
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setPartNumberBrandName(partNumberBrandName);
	}
	
	@Transient
	public String getEndCustomerName() {
		if (stockRow == null)
			return null;
		return stockRow.getEndCustomerName();
	}

	@Transient
	public void setEndCustomerName(String endCustomerName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setEndCustomerName(endCustomerName);
	}
	
	@Transient
	public String getWarehouseName() {
		if (stockRow == null)
			return null;
		return stockRow.getWarehouseName();
	}

	@Transient
	public void setWarehouseName(String warehouseName) {
		if (stockRow == null)
			stockRow = new StockRow();
		stockRow.setWarehouseName(warehouseName);
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
