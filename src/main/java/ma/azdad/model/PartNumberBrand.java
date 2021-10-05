package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
public class PartNumberBrand extends GenericModel<Integer> implements Serializable {

	private String name;
	private String description;
	private String image = "files/no-image.png";
	private String company;
	private String website;

	private List<Supplier> supplierList = new ArrayList<Supplier>();

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && description != null)
			result = description.toLowerCase().contains(query);
		if (!result && company != null)
			result = company.toLowerCase().contains(query);
		if (!result && website != null)
			result = website.toLowerCase().contains(query);
		return result;
	}

	public void addSupplier(Supplier supplier) {
		supplierList.add(supplier);
		supplier.getBrandList().add(this);
	}

	public void removeSupplier(Supplier supplier) {
		supplierList.remove(supplier);
		supplier.getBrandList().remove(this);
	}

	public void remove() {
		for (Supplier supplier : new ArrayList<>(supplierList)) {
			removeSupplier(supplier);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "brand_supplier", joinColumns = { @JoinColumn(name = "brand_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "supplier_id", referencedColumnName = "idsupplier") })
	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<Supplier> supplierList) {
		this.supplierList = supplierList;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
