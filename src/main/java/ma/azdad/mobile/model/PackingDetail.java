package ma.azdad.mobile.model;

import ma.azdad.utils.Public;

public class PackingDetail {
	
	private Integer id;
	private String typeImage;
	private Double length;
	private Double width;
	private Double height;
	private Integer quantity;
	private Double volume;
	private Double grossWeight;
	private Boolean fragile;
	private Boolean stackable;
	private Boolean flammable;

	
	
	
	
	
	
	
	public PackingDetail(Integer id, String typeImage, Double length, Double width, Double height, Integer quantity,
			Double volume, Double grossWeight, Boolean fragile, Boolean stackable, Boolean flammable) {
		super();
		this.id = id;
		this.typeImage = "http://ilogistics.3gcominside.com"+typeImage;
		this.length = length;
		this.width = width;
		this.height = height;
		this.quantity = quantity;
		this.volume = volume;
		this.grossWeight = grossWeight;
		this.fragile = fragile;
		this.stackable = stackable;
		this.flammable = flammable;
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

	
	
	
	
	




}
