package ma.azdad.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

@Entity

public class PartNumber extends GenericBeanOld implements Serializable {

	private String name;
	private String countryOfOrigin;
	private Boolean unit = true;
	private Boolean partialDelivery;
	private PartNumberStatus status = PartNumberStatus.ACTIVE;
	private PartNumberState state = PartNumberState.DRAFT;
	private String description;

	private Double grossWeight = 0.0;
	private Double netWeight = 0.0;
	private Double volume = 0.0;
	private Double length;
	private Double width;
	private Double height;
	private Double storageTemperature;
	private Double storageHumidity;
	private Boolean flammable;
	private Integer maxStack;
	private Integer maxStorageDuration;
	private Boolean fragile;
	private Boolean stackable;
	private Boolean stockItem;
	private Integer stockMin = 0;
	private Integer stockMax = 10;
	private Boolean expirable = false;
	private Integer expiryDuration;
	private String unitType;
	private Boolean spare = false;
	private Boolean hasPacking = false;

	private User user;
	private Brand brand;
	private PartNumberType partNumberType;
	private PartNumberOrange partNumberOrange;

	private String image = "resources/img/noimage.jpg";

	private List<PartNumberDetail> detailList = new ArrayList<>();
	private List<PartNumberFile> fileList = new ArrayList<>();
	private List<PartNumberHistory> historyList = new ArrayList<>();
	private List<PartNumberEquivalence> equivalenceList = new ArrayList<>();
	// private List<PartNumber> relatedPartNumberList = new ArrayList<>();
	private List<Packing> packingList = new ArrayList<Packing>();

	private PartNumberFile selectedPhoto;
	private Integer remainingFileds = 0;

	// tmp
	private PartNumber relatedPartNumber;
	private Integer industryId;
	private Integer categoryId;
	private List<Packing> tmpPackingList = new ArrayList<Packing>();

	public void init() {
		if (partNumberType != null) {
			categoryId = partNumberType.getCategory().getId();
			industryId = partNumberType.getCategory().getIndustry().getId();
		}
	}

	public PartNumber() {
		super();
	}

	public PartNumber(Integer id, String name) {
		super(id);
		this.name = name;
	}

	public PartNumber(Integer id, String name, String description) {
		super(id);
		this.name = name;
		this.description = description;
	}

	public PartNumber copy() {
		PartNumber copy = new PartNumber();
		BeanUtils.copyProperties(this, copy);
		return copy;
	}

