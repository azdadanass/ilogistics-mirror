package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class PartNumberThreshold extends GenericBeanOld implements Serializable {

	private Integer stockMin = 0;
	private Integer stockMax = 10;
	private Integer dormancy;

	private PartNumber partNumber;
	private Project project;

	public PartNumberThreshold() {
		super();
	}

	public PartNumberThreshold(Project project) {
		super();
		this.project = project;
	}

	public PartNumberThreshold(Integer id, Integer stockMin, Integer stockMax,Integer dormancy,Integer partNumberId, String partNumberName, String partNumberDescription) {
		super(id);
		this.stockMin = stockMin;
		this.stockMax = stockMax;
		this.dormancy=dormancy;
		this.setPartNumberId(partNumberId);
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && partNumber != null)
			result = partNumber.getName().toLowerCase().contains(query);
		return result;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@Transient
	public Integer getPartNumberId() {
		if (partNumber == null)
			return null;
		return partNumber.getId();
	}

	@Transient
	public void setPartNumberId(Integer partNumberId) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setId(partNumberId);
	}

	@Transient
	public String getPartNumberName() {
		if (partNumber == null)
			return null;
		return partNumber.getName();
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setName(partNumberName);
	}

	@Transient
	public String getPartNumberDescription() {
		if (partNumber == null)
			return null;
		return partNumber.getDescription();
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (partNumber == null)
			partNumber = new PartNumber();
		partNumber.setDescription(partNumberDescription);
	}

	public Integer getDormancy() {
		return dormancy;
	}

	public void setDormancy(Integer dormancy) {
		this.dormancy = dormancy;
	}

}
