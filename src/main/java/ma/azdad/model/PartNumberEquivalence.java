package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity

public class PartNumberEquivalence extends GenericBean implements Serializable {

	private String formula;
	private String htmlFormula;
	private String inverseFormula;
	private String inverseFormulaHtml;
	private Boolean oneToOne;
	private Boolean active = true;

	private PartNumber partNumber;

	private List<PartNumberEquivalenceDetail> detailList = new ArrayList<>();

	public boolean filter(String query) {
		boolean result = super.filter(query);
		// if (!result && name != null)
		// result = name.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getType() {
		return oneToOne ? "One to one" : "Formula";
	}

	@Transient
	public String getStatus() {
		return active ? "Active" : "Obsolete";
	}

	public void calculateFileds() {
		formula = detailList.stream().map(i -> i.getQuantity() + " x " + i.getPartNumber().getName()).collect(Collectors.joining(" + "));
		htmlFormula = detailList.stream().map(i -> "<b style='color:blue'>" + i.getQuantity() + "</b> x " + i.getPartNumber().getName()).collect(Collectors.joining(" + "));
		oneToOne = detailList.size() == 1 && detailList.get(0).getQuantity() == 1;

		if (detailList.size() == 1) {
			PartNumberEquivalenceDetail detail = detailList.get(0);
			inverseFormula = "(1 / " + detail.getQuantity() + ") x " + partNumber.getName();
			inverseFormulaHtml = "<b style='color:red'> (1 / " + detail.getQuantity() + ")</b> x " + partNumber.getName();
		}
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getHtmlFormula() {
		return htmlFormula;
	}

	public void setHtmlFormula(String htmlFormula) {
		this.htmlFormula = htmlFormula;
	}

	public Boolean getOneToOne() {
		return oneToOne;
	}

	public void setOneToOne(Boolean oneToOne) {
		this.oneToOne = oneToOne;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<PartNumberEquivalenceDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PartNumberEquivalenceDetail> detailList) {
		this.detailList = detailList;
	}

	public String getInverseFormula() {
		return inverseFormula;
	}

	public void setInverseFormula(String inverseFormula) {
		this.inverseFormula = inverseFormula;
	}

	public String getInverseFormulaHtml() {
		return inverseFormulaHtml;
	}

	public void setInverseFormulaHtml(String inverseFormulaHtml) {
		this.inverseFormulaHtml = inverseFormulaHtml;
	}

}
