package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currency")

public class Currency implements Serializable {
	private Integer idcurrency;
	private String name;
	private String currency;
	private Double max_value;
	private Double min_value;
	private Integer month;
	private Integer year;

	public Currency() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcurrency", unique = true, nullable = false)
	public Integer getIdcurrency() {
		return idcurrency;
	}

	public void setIdcurrency(Integer idcurrency) {
		this.idcurrency = idcurrency;
	}

	@Column(name = "currency", insertable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "currency", nullable = false, unique = true, length = 45)
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "max_value")
	public Double getMax_value() {
		return max_value;
	}

	public void setMax_value(Double max_value) {
		this.max_value = max_value;
	}

	@Column(name = "min_value")
	public Double getMin_value() {
		return min_value;
	}

	public void setMin_value(Double min_value) {
		this.min_value = min_value;
	}

	@Column(name = "month")
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Column(name = "year")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
