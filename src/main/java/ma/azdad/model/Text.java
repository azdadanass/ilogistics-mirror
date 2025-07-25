package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Text extends GenericModel<Integer> implements Serializable {

	private String app;
	private String beanName;
	private String type;
	private String value;

	public Text() {
		super();
	}

	public Text(String beanName, String type) {
		super();
		this.beanName = beanName;
		this.type = type;
	}

	@Column(nullable = false)
	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	@Column(nullable = false)
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Column(nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
