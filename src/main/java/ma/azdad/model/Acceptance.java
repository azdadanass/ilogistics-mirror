package ma.azdad.model;

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
public class Acceptance extends GenericModel<Integer> {

	private Boolean ibuy = true;
	private Date date;
	private AcceptanceStatus status = AcceptanceStatus.NOT_INVOICED;
	private String numero;
	private Date dateInvoice;
	private String idInvoice;
	private String invoiceStatus;
	private Double amountHt;
	private Double amountTtc2;

	private OldInvoiceTerm oldInvoiceTerm;

	public Acceptance() {
	}

	// c1
	public Acceptance(Integer id, Date date, AcceptanceStatus status, String numero, Double amountHt, Double amountTtc2, //
			String poNumero,String projectName, String currencyName, String oldInvoiceTermPhase, String customerName, String supplierName) {
		super(id);
		this.date = date;
		this.status = status;
		this.numero = numero;
		this.amountHt = amountHt;
		this.amountTtc2 = amountTtc2;
		this.setPoNumero(poNumero);
		this.setProjectName(projectName);
		this.setCurrencyName(currencyName);
		this.setOldInvoiceTermPhase(oldInvoiceTermPhase);
		this.setCustomerName(customerName);
		this.setSupplierName(supplierName);
	}

	public boolean filter(String query) {
		return contains(query, numero, date);
	}

	@Transient
	public String getPoNumero() {
		if (oldInvoiceTerm == null)
			return null;
		return oldInvoiceTerm.getPoNumero();
	}

	@Transient
	public void setPoNumero(String poNumero) {
		if (oldInvoiceTerm == null)
			oldInvoiceTerm = new OldInvoiceTerm();
		oldInvoiceTerm.setPoNumero(poNumero);
	}
	
	@Transient
	public String getProjectName() {
		if (oldInvoiceTerm == null)
			return null;
		return oldInvoiceTerm.getProjectName();
	}

	@Transient
	public void setProjectName(String projectName) {
		if (oldInvoiceTerm == null)
			oldInvoiceTerm = new OldInvoiceTerm();
		oldInvoiceTerm.setProjectName(projectName);
	}

	@Transient
	public String getSupplierName() {
		if (oldInvoiceTerm == null)
			return null;
		return oldInvoiceTerm.getSupplierName();
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (oldInvoiceTerm == null)
			oldInvoiceTerm = new OldInvoiceTerm();
		oldInvoiceTerm.setSupplierName(supplierName);
	}

	@Transient
	public String getCustomerName() {
		if (oldInvoiceTerm == null)
			return null;
		return oldInvoiceTerm.getCustomerName();
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (oldInvoiceTerm == null)
			oldInvoiceTerm = new OldInvoiceTerm();
		oldInvoiceTerm.setCustomerName(customerName);
	}

	@Transient
	public String getCurrencyName() {
		if (oldInvoiceTerm == null)
			return null;
		return oldInvoiceTerm.getCurrencyName();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (oldInvoiceTerm == null)
			oldInvoiceTerm = new OldInvoiceTerm();
		oldInvoiceTerm.setCurrencyName(currencyName);
	}

	@Transient
	public String getOldInvoiceTermPhase() {
		if (oldInvoiceTerm == null)
			return null;
		return oldInvoiceTerm.getPhase();
	}

	@Transient
	public void setOldInvoiceTermPhase(String oldInvoiceTermPhase) {
		if (oldInvoiceTerm == null)
			oldInvoiceTerm = new OldInvoiceTerm();
		oldInvoiceTerm.setPhase(oldInvoiceTermPhase);
	}

	@Transient
	public Boolean getIsInvoiced() {
		return AcceptanceStatus.INVOICED.equals(status);
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status_new")
	public AcceptanceStatus getStatus() {
		return status;
	}

	public void setStatus(AcceptanceStatus status) {
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idacceptance")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dateacceptance")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "numero", length = 45)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dateinvoice", length = 10)
	public Date getDateInvoice() {
		return this.dateInvoice;
	}

	public void setDateInvoice(Date dateInvoice) {
		this.dateInvoice = dateInvoice;
	}

	@Column(name = "idinvoice", length = 45)
	public String getIdInvoice() {
		return this.idInvoice;
	}

	public void setIdInvoice(String idInvoice) {
		this.idInvoice = idInvoice;
	}

	@Column(name = "invoicestatus", length = 45)
	public String getInvoiceStatus() {
		return this.invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	@Column(name = "total_price")
	public Double getAmountHt() {
		return amountHt;
	}

	public void setAmountHt(Double amountHt) {
		this.amountHt = amountHt;
	}

	@Column(name = "total_price_ttc")
	public Double getAmountTtc2() {
		return amountTtc2;
	}

	public void setAmountTtc2(Double amountTtc2) {
		this.amountTtc2 = amountTtc2;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "paymentterm_idpaymentterm")
	public OldInvoiceTerm getOldInvoiceTerm() {
		return oldInvoiceTerm;
	}

	public void setOldInvoiceTerm(OldInvoiceTerm oldInvoiceTerm) {
		this.oldInvoiceTerm = oldInvoiceTerm;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

}