package ma.azdad.model;

import java.io.Serializable;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "podetails")

public class Podetails implements Serializable {

	private Integer idpoDetails;
	private String description;
	private Double unit;
	private Double unitPrice;
	private Double totalPrice;
	private Integer reference;
	private String unite;
	private RevenueType revenueType;
	private CostType costType;
	private Double totalBoqPrice = 0.0;

	private Po po;

	public Podetails() {
	}

	@Transient
	public Boolean getIsBoqMapped() {
		if (totalBoqPrice == null)
			return false;
		return UtilsFunctions.compareDoubles(totalBoqPrice, unitPrice) == 0;
	}

	@Override
	public boolean equals(Object obj) {
		return this.idpoDetails.equals(((Podetails) obj).getIdpoDetails());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpodetails", unique = true, nullable = false)
	public Integer getIdpoDetails() {
		return this.idpoDetails;
	}

	public void setIdpoDetails(Integer idpoDetails) {
		this.idpoDetails = idpoDetails;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_idpo", nullable = false)
	public Po getPo() {
		return this.po;
	}

	public void setPo(Po po) {
		this.po = po;
	}

	@Column(name = "description", length = 5000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "unit", precision = 22, scale = 0)
	public Double getUnit() {
		return this.unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	@Column(name = "unitprice", precision = 22, scale = 0)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "totalprice", precision = 22, scale = 0)
	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "reference")
	public Integer getReference() {
		return reference;
	}

	public void setReference(Integer reference) {
		this.reference = reference;
	}

	@Column(name = "unite", length = 45)
	public String getUnite() {
		return this.unite;
	}

	public void setUnite(String unite) {
		this.unite = unite;
	}

	@Column(name = "revenue_type", nullable = true)
	@Enumerated(EnumType.STRING)
	public RevenueType getRevenueType() {
		return revenueType;
	}

	public void setRevenueType(RevenueType revenueType) {
		this.revenueType = revenueType;
	}
	
	@Enumerated(EnumType.STRING)
	public CostType getCostType() {
		return costType;
	}

	public void setCostType(CostType costType) {
		this.costType = costType;
	}

	@Column(name = "total_boq_price")
	public Double getTotalBoqPrice() {
		return totalBoqPrice;
	}

	public void setTotalBoqPrice(Double totalBoqPrice) {
		this.totalBoqPrice = totalBoqPrice;
	}

}
