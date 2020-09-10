package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity

public class SiteType extends GenericBeanOld implements Serializable {

	private String name;
	private String image;

	private SiteCategory category;

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

}
