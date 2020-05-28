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
@Table(name = "paymentterm")

public class Paymentterm implements Serializable {

	private Integer idpaymentterm;
	private Po po;

	
	@Override
	public boolean equals(Object obj) {
		return this.idpaymentterm.equals(((Paymentterm)obj).getIdpaymentterm());
	}

	public Paymentterm() {
	}

	public Paymentterm(Po po) {
		this.po = po;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpaymentterm", unique = true, nullable = false)
	public Integer getIdpaymentterm() {
		return this.idpaymentterm;
	}

	public void setIdpaymentterm(Integer idpaymentterm) {
		this.idpaymentterm = idpaymentterm;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_idpo", nullable = false)
	public Po getPo() {
		return this.po;
	}

	public void setPo(Po po) {
		this.po = po;
	}

	

}
