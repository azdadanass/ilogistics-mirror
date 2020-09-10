package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity

public class DocType extends GenericBeanOld implements Serializable {

	private String app;
	private String type;
	private String name;
	private Integer filter;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFilter() {
		return filter;
	}

	public void setFilter(Integer filter) {
		this.filter = filter;
	}

}
