package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Company implements Serializable {

	private Integer id;
	private String name;
	private String logo;

	public Company() {
		super();
	}

	public Company(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return id.equals(((Company) obj).getId());
		} catch (Exception e) {
			return false;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcompany", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}