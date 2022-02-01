package ma.azdad.rest;

import java.util.Date;

import ma.azdad.model.DeliveryRequestDetail;

public class DeliveryRequestCostHistory {

	private Date date;
	private Double unitCost;
	private String deliveryRequestReference;
	private String projectName;

	public DeliveryRequestCostHistory() {
	}

	public DeliveryRequestCostHistory(DeliveryRequestDetail deliveryRequestDetail) {
		this.date = deliveryRequestDetail.getTmpDeliveryRequestDeliveryDate();
		this.unitCost = deliveryRequestDetail.getUnitCost();
		this.deliveryRequestReference = deliveryRequestDetail.getTmpDeliveryRequestReference();
		this.projectName = deliveryRequestDetail.getTmpProjectName();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public String getDeliveryRequestReference() {
		return deliveryRequestReference;
	}

	public void setDeliveryRequestReference(String deliveryRequestReference) {
		this.deliveryRequestReference = deliveryRequestReference;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
