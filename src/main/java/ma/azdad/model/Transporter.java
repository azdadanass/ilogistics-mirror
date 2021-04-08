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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity

public class Transporter extends GenericBean implements Serializable {

	private TransporterType type;

	private User user;

	// Supplier
	private Supplier supplier;

	// Private
	private String firstName;
	private String lastName;
	private String cin;
	private String email;
	private String phone;

	// tmp
	private Integer supplierId;
	private String supplierName;
	private String supplierEmail;
	private String supplierPhone;

	private List<TransporterFile> fileList = new ArrayList<>();
	private List<TransporterHistory> historyList = new ArrayList<>();
	private List<Vehicle> vehicleList = new ArrayList<>();
	private List<User> driverList = new ArrayList<>();

	public void init() {
		if (supplier != null)
			supplierId = supplier.getId();
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
		if (!result && email != null)
			result = email.toLowerCase().contains(query);
		if (!result && phone != null)
			result = phone.toLowerCase().contains(query);
		if (!result && supplierName != null)
			result = supplierName.toLowerCase().contains(query);
		if (!result && supplierEmail != null)
			result = supplierEmail.toLowerCase().contains(query);
		if (!result && supplierPhone != null)
			result = supplierPhone.toLowerCase().contains(query);
		return result;
	}

	public Transporter() {
		super();
	}

	public Transporter(Integer id, TransporterType type, User user, String firstName, String lastName, String email, String phone, String supplierName, String supplierEmail, String supplierPhone) {
		super(id);
		this.type = type;
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.supplierName = supplierName;
		this.supplierEmail = supplierEmail;
		this.supplierPhone = supplierPhone;
	}

	public Transporter(Integer id, String firstName, String lastName, String supplierName) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.supplierName = supplierName;
	}

	@Transient
	public String getName() {
		return TransporterType.PRIVATE.equals(type) ? getFullName() : (supplierName != null) ? supplierName : supplier.getName();
	}

	@Transient
	public String getCorrectEmail() {
		return TransporterType.PRIVATE.equals(type) ? email : supplierEmail;
	}

	@Transient
	public String getCorrectPhone() {
		return TransporterType.PRIVATE.equals(type) ? phone : supplierPhone;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
	public List<User> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<User> driverList) {
		this.driverList = driverList;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

}
