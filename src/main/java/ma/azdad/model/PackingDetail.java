package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class PackingDetail extends GenericModel<Integer> implements Serializable {

	private String type;
	private Integer quantity = 1;
	private Boolean hasSerialnumber = false;
	private Double grossWeight;
	private Double netWeight;
	private Double volume;
	private Double length;
	private Double width;
	private Double height;
	private Boolean fragile;
	private Boolean stackable;
	private Integer maxStack;
	private Integer minStorageTemperature;
	private Integer maxStorageTemperature;
	private Boolean flammable;
	private Integer storageHumidity;

	private Packing parent;

	public PackingDetail() {
	}

	public PackingDetail(PartNumber partNumber) {
		this.grossWeight = partNumber.getGrossWeight();
		this.netWeight = partNumber.getNetWeight();
		this.volume = partNumber.getVolume();
		this.length = partNumber.getLength();
		this.width = partNumber.getWidth();
		this.height = partNumber.getHeight();
		this.fragile = partNumber.getFragile();
		this.stackable = partNumber.getStackable();
		this.maxStack = partNumber.getMaxStack();
		this.flammable = partNumber.getFlammable();
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && type != null)
			result = type.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getTypeImage() {
		switch (type) {
		case "Pallet":
			return "/resources/img/pdt/pdt0.png";
		case "Box":
			return "/resources/img/pdt/pdt1.png";
		case "Crate":
			return "/resources/img/pdt/pdt2.png";
		case "Reel":
			return "/resources/img/pdt/pdt3.png";
		case "Plastic Bag":
			return "/resources/img/pdt/pdt4.png";
		case "Plastic Barrel":
			return "/resources/img/pdt/pdt5.png";
		default:
			return null;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getHasSerialnumber() {
		return hasSerialnumber;
	}

	public void setHasSerialnumber(Boolean hasSerialnumber) {
		this.hasSerialnumber = hasSerialnumber;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Boolean getStackable() {
		return stackable;
	}

	public void setStackable(Boolean stackable) {
		this.stackable = stackable;
	}

	public Integer getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(Integer maxStack) {
		this.maxStack = maxStack;
	}

	public Integer getMinStorageTemperature() {
		return minStorageTemperature;
	}

	public void setMinStorageTemperature(Integer minStorageTemperature) {
		this.minStorageTemperature = minStorageTemperature;
	}

	public Integer getMaxStorageTemperature() {
		return maxStorageTemperature;
	}

	public void setMaxStorageTemperature(Integer maxStorageTemperature) {
		this.maxStorageTemperature = maxStorageTemperature;
	}

	public Boolean getFlammable() {
		return flammable;
	}

	public void setFlammable(Boolean flammable) {
		this.flammable = flammable;
	}

	public Integer getStorageHumidity() {
		return storageHumidity;
	}

	public void setStorageHumidity(Integer storageHumidity) {
		this.storageHumidity = storageHumidity;
	}

	public Boolean getFragile() {
		return fragile;
	}

	public void setFragile(Boolean fragile) {
		this.fragile = fragile;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Packing getParent() {
		return parent;
	}

	public void setParent(Packing parent) {
		this.parent = parent;
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
