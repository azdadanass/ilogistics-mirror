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

public class BoqMapping extends GenericModel<Integer> implements Serializable {

	private Double quantity = 0.0;

	private DeliveryRequest deliveryRequest;
	private Boq boq;
	private PartNumberEquivalence partNumberEquivalence; // if null then same part number

	private Boolean directEquivalence = true;

	// tmp
	private Double remainingQuantity;
	private Boolean equivalence = false;

	public BoqMapping() {
		super();
	}

	public BoqMapping(Double quantity, DeliveryRequest deliveryRequest, Boq boq, PartNumberEquivalence partNumberEquivalence, Boolean directEquivalence) {
		super();
		this.quantity = quantity;
		this.deliveryRequest = deliveryRequest;
		this.boq = boq;
		this.partNumberEquivalence = partNumberEquivalence;
		this.directEquivalence = directEquivalence;
	}

	public BoqMapping(Boq boq, Double remainingQuantity) {
		super();
		this.boq = boq;
		this.remainingQuantity = remainingQuantity;
	}

	@Transient
	public Integer getKey() {
		return boq.getId();
	}

	@Override // if id == null compare by key
	public boolean equals(Object obj) {
		if (id != null)
			return super.equals(obj);
		try {
			return getKey().equals(((BoqMapping) obj).getKey());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Boolean getDirectEquivalence() {
		return directEquivalence;
	}

	public void setDirectEquivalence(Boolean directEquivalence) {
		this.directEquivalence = directEquivalence;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Boq getBoq() {
		return boq;
	}

	public void setBoq(Boq boq) {
		this.boq = boq;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public PartNumberEquivalence getPartNumberEquivalence() {
		return partNumberEquivalence;
	}

	public void setPartNumberEquivalence(PartNumberEquivalence partNumberEquivalence) {
		this.partNumberEquivalence = partNumberEquivalence;
	}

	@Transient
	public Double getRemainingQuantity() {
		return remainingQuantity;
	}

	@Transient
	public void setRemainingQuantity(Double remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	@Transient
	public Boolean getEquivalence() {
		return equivalence;
	}

	@Transient
	public void setEquivalence(Boolean equivalence) {
		this.equivalence = equivalence;
	}

	@Override
	public String toString() {
		return "BoqMapping [quantity=" + quantity + ", boq=" + boq + ", partNumberEquivalence=" + partNumberEquivalence + "]\n";
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
