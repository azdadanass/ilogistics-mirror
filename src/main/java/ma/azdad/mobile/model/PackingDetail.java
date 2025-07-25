package ma.azdad.mobile.model;

import ma.azdad.utils.Public;

public class PackingDetail {
	
	private Integer id;
	private String typeImage;
	private Double length;
	private Double width;
	private Double height;
	private Boolean hasSerialnumber = false;
	private Integer quantity;
	private Double volume;
	private Double grossWeight;
	private Boolean fragile;
	private Boolean stackable;
	private Boolean flammable;
	private Integer minStorageTemperature;
	private Integer maxStorageTemperature;
	private Integer storageHumidity;
	private Integer maxStack;

	public PackingDetail() {
    }
	
	public PackingDetail(Integer id, String typeImage, Double length, Double width, Double height, Integer quantity,
			Double volume, Double grossWeight, Boolean fragile, Boolean stackable, Boolean flammable,Integer minStorageTemperature,Integer maxStorageTemperature
			,Integer storageHumidity,Integer maxStack,Boolean hasSerialnumber) {
		super();
		this.id = id;
		this.typeImage = Public.getPublicUrl(typeImage);
		this.length = length;
		this.width = width;
		this.height = height;
		this.quantity = quantity;
		this.volume = volume;
		this.grossWeight = grossWeight;
		this.fragile = fragile;
		this.stackable = stackable;
		this.flammable = flammable;
		this.minStorageTemperature = minStorageTemperature;
		this.maxStorageTemperature = maxStorageTemperature;
		this.storageHumidity = storageHumidity;
		this.maxStack = maxStack;
		this.hasSerialnumber = hasSerialnumber;


	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeImage() {
		return typeImage;
	}
	public void setTypeImage(String typeImage) {
		this.typeImage = typeImage;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Boolean getFragile() {
		return fragile;
	}

	public void setFragile(Boolean fragile) {
		this.fragile = fragile;
	}
	public Boolean getStackable() {
		return stackable;
	}
	public void setStackable(Boolean stackable) {
		this.stackable = stackable;
	}

	public Boolean getFlammable() {
		return flammable;
	}

	public void setFlammable(Boolean flammable) {
		this.flammable = flammable;
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

	public Integer getStorageHumidity() {
		return storageHumidity;
	}

	public void setStorageHumidity(Integer storageHumidity) {
		this.storageHumidity = storageHumidity;
	}

	public Integer getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(Integer maxStack) {
		this.maxStack = maxStack;
	}

	public Boolean getHasSerialnumber() {
		return hasSerialnumber;
	}

	public void setHasSerialnumber(Boolean hasSerialnumber) {
		this.hasSerialnumber = hasSerialnumber;
	}
	
	
	
	
	
	
	

	
	
	
	
	




}
