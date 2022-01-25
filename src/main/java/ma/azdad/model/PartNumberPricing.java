package ma.azdad.model;

import java.util.Date;

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
public class PartNumberPricing extends GenericModel<Integer> {

	private Date date;
	private Double baseLineCost;
	private Double baseLinePrice;
	private Double maxAllowedDiscount = 0.0;

	private Currency currency;
	private PartNumber partNumber;
	private Company company;

	public PartNumberPricing() {
		super();
	}

	// c1
	public PartNumberPricing(Integer id, Date date, Double baseLineCost, Double baseLinePrice, Double maxAllowedDiscount, String currencyName, Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, String companyName) {
		super(id);
		this.date = date;
		this.baseLineCost = baseLineCost;
		this.baseLinePrice = baseLinePrice;
		this.maxAllowedDiscount = maxAllowedDiscount;
		this.setCurrencyName(currencyName);
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setCompanyName(companyName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getPartNumberName(), date);
	}

	@Transient
	public Double getMaxMarginPercentage() {
		if (baseLinePrice > 0)
			return (baseLinePrice - baseLineCost) / baseLinePrice;
		return null;
	}

	@Transient
	public Double getMinMarginPercentage() {
		if (baseLinePrice > 0)
			return ((baseLinePrice * (1 - maxAllowedDiscount / 100.0)) - baseLineCost) / (baseLinePrice * (1 - maxAllowedDiscount / 100.0));
		return null;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	@Transient
	public String getCompanyName() {
		return company == null ? null : company.getName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
	}

	@Transient
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !companyId.equals(company.getId()))
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public String getPartNumberName() {
		return partNumber == null ? null : partNumber.getName();
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}

	@Transient
	public String getPartNumberDescription() {
		return partNumber == null ? null : partNumber.getDescription();
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
	}

	@Transient
	public String getPartNumberBrandName() {
		return partNumber == null ? null : partNumber.getBrandName();
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setBrandName(partNumberBrandName);
	}

	@Transient
	public String getPartNumberCategoryName() {
		return partNumber == null ? null : partNumber.getCategoryName();
	}

	@Transient
	public void setPartNumberCategoryName(String partNumberCategoryName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setCategoryName(partNumberCategoryName);
	}

	@Transient
	public String getPartNumberTypeName() {
		return partNumber == null ? null : partNumber.getTypeName();
	}

	@Transient
	public void setPartNumberTypeName(String partNumberTypeName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setTypeName(partNumberTypeName);
	}

	@Transient
	public Integer getPartNumberId() {
		return partNumber == null ? null : partNumber.getId();
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		if (partNumber == null || !partNumberId.equals(partNumber.getId()))
			partNumber = new PartNumber();
		partNumber.setId(partNumberId);
	}

	@Transient
	public String getCurrencyName() {
		return currency == null ? null : currency.getCurrency();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (currency == null)
			currency = new Currency();
		currency.setCurrency(currencyName);
	}

	@Transient
	public Integer getCurrencyId() {
		return currency == null ? null : currency.getIdcurrency();
	}

	@Transient
	public void setCurrencyId(Integer currencyId) {
		if (currency == null || !currencyId.equals(currency.getIdcurrency()))
			currency = new Currency();
		currency.setIdcurrency(currencyId);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getBaseLineCost() {
		return baseLineCost;
	}

	public void setBaseLineCost(Double baseLineCost) {
		this.baseLineCost = baseLineCost;
	}

	public Double getBaseLinePrice() {
		return baseLinePrice;
	}

	public void setBaseLinePrice(Double baseLinePrice) {
		this.baseLinePrice = baseLinePrice;
	}

	public Double getMaxAllowedDiscount() {
		return maxAllowedDiscount;
	}

	public void setMaxAllowedDiscount(Double maxAllowedDiscount) {
		this.maxAllowedDiscount = maxAllowedDiscount;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