	public String getChanges(PartNumber old) {
		List<String> typeList = Arrays.asList("Boolean", "Integer", "Double", "String", "Date");
		List<String> excludeList = Arrays.asList("remainingFileds");

		List<Field> fileds = Arrays.stream(PartNumber.class.getDeclaredFields()).filter(f -> typeList.contains(f.getType().getSimpleName()) && !excludeList.contains(f.getName()) && !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());

		String result = "";
		try {
			for (Field field : fileds) {
				Object f1 = field.get(this);
				Object f2 = field.get(old);

				String f1Str = String.valueOf(f1 != null ? f1 : "");
				String f2Str = String.valueOf(f2 != null ? f2 : "");
				if (!f1Str.equals(f2Str))
					result += field.getName() + "[" + f2Str + " --> " + f1Str + "]<br/>";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	@Override
	public boolean filter(String query) {
		return contains(name, query) //
				|| contains(description, query) //
				|| contains(getIndustryName(), query) //
				|| contains(getCategoryName(), query) //
				|| contains(getTypeName(), query)//
				|| contains(getPartNumberOrangeName(), query);
	}

	public void addPacking(Packing packing) {
		packing.setPartNumber(this);
		packingList.add(packing);
	}

	public void removePacking(Packing packing) {
		packing.setPartNumber(null);
		packingList.remove(packing);
	}

	// public void addRelatedPartNumber(PartNumber partNumber) {
	// this.relatedPartNumberList.add(partNumber);
	// }
	//
	// public void removeRelatedPartNumber(PartNumber partNumber) {
	// this.relatedPartNumberList.remove(partNumber);
	// }
	//
	// public void removeAllRelatedPartNumber() {
	// for (PartNumber partNumber : new ArrayList<>(relatedPartNumberList))
	// removeRelatedPartNumber(partNumber);
	//
	// }

	public void initDetailList() {
		for (PartNumberDetail detail : detailList)
			detail.init();
	}

	@Transient
	public String getIndustryName() {
		return partNumberType == null ? null : partNumberType.getIndustryName();
	}

	@Transient
	public void setIndustryName(String industryName) {
		if (partNumberType == null)
			partNumberType = new PartNumberType();
		partNumberType.setIndustryName(industryName);
	}

	@Transient
	public String getCategoryName() {
		return partNumberType == null ? null : partNumberType.getCategoryName();
	}

	@Transient
	public void setCategoryName(String categoryName) {
		if (partNumberType == null)
			partNumberType = new PartNumberType();
		partNumberType.setCategoryName(categoryName);
	}

	@Transient
	public String getTypeName() {
		return partNumberType == null ? null : partNumberType.getName();
	}

	@Transient
	public void setTypeName(String typeName) {
		if (partNumberType == null)
			partNumberType = new PartNumberType();
		partNumberType.setName(typeName);
	}

	@Transient
	public String getPartNumberOrangeName() {
		return partNumberOrange == null ? null : partNumberOrange.getName();
	}

	@Transient
	public void setPartNumberOrangeName(String partNumberOrangeName) {
		if (partNumberOrange == null)
			partNumberOrange = new PartNumberOrange();
		partNumberOrange.setName(partNumberOrangeName);
	}

	@Transient
	public String getPartNumberOrangeDescription() {
		return partNumberOrange == null ? null : partNumberOrange.getDescription();
	}

	@Transient
	public void setPartNumberOrangeDescription(String partNumberOrangeDescription) {
		if (partNumberOrange == null)
			partNumberOrange = new PartNumberOrange();
		partNumberOrange.setDescription(partNumberOrangeDescription);
	}

	@Transient
	public List<PartNumberFile> getFileList2() {
		List<PartNumberFile> result = new ArrayList<>();
		for (PartNumberFile pnf : fileList)
			if (!pnf.getIsImage())
				result.add(pnf);
		return result;
	}

	@Transient
	public Double getCompletenessPercentage() {
		int total = 0, filled = 0;

		total++;
		if (name != null && !name.isEmpty())
			filled++;

		total++;
		if (partNumberType != null)
			filled++;

		total++;
		if (countryOfOrigin != null && !countryOfOrigin.isEmpty())
			filled++;

		total++;
		if (description != null && !description.isEmpty())
			filled++;

		total++;
		if (grossWeight != null)
			filled++;

		total++;
		if (netWeight != null)
			filled++;

		total++;
		if (volume != null)
			filled++;

		total++;
		if (length != null)
			filled++;

		total++;
		if (width != null)
			filled++;

		total++;
		if (height != null)
			filled++;

		total++;
		if (storageTemperature != null)
			filled++;

		total++;
		if (storageHumidity != null)
			filled++;

		total++;
		if (flammable != null)
			filled++;

		total++;
		if (maxStack != null)
			filled++;

		total++;
		if (maxStorageDuration != null)
			filled++;

		total++;
		if (fragile != null)
			filled++;
		remainingFileds = total - filled;
		return ((double) filled) / ((double) total);

	}

	@Transient
	public List<PartNumberFile> getPhotoList() {
		List<PartNumberFile> result = new ArrayList<>();
		for (PartNumberFile file : fileList)
			if (file.getIsImage())
				result.add(file);
		return result;
	}

	@Transient
	public String getStatusStyleClass() {
		return PartNumberStatus.ACTIVE.equals(status) ? "green" : "red";
	}

	@Transient
	public String getStateStyleClass() {
		return PartNumberState.DRAFT.equals(state) ? "badge badge-warning" : "badge badge-success";
	}

	public void calculateState() {
		if (name == null || name.isEmpty() || description == null || description.isEmpty() || partNumberType == null || grossWeight == null || netWeight == null || length == null || width == null || height == null || volume == null)
			state = PartNumberState.DRAFT;
		else
			state = PartNumberState.CONFIRMED;
	}

	@Transient
	public Integer getMaxNumberOfDetailList() {
		if (detailList.isEmpty())
			return 0;
		int max = 0;
		for (PartNumberDetail partNumberDetail : detailList)
			if (max < partNumberDetail.getNumber())
				max = partNumberDetail.getNumber();
		return max;
	}

	// GETTERS & SETTERS

	@Transient
	public PartNumberFile getSelectedPhoto() {
		if (selectedPhoto == null)
			try {
				return getPhotoList().get(0);
			} catch (Exception e) {
				return new PartNumberFile();
			}
		return selectedPhoto;
	}

	@Transient
	public void setSelectedPhoto(PartNumberFile selectedPhoto) {
		this.selectedPhoto = selectedPhoto;
	}

	@Column(unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	public Boolean getUnit() {
		return unit;
	}

	public void setUnit(Boolean unit) {
		this.unit = unit;
	}

	public Boolean getPartialDelivery() {
		return partialDelivery;
	}

	public void setPartialDelivery(Boolean partialDelivery) {
		this.partialDelivery = partialDelivery;
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

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public PartNumberStatus getStatus() {
		return status;
	}

	public void setStatus(PartNumberStatus status) {
		this.status = status;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public PartNumberState getState() {
		return state;
	}

	public void setState(PartNumberState state) {
		this.state = state;
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

	public Double getStorageTemperature() {
		return storageTemperature;
	}

	public void setStorageTemperature(Double storageTemperature) {
		this.storageTemperature = storageTemperature;
	}

	public Double getStorageHumidity() {
		return storageHumidity;
	}

	public void setStorageHumidity(Double storageHumidity) {
		this.storageHumidity = storageHumidity;
	}

	public Boolean getFlammable() {
		return flammable;
	}

	public void setFlammable(Boolean flammable) {
		this.flammable = flammable;
	}

	public Integer getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(Integer maxStack) {
		this.maxStack = maxStack;
	}

	public Integer getMaxStorageDuration() {
		return maxStorageDuration;
	}

	public void setMaxStorageDuration(Integer maxStorageDuration) {
		this.maxStorageDuration = maxStorageDuration;
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

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<PartNumberFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<PartNumberFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<PartNumberHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<PartNumberHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<PartNumberDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PartNumberDetail> detailList) {
		this.detailList = detailList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partNumber", cascade = CascadeType.ALL)
	public List<PartNumberEquivalence> getEquivalenceList() {
		return equivalenceList;
	}

	public void setEquivalenceList(List<PartNumberEquivalence> equivalenceList) {
		this.equivalenceList = equivalenceList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partNumber", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Packing> getPackingList() {
		return packingList;
	}

	public void setPackingList(List<Packing> packingList) {
		this.packingList = packingList;
	}

	@Override
	public String toString() {
		return name;
	}

	@Transient
	public Integer getRemainingFileds() {
		return remainingFileds;
	}

	public void setRemainingFileds(Integer remainingFileds) {
		this.remainingFileds = remainingFileds;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getStockItem() {
		return stockItem;
	}

	public void setStockItem(Boolean stockItem) {
		this.stockItem = stockItem;
	}

	public Integer getStockMin() {
		return stockMin;
	}

	public void setStockMin(Integer stockMin) {
		this.stockMin = stockMin;
	}

	public Integer getStockMax() {
		return stockMax;
	}

	public void setStockMax(Integer stockMax) {
		this.stockMax = stockMax;
	}

	public Boolean getExpirable() {
		return expirable;
	}

	public void setExpirable(Boolean expirable) {
		this.expirable = expirable;
	}

	public Integer getExpiryDuration() {
		return expiryDuration;
	}

	public void setExpiryDuration(Integer expiryDuration) {
		this.expiryDuration = expiryDuration;
	}

	// @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	// @JoinTable(name = "related_part_number_old", joinColumns = { @JoinColumn(name
	// = "part_number_id_1", referencedColumnName = "id") }, inverseJoinColumns = {
	// @JoinColumn(name = "part_number_id_2", referencedColumnName = "id") })
	// public List<PartNumber> getRelatedPartNumberList() {
	// return relatedPartNumberList;
	// }
	//
	// public void setRelatedPartNumberList(List<PartNumber> relatedPartNumberList)
	// {
	// this.relatedPartNumberList = relatedPartNumberList;
	// }

	@Transient
	public PartNumber getRelatedPartNumber() {
		return relatedPartNumber;
	}

	@Transient
	public void setRelatedPartNumber(PartNumber relatedPartNumber) {
		this.relatedPartNumber = relatedPartNumber;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public Boolean getSpare() {
		return spare;
	}

	public void setSpare(Boolean spare) {
		this.spare = spare;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public PartNumberType getPartNumberType() {
		return partNumberType;
	}

	public void setPartNumberType(PartNumberType partNumberType) {
		this.partNumberType = partNumberType;
	}

	@Transient
	public Integer getIndustryId() {
		return industryId;
	}

	@Transient
	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}

	@Transient
	public Integer getCategoryId() {
		return categoryId;
	}

	@Transient
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Boolean getHasPacking() {
		return hasPacking;
	}

	public void setHasPacking(Boolean hasPacking) {
		this.hasPacking = hasPacking;
	}

	@Transient
	public List<Packing> getTmpPackingList() {
		return tmpPackingList;
	}

	@Transient
	public void setTmpPackingList(List<Packing> tmpPackingList) {
		this.tmpPackingList = tmpPackingList;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public PartNumberOrange getPartNumberOrange() {
		return partNumberOrange;
	}

	public void setPartNumberOrange(PartNumberOrange partNumberOrange) {
		this.partNumberOrange = partNumberOrange;
	}

}
