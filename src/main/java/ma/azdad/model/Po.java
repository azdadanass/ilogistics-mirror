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

@Entity
@Table(name = "po")

public class Po implements Serializable {

	private Integer idpo;
	private Boolean ibuy;
	private Double madConversionRate;
	private String numeroIbuy;
	private String numeroInvoice;
	private String type;
	private PoStatus status;
	private Currency currency;
	private Supplier supplier;
	private Project project;
	private PoBoqStatus boqStatus;
	private PoDeliveryStatus deliveryStatus;

	public Po() {
	}

	public Po(Integer idpo, String numeroInvoice) {
		super();
		this.idpo = idpo;
		this.numeroInvoice = numeroInvoice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idpo == null) ? 0 : idpo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Po other = (Po) obj;
		if (idpo == null) {
			if (other.idpo != null)
				return false;
		} else if (!idpo.equals(other.idpo))
			return false;
		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpo", unique = true, nullable = false)
	public Integer getIdpo() {
		return this.idpo;
	}

	public void setIdpo(Integer idpo) {
		this.idpo = idpo;
	}

	@Column(name = "madconversionrate")
	public Double getMadConversionRate() {
		return madConversionRate;
	}

	public void setMadConversionRate(Double madConversionRate) {
		this.madConversionRate = madConversionRate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "currency_idcurrency", nullable = true)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_idsupplier", nullable = true)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Column(name = "numero", length = 45,insertable = false, updatable = false)
	public String getNumeroIbuy() {
		return numeroIbuy;
	}

	public void setNumeroIbuy(String numeroIbuy) {
		this.numeroIbuy = numeroIbuy;
	}

	@Column(name = "numero", length = 45)
	public String getNumeroInvoice() {
		return numeroInvoice;
	}

	public void setNumeroInvoice(String numeroInvoice) {
		this.numeroInvoice = numeroInvoice;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_idproject", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status_new")
	public PoStatus getStatus() {
		return status;
	}

	public void setStatus(PoStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	public PoBoqStatus getBoqStatus() {
		return boqStatus;
	}

	public void setBoqStatus(PoBoqStatus boqStatus) {
		this.boqStatus = boqStatus;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	@Enumerated(EnumType.STRING)
	public PoDeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(PoDeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

}