package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
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
import ma.azdad.utils.App;

@Entity
@Table(name = "users")
public class User extends GenericModel<String> implements Serializable {

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
	private Boolean vpnAccess = false;
	private Date birthday;

	private Boolean accountNonLocked;
	private Integer failedAttempt;
	private Date lockTime;

	// double auth
	private Boolean twoFactorAuthentication = false;
	private String twoFactorAuthenticationType; // totp,email
	private String verificationCode;
	private String totpSecret;

	// external systems
	private Boolean myhr = false;
	private Boolean mytools = false;
	private Boolean iexpense = false;
	private Boolean ilogistics = false;
	private Boolean sdm = false;

	private UserData userData = new UserData();
	private CompanyType companyType;
	private Company company;
	private Customer customer;
	private Supplier supplier;
	private String other;
	private Transporter transporter;

	// gps
	private Double latitude;
	private Double longitude;

	private User user;
	private Date date;

	private Integer countFiles = 0;

	private List<UserFile> fileList = new ArrayList<>();
	private List<UserHistory> historyList = new ArrayList<>();
	private List<UserVehicle> vehicleList = new ArrayList<UserVehicle>();

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

	public User(String username, String fullName, String email, Boolean internal) {
		this.username = username;
		this.fullName = fullName;
		this.email = email;
		this.internal = internal;
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

	public User(String username, String firstName, String lastName, String login, String photo, String email) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.photo = photo;
		this.email = email;

	}

	public User(String username, String photo, String fullName, String cin, String job, String email, String phone, Boolean active, CompanyType companyType, String companyName, String customerName,
			String supplierName) {
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
		try {
			return username.equals(((User) obj).getUsername());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean filter(String query) {
		return contains(query, fullName, job, cin);
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
	public Integer getTransporterId(){
		return transporter!=null?transporter.getId():null;
	}

	@Transient
	public void setTransporterId(Integer transporterId){
		if(transporter==null || !transporterId.equals(transporter.getId()))
			transporter=new Transporter();
		transporter.setId(transporterId);
	}

	@Transient
	public String getPublicPhoto() {
		return App.IADMIN.getLink() + "/photos/" + photo;
	}

	@Transient
	public String getName() {
		return this.fullName;
	}

	@Transient
	public String getEntityName() {
		switch (companyType) {
		case COMPANY:
		case CONSULTANT:
			return getCompanyName();
		case CUSTOMER:
			return getCustomerName();
		case SUPPLIER:
			return getSupplierName();
		default:
			return null;
		}
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
		return getIsPm();
	}

	@Transient
	public Boolean getIsPm() {
		return hasRole(Role.ROLE_ILOGISTICS_PM);
	}

	@Transient
	public Boolean getIsExternalPm() {
		return !internal && hasRole(Role.ROLE_ILOGISTICS_PM);
	}

	@Transient
	public Boolean getIsInternalPM() {
		return internal && hasRole(Role.ROLE_ILOGISTICS_PM);
	}

	@Transient
	public Boolean getIsExternalPM() {
		return !internal && hasRole(Role.ROLE_ILOGISTICS_PM);
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
	public Boolean getIsDriver() {
		return hasRole(Role.ROLE_ILOGISTICS_DRIVER);
	}
	
	@Transient
	public Boolean getIsTrAdmin() {
		return hasRole(Role.ROLE_ILOGISTICS_TR_ADMIN);
	}

	@Transient
	public Boolean getIsAdmin() {
		return hasRole(Role.ROLE_ILOGISTICS_ADMIN);
	}

	@Transient
	public Boolean getIsLobManager() {
		return hasRole(Role.ROLE_ILOGISTICS_LOB_MANAGER);
	}

	@Transient
	public Boolean getIsBuManager() {
		return hasRole(Role.ROLE_ILOGISTICS_BU_MANAGER);
	}

	@Column(name = "username", insertable = false, updatable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Enumerated(EnumType.STRING)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<UserFile> fileList) {
		this.fileList = fileList;
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public List<UserVehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<UserVehicle> vehicleList) {
		this.vehicleList = vehicleList;
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

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Integer getFailedAttempt() {
		return failedAttempt;
	}

	public void setFailedAttempt(Integer failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	@Transient
	public Boolean isEnabled() {
		return this.active;
	}

	@Transient
	public Boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public Boolean getMyhr() {
		return myhr;
	}

	public void setMyhr(Boolean myhr) {
		this.myhr = myhr;
	}

	public Boolean getMytools() {
		return mytools;
	}

	public void setMytools(Boolean mytools) {
		this.mytools = mytools;
	}

	public Boolean getIexpense() {
		return iexpense;
	}

	public void setIexpense(Boolean iexpense) {
		this.iexpense = iexpense;
	}

	public Boolean getIlogistics() {
		return ilogistics;
	}

	public void setIlogistics(Boolean ilogistics) {
		this.ilogistics = ilogistics;
	}

	public Boolean getSdm() {
		return sdm;
	}

	public void setSdm(Boolean sdm) {
		this.sdm = sdm;
	}

	@Transient
	public Boolean getIsCustomerUser() {
		return CompanyType.CUSTOMER.equals(companyType);
	}

	@Transient
	public Boolean getIsSupplierUser() {
		return CompanyType.SUPPLIER.equals(companyType);
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Boolean getVpnAccess() {
		return vpnAccess;
	}

	public void setVpnAccess(Boolean vpnAccess) {
		this.vpnAccess = vpnAccess;
	}

	public Boolean getTwoFactorAuthentication() {
		return twoFactorAuthentication;
	}

	public void setTwoFactorAuthentication(Boolean twoFactorAuthentication) {
		this.twoFactorAuthentication = twoFactorAuthentication;
	}

	public String getTwoFactorAuthenticationType() {
		return twoFactorAuthenticationType;
	}

	public void setTwoFactorAuthenticationType(String twoFactorAuthenticationType) {
		this.twoFactorAuthenticationType = twoFactorAuthenticationType;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getTotpSecret() {
		return totpSecret;
	}

	public void setTotpSecret(String totpSecret) {
		this.totpSecret = totpSecret;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
