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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class IbuyPayment extends GenericModel<Integer> {

	private String reference;
	private PaymentMode mode;
	private Date paymentDate;
	private Date cashDate;
	private Double amount;
	private Boolean isMapped = false;
	private Integer countFiles = 0;
	private Boolean isLc;
	private Boolean hasLc = false;
	private Double madConversionRate;
	private PaymentStatus status = PaymentStatus.PENDING;

	private Currency currency;
	private Supplier supplier;
	private BankAccount bankAccount;

	public IbuyPayment() {
		super();
	}

	// constructor1
	public IbuyPayment(Integer id, String reference, PaymentMode mode, Date paymentDate, Date cashDate, Double amount, Boolean isMapped, Boolean isLc, Boolean hasLc,
			Integer countFiles, Double madConversionRate, PaymentStatus status, String supplierName, String bankAccountFullName, String currencyName) {
		super(id);
		this.reference = reference;
		this.mode = mode;
		this.paymentDate = paymentDate;
		this.cashDate = cashDate;
		this.amount = amount;
		this.isMapped = isMapped;
		this.isLc = isLc;
		this.hasLc = hasLc;
		this.countFiles = countFiles;
		this.madConversionRate = madConversionRate;
		this.status = status;
		this.setSupplierName(supplierName);
		this.setBankAccountFullName(bankAccountFullName);
		this.setCurrencyName(currencyName);
	}

	public boolean filter(String query) {
		return contains(query, reference);
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

	@Temporal(TemporalType.DATE)
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

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

	public Double getMadConversionRate() {
		return madConversionRate;
	}

	public void setMadConversionRate(Double madConversionRate) {
		this.madConversionRate = madConversionRate;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Transient
	public String getSupplierName() {
		if (supplier == null)
			return null;
		return supplier.getName();
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(supplierName);
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

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	public Boolean getIsLc() {
		return isLc;
	}

	public void setIsLc(Boolean isLc) {
		this.isLc = isLc;
	}

	public Boolean getHasLc() {
		return hasLc;
	}

	public void setHasLc(Boolean hasLc) {
		this.hasLc = hasLc;
	}

}
