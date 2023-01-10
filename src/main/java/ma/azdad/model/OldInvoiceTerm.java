package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "paymentterm")

public class OldInvoiceTerm extends GenericModel<Integer> {

	private String phase;
	
	private Po po;


	public OldInvoiceTerm() {
	}

	public OldInvoiceTerm(Po po) {
		this.po = po;
	}
	
	@Transient
	public String getPoNumero() {
		if (po == null)
			return null;
		return po.getNumero();
	}

	@Transient
	public void setPoNumero(String poNumero) {
		if (po == null)
			po = new Po();
		po.setNumero(poNumero);
	}
	
	@Transient
	public String getProjectName() {
		if (po == null)
			return null;
		return po.getProjectName();
	}
	
	@Transient
	public void setProjectName(String projectName) {
		if (po == null)
			po = new Po();
		po.setProjectName(projectName);
	}
	
	@Transient
	public String getSupplierName() {
		if (po == null)
			return null;
		return po.getSupplierName();
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (po == null)
			po = new Po();
		po.setSupplierName(supplierName);
	}
	
	@Transient
	public String getCustomerName() {
		if (po == null)
			return null;
		return po.getCustomerName();
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (po == null)
			po = new Po();
		po.setCustomerName(customerName);
	}
	
	@Transient
	public String getCurrencyName() {
		if (po == null)
			return null;
		return po.getCurrencyName();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (po == null)
			po = new Po();
		po.setCurrencyName(currencyName);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpaymentterm")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_idpo", nullable = false)
	public Po getPo() {
		return this.po;
	}

	public void setPo(Po po) {
		this.po = po;
	}
	
	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

}
