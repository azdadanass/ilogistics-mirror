package ma.azdad.model;

import java.util.Date;

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
public class PartNumberPricingDetail extends GenericModel<Integer> {

	private Date date;
	private CustomerType businessType;
	private Double maxAllowedDiscount = 0.0;

	private PartNumberPricing partNumberPricing;

	@Transient
	public Double getMaxPrice() {
		return partNumberPricing.getBaseLinePrice();
	}

	@Transient
	public Double getMaxMargin() {
		return partNumberPricing.getBaseLinePrice() - partNumberPricing.getBaseLineCost();
	}

	@Transient
	public Double getMaxMarginPercentage() {
		if (partNumberPricing.getBaseLinePrice() > 0)
			return getMaxMargin() / getMaxPrice();
		return null;
	}

	@Transient
	public Double getMinPrice() {
		return partNumberPricing.getBaseLinePrice() * (1 - maxAllowedDiscount / 100.0);
	}

	@Transient
	public Double getMinMargin() {
		return getMinPrice() - partNumberPricing.getBaseLineCost();
	}

	@Transient
	public Double getMinMarginPercentage() {
		if (partNumberPricing.getBaseLinePrice() > 0)
			return getMinMargin() / getMinPrice();
		return null;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Enumerated(EnumType.STRING)
	public CustomerType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(CustomerType businessType) {
		this.businessType = businessType;
	}

	public Double getMaxAllowedDiscount() {
		return maxAllowedDiscount;
	}

	public void setMaxAllowedDiscount(Double maxAllowedDiscount) {
		this.maxAllowedDiscount = maxAllowedDiscount;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumberPricing getPartNumberPricing() {
		return partNumberPricing;
	}

	public void setPartNumberPricing(PartNumberPricing partNumberPricing) {
		this.partNumberPricing = partNumberPricing;
	}

}