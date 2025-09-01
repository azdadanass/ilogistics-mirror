package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.Period;

@Entity
public class Issue extends GenericModel<Integer> implements Serializable {

	private IssueParentType parentType;
	private IssueStatus status = IssueStatus.RAISED;
	private Severity severity;
	private IssueCategory category;
	private IssueType type;
	private Boolean blocking;
	private Boolean permanent;
	private Integer countFiles = 0;

	private String description;

	private Boolean confirmation;
	private Date confirmationDate;
	private String confirmationMethod;

	private Boolean confirmResolution;
	private Date resolutionDate;
	private String resolutionType;

	private CompanyType ownershipType;
	private Company company;
	private Customer customer;
	private Supplier supplier;
	private User ownershipUser;

	private CompanyType assignatorCompanyType;
	private Customer assignatorCustomer;
	private Supplier assignatorSupplier;
	private User assignator;

	private CompanyType confirmatorCompanyType;
	private Customer confirmatorCustomer;
	private Supplier confirmatorSupplier;
	private User confirmator;

	private Date date1; // raised
	private Date date8; // submitted
	private Date date2; // confirmed
	private Date date3; // assigned
	private Date date4; // resolved
	private Date date5; // ack
	private Date date6; // not resolved
	private Date date7; // closed

	private User user1;
	private User user8;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User user6;
	private User user7;

	private DeliveryRequest deliveryRequest;
	private TransportationRequest transportationRequest;

	private List<IssueFile> fileList = new ArrayList<>();
	private List<IssueHistory> historyList = new ArrayList<>();
	private List<IssueComment> commentList = new ArrayList<>();
	private List<ToNotify> toNotifyList = new ArrayList<>();

	// tmp
	private String tmpComment;
	private List<CommentGroup<IssueComment>> commentGroupList;
	private String tmpInternalOwnershipResourceUsername;
	private Integer tmpExternalOwnershipResourceId;

	public Issue() {
		super();
	}

	// c1
	public Issue(Integer id, IssueStatus status, Severity severity, IssueCategory category, IssueType type, Boolean blocking, //
			CompanyType ownershipType, Integer companyId, String companyName, Integer customerId, String customerName, Integer supplierId, String supplierName, //
			Integer deliveryRequestId, String deliveryRequestReference, DeliveryRequestType deliveryRequestType, DeliveryRequestStatus deliveryRequestStatus, Integer projectId,
			String projectName) {
		super(id);
		this.status = status;
		this.severity = severity;
		this.category = category;
		this.type = type;
		this.blocking = blocking;
		this.ownershipType = ownershipType;
		this.setCompanyId(companyId);
		this.setCompanyName(companyName);
		this.setCustomerId(customerId);
		this.setCustomerName(customerName);
		this.setSupplierId(supplierId);
		this.setSupplierName(supplierName);
		this.setDeliveryRequestId(deliveryRequestId);
		this.setDeliveryRequestReference(deliveryRequestReference);
		this.setDeliveryRequestType(deliveryRequestType);
		this.setDeliveryRequestStatus(deliveryRequestStatus);
		this.setProjectId(projectId);
		this.setProjectName(projectName);
	}

	public Issue(DeliveryRequest deliveryRequest) {
		super();
		this.parentType = IssueParentType.DN;
		this.deliveryRequest = deliveryRequest;
	}

	public Issue(TransportationRequest transportationRequest) {
		super();
		this.parentType = IssueParentType.TR;
		this.transportationRequest = transportationRequest;
	}

	@Transient
	public Boolean getIsDnIssue() {
		return IssueParentType.DN.equals(parentType);
	}

	@Transient
	public Boolean getIsTrIssue() {
		return IssueParentType.TR.equals(parentType);
	}

