package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity

public class PartNumberIndustry extends GenericBean implements Serializable {

	private String name;

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

}
