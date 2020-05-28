package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "budget")

public class Budget implements Serializable {

	private Integer idbudget;
	private String reference;
	private Double madConversionRate;
	private Currency currency;

	public Budget() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idbudget", unique = true, nullable = false)
	public Integer getIdbudget() {
		return this.idbudget;
	}

	public void setIdbudget(Integer idbudget) {
		this.idbudget = idbudget;
	}

	@Column(name = "reference", length = 45)
	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Column(name = "madconversionrate")
	public Double getMadConversionRate() {
		return madConversionRate;
	}

	public void setMadConversionRate(Double madConversionRate) {
		this.madConversionRate = madConversionRate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currency_idcurrency", nullable = true)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}