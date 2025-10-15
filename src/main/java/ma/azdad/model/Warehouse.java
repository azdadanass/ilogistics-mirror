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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ma.azdad.utils.LabelValue;

@Entity
@Table(name = "il_warehouse")

public class Warehouse extends GenericPlace implements Serializable {

	private Double surface;
	private Double volume;
	private WarehouseStatus status = WarehouseStatus.ACTIVE;
	private WorkingTime workingTime;

//	private User manager;
	private Customer customer;
	private Supplier supplier;

	private List<Location> locationList = new ArrayList<>();
	private List<WarehouseFile> fileList = new ArrayList<>();
	private List<WarehouseHistory> historyList = new ArrayList<>();
	private List<WarehouseManager> managerList = new ArrayList<>();

	// TMP
	private String managerUsername;
	private LabelValue owner;
	private Boolean hasStock;

	public Warehouse(Integer id, String name) {
		super(id, name);
	}

	public Warehouse(Integer id, String name, Boolean hasStock) {
		super(id, name);
		this.hasStock = hasStock;
	}

	public Warehouse() {
		super();
	}

	public void init() {
//		if (manager != null)
//			managerUsername = manager.getUsername();
		if (customer != null)
			owner = new LabelValue(customer.getName(), customer.getId(), "customer");
		if (supplier != null)
			owner = new LabelValue(supplier.getName(), supplier.getId(), "supplier");
	}

	public void addManager(WarehouseManager manager) {
		manager.setWarehouse(this);
		managerList.add(manager);
	}

	public void removeManager(WarehouseManager manager) {
		manager.setWarehouse(null);
		managerList.remove(manager);
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

//	@Transient
//	public User getManager() {
//		return managerList.isEmpty() ? null : managerList.get(0).getUser();
//	}

	@Transient
	public String getStatusStyleClass() {
		return WarehouseStatus.ACTIVE.equals(status) ? "green" : "red";
	}

	@Transient
	public User getFirstManager() {
		return managerList.isEmpty() ? null : managerList.get(0).getUser();
	}

	@Transient
	public String getOwnerName() {
		if (customer != null)
			return customer.getName();
		if (supplier != null)
			return supplier.getName();
		return "3Gcom";
	}

	@Transient
	public LabelValue getOwner() {
		return owner;
	}

	@Transient
	public void setOwner(LabelValue owner) {
		this.owner = owner;
	}

	@Transient
	public String getManagerUsername() {
		return managerUsername;
	}

	@Transient
	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Double getSurface() {
		return surface;
	}

	public void setSurface(Double surface) {
		this.surface = surface;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

//	@ManyToOne(fetch = FetchType.EAGER, optional = false)
//	public User getManager() {
//		return manager;
//	}
//
//	public void setManager(User manager) {
//		this.manager = manager;
//	}

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<WarehouseFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<WarehouseFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<WarehouseHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<WarehouseHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "warehouse", cascade = CascadeType.ALL)
	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<WarehouseManager> getManagerList() {
		return managerList;
	}

	public void setManagerList(List<WarehouseManager> managerList) {
		this.managerList = managerList;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public WarehouseStatus getStatus() {
		return status;
	}

	public void setStatus(WarehouseStatus status) {
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public Boolean getHasStock() {
		return hasStock;
	}

	@Transient
	public void setHasStock(Boolean hasStock) {
		this.hasStock = hasStock;
	}

	@Enumerated(EnumType.STRING)
	public WorkingTime getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(WorkingTime workingTime) {
		this.workingTime = workingTime;
	}

}
