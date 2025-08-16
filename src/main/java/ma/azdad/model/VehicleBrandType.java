package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class VehicleBrandType extends GenericModel<Integer> {

	private String name;
	private String description;
	private String image = "files/no-image.png";

	private VehicleBrand brand;

	public boolean filter(String query) {
		return contains(query, name, description);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}
	
	@Transient
	public Integer getBrandId(){
		return brand!=null?brand.getId():null;
	}

	@Transient
	public void setBrandId(Integer brandId){
		if(brand==null || !brandId.equals(brand.getId()))
			brand=new VehicleBrand();
		brand.setId(brandId);
	}
	
	@Transient
	public String getBrandName(){
		return brand!=null?brand.getName():null;
	}

	@Transient
	public void setBrandName(String brandName){
		if(brand==null)
			brand=new VehicleBrand();
		brand.setName(brandName);
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

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public VehicleBrand getBrand() {
		return brand;
	}

	public void setBrand(VehicleBrand brand) {
		this.brand = brand;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}