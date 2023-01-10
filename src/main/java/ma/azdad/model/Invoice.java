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
import javax.persistence.Transient;

@Entity
public class Invoice extends GenericModel<Integer> {

	private Boolean ibuy = true;
	private String numero;
	private Date date;
	private Date dueDate;
	private InvoiceStatus status = InvoiceStatus.EDITED;

	private Double amountHt = 0.0;
	private Double amountTtc = 0.0;
	private Double amountHt1 = 0.0;
	private Double amountTtc1 = 0.0;
	private Double amountHt2 = 0.0;
	private Double amountTtc2 = 0.0;
	private Double deductedHt = 0.0;
	private Double deductedTtc = 0.0;
	private Double paidTtc = 0.0;
	private Double appLinkHt = 0.0;
	private Double madConversionRate;

	private Company company;
	private Currency currency;
	private Supplier supplier;
	private Customer customer;

	public Invoice() {
		super();
	}

	// c1
	public Invoice(Integer id,String numero, Date date, Date dueDate, InvoiceStatus status, Double amountHt, Double amountTtc, Double amountHt1, Double amountTtc1, Double amountHt2,
			Double amountTtc2, Double deductedHt, Double deductedTtc, Double paidTtc,Double  madConversionRate,//
			String companyName, String currencyName, String customerName, String supplierName) {
		super(id);
		this.numero = numero;
		this.date = date;
		this.dueDate = dueDate;
		this.status = status;
		this.amountHt = amountHt;
		this.amountTtc = amountTtc;
		this.amountHt1 = amountHt1;
		this.amountTtc1 = amountTtc1;
		this.amountHt2 = amountHt2;
		this.amountTtc2 = amountTtc2;
		this.deductedHt = deductedHt;
		this.deductedTtc = deductedTtc;
		this.paidTtc = paidTtc;
		this.madConversionRate = madConversionRate;
		this.setCompanyName(companyName);
		this.setCurrencyName(currencyName);
		this.setCustomerName(customerName);
		this.setSupplierName(supplierName);
	}

	public boolean filter(String query) {
		return contains(query, numero);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.numero;
	}

	// getters & setters

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
	public String getCompanyName() {
		if (company == null)
			return null;
		return company.getName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Enumerated(EnumType.STRING)
	public InvoiceStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public Double getAmountHt() {
		return amountHt;
	}

	public void setAmountHt(Double amountHt) {
		this.amountHt = amountHt;
	}

	public Double getAmountTtc() {
		return amountTtc;
	}

	public void setAmountTtc(Double amountTtc) {
		this.amountTtc = amountTtc;
	}

	public Double getAmountHt1() {
		return amountHt1;
	}

	public void setAmountHt1(Double amountHt1) {
		this.amountHt1 = amountHt1;
	}

	public Double getAmountTtc1() {
		return amountTtc1;
	}

	public void setAmountTtc1(Double amountTtc1) {
		this.amountTtc1 = amountTtc1;
	}

	public Double getAmountHt2() {
		return amountHt2;
	}

	public void setAmountHt2(Double amountHt2) {
		this.amountHt2 = amountHt2;
	}

	public Double getAmountTtc2() {
		return amountTtc2;
	}

	public void setAmountTtc2(Double amountTtc2) {
		this.amountTtc2 = amountTtc2;
	}

	public Double getDeductedHt() {
		return deductedHt;
	}

	public void setDeductedHt(Double deductedHt) {
		this.deductedHt = deductedHt;
	}

	public Double getDeductedTtc() {
		return deductedTtc;
	}

	public void setDeductedTtc(Double deductedTtc) {
		this.deductedTtc = deductedTtc;
	}

	public Double getPaidTtc() {
		return paidTtc;
	}

	public void setPaidTtc(Double paidTtc) {
		this.paidTtc = paidTtc;
	}

	public Double getAppLinkHt() {
		return appLinkHt;
	}

	public void setAppLinkHt(Double appLinkHt) {
		this.appLinkHt = appLinkHt;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Double getMadConversionRate() {
		return madConversionRate;
	}

	public void setMadConversionRate(Double madConversionRate) {
		this.madConversionRate = madConversionRate;
	}

}
