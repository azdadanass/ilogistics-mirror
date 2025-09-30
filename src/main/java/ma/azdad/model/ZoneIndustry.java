package ma.azdad.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class ZoneIndustry extends GenericModel<Integer> {

	private Location location;
	private PartNumberIndustry industry;

	private List<ZoneCategory> categoryList = new ArrayList<ZoneCategory>();

	public boolean filter(String query) {
		return contains(query, getIndustryName());
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return getIndustryName();
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumberIndustry getIndustry() {
		return industry;
	}

	public void setIndustry(PartNumberIndustry industry) {
		this.industry = industry;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "industry", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ZoneCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<ZoneCategory> categoryList) {
		this.categoryList = categoryList;
	}

	@Transient
	public Integer getIndustryId() {
		return industry != null ? industry.getId() : null;
	}

	@Transient
	public void setIndustryId(Integer industryId) {
		if (industry == null || !industryId.equals(industry.getId()))
			industry = new PartNumberIndustry();
		industry.setId(industryId);
	}

	@Transient
	public String getIndustryName() {
		return industry != null ? industry.getName() : null;
	}

	@Transient
	public void setIndustryName(String industryName) {
		if (industry == null)
			industry = new PartNumberIndustry();
		industry.setName(industryName);
	}

	public void addCategory(ZoneCategory category) {
		category.setIndustry(this);
		categoryList.add(category);
	}

	public void removeCategory(ZoneCategory category) {
		category.setIndustry(null);
		categoryList.remove(category);
	}

}
