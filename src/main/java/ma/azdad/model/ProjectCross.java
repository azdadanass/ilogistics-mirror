package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
@Table(name = "project_cross")

public class ProjectCross implements Serializable {

	private Integer idprojectcross;
	private Date date;
	private ProjectCrossCategory category;
	private String type;
	private String description;
	private Double amount;
	private Double cashAmount;
	private Date cashDate;

	private Boolean manual = true;
	private String erp;
	private DeliveryRequest deliveryRequest;

	private Currency currency;
	private Project fromProject;
	private Project toProject;

	public ProjectCross() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idprojectcross", unique = true, nullable = false)
	public Integer getIdprojectcross() {
		return idprojectcross;
	}

	public void setIdprojectcross(Integer idprojectcross) {
		this.idprojectcross = idprojectcross;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", length = 10)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "type", length = 45)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "cash_amount")
	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "cash_date", length = 10)
	public Date getCashDate() {
		return cashDate;
	}

	public void setCashDate(Date cashDate) {
		this.cashDate = cashDate;
	}

	@Column(name = "description", columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currency_idcurrency", nullable = false)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprojectfrom", nullable = true)
	public Project getFromProject() {
		return fromProject;
	}

	public void setFromProject(Project fromProject) {
		this.fromProject = fromProject;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprojectto", nullable = false)
	public Project getToProject() {
		return toProject;
	}

	public void setToProject(Project toProject) {
		this.toProject = toProject;
	}

	public Boolean getManual() {
		return manual;
	}

	public void setManual(Boolean manual) {
		this.manual = manual;
	}

	public String getErp() {
		return erp;
	}

	public void setErp(String erp) {
		this.erp = erp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_request_id", nullable = true, unique = true)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@Enumerated
	public ProjectCrossCategory getCategory() {
		return category;
	}

	public void setCategory(ProjectCrossCategory category) {
		this.category = category;
	}

}
