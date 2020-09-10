package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class SiteCategory extends GenericBeanOld implements Serializable {

	private String name;
	private String image;

	private List<SiteType> typeList = new ArrayList<>();

	public SiteCategory() {
		super();
	}

	public SiteCategory(String name, String image) {
		super();
		this.name = name;
		this.image = image;
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.ALL)
	public List<SiteType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<SiteType> typeList) {
		this.typeList = typeList;
	}

}
