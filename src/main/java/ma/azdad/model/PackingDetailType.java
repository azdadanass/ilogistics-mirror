package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class PackingDetailType extends GenericModel<Integer> {

	private String name;
	private Boolean active = true;
	private String image = "files/no-image.png";
	
	private PartNumberClass partNumberClass;

	public boolean filter(String query) {
		return contains(query, name,partNumberClass.getValue());
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(columnDefinition = "TEXT")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	public PartNumberClass getPartNumberClass() {
		return partNumberClass;
	}

	public void setPartNumberClass(PartNumberClass partNumberClass) {
		this.partNumberClass = partNumberClass;
	}

}
