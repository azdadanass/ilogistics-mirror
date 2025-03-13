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
	
	
	public PackingDetail(Integer id, String typeImage, Double length, Double width, Double height, Integer quantity,
			Double volume, Double grossWeight) {
		super();
		this.id = id;
		this.typeImage = "http://ilogistics.3gcominside.com"+typeImage;
		this.length = length;
		this.width = width;
		this.height = height;
		this.quantity = quantity;
		this.volume = volume;
		this.grossWeight = grossWeight;
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
	
	




}
