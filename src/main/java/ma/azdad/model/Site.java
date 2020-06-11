package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity

public class Site extends GenericPlace implements Serializable {

	private SiteType type;

	// Owner
	private Integer ownerType;
	private Customer customer;
	private Supplier supplier;
	private String owner;

	private User user;
	private Warehouse warehouse;
	private User contact;

	private List<SiteFile> fileList = new ArrayList<>();
	private List<SiteHistory> historyList = new ArrayList<>();

	// TMP
	private Integer customerId;
	private Integer supplierId;

	// PERFORMANCE
	private String customerName;
	private String supplierName;

	public void init() {
		if (customer != null) {
			customerId = customer.getId();
			ownerType = 0;
		}

		if (supplier != null) {
			supplierId = supplier.getId();
			ownerType = 1;
		}

		if (owner != null)
			ownerType = 2;

	}

	public Site(String name, Double latitude, Double longitude, String address1, String address2, String address3, String phone, String fax, SiteType type, Integer ownerType, Customer customer, Supplier supplier, String owner, User user) {
		super(name, latitude, longitude, address1, address2, address3, phone, fax);
		this.type = type;
		this.ownerType = ownerType;
		this.customer = customer;
		this.supplier = supplier;
		this.owner = owner;
		this.user = user;
	}

	public Site(Integer id, String name) {
		super(id, name);
	}

	public Site(Integer id, String name, Double latitude, Double longitude) {
		super(id, name, latitude, longitude);
	}

	public Site(Integer id, String name, Double latitude, Double longitude, SiteType type, User user, String customerName, String supplierName, String owner) {
		super(id, name, latitude, longitude);
		this.type = type;
		this.user = user;
		this.customerName = customerName;
		this.supplierName = supplierName;
		this.owner = owner;
	}

	public Site(Integer id, String name, Double latitude, Double longitude, Integer customerId, Integer supplierId, String owner) {
		super(id, name, latitude, longitude);
		this.customerId = customerId;
		this.supplierId = supplierId;
		this.owner = owner;
	}

	public Site() {
		super();
	}

	@Transient
	public String getOwnerName() {
		if (owner != null)
			return owner;
		if (customerName != null)
			return customerName;
		if (customer != null)
			return customer.getName();
		if (supplierName != null)
			return supplierName;
		if (supplier != null)
			return supplier.getName();
		return null;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<SiteFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<SiteFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<SiteHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<SiteHistory> historyList) {
		this.historyList = historyList;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public SiteType getType() {
		return type;
	}

	public void setType(SiteType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Transient
	public Integer getOwnerType() {
		return ownerType;
	}

	@Transient
	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	@Transient
	public Integer getCustomerId() {
		return customerId;
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Transient
	public Integer getSupplierId() {
		return supplierId;
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	@Transient
	public String getContactUsername() {
		return contact == null ? null : contact.getUsername();
	}

	@Transient
	public void setContactUsername(String contactUsername) {
		if (contact == null || !contactUsername.equals(contact.getUsername()))
			contact = new User();
		contact.setUsername(contactUsername);
	}

	@Transient
	public String getCustomerName() {
		return customerName;
	}

	@Transient
	public String getSupplierName() {
		return supplierName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getContact() {
		return contact;
	}

	public void setContact(User contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "Site [name=" + name + "]";
	}

}
