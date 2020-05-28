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
@Table(name = "budgetdetail")

public class Budgetdetail implements Serializable {

	private Integer idbudgetdetail;
	private Budget budget;

	public Budgetdetail() {
	}

	public Budgetdetail(Budget budget) {
		this.budget = budget;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idbudgetdetail", unique = true, nullable = false)
	public Integer getIdbudgetdetail() {
		return this.idbudgetdetail;
	}

	public void setIdbudgetdetail(Integer idbudgetdetail) {
		this.idbudgetdetail = idbudgetdetail;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "budget_idbudget", nullable = false)
	public Budget getBudget() {
		return this.budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

}