package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
public class PartNumberPricing extends GenericModel<Integer> {

	private Double baseLineCost;
	private Double baseLinePrice;
	private Double physicalQuantity = 0.0;
	private Double pendingQuantity = 0.0; // pending outbound qty
	private Integer deliveryLeadTime;
	private Boolean active = true;
	private Integer countFiles = 0;

	private Currency currency;
	private PartNumber partNumber;
	private Company company;

	private List<PartNumberPricingDetail> detailList = new ArrayList<>();
	private List<PartNumberPricingFile> fileList = new ArrayList<>();
	private List<PartNumberPricingHistory> historyList = new ArrayList<>();
	private List<PartNumberPricingComment> commentList = new ArrayList<>();

	private List<CommentGroup<PartNumberPricingComment>> commentGroupList;

	public PartNumberPricing() {
		super();
	}

	// c1
	public PartNumberPricing(Integer id, Double baseLineCost, Double baseLinePrice, Double physicalQuantity, Double pendingQuantity, Integer countFiles, String currencyName, Integer partNumberId, String partNumberName, String partNumberDescription, String partNumberCategoryName, String partNumberTypeName, String partNumberBrandName, String companyName) {
		super(id);
		this.baseLineCost = baseLineCost;
		this.baseLinePrice = baseLinePrice;
		this.physicalQuantity = physicalQuantity;
		this.pendingQuantity = pendingQuantity;
		this.countFiles = countFiles;
		this.setCurrencyName(currencyName);
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberCategoryName(partNumberCategoryName);
		this.setPartNumberTypeName(partNumberTypeName);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setCompanyName(companyName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getPartNumberName());
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	@Transient
	public Double getAvailableQuantity() {
		return physicalQuantity - pendingQuantity;
	}

	public void addDetail(PartNumberPricingDetail detail) {
		detail.setPartNumberPricing(this);
		detailList.add(detail);
	}

	public void removeDetail(PartNumberPricingDetail detail) {
		detail.setPartNumberPricing(null);
		detailList.remove(detail);
	}

	public void addFile(PartNumberPricingFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(PartNumberPricingFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(PartNumberPricingHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(PartNumberPricingHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(PartNumberPricingComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(PartNumberPricingComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<PartNumberPricingComment>> map = new HashMap<>();
		for (PartNumberPricingComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<PartNumberPricingComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<PartNumberPricingComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<PartNumberPricingComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	@Transient
	public String getCompanyName() {
		return company == null ? null : company.getName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
	}

	@Transient
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !companyId.equals(company.getId()))
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public String getPartNumberName() {
		return partNumber == null ? null : partNumber.getName();
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}

	@Transient
	public String getPartNumberDescription() {
		return partNumber == null ? null : partNumber.getDescription();
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
	}

	@Transient
	public String getPartNumberBrandName() {
		return partNumber == null ? null : partNumber.getBrandName();
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setBrandName(partNumberBrandName);
	}

	@Transient
	public String getPartNumberCategoryName() {
		return partNumber == null ? null : partNumber.getCategoryName();
	}

	@Transient
	public void setPartNumberCategoryName(String partNumberCategoryName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setCategoryName(partNumberCategoryName);
	}

	@Transient
	public String getPartNumberTypeName() {
		return partNumber == null ? null : partNumber.getTypeName();
	}

	@Transient
	public void setPartNumberTypeName(String partNumberTypeName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setTypeName(partNumberTypeName);
	}

	@Transient
	public Integer getPartNumberId() {
		return partNumber == null ? null : partNumber.getId();
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		if (partNumber == null || !partNumberId.equals(partNumber.getId()))
			partNumber = new PartNumber();
		partNumber.setId(partNumberId);
	}

	@Transient
	public String getCurrencyName() {
		return currency == null ? null : currency.getName();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (currency == null)
			currency = new Currency();
		currency.setName(currencyName);
	}

	@Transient
	public Integer getCurrencyId() {
		return currency == null ? null : currency.id();
	}

	@Transient
	public void setCurrencyId(Integer currencyId) {
		if (currency == null || !currencyId.equals(currency.getId()))
			currency = new Currency();
		currency.setId(currencyId);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getBaseLineCost() {
		return baseLineCost;
	}

	public void setBaseLineCost(Double baseLineCost) {
		this.baseLineCost = baseLineCost;
	}

	public Double getBaseLinePrice() {
		return baseLinePrice;
	}

	public void setBaseLinePrice(Double baseLinePrice) {
		this.baseLinePrice = baseLinePrice;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Double getPhysicalQuantity() {
		return physicalQuantity;
	}

	public void setPhysicalQuantity(Double physicalQuantity) {
		this.physicalQuantity = physicalQuantity;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PartNumberPricingFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<PartNumberPricingFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PartNumberPricingHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<PartNumberPricingHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PartNumberPricingComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<PartNumberPricingComment> commentList) {
		this.commentList = commentList;
	}

	public Double getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Double pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Integer getDeliveryLeadTime() {
		return deliveryLeadTime;
	}

	public void setDeliveryLeadTime(Integer deliveryLeadTime) {
		this.deliveryLeadTime = deliveryLeadTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partNumberPricing", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PartNumberPricingDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PartNumberPricingDetail> detailList) {
		this.detailList = detailList;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