	@Override
	public boolean filter(String query) {
		return contains(category.getName(), query) || contains(description, query) || contains(type.getName(), query);
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	public void addFile(IssueFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(IssueFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(IssueHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(IssueHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(IssueComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(IssueComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<IssueComment>> map = new HashMap<>();
		for (IssueComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<IssueComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public String getReference() {
		return "IS" + String.format("%06d", id);
	}

	@Transient
	public Period getPeriod() {
		switch (status) {
		case SUBMITTED:
		case CONFIRMED:
		case ASSIGNED:
			return new Period(date8, new Date());
		case RESOLVED:
		case ACKNOWLEDGED:
			return new Period(date8, resolutionDate);
		default:
			return null;
		}
	}

	@Transient
	public DeliveryRequestType getDeliveryRequestType() {
		return deliveryRequest != null ? deliveryRequest.getType() : null;
	}

	@Transient
	public void setDeliveryRequestType(DeliveryRequestType deliveryRequestType) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setType(deliveryRequestType);
	}

	@Transient
	public DeliveryRequestStatus getDeliveryRequestStatus() {
		return deliveryRequest != null ? deliveryRequest.getStatus() : null;
	}

	@Transient
	public void setDeliveryRequestStatus(DeliveryRequestStatus deliveryRequestStatus) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setStatus(deliveryRequestStatus);
	}

	@Transient
	public String getParentNumero() {
		return deliveryRequest.getReference();
	}

	@Transient
	public String getParentStatus() {
		return deliveryRequest.getStatus().getValue();
	}

	@Transient
	public List<UserDescription> getUserList() {
		List<UserDescription> result = new ArrayList<UserDescription>();
		result.add(new UserDescription("Requester", user1));
		result.add(new UserDescription("Confirmator", confirmator));
		result.add(new UserDescription("Assignator", assignator));
		return result;
	}

	@Transient
	public Boolean getIsJrIssue() {
		return IssueParentType.JR.equals(parentType);
	}

	@Transient
	public Boolean getIsTicketIssue() {
		return IssueParentType.TICKET.equals(parentType);
	}

	@Transient
	public List<CommentGroup<IssueComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<IssueComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@Transient
	public Project getProject() {
		switch (parentType) {
		case DN:
			return deliveryRequest == null ? null : deliveryRequest.getProject();
		case TR:
			return transportationRequest == null ? null : transportationRequest.getDeliveryRequest().getProject();
		default:
			return null;
		}
	}

	@Transient
	public Integer getDeliveryRequestId() {
		return deliveryRequest == null ? null : deliveryRequest.getId();
	}

	@Transient
	public void setDeliveryRequestId(Integer deliveryRequestId) {
		if (deliveryRequest == null || !deliveryRequestId.equals(deliveryRequest.getId()))
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setId(deliveryRequestId);
	}

	@Transient
	public String getDeliveryRequestReference() {
		return deliveryRequest == null ? null : deliveryRequest.getReference();
	}

	@Transient
	public void setDeliveryRequestReference(String deliveryRequestReference) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setReference(deliveryRequestReference);
	}

	@Transient
	public Integer getProjectId() {
		return deliveryRequest == null ? null : deliveryRequest.getProjectId();
	}

	@Transient
	public void setProjectId(Integer projectId) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setProjectId(projectId);
	}

	@Transient
	public String getProjectName() {
		return deliveryRequest == null ? null : deliveryRequest.getProjectName();
	}

	@Transient
	public void setProjectName(String projectName) {
		if (deliveryRequest == null)
			deliveryRequest = new DeliveryRequest();
		deliveryRequest.setProjectName(projectName);
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

	@Transient
	public String getOwnership() {
		if (ownershipType == null)
			return null;
		switch (ownershipType) {
		case COMPANY:
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
	public String getOwnershipUserUsername() {
		return ownershipUser == null ? null : ownershipUser.getUsername();
	}

	@Transient
	public String getOwnershipUserFullName() {
		return ownershipUser == null ? null : ownershipUser.getFullName();
	}

	@Transient
	public String getOwnershipUserEmail() {
		return ownershipUser == null ? null : ownershipUser.getEmail();
	}

	@Transient
	public String getOwnershipUserPhone() {
		return ownershipUser == null ? null : ownershipUser.getPhone();
	}

	@Transient
	public void setOwnershipUserUsername(String ownershipUserUsername) {
		if (ownershipUser == null || !ownershipUser.getUsername().equals(ownershipUserUsername))
			ownershipUser = new User();
		ownershipUser.setUsername(ownershipUserUsername);
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
	public void addHistory(User user) {
		historyList.add(new IssueHistory(status.getValue(), user, null, this));
	}

	@Transient
	public void addHistory(String status, User user) {
		historyList.add(new IssueHistory(status, user, null, this));
	}

	@Transient
	public void addHistory(String status, User user, String description) {
		historyList.add(new IssueHistory(status, user, description, this));
	}

	@Transient
	public void addHistory(User user, String description) {
		historyList.add(new IssueHistory(status.getValue(), user, description, this));
	}

	@Transient
	public Integer getConfirmatorSupplierId() {
		return confirmatorSupplier != null ? confirmatorSupplier.getId() : null;
	}

	@Transient
	public void setConfirmatorSupplierId(Integer confirmatorSupplierId) {
		if (confirmatorSupplier == null || !confirmatorSupplierId.equals(confirmatorSupplier.getId()))
			confirmatorSupplier = new Supplier();
		confirmatorSupplier.setId(confirmatorSupplierId);
	}

	@Transient
	public Integer getConfirmatorCustomerId() {
		return confirmatorCustomer != null ? confirmatorCustomer.getId() : null;
	}

	@Transient
	public void setConfirmatorCustomerId(Integer confirmatorCustomerId) {
		if (confirmatorCustomer == null || !confirmatorCustomerId.equals(confirmatorCustomer.getId()))
			confirmatorCustomer = new Customer();
		confirmatorCustomer.setId(confirmatorCustomerId);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<IssueFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<IssueFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<IssueHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<IssueHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<IssueComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<IssueComment> commentList) {
		this.commentList = commentList;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public IssueCategory getCategory() {
		return category;
	}

	public void setCategory(IssueCategory category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public IssueType getType() {
		return type;
	}

	public void setType(IssueType type) {
		this.type = type;
	}

	public Boolean getPermanent() {
		return permanent;
	}

	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(Date date3) {
		this.date3 = date3;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public Date getDate6() {
		return date6;
	}

	public void setDate6(Date date6) {
		this.date6 = date6;
	}

	public Date getDate7() {
		return date7;
	}

	public void setDate7(Date date7) {
		this.date7 = date7;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser6() {
		return user6;
	}

	public void setUser6(User user6) {
		this.user6 = user6;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser7() {
		return user7;
	}

	public void setUser7(User user7) {
		this.user7 = user7;
	}

	@Transient
	public String getTmpComment() {
		return tmpComment;
	}

	@Transient
	public void setTmpComment(String tmpComment) {
		this.tmpComment = tmpComment;
	}

	public Boolean getBlocking() {
		return blocking;
	}

	public void setBlocking(Boolean blocking) {
		this.blocking = blocking;
	}

	@Enumerated(EnumType.STRING)
	public CompanyType getOwnershipType() {
		return ownershipType;
	}

	public void setOwnershipType(CompanyType ownershipType) {
		this.ownershipType = ownershipType;
	}

	@Transient
	public String getTmpInternalOwnershipResourceUsername() {
		return tmpInternalOwnershipResourceUsername;
	}

	@Transient
	public void setTmpInternalOwnershipResourceUsername(String tmpInternalOwnershipResourceUsername) {
		this.tmpInternalOwnershipResourceUsername = tmpInternalOwnershipResourceUsername;
	}

	@Transient
	public Integer getTmpExternalOwnershipResourceId() {
		return tmpExternalOwnershipResourceId;
	}

	@Transient
	public void setTmpExternalOwnershipResourceId(Integer tmpExternalOwnershipResourceId) {
		this.tmpExternalOwnershipResourceId = tmpExternalOwnershipResourceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getDate4() {
		return date4;
	}

	public void setDate4(Date date4) {
		this.date4 = date4;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	public Date getDate5() {
		return date5;
	}

	public void setDate5(Date date5) {
		this.date5 = date5;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getOwnershipUser() {
		return ownershipUser;
	}

	public void setOwnershipUser(User ownershipUser) {
		this.ownershipUser = ownershipUser;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public IssueParentType getParentType() {
		return parentType;
	}

	public void setParentType(IssueParentType parentType) {
		this.parentType = parentType;
	}

	public Date getDate8() {
		return date8;
	}

	public void setDate8(Date date8) {
		this.date8 = date8;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser8() {
		return user8;
	}

	public void setUser8(User user8) {
		this.user8 = user8;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getAssignator() {
		return assignator;
	}

	public void setAssignator(User assignator) {
		this.assignator = assignator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getConfirmator() {
		return confirmator;
	}

	public void setConfirmator(User confirmator) {
		this.confirmator = confirmator;
	}

	@Transient
	public String getConfirmatorUsername() {
		return confirmator != null ? confirmator.getUsername() : null;
	}

	@Transient
	public void setConfirmatorUsername(String confirmatorUsername) {
		if (confirmator == null || !confirmatorUsername.equals(confirmator.getUsername()))
			confirmator = new User();
		confirmator.setUsername(confirmatorUsername);
	}

	@Transient
	public String getConfirmatorFullName() {
		return confirmator != null ? confirmator.getFullName() : null;
	}

	@Transient
	public void setConfirmatorFullName(String confirmatorFullName) {
		if (confirmator == null)
			confirmator = new User();
		confirmator.setFullName(confirmatorFullName);
	}

	@Transient
	public String getAssignatorUsername() {
		return assignator != null ? assignator.getUsername() : null;
	}

	@Transient
	public void setAssignatorUsername(String assignatorUsername) {
		if (assignator == null || !assignatorUsername.equals(assignator.getUsername()))
			assignator = new User();
		assignator.setUsername(assignatorUsername);
	}

	@Transient
	public String getAssignatorFullName() {
		return assignator != null ? assignator.getFullName() : null;
	}

	@Transient
	public void setAssignatorFullName(String assignatorFullName) {
		if (assignator == null)
			assignator = new User();
		assignator.setFullName(assignatorFullName);
	}

	@Enumerated(EnumType.STRING)
	public CompanyType getAssignatorCompanyType() {
		return assignatorCompanyType;
	}

	public void setAssignatorCompanyType(CompanyType assignatorCompanyType) {
		this.assignatorCompanyType = assignatorCompanyType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getAssignatorCustomer() {
		return assignatorCustomer;
	}

	public void setAssignatorCustomer(Customer assignatorCustomer) {
		this.assignatorCustomer = assignatorCustomer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getAssignatorSupplier() {
		return assignatorSupplier;
	}

	public void setAssignatorSupplier(Supplier assignatorSupplier) {
		this.assignatorSupplier = assignatorSupplier;
	}

	@Transient
	public Integer getAssignatorCustomerId() {
		return assignatorCustomer != null ? assignatorCustomer.getId() : null;
	}

	@Transient
	public void setAssignatorCustomerId(Integer assignatorCustomerId) {
		if (assignatorCustomer == null || !assignatorCustomerId.equals(assignatorCustomer.getId()))
			assignatorCustomer = new Customer();
		assignatorCustomer.setId(assignatorCustomerId);
	}

	@Transient
	public Integer getAssignatorSupplierId() {
		return assignatorSupplier != null ? assignatorSupplier.getId() : null;
	}

	@Transient
	public void setAssignatorSupplierId(Integer assignatorSupplierId) {
		if (assignatorSupplier == null || !assignatorSupplierId.equals(assignatorSupplier.getId()))
			assignatorSupplier = new Supplier();
		assignatorSupplier.setId(assignatorSupplierId);
	}

	@Enumerated(EnumType.STRING)
	public CompanyType getConfirmatorCompanyType() {
		return confirmatorCompanyType;
	}

	public void setConfirmatorCompanyType(CompanyType confirmatorCompanyType) {
		this.confirmatorCompanyType = confirmatorCompanyType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getConfirmatorCustomer() {
		return confirmatorCustomer;
	}

	public void setConfirmatorCustomer(Customer confirmatorCustomer) {
		this.confirmatorCustomer = confirmatorCustomer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getConfirmatorSupplier() {
		return confirmatorSupplier;
	}

	public void setConfirmatorSupplier(Supplier confirmatorSupplier) {
		this.confirmatorSupplier = confirmatorSupplier;
	}

	public Boolean getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(Boolean confirmation) {
		this.confirmation = confirmation;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public String getConfirmationMethod() {
		return confirmationMethod;
	}

	public void setConfirmationMethod(String confirmationMethod) {
		this.confirmationMethod = confirmationMethod;
	}

	public Boolean getConfirmResolution() {
		return confirmResolution;
	}

	public void setConfirmResolution(Boolean confirmResolution) {
		this.confirmResolution = confirmResolution;
	}

	public Date getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Date resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	public String getResolutionType() {
		return resolutionType;
	}

	public void setResolutionType(String resolutionType) {
		this.resolutionType = resolutionType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ToNotify> getToNotifyList() {
		return toNotifyList;
	}

	public void setToNotifyList(List<ToNotify> toNotifyList) {
		this.toNotifyList = toNotifyList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public TransportationRequest getTransportationRequest() {
		return transportationRequest;
	}

	public void setTransportationRequest(TransportationRequest transportationRequest) {
		this.transportationRequest = transportationRequest;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@Transient
	public String getTransportationRequestReference() {
		return transportationRequest != null ? transportationRequest.getReference() : null;
	}

	@Transient
	public void setTransportationRequestReference(String transportationRequestReference) {
		if (transportationRequest == null)
			transportationRequest = new TransportationRequest();
		transportationRequest.setReference(transportationRequestReference);
	}

	@Transient
	public Integer getTransportationRequestId() {
		return transportationRequest != null ? transportationRequest.getId() : null;
	}

	@Transient
	public void setTransportationRequestId(Integer transportationRequestId) {
		if (transportationRequest == null || !transportationRequestId.equals(transportationRequest.getId()))
			transportationRequest = new TransportationRequest();
		transportationRequest.setId(transportationRequestId);
	}

}
