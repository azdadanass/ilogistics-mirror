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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;

@Entity

public class Project extends GenericModel<Integer> {

	private Integer id;
	private String name;
	private String status;
	private String type;
	private String subType;
	private Costcenter costcenter;
	private Contract contract;
	private Currency currency;
	private User manager;
	private Customer customer;
	private Date startDate;
	private Date endDate;
	private String duration;
	private ProjectCustomerType customerType;
	private String category;

	private Boolean companyWarehousing = true;
	private Boolean companyStockManagement = true;

	private Boolean supplierWarehousing = false;
	private Boolean supplierStockManagement = false;

	private Boolean customerWarehousing = false;
	private Boolean customerStockManagement = false;

	private Boolean sdm = false;
	private Boolean ism = false;

	// ilogistics
	private Integer approximativeStoragePeriod;
	private Warehouse preferredWarehouse;
	private Location preferredLocation;

	private List<ProjectManager> managerList = new ArrayList<>();

	// tmp
	private Integer tmpCustomerId;
	private Boolean hasStock;

	public Project() {
	}

	public Project(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Project(Integer id, String name, Boolean hasStock) {
		super();
		this.id = id;
		this.name = name;
		this.hasStock = hasStock;
	}

	public Project(Integer id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public Project(Integer id, String name, String type, String managerFullName) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.setManagerFullName(managerFullName);
	}

	public Project(Integer id, String name, String type, Date startDate, Date endDate, Integer tmpCustomerId) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.tmpCustomerId = tmpCustomerId;
	}

