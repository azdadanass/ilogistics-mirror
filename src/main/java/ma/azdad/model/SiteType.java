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

public class SiteType extends GenericModel<Integer> implements Serializable {

	private String name;
	private String image;

	private SiteCategory category;

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && category != null)
			result = category.filter(query);
		return result;
	}

	@Transient
	public String getCategoryName() {
		return category != null ? category.getName() : null;
	}

	@Transient
	public void setCategoryName(String categoryName) {
		if (category == null)
			category = new SiteCategory();
		category.setName(categoryName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public SiteCategory getCategory() {
		return category;
	}

	public void setCategory(SiteCategory category) {
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
}
