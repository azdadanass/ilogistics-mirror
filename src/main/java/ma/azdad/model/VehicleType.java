package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class VehicleType extends GenericModel<Integer> implements Serializable {

	private String name;
	private Double price;
	private String comment;
	private String image = "files/no-image.png";
	private Double maxWeight = 0.0;
	private Double maxVolume = 0.0;

	private VehicleBrandType brandType;

	private List<VehicleTypeHistory> historyList = new ArrayList<>();

	@Override
	public boolean filter(String query) {
		return contains(query, name, comment);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<VehicleTypeHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<VehicleTypeHistory> historyList) {
		this.historyList = historyList;
	}

	@Column(columnDefinition = "TEXT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public VehicleBrandType getBrandType() {
		return brandType;
	}

	public void setBrandType(VehicleBrandType brandType) {
		this.brandType = brandType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(Double maxVolume) {
		this.maxVolume = maxVolume;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

}