	// c2
	public Project(Integer id, String name, String type, String subType, String status, Date startDate, Date endDate, //
			String customerName, Boolean companyWarehousing, Boolean companyStockManagement, Boolean customerWarehousing, Boolean customerStockManagement, //
			Boolean supplierWarehousing, Boolean supplierStockManagement, //
			Boolean sdm, Boolean ism, //
			String managerFullName) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.subType = subType;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.setCustomerName(customerName);
		this.customerWarehousing = customerWarehousing;
		this.customerStockManagement = customerStockManagement;
		this.companyWarehousing = companyWarehousing;
		this.companyStockManagement = companyStockManagement;
		this.supplierWarehousing = supplierWarehousing;
		this.supplierStockManagement = supplierStockManagement;
		this.sdm = sdm;
		this.ism = ism;
		this.setManagerFullName(managerFullName);
	}

	public boolean filter(String query) {
		boolean result = false;
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && type != null)
			result = type.toLowerCase().contains(query);
		if (!result && subType != null)
			result = subType.toLowerCase().contains(query);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return id.equals(((Project) obj).getId());
		} catch (Exception e) {
			return false;
		}
	}
	
	
	@Transient
	public Boolean getWarehousing() {
		return Boolean.TRUE.equals(companyWarehousing) ||Boolean.TRUE.equals(customerWarehousing)||Boolean.TRUE.equals(supplierWarehousing);  
	}
	
	@Transient
	public Boolean getStockManagement() {
		return Boolean.TRUE.equals(companyStockManagement) ||Boolean.TRUE.equals(customerStockManagement)||Boolean.TRUE.equals(supplierStockManagement);  
	}
	

	@Transient
	public Integer getPreferredWarehouseId() {
		return preferredWarehouse != null ? preferredWarehouse.getId() : null;
	}

	@Transient
	public void setPreferredWarehouseId(Integer preferredWarehouseId) {
		if (preferredWarehouse == null || !preferredWarehouseId.equals(preferredWarehouse.getId()))
			preferredWarehouse = new Warehouse();
		preferredWarehouse.setId(preferredWarehouseId);
	}

	@Transient
	public Integer getPreferredLocationId() {
		return preferredLocation != null ? preferredLocation.getId() : null;
	}

	@Transient
	public void setPreferredLocationId(Integer preferredLocationId) {
		if (preferredLocation == null || !preferredLocationId.equals(preferredLocation.getId()))
			preferredLocation = new Location();
		preferredLocation.setId(preferredLocationId);
	}

	@Transient
	public String getPreferredLocationName() {
		return preferredLocation != null ? preferredLocation.getName() : null;
	}

	@Transient
	public void setPreferredLocationName(String preferredLocationName) {
		if (preferredLocation == null)
			preferredLocation = new Location();
		preferredLocation.setName(preferredLocationName);
	}

	@Transient
	public String getPreferredWarehouseName() {
		return preferredWarehouse != null ? preferredWarehouse.getName() : null;
	}

	@Transient
	public void setPreferredWarehouseName(String preferredWarehouseName) {
		if (preferredWarehouse == null)
			preferredWarehouse = new Warehouse();
		preferredWarehouse.setName(preferredWarehouseName);
	}

	@Transient
	public Integer getCustomerId() {
		return customer != null ? customer.getId() : null;
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || ObjectUtils.notEqual(customerId, customer.getId()))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Transient
	public Boolean getIsStockProject() {
		return ProjectTypes.STOCK.getValue().equals(type);
	}

	@Transient
	public String getManagerFullName() {
		return manager == null ? null : manager.getFullName();
	}

	@Transient
	public void setManagerFullName(String managerFullName) {
		if (manager == null)
			manager = new User();
		manager.setFullName(managerFullName);
	}

	@Transient
	public String getManagerEmail() {
		return manager == null ? null : manager.getEmail();
	}

	@Transient
	public void setManagerEmail(String managerEmail) {
		if (manager == null)
			manager = new User();
		manager.setEmail(managerEmail);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idproject", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_idmanager")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + "]\n";
	}

	@Column(name = "project_type", length = 45)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "costcenter_idcostcenter", nullable = false)
	public Costcenter getCostcenter() {
		return this.costcenter;
	}

	public void setCostcenter(Costcenter costcenter) {
		this.costcenter = costcenter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_idcustomer")
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startdate", length = 10)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "enddate", length = 10)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public Boolean getCustomerWarehousing() {
		return customerWarehousing;
	}

	public void setCustomerWarehousing(Boolean customerWarehousing) {
		this.customerWarehousing = customerWarehousing;
	}

	public Boolean getCustomerStockManagement() {
		return customerStockManagement;
	}

	public void setCustomerStockManagement(Boolean customerStockManagement) {
		this.customerStockManagement = customerStockManagement;
	}

	@Transient
	public Integer getTmpCustomerId() {
		return tmpCustomerId;
	}

	@Transient
	public String getCustomerName() {
		if (customer == null)
			return null;
		return customer.getName();
	}

	@Transient
	public void setCustomerName(String name) {
		if (customer == null)
			customer = new Customer();
		customer.setName(name);
	}

	@Transient
	public String getCustomerPhoto() {
		if (customer == null)
			return null;
		return customer.getPhoto();
	}

	@Transient
	public void setCustomerPhoto(String name) {
		if (customer == null)
			customer = new Customer();
		customer.setPhoto(name);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectManager> getManagerList() {
		return managerList;
	}

	public void setManagerList(List<ProjectManager> managerList) {
		this.managerList = managerList;
	}

	public Boolean getSdm() {
		return sdm;
	}

	public void setSdm(Boolean sdm) {
		this.sdm = sdm;
	}

	@Enumerated(EnumType.STRING)
	public ProjectCustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(ProjectCustomerType customerType) {
		this.customerType = customerType;
	}

	public Boolean getCompanyWarehousing() {
		return companyWarehousing;
	}

	public void setCompanyWarehousing(Boolean companyWarehousing) {
		this.companyWarehousing = companyWarehousing;
	}

	public Boolean getCompanyStockManagement() {
		return companyStockManagement;
	}

	public void setCompanyStockManagement(Boolean companyStockManagement) {
		this.companyStockManagement = companyStockManagement;
	}

	public Boolean getSupplierWarehousing() {
		return supplierWarehousing;
	}

	public void setSupplierWarehousing(Boolean supplierWarehousing) {
		this.supplierWarehousing = supplierWarehousing;
	}

	public Boolean getSupplierStockManagement() {
		return supplierStockManagement;
	}

	public void setSupplierStockManagement(Boolean supplierStockManagement) {
		this.supplierStockManagement = supplierStockManagement;
	}

	@Transient
	public Boolean getHasStock() {
		return hasStock;
	}

	@Transient
	public void setHasStock(Boolean hasStock) {
		this.hasStock = hasStock;
	}

	public Boolean getIsm() {
		return ism;
	}

	public void setIsm(Boolean ism) {
		this.ism = ism;
	}

	@Column(name = "type", length = 45)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Integer getApproximativeStoragePeriod() {
		return approximativeStoragePeriod;
	}

	public void setApproximativeStoragePeriod(Integer approximativeStoragePeriod) {
		this.approximativeStoragePeriod = approximativeStoragePeriod;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Location getPreferredLocation() {
		return preferredLocation;
	}

	public void setPreferredLocation(Location preferredLocation) {
		this.preferredLocation = preferredLocation;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Warehouse getPreferredWarehouse() {
		return preferredWarehouse;
	}

	public void setPreferredWarehouse(Warehouse preferredWarehouse) {
		this.preferredWarehouse = preferredWarehouse;
	}

}
