package ma.azdad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class ExternalResource extends GenericBean {

	private String firstName;
	private String lastName;
	private String fullName;
	private String job;
	private String cin;
	private String email;
	private String email2;
	private String password;
	private String description;
	private Boolean active = true;
	private String photo = "photos/nopict.jpg";
	private Date birthday;
	private Boolean gender;
	private String passportId;
	private Date passportExpireDate;
	private String driveLicenceId;
	private String driveLicenceType;
	private Date driveLicenceIssuedDate;
	private Date driveLicenceExpireDate;

	private String phone;
	private String phone2;
	private String businessPhone;
	private String businessFax;
	private String homeAddress;
	private String homePhone;
	private String emergencyName1;
	private String emergencyPhone1;
	private String emergencyName2;
	private String emergencyPhone2;
	private String skypeId;
	private String imId;

	private User user;

	private Transporter transporter;

	private CompanyType companyType;
	private Customer customer;
	private Supplier supplier;
	private String company;

	private List<ExternalResourceRole> roleList = new ArrayList<ExternalResourceRole>();
	private List<ExternalResourceFile> fileList = new ArrayList<>();
	private List<ExternalResourceHistory> historyList = new ArrayList<>();
	private List<ExternalResourceProjectAssignment> assignmentList = new ArrayList<>();

	// TMP
	private Boolean isUser = false;
	private Boolean isSE = false;
	private Boolean isPM = false;
	private Boolean isWM = false;
	private Boolean isTM = false;
	private Boolean isAdmin = false;

	public ExternalResource() {
		super();
	}

	public ExternalResource(Transporter transporter) {
		super();
		this.transporter = transporter;
	}

	public ExternalResource(Integer id, String fullName) {
		super(id);
		this.fullName = fullName;
	}

	public ExternalResource(Integer id, String fullName, String cin) {
		super(id);
		this.fullName = fullName;
		this.cin = cin;
	}

	public ExternalResource(Integer id, String photo, String fullName, String job, String email, String phone, Boolean active, CompanyType companyType, String company, String customerName, String supplierName) {
		super(id);
		this.photo = photo;
		this.fullName = fullName;
		this.job = job;
		this.email = email;
		this.phone = phone;
		this.active = active;
		this.companyType = companyType;
		this.company = customerName != null ? customerName : supplierName != null ? supplierName : company;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && firstName != null)
			result = firstName.toLowerCase().contains(query);
		if (!result && lastName != null)
			result = lastName.toLowerCase().contains(query);
		if (!result && cin != null)
			result = cin.toLowerCase().contains(query);
		return result;
	}

	public void addRole(ExternalResourceRole role) {
		System.out.println(roleList.contains(role));
		if (roleList.contains(role))
			return;
		role.setExternalResource(this);
		roleList.add(role);
	}

	public void removeRole(ExternalResourceRole role) {
		roleList.remove(role);
		role.setExternalResource(null);
	}

	public void toggleRole(Role role) {
		if (hasRole(role))
			removeRole(roleList.stream().filter(i -> i.getRole().equals(role)).findFirst().get());
		else
			addRole(new ExternalResourceRole(role));
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

	@Transient
	public Boolean hasRole(Role role) {
		return roleList.stream().filter(i -> i.getRole().equals(role)).count() > 0;
	}

	public void initRoles() {
		isUser = hasRole(Role.ROLE_ILOGISTICS_USER);
		isSE = hasRole(Role.ROLE_ILOGISTICS_SE);
		isPM = hasRole(Role.ROLE_ILOGISTICS_PM);
		isWM = hasRole(Role.ROLE_ILOGISTICS_WM);
		isTM = hasRole(Role.ROLE_ILOGISTICS_TM);
		isAdmin = hasRole(Role.ROLE_ILOGISTICS_ADMIN);
	}

//	@Transient
//	public String getCompanyName() {
//		if (companyType == null)
//			return null;
//		switch (companyType) {
//		case CUSTOMER:
//			return customer.getName();
//		case SUPPLIER:
//			return supplier.getName();
//		case OTHER:
//			return company;
//		default:
//			return null;
//		}
//	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<ExternalResourceFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ExternalResourceFile> fileList) {
		this.fileList = fileList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<ExternalResourceHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<ExternalResourceHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externalResource", cascade = CascadeType.ALL)
	public List<ExternalResourceProjectAssignment> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(List<ExternalResourceProjectAssignment> assignmentList) {
		this.assignmentList = assignmentList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "user_username")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	@Enumerated
	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
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
	public Integer getSupplierId() {
		return supplier == null ? null : supplier.getId();
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplierId.equals(supplier.getId()))
			supplier = new Supplier();
		supplier.setId(supplierId);
	}

	@Column(nullable = false, columnDefinition = "tinyint default true")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(columnDefinition = "VARCHAR(255) default 'photos/nopict.jpg'")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getPassportId() {
		return passportId;
	}

	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}

	@Temporal(TemporalType.DATE)
	public Date getPassportExpireDate() {
		return passportExpireDate;
	}

	public void setPassportExpireDate(Date passportExpireDate) {
		this.passportExpireDate = passportExpireDate;
	}

	public String getDriveLicenceId() {
		return driveLicenceId;
	}

	public void setDriveLicenceId(String driveLicenceId) {
		this.driveLicenceId = driveLicenceId;
	}

	public String getDriveLicenceType() {
		return driveLicenceType;
	}

	public void setDriveLicenceType(String driveLicenceType) {
		this.driveLicenceType = driveLicenceType;
	}

	@Temporal(TemporalType.DATE)
	public Date getDriveLicenceIssuedDate() {
		return driveLicenceIssuedDate;
	}

	public void setDriveLicenceIssuedDate(Date driveLicenceIssuedDate) {
		this.driveLicenceIssuedDate = driveLicenceIssuedDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getDriveLicenceExpireDate() {
		return driveLicenceExpireDate;
	}

	public void setDriveLicenceExpireDate(Date driveLicenceExpireDate) {
		this.driveLicenceExpireDate = driveLicenceExpireDate;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getBusinessFax() {
		return businessFax;
	}

	public void setBusinessFax(String businessFax) {
		this.businessFax = businessFax;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getEmergencyName1() {
		return emergencyName1;
	}

	public void setEmergencyName1(String emergencyName1) {
		this.emergencyName1 = emergencyName1;
	}

	public String getEmergencyPhone1() {
		return emergencyPhone1;
	}

	public void setEmergencyPhone1(String emergencyPhone1) {
		this.emergencyPhone1 = emergencyPhone1;
	}

	public String getEmergencyName2() {
		return emergencyName2;
	}

	public void setEmergencyName2(String emergencyName2) {
		this.emergencyName2 = emergencyName2;
	}

	public String getEmergencyPhone2() {
		return emergencyPhone2;
	}

	public void setEmergencyPhone2(String emergencyPhone2) {
		this.emergencyPhone2 = emergencyPhone2;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externalResource", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ExternalResourceRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<ExternalResourceRole> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return getFullName();
	}

	@Transient
	public Boolean getIsUser() {
		return isUser;
	}

	@Transient
	public void setIsUser(Boolean isUser) {
		this.isUser = isUser;
	}

	@Transient
	public Boolean getIsSE() {
		return isSE;
	}

	@Transient
	public void setIsSE(Boolean isSE) {
		this.isSE = isSE;
	}

	@Transient
	public Boolean getIsPM() {
		return isPM;
	}

	@Transient
	public void setIsPM(Boolean isPM) {
		this.isPM = isPM;
	}

	@Transient
	public Boolean getIsWM() {
		return isWM;
	}

	@Transient
	public void setIsWM(Boolean isWM) {
		this.isWM = isWM;
	}

	@Transient
	public Boolean getIsTM() {
		return isTM;
	}

	@Transient
	public void setIsTM(Boolean isTM) {
		this.isTM = isTM;
	}

	@Transient
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	@Transient
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
