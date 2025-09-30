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

	private String name;
	private String type;
	private Integer quantity = 1;
	private Boolean hasSerialnumber = false;
	private String snType;
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
	private Double storageFactor = 1.0;

	private Packing parent;

	// tmp
	private Integer tmpQuantity;

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
	public String getParentName() {
		return parent != null ? parent.getName() : null;
	}

	@Transient
	public void setParentName(String parentName) {
		if (parent == null)
			parent = new Packing();
		parent.setName(parentName);
	}

	@Transient
	public String getPartNumberName() {
		return parent != null ? parent.getPartNumberName() : null;
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (parent == null)
			parent = new Packing();
		parent.setPartNumberName(partNumberName);
	}

	@Transient
	public String getPartNumberDescription() {
		return parent != null ? parent.getPartNumberDescription() : null;
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (parent == null)
			parent = new Packing();
		parent.setPartNumberDescription(partNumberDescription);
	}

	@Transient
	public String getPartNumberBrandName() {
		return parent != null ? parent.getPartNumberBrandName() : null;
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName) {
		if (parent == null)
			parent = new Packing();
		parent.setPartNumberBrandName(partNumberBrandName);
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

	public String getSnType() {
		return snType;
	}

	public void setSnType(String snType) {
		this.snType = snType;
	}

	@Transient
	public Integer getTmpQuantity() {
		return tmpQuantity;
	}

	@Transient
	public void setTmpQuantity(Integer tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	public Double getStorageFactor() {
		return storageFactor;
	}

	public void setStorageFactor(Double storageFactor) {
		this.storageFactor = storageFactor;
	}
	
	public void calculateVolume() {
		try {
			this.volume = this.length * this.width * this.height;
		} catch (Exception e) {
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
