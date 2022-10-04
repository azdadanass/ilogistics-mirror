package ma.azdad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "po")

public class Po extends GenericModel<Integer> {

	private Boolean ibuy;
	private String numero;
	private Date date;
	private Double amountHt = 0.0;

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

	private Company company;

	private List<PoFile> fileList = new ArrayList<>();

	public Po() {
	}

	// c1
	public Po(Integer id, String numeroInvoice) {
		super();
		this.id = id;
		this.numeroInvoice = numeroInvoice;
	}

	// c2
	public Po(Integer id, Boolean ibuy, String numero, Date date, Double amountHt, PoStatus status, PoBoqStatus boqStatus, PoDeliveryStatus deliveryStatus, //
			String currencyName, String projectName, String supplierOrCustomerName) {
		super();
		this.id = id;
		this.ibuy = ibuy;
		this.numero = numero;
		this.date = date;
		this.amountHt = amountHt;
		this.status = status;
		this.boqStatus = boqStatus;
		this.deliveryStatus = deliveryStatus != null ? deliveryStatus : PoDeliveryStatus.PENDING;
		this.setCurrencyName(currencyName);
		this.setProjectName(projectName);
		if (ibuy)
			this.setSupplierName(supplierOrCustomerName);
		else
			this.setCustomerName(supplierOrCustomerName);
	}

	public boolean filter(String query) {
		return contains(query, numero, amountHt, status.getValue(), getProjectName(), getSupplierName(), getCustomerName());
	}

	protected Boolean contains(String query, Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			Object o = objects[i];
			if (o == null)
				continue;
			if (o instanceof String && ((String) o).toLowerCase().contains(query))
				return true;
			if (o instanceof Double && ((Double) o).toString().contains(query))
				return true;
			if (o instanceof Integer && ((Integer) o).toString().contains(query))
				return true;
			if (o instanceof Date && UtilsFunctions.getFormattedDate(((Date) o)).contains(query))
				return true;
		}
		return false;
	}

	@Transient
	public String getSupplierName() {
		return supplier != null ? supplier.getName() : null;
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(supplierName);
	}

	@Transient
	public String getCompanyName() {
		return company != null ? company.getName() : null;
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
	}

	@Transient
	public String getCurrencyName() {
		return currency != null ? currency.getName() : null;
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (currency == null)
			currency = new Currency();
		currency.setName(currencyName);
	}

	@Transient
	public String getProjectName() {
		return project != null ? project.getName() : null;
	}

	@Transient
	public void setProjectName(String projectName) {
		if (project == null)
			project = new Project();
		project.setName(projectName);
	}

	@Transient
	public String getCustomerName() {
		return project != null ? project.getCustomerName() : null;
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (project == null)
			project = new Project();
		project.setCustomerName(customerName);
	}

	@Transient
	public String getCustomerPhoto() {
		return project != null ? project.getCustomerPhoto() : null;
	}

	@Transient
	public void setCustomerPhoto(String customerPhoto) {
		if (project == null)
			project = new Project();
		project.setCustomerPhoto(customerPhoto);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpo")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "numero", length = 45, insertable = false, updatable = false)
	public String getNumeroIbuy() {
		return numeroIbuy;
	}

	public void setNumeroIbuy(String numeroIbuy) {
		this.numeroIbuy = numeroIbuy;
	}

	@Column(name = "numero", length = 45, insertable = false, updatable = false)
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

	@Column(name = "old_type")
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

	@Temporal(TemporalType.DATE)
	@Column(name = "podate")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "total_amount")
	public Double getAmountHt() {
		return amountHt;
	}

	public void setAmountHt(Double amountHt) {
		this.amountHt = amountHt;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "company_idcompany")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PoFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<PoFile> fileList) {
		this.fileList = fileList;
	}

}