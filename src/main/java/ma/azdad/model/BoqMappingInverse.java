package ma.azdad.model;

public class BoqMappingInverse {

	private PartNumber partNumber; // DN Part Number
	private Double quantity; // DN Qty

	private Boq boq;
	private PartNumberEquivalence partNumberEquivalence; // if null then same part number
	private Boolean directEquivalence = true;

	private Double tmpQuantity;

	public BoqMappingInverse(PartNumber partNumber, Double quantity, Double tmpQuantity) {
		super();
		this.partNumber = partNumber;
		this.quantity = quantity;
		this.tmpQuantity = tmpQuantity;
	}

	public BoqMappingInverse(PartNumber partNumber, Double quantity, Boq boq, PartNumberEquivalence partNumberEquivalence, Boolean directEquivalence) {
		super();
		this.partNumber = partNumber;
		this.quantity = quantity;
		this.tmpQuantity = quantity;
		this.boq = boq;
		this.partNumberEquivalence = partNumberEquivalence;
		this.directEquivalence = directEquivalence;
	}

	public BoqMappingInverse(PartNumber partNumber, Double quantity, Double tmpQuantity, Boq boq, PartNumberEquivalence partNumberEquivalence, Boolean directEquivalence) {
		super();
		this.partNumber = partNumber;
		this.quantity = quantity;
		this.boq = boq;
		this.partNumberEquivalence = partNumberEquivalence;
		this.tmpQuantity = tmpQuantity;
		this.directEquivalence = directEquivalence;
	}

	public String getKey() {
		return partNumber.getId() + ";" + boq.getId() + ";" + (partNumberEquivalence == null ? "null" : partNumberEquivalence.getId());
	}

	public String getType() {
		if (boq == null)
			return "";
		if (partNumberEquivalence == null)
			return "Same PN";
		else
			return partNumberEquivalence.getType();
	}

	public Double getBoqQuantity() {
		if (partNumberEquivalence == null)
			return quantity;
		else {
			if (directEquivalence)
				return quantity / partNumberEquivalence.getDetailList().stream().filter(i -> i.getPartNumber().getId().equals(partNumber.getId())).findFirst().get().getQuantity();
			else
				return quantity * partNumberEquivalence.getDetailList().get(0).getQuantity();
		}
	}

	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Boq getBoq() {
		return boq;
	}

	public void setBoq(Boq boq) {
		this.boq = boq;
	}

	public PartNumberEquivalence getPartNumberEquivalence() {
		return partNumberEquivalence;
	}

	public void setPartNumberEquivalence(PartNumberEquivalence partNumberEquivalence) {
		this.partNumberEquivalence = partNumberEquivalence;
	}

	public Double getTmpQuantity() {
		return tmpQuantity;
	}

	public void setTmpQuantity(Double tmpQuantity) {
		this.tmpQuantity = tmpQuantity;
	}

	public Boolean getDirectEquivalence() {
		return directEquivalence;
	}

	public void setDirectEquivalence(Boolean directEquivalence) {
		this.directEquivalence = directEquivalence;
	}

}