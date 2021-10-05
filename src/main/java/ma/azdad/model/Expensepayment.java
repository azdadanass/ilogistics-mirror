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
import javax.persistence.Transient;

@Entity
@Table(name = "expensepayment")

public class Expensepayment implements Serializable {

	private Integer idexpensepayment;
	private Date dateExpense;
	private String description;
	private Double quantity;
	private Double totalCost;
	private String status;
	private User user;
	private Budgetdetail budgetdetail;

	public Expensepayment() {
	}

	@Transient
	public String getBeneficiaryFullName() {
		return user!=null?user.getFullName():null;
	}

	@Transient
	public String getNumero() {
		return String.format("%05d", idexpensepayment);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idexpensepayment", unique = true, nullable = false)
	public Integer getIdexpensepayment() {
		return this.idexpensepayment;
	}

	public void setIdexpensepayment(Integer idexpensepayment) {
		this.idexpensepayment = idexpensepayment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "budgetdetail_idbudgetdetail", nullable = false)
	public Budgetdetail getBudgetdetail() {
		return this.budgetdetail;
	}

	public void setBudgetdetail(Budgetdetail budgetdetail) {
		this.budgetdetail = budgetdetail;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_username", nullable = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dateexpense", length = 10)
	public Date getDateExpense() {
		return this.dateExpense;
	}

	public void setDateExpense(Date dateExpense) {
		this.dateExpense = dateExpense;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "quantity", precision = 22, scale = 0)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "totalcost", precision = 22, scale = 0)
	public Double getTotalCost() {
		return this.totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	@Column(name = "status", length = 45)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}