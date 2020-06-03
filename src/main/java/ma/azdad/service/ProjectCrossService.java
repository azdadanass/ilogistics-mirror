package ma.azdad.service;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.ProjectCross;
import ma.azdad.model.ProjectCrossCategory;
import ma.azdad.model.ProjectCrossTypes;
import ma.azdad.repos.DeliveryRequestRepos;
import ma.azdad.repos.ProjectCrossRepos;

@Component
@Transactional
public class ProjectCrossService {

	@Autowired
	private ProjectCrossRepos projectCrossRepos;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private DeliveryRequestRepos deliveryRequestRepos;

	public ProjectCross save(ProjectCross pc) {
		return projectCrossRepos.save(pc);
	}

	public ProjectCross findByDeliveryRequest(Integer deliveryRequestId) {
		ProjectCross pc = projectCrossRepos.findByDeliveryRequest(deliveryRequestId);
		if (pc == null)
			return null;
		Hibernate.initialize(pc.getFromProject());
		Hibernate.initialize(pc.getToProject());
		return pc;
	}

	public Long countByDeliveryRequest(Integer deliveryRequestId) {
		Long l = projectCrossRepos.countByDeliveryRequest(deliveryRequestId);
		return l != null ? l : 0;
	}

	public void addCrossCharge(DeliveryRequest deliveryRequest) {
		Boolean canAddCrossCharge = deliveryRequest.getIsOutbound() && deliveryRequest.getDestinationProject() != null && !deliveryRequest.getProject().getId().equals(deliveryRequest.getDestinationProject().getId()) && UtilsFunctions.compareDoubles(deliveryRequest.getTotalCost(), 0.0) > 0 && projectCrossRepos.countByDeliveryRequest(deliveryRequest.getId()) == 0;

		if (canAddCrossCharge)
			System.err.println(deliveryRequest.getId());
		if (!canAddCrossCharge)
			return;
		ProjectCross pc = new ProjectCross();
		pc.setCategory(ProjectCrossCategory.PROJECT_TO_PROJECT);
		pc.setFromProject(deliveryRequest.getProject());
		pc.setToProject(deliveryRequest.getDestinationProject());
		pc.setType(ProjectCrossTypes.COST.getValue());
		pc.setDate(deliveryRequest.getDate4());
		pc.setCashDate(deliveryRequest.getDate4());
		pc.setManual(false);
		pc.setDeliveryRequest(deliveryRequest);
		pc.setErp("Ilogistics");
		pc.setCurrency(currencyService.findOne(1));
		pc.setAmount(deliveryRequest.getTotalCost());
		pc.setCashAmount(deliveryRequest.getTotalCost() * 1.2);
		pc.setDescription("System Cross charge related to  " + deliveryRequest.getReference() + " from " + deliveryRequest.getProject().getName() + " delivered to " + deliveryRequest.getDestinationProject().getName() + " on " + UtilsFunctions.getFormattedDate(pc.getDate()));
		save(pc);

	}

	public void addCrossChargeForReturnFromOutbound(DeliveryRequest deliveryRequest) {
		Boolean canAddCrossCharge = deliveryRequest.getIsInboundReturn() && UtilsFunctions.compareDoubles(deliveryRequest.getTotalCost(), 0.0) > 0 && projectCrossRepos.countByDeliveryRequest(deliveryRequest.getId()) == 0 && projectCrossRepos.countByDeliveryRequest(deliveryRequest.getOutboundDeliveryRequestReturn().getId()) > 0;
		if (!canAddCrossCharge)
			return;
		ProjectCross pc = new ProjectCross();
		pc.setCategory(ProjectCrossCategory.PROJECT_TO_PROJECT);
		pc.setFromProject(deliveryRequest.getOutboundDeliveryRequestReturn().getDestinationProject());
		pc.setToProject(deliveryRequest.getProject());
		pc.setType(ProjectCrossTypes.COST.getValue());
		pc.setDate(deliveryRequest.getDate4());
		pc.setCashDate(deliveryRequest.getDate4());
		pc.setManual(false);
		pc.setDeliveryRequest(deliveryRequest);
		pc.setErp("Ilogistics");
		pc.setCurrency(currencyService.findOne(1));
		pc.setAmount(deliveryRequest.getTotalCost());
		pc.setCashAmount(deliveryRequest.getTotalCost() * 1.2);
		pc.setDescription("System Cross charge related to  " + deliveryRequest.getReference() + " from " + pc.getFromProject().getName() + " delivered to " + pc.getToProject().getName() + " on " + UtilsFunctions.getFormattedDate(pc.getDate()));
		save(pc);

	}

	public void delete(Integer id) {
		projectCrossRepos.deleteById(id);
	}

	public void addOrUpdateCrossChargeScript() {
		List<DeliveryRequest> toUpdate = deliveryRequestRepos.findToUpdateCrossCharge(DeliveryRequestType.OUTBOUND, Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
		if (toUpdate != null)
			for (DeliveryRequest deliveryRequest : toUpdate) {
				if (deliveryRequest.getCrossChargeId() == null)
					addCrossCharge(deliveryRequestService.findOne(deliveryRequest.getId()));
				else
					projectCrossRepos.updateAmount(deliveryRequest.getCrossChargeId(), deliveryRequest.getqTotalCost());
			}
	}

	public void addOrUpdateCrossChargeForReturnFromOutboundScript() {
		List<DeliveryRequest> toUpdate = deliveryRequestRepos.findToUpdateCrossChargeForReturnFromOutbound(DeliveryRequestStatus.DELIVRED);
		System.out.println("toupdate " + toUpdate);
		if (toUpdate != null)
			for (DeliveryRequest deliveryRequest : toUpdate) {
				System.out.println(deliveryRequest.getCrossChargeId());
				if (deliveryRequest.getCrossChargeId() == null)
					addCrossChargeForReturnFromOutbound(deliveryRequestService.findOne(deliveryRequest.getId()));
				else
					projectCrossRepos.updateAmount(deliveryRequest.getCrossChargeId(), deliveryRequest.getqTotalCost());
			}
	}

	public void deleteAndReCreateCrossCharge(DeliveryRequest deliveryRequest) {
		projectCrossRepos.deleteByDeliveryRequest(deliveryRequest.getId());
		addCrossCharge(deliveryRequest);

		List<DeliveryRequest> list = deliveryRequestRepos.findByOutboundDeliveryRequestReturn(deliveryRequest.getId());
		for (DeliveryRequest deliveryRequest2 : list) {
			projectCrossRepos.deleteByDeliveryRequest(deliveryRequest2.getId());
			addCrossChargeForReturnFromOutbound(deliveryRequest2);
		}
	}

}
