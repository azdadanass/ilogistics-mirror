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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity

public class Packing extends GenericModel<Integer> implements Serializable {

	private String name;
	private Integer quantity = 1;
	private Boolean active = true;
	private Integer totalItems = 0;
	private Double grossWeight = 0.0;
	private Double volume = 0.0;

	private PartNumber partNumber;

	private List<PackingDetail> detailList = new ArrayList<PackingDetail>();

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		return result;
	}

	public void addDetail(PackingDetail detail) {
		detail.setParent(this);
		detailList.add(detail);
	}

	public void removeDetail(PackingDetail detail) {
		detail.setParent(null);
		detailList.remove(detail);
	}

	public void calculateFields() {
		totalItems = 0;
		grossWeight = 0.0;
		volume = 0.0;

		for (PackingDetail packingDetail : detailList) {
			totalItems += packingDetail.getQuantity();
			if (packingDetail.getGrossWeight() != null)
				grossWeight += packingDetail.getQuantity() * packingDetail.getGrossWeight();
			if (packingDetail.getVolume() != null)
				volume += packingDetail.getQuantity() * packingDetail.getVolume();
		}
	}

	@Transient
	public Boolean getHasSerialnumber() {
		return detailList.stream().anyMatch(i -> i.getHasSerialnumber());
	}

	@Transient
	public String getStatus() {
		return active ? "Active" : "Non Active";
	}

	@Transient
	public String getPartNumberName() {
		return partNumber != null ? partNumber.getName() : null;
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}
	
	@Transient
	public String getPartNumberDescription() {
		return partNumber != null ? partNumber.getDescription() : null;
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
	}
	
	@Transient
	public String getPartNumberBrandName(){
		return partNumber!=null?partNumber.getBrandName():null;
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName){
		if(partNumber==null)
			partNumber=new PartNumber();
		partNumber.setBrandName(partNumberBrandName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PackingDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PackingDetail> detailList) {
		this.detailList = detailList;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
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
