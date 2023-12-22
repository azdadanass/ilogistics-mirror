package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@SuppressWarnings("serial")
public class LocationDetail extends GenericModel<Integer> {

	private CompanyType ownerType;
	private Company company;// Owner
	private Customer customer; // Owner
	private Supplier supplier; // Owner

	private Location location;
	
	@Transient
	public Integer getOwnerId() {
		switch (ownerType) {
		case COMPANY:
			return company.getId();
		case CUSTOMER:
			return customer.getId();
		case SUPPLIER:
			return supplier.getId();
		default:
			return null;
		}
	}

	@Transient
	public String getOwnerName() {
		if (ownerType == null)
			return null;
		switch (ownerType) {
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
	public String getCompanyName() {
		if (company == null)
			return null;
		return company.getName();
	}

	@Transient
	public void setCompanyName(String name) {
		if (company == null)
			company = new Company();
		company.setName(name);
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
	public String getSupplierName() {
		if (supplier == null)
			return null;
		return supplier.getName();
	}

	@Transient
	public void setSupplierName(String name) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(name);
	}

	@Transient
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !company.getId().equals(companyId))
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public Integer getCustomerId() {
		return customer == null ? null : customer.getId();
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || !customer.getId().equals(customerId))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Transient
	public Integer getSupplierId() {
		return supplier == null ? null : supplier.getId();
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplier.getId().equals(supplierId))
			supplier = new Supplier();
		supplier.setId(supplierId);
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
	public CompanyType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(CompanyType ownerType) {
		this.ownerType = ownerType;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}