package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class PartNumberType extends GenericModel<Integer> implements Serializable {

	private String name;

	private PartNumberCategory category;

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && category != null)
			result = category.filter(query);
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getIndustryName() {
		return category == null ? null : category.getIndustryName();
	}

	@Transient
	public void setIndustryName(String industryName) {
		if (category == null)
			category = new PartNumberCategory();
		category.setIndustryName(industryName);
	}

	@Transient
	public String getCategoryName() {
		return category == null ? null : category.getName();
	}

	@Transient
	public void setCategoryName(String categoryName) {
		if (category == null)
			category = new PartNumberCategory();
		category.setName(categoryName);
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumberCategory getCategory() {
		return category;
	}

	public void setCategory(PartNumberCategory category) {
		this.category = category;
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
	public Integer getCategoryId(){
		return category!=null?category.getId():null;
	}

	@Transient
	public void setCategoryId(Integer categoryId){
		if(category==null || !categoryId.equals(category.getId()))
			category=new PartNumberCategory();
		category.setId(categoryId);
	}
	
	@Transient
	public Integer getIndustryId(){
		return category!=null?category.getIndustryId():null;
	}

	@Transient
	public void setIndustryId(Integer industryId){
		if(category==null)
			category=new PartNumberCategory();
		category.setIndustryId(industryId);
	}


}
