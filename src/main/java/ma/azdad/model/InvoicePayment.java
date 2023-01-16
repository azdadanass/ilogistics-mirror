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
public class InvoicePayment extends GenericModel<Integer> {

	private String reference;
	private PaymentMode mode;
	private Date paymentDate;
	private Date cashDate;
	private Double amount;
	private Boolean isMapped = false;
	private PaymentStatus status = PaymentStatus.PENDING;
	private Double paidTtc = 0.0;
	private Double madConversionRate;
	private Integer countFiles = 0;

	private Currency currency;
	private Customer customer;
	private BankAccount bankAccount;

	public boolean filter(String query) {
		return contains(query, reference);
	}
	
	// c1
	public InvoicePayment(Integer id, String reference, PaymentMode mode, Date paymentDate, Date cashDate, Double amount, Double paidTtc,
			Boolean isMapped, Integer countFiles, Double madConversionRate, PaymentStatus status, String customerName, String bankAccountFullName,
			String currencyName) {
		super(id);
		this.reference = reference;
		this.mode = mode;
		this.paymentDate = paymentDate;
		this.cashDate = cashDate;
		this.amount = amount;
		this.paidTtc = paidTtc;
		this.isMapped = isMapped;
		this.countFiles = countFiles;
		this.madConversionRate = madConversionRate;
		this.status = status;
		this.setCustomerName(customerName);
		this.setBankAccountFullName(bankAccountFullName);
		this.setCurrencyName(currencyName);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.reference;
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

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Enumerated(EnumType.STRING)
	public PaymentMode getMode() {
		return mode;
	}

	public void setMode(PaymentMode mode) {
		this.mode = mode;
	}

	@Column(name = "valuedate")
	@Temporal(TemporalType.DATE)
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Column(name = "cashdate")
	@Temporal(TemporalType.DATE)
	public Date getCashDate() {
		return cashDate;
	}

	public void setCashDate(Date cashDate) {
		this.cashDate = cashDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Boolean getIsMapped() {
		return isMapped;
	}

	public void setIsMapped(Boolean isMapped) {
		this.isMapped = isMapped;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "bankaccount_idbankaccount")
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Transient
	public String getCustomerName() {
		if (customer == null)
			return null;
		return customer.getName();
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (customer == null)
			customer = new Customer();
		customer.setName(customerName);
	}

	@Transient
	public String getCurrencyName() {
		if (currency == null)
			return null;
		return currency.getName();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (currency == null)
			currency = new Currency();
		currency.setName(currencyName);
	}

	@Transient
	public String getBankAccountFullName() {
		if (bankAccount == null)
			return null;
		return bankAccount.getFullName();
	}

	@Transient
	public void setBankAccountFullName(String bankAccountFullName) {
		if (bankAccount == null)
			bankAccount = new BankAccount();
		bankAccount.setFullName(bankAccountFullName);
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_new", nullable = false)
	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}
	
	public Double getPaidTtc() {
		return paidTtc;
	}

	public void setPaidTtc(Double paidTtc) {
		this.paidTtc = paidTtc;
	}
	
	public Double getMadConversionRate() {
		return madConversionRate;
	}

	public void setMadConversionRate(Double madConversionRate) {
		this.madConversionRate = madConversionRate;
	}
	
	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}
}
