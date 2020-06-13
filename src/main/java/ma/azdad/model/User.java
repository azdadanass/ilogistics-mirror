package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private Boolean internal = false;
	private String username;
	private String login;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private String email2;
	private String password;
	private String photo;
	private String phone;
	private String phone2;
	private String job;
	private String cin;
	private Boolean gender = true;
	private Boolean active = true;
	private Date birthday;

	private UserData userData = new UserData();
	private CompanyType companyType;
	private Company company;
	private Customer customer;
	private Supplier supplier;
	private String other;
	private Transporter transporter;

	private User user;
	private Date date;

	private Integer countFiles = 0;

	private List<UserFile> fileList = new ArrayList<>();
	private List<UserHistory> historyList = new ArrayList<>();

	private List<UserRole> roleList = new ArrayList<>();

	// TMP
//	private Boolean isUser = false;
//	private Boolean isSE = false;
//	private Boolean isPM = false;
//	private Boolean isWM = false;
//	private Boolean isTM = false;
//	private Boolean isAdmin = false;

	private User lineManager;

	public User() {

	}

	public User(String username, String fullName) {
		super();
		this.username = username;
		this.fullName = fullName;
	}

	public User(String username, String fullName, String photo, String email, String job) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.photo = photo;
		this.email = email;
		this.job = job;
	}

	public User(String username, String fullName, String photo, String email, String job, String phone, String cin) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.photo = photo;
		this.email = email;
		this.job = job;
		this.phone = phone;
		this.cin = cin;
	}

	public User(String username, String photo, String fullName, String cin, String job, String email, String phone, Boolean active, CompanyType companyType, String companyName, String customerName, String supplierName) {
		this.username = username;
		this.photo = photo;
		this.fullName = fullName;
		this.cin = cin;
		this.job = job;
		this.email = email;
		this.phone = phone;
		this.active = active;
		this.companyType = companyType;
		setCompanyName(companyName);
		setCustomerName(customerName);
		setSupplierName(supplierName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public boolean filter(String query) {
		return contains(fullName, query) || contains(job, query) || contains(cin, query);
	}

	protected Boolean contains(String string, String query) {
		return string != null && string.toLowerCase().contains(query);
	}

	protected Boolean contains(Double d, String query) {
		return d != null && String.valueOf(d).toLowerCase().contains(query);
	}

	protected Boolean contains(Date date, String query) {
		return date != null && UtilsFunctions.getFormattedDate(date).toLowerCase().contains(query);
	}

	@Transient
	public String getPublicPhoto() {
		return "https://iadmin.orange.telodigital.com/photos/" + photo;
	}

	@Transient
	public String getName() {
		return this.fullName;
	}

	@Transient
	public String getCompanyName() {
		return company == null ? null : company.getName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
	}

	@Transient
	public String getCustomerName() {
		return customer == null ? null : customer.getName();
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (customer == null)
			customer = new Customer();
		customer.setName(customerName);
	}

	@Transient
	public String getSupplierName() {
		return supplier == null ? null : supplier.getName();
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(supplierName);
	}

	@Transient
	public Integer getCustomerId() {
		return customer == null ? null : customer.getId();
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || !customerId.equals(customer.getId()))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Transient
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !companyId.equals(company.getId()))
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public Integer getSupplierId() {
		return supplier == null ? null : supplier.getId();
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplierId.equals(supplier.getId()))
			supplier = new Supplier();
		supplier.setId(supplierId);
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

//	@Transient
//	public void setCompany(Customer customer, Supplier supplier, String company) {
//		switch (companyType) {
//		case CUSTOMER:
//			this.customer = customer;
//			this.supplier = null;
//			this.company = null;
//			break;
//		case SUPPLIER:
//			this.customer = null;
//			this.supplier = supplier;
//			this.company = null;
//			break;
//		case OTHER:
//			this.customer = null;
//			this.supplier = null;
//			this.company = company;
//		}
//	}

	public void addRole(UserRole role) {
		if (roleList.contains(role))
			return;
		role.setUser(this);
		roleList.add(role);
	}

	public void removeRole(UserRole role) {
		roleList.remove(role);
		role.setUser(null);
	}

	public void toggleRole(Role role) {
		if (hasRole(role))
			removeRole(roleList.stream().filter(i -> i.getRole().equals(role)).findFirst().get());
		else
			addRole(new UserRole(role));
	}

	public void addFile(UserFile file) {
		file.setUser(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(UserFile file) {
		file.setUser(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addUserData(UserData userData) {
		this.userData = userData;
		userData.setUser(this);
	}

	public void addHistory(UserHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(UserHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean hasRole(Role role) {
		return roleList.stream().filter(i -> i.getRole().equals(role)).count() > 0;
	}

	@Transient
	public Boolean getIsSE() {
		return hasRole(Role.ROLE_ILOGISTICS_SE);
	}

	@Transient
	public Boolean getIsUser() {
		return hasRole(Role.ROLE_ILOGISTICS_USER);
	}

	@Transient
	public Boolean getIsPM() {
		return hasRole(Role.ROLE_ILOGISTICS_PM);
	}

	@Transient
	public Boolean getIsWM() {
		return hasRole(Role.ROLE_ILOGISTICS_WM);
	}

	@Transient
	public Boolean getIsTM() {
		return hasRole(Role.ROLE_ILOGISTICS_TM);
	}

	@Transient
	public Boolean getIsAdmin() {
		return hasRole(Role.ROLE_ILOGISTICS_ADMIN);
	}

	@Transient
	public Boolean getIsLobManager() {
		return hasRole(Role.ROLE_ILOGISTICS_LOB_MANAGER);
	}

	@Id
	@Column(nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "info")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "fullname")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "id_photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public List<UserRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<UserRole> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Transient
	public String getUserUsername() {
		return username;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	@Column(name = "info2")
	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Enumerated
	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	@Column(name = "firstname")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "lastname")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<UserHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<UserFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

	@Transient
	public User getLineManager() {
		return lineManager;
	}

	@Transient
	public void setLineManager(User lineManager) {
		this.lineManager = lineManager;
	}

}
