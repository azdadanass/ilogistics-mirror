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

public class PartNumberCategory extends GenericModel<Integer> implements Serializable {

	private String name;

	private PartNumberIndustry industry;

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && industry != null)
			result = industry.filter(query);
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getIndustryName() {
		return industry == null ? null : industry.getName();
	}

	@Transient
	public void setIndustryName(String industryName) {
		if (industry == null)
			industry = new PartNumberIndustry();
		industry.setName(industryName);
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PartNumberIndustry getIndustry() {
		return industry;
	}

	public void setIndustry(PartNumberIndustry industry) {
		this.industry = industry;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Transient
	public Integer getIndustryId(){
		return industry!=null?industry.getId():null;
	}

	@Transient
	public void setIndustryId(Integer industryId){
		if(industry==null || !industryId.equals(industry.getId()))
			industry=new PartNumberIndustry();
		industry.setId(industryId);
	}

}
