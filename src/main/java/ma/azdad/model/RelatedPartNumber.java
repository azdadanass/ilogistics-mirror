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

public class RelatedPartNumber extends GenericModel<Integer> implements Serializable {

	private PartNumber partNumber1;
	private PartNumber partNumber2;

	// tmp
	private Boolean isPartNumber1 = true;
	private PartNumber tmpPartNumber;

	public RelatedPartNumber() {
		super();
	}

	public RelatedPartNumber(PartNumber partNumber1) {
		super();
		this.partNumber1 = partNumber1;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && partNumber1 != null)
			result = partNumber1.getName().toLowerCase().contains(query);
		if (!result && partNumber2 != null)
			result = partNumber2.getName().toLowerCase().contains(query);
		return result;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber1() {
		return partNumber1;
	}

	public void setPartNumber1(PartNumber partNumber1) {
		this.partNumber1 = partNumber1;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumber getPartNumber2() {
		return partNumber2;
	}

	public void setPartNumber2(PartNumber partNumber2) {
		this.partNumber2 = partNumber2;
	}

	@Transient
	public PartNumber getTmpPartNumber() {
		return tmpPartNumber;
	}

	@Transient
	public void setTmpPartNumber(PartNumber tmpPartNumber) {
		this.tmpPartNumber = tmpPartNumber;
		this.isPartNumber1 = partNumber1.equals(tmpPartNumber);
	}

	@Transient
	public Boolean getIsPartNumber1() {
		return isPartNumber1;
	}

	@Transient
	public void setIsPartNumber1(Boolean isPartNumber1) {
		this.isPartNumber1 = isPartNumber1;
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
