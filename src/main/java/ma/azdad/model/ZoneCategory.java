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
public class ZoneCategory extends GenericModel<Integer> {

	private ZoneIndustry industry;
	private PartNumberCategory category;

	private List<ZoneType> typeList = new ArrayList<>();

	public boolean filter(String query) {
		return contains(query, getCategoryName());
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return getCategoryName();
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
	public ZoneIndustry getIndustry() {
		return industry;
	}

	public void setIndustry(ZoneIndustry industry) {
		this.industry = industry;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumberCategory getCategory() {
		return category;
	}

	public void setCategory(PartNumberCategory category) {
		this.category = category;
	}

	@Transient
	public String getCategoryName() {
		return category != null ? category.getName() : null;
	}

	@Transient
	public void setCategoryName(String categoryName) {
		if (category == null)
			category = new PartNumberCategory();
		category.setName(categoryName);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ZoneType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ZoneType> typeList) {
		this.typeList = typeList;
	}

	public void addType(ZoneType type) {
		type.setCategory(this);
		typeList.add(type);
	}

	public void removeType(ZoneType type) {
		type.setCategory(null);
		typeList.remove(type);
	}
	
	@Transient
	public Integer getCategoryId(){
		return category!=null?category.getId():null;
	}

	@Transient
	public void setCategoryId(Integer categoryId){
		if(category==null || !categoryId.equals(category.getId()))
			category=new PartNumberCategory();
		category.setId(categoryId);
	}


}
