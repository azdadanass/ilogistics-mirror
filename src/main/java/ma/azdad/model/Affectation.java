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

@Entity

public class Affectation implements Serializable {

	private Integer idaffectation;
	private User user;
	private User hrManager;
	private User lineManager;
	private User logisticManager;

	public Affectation() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idaffectation", unique = true, nullable = false)
	public Integer getIdaffectation() {
		return this.idaffectation;
	}

	public void setIdaffectation(Integer idaffectation) {
		this.idaffectation = idaffectation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_hr")
	public User getHrManager() {
		return hrManager;
	}

	public void setHrManager(User hrManager) {
		this.hrManager = hrManager;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_lm")
	public User getLineManager() {
		return lineManager;
	}

	public void setLineManager(User lineManager) {
		this.lineManager = lineManager;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_username", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_logistic_manager")
	public User getLogisticManager() {
		return logisticManager;
	}

	public void setLogisticManager(User logisticManager) {
		this.logisticManager = logisticManager;
	}

}
