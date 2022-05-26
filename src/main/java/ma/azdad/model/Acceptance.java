package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "acceptance")

public class Acceptance implements Serializable {

	private Integer idacceptance;
	private Date dateAcceptance;
	private String status;
	private String numero;
	private Date dateInvoice;
	private String idInvoice;
	private String invoiceStatus;
	private Double totalPrice;
	private Double totalPriceTTC;
	
	private Paymentterm paymentterm;
	
	

	public Acceptance() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idacceptance", unique = true, nullable = false)
	public Integer getIdacceptance() {
		return this.idacceptance;
	}

	public void setIdacceptance(Integer idacceptance) {
		this.idacceptance = idacceptance;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dateacceptance", length = 10)
	public Date getDateAcceptance() {
		return this.dateAcceptance;
	}

	public void setDateAcceptance(Date dateAcceptance) {
		this.dateAcceptance = dateAcceptance;
	}

	@Column(name = "numero", length = 45)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "status", length = 45)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "total_price_TTC")
	public Double getTotalPriceTTC() {
		return totalPriceTTC;
	}

	public void setTotalPriceTTC(Double totalPriceTTC) {
		this.totalPriceTTC = totalPriceTTC;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentterm_idpaymentterm", nullable = false)
	public Paymentterm getPaymentterm() {
		return paymentterm;
	}

	public void setPaymentterm(Paymentterm paymentterm) {
		this.paymentterm = paymentterm;
	}
	
	

}