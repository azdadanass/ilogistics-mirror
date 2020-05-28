package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "payment")

public class Payment implements Serializable {

	private Integer idPayment;
	private String approuvalPayment;
	private Date dateCash;
	private Acceptance acceptance;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpayment", unique = true, nullable = false)
	public Integer getIdPayment() {
		return idPayment;
	}

	public void setIdPayment(Integer idPayment) {
		this.idPayment = idPayment;
	}

	@Column(name = "approuvalpayment", length = 45)
	public String getApprouvalPayment() {
		return approuvalPayment;
	}

	public void setApprouvalPayment(String approuvalPayment) {
		this.approuvalPayment = approuvalPayment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acceptance_idacceptance")
	public Acceptance getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(Acceptance acceptance) {
		this.acceptance = acceptance;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "datecash", length = 10)
	public Date getDateCash() {
		return dateCash;
	}

	public void setDateCash(Date dateCash) {
		this.dateCash = dateCash;
	}

}