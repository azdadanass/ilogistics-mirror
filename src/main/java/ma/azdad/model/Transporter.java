package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity

public class Transporter extends GenericModel<Integer> implements Serializable {

	private Boolean active = true;
	private TransporterType type;

	private User user;

	// Supplier
	private Supplier supplier;
	private Company company;

	// Private
	private String privateFirstName;
	private String privateLastName;
	private String privateCin;
	private String privateEmail;
	private String privatePhone;

	// tmp
	private Long countPendingTr;
	private Long reactivity;
	private Long performance;

	private List<TransporterFile> fileList = new ArrayList<>();
	private List<TransporterHistory> historyList = new ArrayList<>();
	private List<Vehicle> vehicleList = new ArrayList<>();
	private List<User> userList = new ArrayList<>();

	@Override
	public boolean filter(String query) {
		return contains(query, getName(), getEmail(), getPhone());
	}

	public Transporter() {
		super();
	}

	// c1
	public Transporter(Integer id,Boolean active, TransporterType type, User user, //
			String privateFirstName, String privateLastName, String privateEmail, String privatePhone, String privateCin, //
			String supplierName, String supplierEmail, String supplierPhone, //
			String companyName) {
		super(id);
		this.active = active;
		this.type = type;
		this.user = user;
		this.privateFirstName = privateFirstName;
		this.privateLastName = privateLastName;
		this.privateEmail = privateEmail;
		this.privatePhone = privatePhone;
		this.privateCin = privateCin;
		this.setSupplierName(supplierName);
		this.setSupplierEmail(supplierEmail);
		this.setSupplierPhone(supplierPhone);
		this.setCompanyName(companyName);
	}

	@Transient
	public String getName() {
		if (type == null)
			return null;
		switch (type) {
		case INTERNAL:
			return getCompanyName();
		case SUPPLIER:
			return getSupplierName();
		case PRIVATE:
			return privateFirstName + " " + privateLastName;
		default:
			return null;
		}
	}

	@Transient
	public String getEmail() {
		if (type == null)
			return null;
		switch (type) {
		case SUPPLIER:
			return getSupplierEmail();
		case PRIVATE:
			return privateEmail;
		default:
			return null;
		}
	}

	@Transient
	public String getPhone() {
		if (type == null)
			return null;
		switch (type) {
		case SUPPLIER:
			return getSupplierPhone();
		case PRIVATE:
			return privatePhone;
		default:
			return null;
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public TransporterType getType() {
		return type;
	}

	public void setType(TransporterType type) {
		this.type = type;
	}

	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(unique = true)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<TransporterFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<TransporterFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<TransporterHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<TransporterHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transporter", cascade = CascadeType.ALL)
	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transporter", cascade = CascadeType.ALL)
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public Integer getCompanyId() {
		return company != null ? company.getId() : null;
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !companyId.equals(company.getId()))
			company = new Company();
		company.setId(companyId);
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
	public Integer getSupplierId() {
		return supplier != null ? supplier.getId() : null;
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplierId.equals(supplier.getId()))
			supplier = new Supplier();
		supplier.setId(supplierId);
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
	public String getSupplierPhone() {
		return supplier != null ? supplier.getPhone() : null;
	}

	@Transient
	public void setSupplierPhone(String supplierPhone) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setPhone(supplierPhone);
	}

	@Transient
	public String getSupplierEmail() {
		return supplier != null ? supplier.getEmail() : null;
	}

	@Transient
	public void setSupplierEmail(String supplierEmail) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setEmail(supplierEmail);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrivateFirstName() {
		return privateFirstName;
	}

	public void setPrivateFirstName(String privateFirstName) {
		this.privateFirstName = privateFirstName;
	}

	public String getPrivateLastName() {
		return privateLastName;
	}

	public void setPrivateLastName(String privateLastName) {
		this.privateLastName = privateLastName;
	}

	public String getPrivateCin() {
		return privateCin;
	}

	public void setPrivateCin(String privateCin) {
		this.privateCin = privateCin;
	}

	public String getPrivateEmail() {
		return privateEmail;
	}

	public void setPrivateEmail(String privateEmail) {
		this.privateEmail = privateEmail;
	}

	public String getPrivatePhone() {
		return privatePhone;
	}

	public void setPrivatePhone(String privatePhone) {
		this.privatePhone = privatePhone;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	@Transient
	public Long getReactivity() {
		return reactivity;
	}

	public void setReactivity(Long reactivity) {
		this.reactivity = reactivity;
	}
	@Transient
	public Long getPerformance() {
		return performance;
	}

	public void setPerformance(Long performance) {
		this.performance = performance;
	}
	
	@Transient
	public Long getCountPendingTr() {
		return countPendingTr;
	}

	@Transient
	public void setCountPendingTr(Long countPendingTr) {
		this.countPendingTr = countPendingTr;
	}

}
