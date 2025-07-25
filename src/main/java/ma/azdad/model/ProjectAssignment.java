package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
public class ProjectAssignment extends GenericModel<Integer> implements Serializable {

	private ProjectAssignmentType type;
	private Date startDate;
	private Date endDate;

	private User user;
	private Project project;
	private Team team;
	private Supplier supplier;
	private Customer customer;
	private ProjectAssignment parent;

	public void init() {
	}

	public ProjectAssignment() {
		super();
	}

	public ProjectAssignment(Team team) {
		super();
		this.team = team;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// c1
	public ProjectAssignment(Integer id, ProjectAssignmentType type, Date startDate, Date endDate, //
			String projectName, String userUsername, String userFullName, Boolean userActive, //
			String teamName, String teamType, Boolean teamActive, String supplierName, String supplierPhoto, //
			String customerName, String customerPhoto, String teamLeaderUsername) {
		super(id);
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.setProjectName(projectName);
		this.setUserUsername(userUsername);
		this.setUserFullName(userFullName);
		this.setUserActive(userActive);
		this.setTeamName(teamName);
		this.setTeamType(teamType);
		this.setTeamActive(teamActive);
		this.setSupplierName(supplierName);
		this.setSupplierPhoto(supplierPhoto);
		this.setCustomerName(customerName);
		this.setCustomerPhoto(customerPhoto);
		this.setTeamLeaderUsername(teamLeaderUsername);
	}

	@Transient
	public String getAssignedName() {
		switch (type) {
		case INTERNAL:
		case EXTERNAL_PM:
			return getUserFullName();
		case SUPPLIER:
			return getSupplierName();
		case CUSTOMER:
			return getCustomerName();
		case INTERNAL_TEAM:
		case EXTERNAL_TEAM:
			return getTeamName();
		default:
			return null;
		}
	}

	@Transient
	public Boolean getIsActive() {
		return UtilsFunctions.isBetween(new Date(), startDate, endDate);
	}

	@Transient
	public Integer getProjectId() {
		return project == null ? null : project.getId();
	}

	@Transient
	public void setProjectId(Integer projectId) {
		if (project == null || !projectId.equals(project.getId()))
			project = new Project();
		project.setId(projectId);
	}

	@Transient
	public String getProjectName() {
		return project == null ? null : project.getName();
	}

	@Transient
	public void setProjectName(String projectName) {
		if (project == null)
			project = new Project();
		project.setName(projectName);
	}

	@Transient
	public String getTeamName() {
		return team == null ? null : team.getName();
	}

	@Transient
	public void setTeamName(String teamName) {
		if (team == null)
			team = new Team();
		team.setName(teamName);
	}

	@Transient
	public Boolean getTeamActive() {
		return team == null ? null : team.getActive();
	}

	@Transient
	public void setTeamActive(Boolean teamActive) {
		if (team == null)
			team = new Team();
		team.setActive(teamActive);
	}

	@Transient
	public String getTeamLeaderUsername() {
		return team == null ? null : team.getTeamLeaderUsername();
	}

	@Transient
	public void setTeamLeaderUsername(String teamLeaderUsername) {
		if (team == null)
			team = new Team();
		team.setTeamLeaderUsername(teamLeaderUsername);
	}

	@Transient
	public String getTeamType() {
		return team == null ? null : team.getType();
	}

	@Transient
	public void setTeamType(String teamType) {
		if (team == null)
			team = new Team();
		team.setType(teamType);
	}

	@Transient
	public String getUserFullName() {
		return user == null ? null : user.getFullName();
	}

	@Transient
	public void setUserFullName(String userFullName) {
		if (user == null)
			user = new User();
		user.setFullName(userFullName);
	}

	@Transient
	public Boolean getUserActive() {
		return user == null ? null : user.getActive();
	}

	@Transient
	public void setUserActive(Boolean userActive) {
		if (user == null)
			user = new User();
		user.setActive(userActive);
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
	public String getSupplierPhoto() {
		return supplier == null ? null : supplier.getPhoto();
	}

	@Transient
	public void setSupplierPhoto(String supplierPhoto) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setPhoto(supplierPhoto);
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
	public String getCustomerPhoto() {
		return customer == null ? null : customer.getPhoto();
	}

	@Transient
	public void setCustomerPhoto(String customerPhoto) {
		if (customer == null)
			customer = new Customer();
		customer.setPhoto(customerPhoto);
	}

	@Transient
	public String getUserUsername() {
		return user == null ? null : user.getUsername();
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null || !userUsername.equals(user.getUsername()))
			user = new User();
		user.setUsername(userUsername);
	}

	@Transient
	public Integer getTeamId() {
		return team == null ? null : team.getId();
	}

	@Transient
	public void setTeamId(Integer teamId) {
		if (team == null || !teamId.equals(team.getId()))
			team = new Team();
		team.setId(teamId);
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
	public Integer getCustomerId() {
		return customer == null ? null : customer.getId();
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || !customerId.equals(customer.getId()))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getProjectName(), getUserUsername(), getUserFullName(), getTeamName(), getSupplierName());
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Enumerated(EnumType.STRING)
	public ProjectAssignmentType getType() {
		return type;
	}

	public void setType(ProjectAssignmentType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ProjectAssignment getParent() {
		return parent;
	}

	public void setParent(ProjectAssignment parent) {
		this.parent = parent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
