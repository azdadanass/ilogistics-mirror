package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity

public class SiteCategory extends GenericModel<Integer> implements Serializable {

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

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		return result;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
