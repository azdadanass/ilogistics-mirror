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

	private Currency costCurrency;
	private Currency priceCurrency;
	private PartNumber partNumber;

	public PartNumberPricing() {
		super();
	}

	// c1
	public PartNumberPricing(Integer id, Date date, Double baseLineCost, Double baseLinePrice, Double maxAllowedDiscount, String costCurrencyName, String priceCurrencyName, Integer partNumberId, String partNumberName) {
		super(id);
		this.date = date;
		this.baseLineCost = baseLineCost;
		this.baseLinePrice = baseLinePrice;
		this.maxAllowedDiscount = maxAllowedDiscount;
		this.setCostCurrencyName(costCurrencyName);
		this.setPriceCurrencyName(priceCurrencyName);
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getPartNumberName(), date);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
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
	public String getCostCurrencyName() {
		return costCurrency == null ? null : costCurrency.getCurrency();
	}

	@Transient
	public void setCostCurrencyName(String costCurrencyName) {
		if (costCurrency == null)
			costCurrency = new Currency();
		costCurrency.setCurrency(costCurrencyName);
	}

	@Transient
	public Integer getCostCurrencyId() {
		return costCurrency == null ? null : costCurrency.getIdcurrency();
	}

	@Transient
	public void setCostCurrencyId(Integer costCurrencyId) {
		if (costCurrency == null || !costCurrencyId.equals(costCurrency.getIdcurrency()))
			costCurrency = new Currency();
		costCurrency.setIdcurrency(costCurrencyId);
	}

	@Transient
	public String getPriceCurrencyName() {
		return priceCurrency == null ? null : priceCurrency.getCurrency();
	}

	@Transient
	public void setPriceCurrencyName(String priceCurrencyName) {
		if (priceCurrency == null)
			priceCurrency = new Currency();
		priceCurrency.setCurrency(priceCurrencyName);
	}

	@Transient
	public Integer getPriceCurrencyId() {
		return priceCurrency == null ? null : priceCurrency.getIdcurrency();
	}

	@Transient
	public void setPriceCurrencyId(Integer priceCurrencyId) {
		if (priceCurrency == null || !priceCurrencyId.equals(priceCurrency.getIdcurrency()))
			priceCurrency = new Currency();
		priceCurrency.setIdcurrency(priceCurrencyId);
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
	public Currency getCostCurrency() {
		return costCurrency;
	}

	public void setCostCurrency(Currency costCurrency) {
		this.costCurrency = costCurrency;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Currency getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(Currency priceCurrency) {
		this.priceCurrency = priceCurrency;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

}
