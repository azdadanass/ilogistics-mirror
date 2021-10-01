package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity

public class AppLink extends GenericModel<Integer> implements Serializable {

	private CostType costType;
	private RevenueType revenueType;
	private Date startDate;
	private Date endDate;
	private Double amount;

	private Acceptance acceptance;
	private Expensepayment expensepayment;
	private DeliveryRequest deliveryRequest;
	private TransportationRequest transportationRequest;
	private Warehouse warehouse;

	//
	private Integer acceptanceId;
	private Integer expensepaymentId;
	private Double madConversionRate;
	private String currency;
	private String supplierName;
	private String customerName;
	private String idInvoice;
	private String invoiceStatus;
	private Date invoiceDate;
	private String poNumeroIbuy;
	private String poNumeroInvoice;

	public AppLink() {
		super();
	}

	public AppLink(CostType costType, RevenueType revenueType, Date startDate, Date endDate, Double amount, Double madConversionRate1, Double madConversionRate2, String currency1, String currency2, Integer acceptanceId, Integer expensepaymentId, String supplierName, String customerName, String idInvoice, String invoiceStatus, Date invoiceDate, String poNumeroIbuy, String poNumeroInvoice) {
		super();
		this.costType = costType;
		this.revenueType = revenueType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.acceptanceId = acceptanceId;
		this.expensepaymentId = expensepaymentId;
		this.madConversionRate = madConversionRate1 != null ? madConversionRate1 : madConversionRate2;
		this.currency = currency1 != null ? currency1 : currency2;
		this.supplierName = supplierName;
		this.customerName = customerName;
		this.idInvoice = idInvoice;
		this.invoiceStatus = invoiceStatus;
		this.invoiceDate = invoiceDate;
		this.poNumeroIbuy = poNumeroIbuy;
		this.poNumeroInvoice = poNumeroInvoice;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && acceptance != null)
			result = acceptance.getNumero().toLowerCase().contains(query);
		if (!result && acceptance != null)
			result = acceptance.getIdInvoice().toLowerCase().contains(query);
		if (!result && costType != null)
			result = costType.getValue().toLowerCase().contains(query);
		return result;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", length = 10)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", length = 10)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "expensepayment_id")
	public Expensepayment getExpensepayment() {
		return expensepayment;
	}

	public void setExpensepayment(Expensepayment expensepayment) {
		this.expensepayment = expensepayment;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "delivery_request_id")
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@Column(name = "cost_type", nullable = true)
	@Enumerated(EnumType.STRING)
	public CostType getCostType() {
		return costType;
	}

	public void setCostType(CostType costType) {
		this.costType = costType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "acceptance_id")
	public Acceptance getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(Acceptance acceptance) {
		this.acceptance = acceptance;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "transportation_request_id")
	public TransportationRequest getTransportationRequest() {
		return transportationRequest;
	}

	public void setTransportationRequest(TransportationRequest transportationRequest) {
		this.transportationRequest = transportationRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "warehouse_id")
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Transient
	public Integer getAcceptanceId() {
		return acceptanceId;
	}

	@Transient
	public Integer getExpensepaymentId() {
		return expensepaymentId;
	}

	@Enumerated(EnumType.STRING)
	public RevenueType getRevenueType() {
		return revenueType;
	}

	public void setRevenueType(RevenueType revenueType) {
		this.revenueType = revenueType;
	}

	@Transient
	public Double getMadConversionRate() {
		return madConversionRate;
	}

	@Transient
	public void setMadConversionRate(Double madConversionRate) {
		this.madConversionRate = madConversionRate;
	}

	@Transient
	public String getCurrency() {
		return currency;
	}

	@Transient
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Transient
	public String getSupplierName() {
		return supplierName;
	}

	@Transient
	public String getCustomerName() {
		return customerName;
	}

	@Transient
	public String getIdInvoice() {
		return idInvoice;
	}

	@Transient
	public String getPoNumeroIbuy() {
		return poNumeroIbuy;
	}

	@Transient
	public String getPoNumeroInvoice() {
		return poNumeroInvoice;
	}

	@Transient
	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	@Transient
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
