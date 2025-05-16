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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

@Entity
public class PartNumber extends GenericModel<Integer> implements Serializable {

	private String name;
	private String countryOfOrigin;
	private Boolean unit = true;
	private Boolean partialDelivery;
	private PartNumberStatus status = PartNumberStatus.ACTIVE;
	private PartNumberState state = PartNumberState.DRAFT;
	private String description;
	private PartNumberClass partNumberClass;

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
	private String image = "files/no-image.png";

	// performance
	private String industryName;
	private String categoryName;
	private String typeName;
	private String brandName;
	private String internalPartNumberName;
	private String internalPartNumberDescription;

	private User user;
	private PartNumberBrand brand;
	private PartNumberType partNumberType;
	private InternalPartNumber internalPartNumber;

	private List<PartNumberDetail> detailList = new ArrayList<>();
	private List<PartNumberFile> fileList = new ArrayList<>();
	private List<PartNumberHistory> historyList = new ArrayList<>();
	private List<PartNumberEquivalence> equivalenceList = new ArrayList<>();
	// private List<PartNumber> relatedPartNumberList = new ArrayList<>();
	private List<Packing> packingList = new ArrayList<Packing>();

	private PartNumberFile selectedPhoto;

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

		List<Field> fileds = Arrays.stream(PartNumber.class.getDeclaredFields())
				.filter(f -> typeList.contains(f.getType().getSimpleName()) && !excludeList.contains(f.getName()) && !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());

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
		return contains(query, name, description, getIndustryName(), getCategoryName(), getTypeName(), getInternalPartNumberName(), getBrandName());
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
	public Boolean getIsHardware() {
		return PartNumberClass.HW.equals(partNumberClass);
	}

	@Transient
	public Boolean getIsSoftware() {
		return PartNumberClass.SW.equals(partNumberClass);
	}

	@Transient
	public Boolean getIsService() {
		return PartNumberClass.SRV.equals(partNumberClass);
	}

//	@Transient
//	public String getIndustryName() {
//		return partNumberType == null ? null : partNumberType.getIndustryName();
//	}
//
//	@Transient
//	public void setIndustryName(String industryName) {
//		if (partNumberType == null)
//			partNumberType = new PartNumberType();
//		partNumberType.setIndustryName(industryName);
//	}

//	@Transient
//	public String getCategoryName() {
//		return partNumberType == null ? null : partNumberType.getCategoryName();
//	}
//
//	@Transient
//	public void setCategoryName(String categoryName) {
//		if (partNumberType == null)
//			partNumberType = new PartNumberType();
//		partNumberType.setCategoryName(categoryName);
//	}

//	@Transient
//	public String getTypeName() {
//		return partNumberType == null ? null : partNumberType.getName();
//	}
//
//	@Transient
//	public void setTypeName(String typeName) {
//		if (partNumberType == null)
//			partNumberType = new PartNumberType();
//		partNumberType.setName(typeName);
//	}

//	@Transient
//	public String getBrandName() {
//		return brand == null ? null : brand.getName();
//	}
//
//	@Transient
//	public void setBrandName(String brandName) {
//		if (brand == null)
//			brand = new PartNumberBrand();
//		brand.setName(brandName);
//	}

	@Transient
	public List<PartNumberFile> getFileList2() {
		List<PartNumberFile> result = new ArrayList<>();
		for (PartNumberFile pnf : fileList)
			if (!pnf.getIsImage())
				result.add(pnf);
		return result;
	}

	public String getInternalPartNumberName() {
		return internalPartNumberName;
	}

	public void setInternalPartNumberName(String internalPartNumberName) {
		this.internalPartNumberName = internalPartNumberName;
	}

	public String getInternalPartNumberDescription() {
		return internalPartNumberDescription;
	}

	public void setInternalPartNumberDescription(String internalPartNumberDescription) {
		this.internalPartNumberDescription = internalPartNumberDescription;
	}

	@Transient
	public List<String> getRemainingFields() {
		List<String> remainingFields = new ArrayList<String>();
		if (StringUtils.isBlank(name))
			remainingFields.add("Name");
		if (partNumberClass == null)
			remainingFields.add("Class");
		if (StringUtils.isBlank(industryName))
			remainingFields.add("Industry");
		if (StringUtils.isBlank(typeName))
			remainingFields.add("Type");
		if (StringUtils.isBlank(categoryName))
			remainingFields.add("Category");
		if (partialDelivery == null)
			remainingFields.add("Partial Delivery");
		if (StringUtils.isBlank(brandName))
			remainingFields.add("Brand");
		if (unit == null)
			remainingFields.add("Unit/Kit");
		if (StringUtils.isBlank(countryOfOrigin))
			remainingFields.add("Country Of Origin");
		if (stockItem == null)
			remainingFields.add("Stock PN");
		if (StringUtils.isBlank(unitType))
			remainingFields.add("Unit Type");
		if (expirable == null)
			remainingFields.add("Expirable");
		if (spare == null)
			remainingFields.add("Spare");
		if (state == null)
			remainingFields.add("State");
		if (expiryDuration == null)
			remainingFields.add("Expiry Duration");
		if (StringUtils.isBlank(description))
			remainingFields.add("Description");
		return remainingFields;
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
		if (name == null || name.isEmpty() || description == null || description.isEmpty() || partNumberType == null || grossWeight == null || netWeight == null || length == null || width == null
				|| height == null || volume == null)
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
	@Enumerated(EnumType.STRING)
	public PartNumberStatus getStatus() {
		return status;
	}

	public void setStatus(PartNumberStatus status) {
		this.status = status;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
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
	public PartNumberBrand getBrand() {
		return brand;
	}

	public void setBrand(PartNumberBrand brand) {
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
	public InternalPartNumber getInternalPartNumber() {
		return internalPartNumber;
	}

	public void setInternalPartNumber(InternalPartNumber internalPartNumber) {
		this.internalPartNumber = internalPartNumber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	public PartNumberClass getPartNumberClass() {
		return partNumberClass;
	}

	public void setPartNumberClass(PartNumberClass partNumberClass) {
		this.partNumberClass = partNumberClass;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

}
