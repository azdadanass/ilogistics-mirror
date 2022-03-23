package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequestType;
import ma.azdad.service.CustomerService;
import ma.azdad.service.DeliveryRequestExpiryDateService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.EmailService;
import ma.azdad.service.PackingService;
import ma.azdad.service.PartNumberEquivalenceService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.PoService;
import ma.azdad.service.ProjectCrossService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class ScriptView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public DeliveryRequestService deliveryRequestService;

	@Autowired
	public PartNumberService partNumberService;

	@Autowired
	public ProjectCrossService projectCrossService;

	@Autowired
	public SessionView sessionView;

	@Autowired
	PartNumberEquivalenceService partNumberEquivalenceService;

	@Autowired
	PoService poService;

	@Autowired
	CustomerService customerService;

	@Autowired
	PackingService packingService;

	@Autowired
	DeliveryRequestExpiryDateService deliveryRequestExpiryDateService;

	@Autowired
	EmailService emailService;

	public Boolean canExecute;

	private Integer inboundDeliveryRequestId;

	@PostConstruct
	public void init() {
		canExecute = "a.azdad".equals(sessionView.getUsername());
	}

	public void sendSimpleMail() {
		emailService.sendSimpleMail("a.azdad@3gcom-int.com", "test", "test test");
	}

	public void fillDestinationProject() {
		if (!canExecute)
			return;
		deliveryRequestService.fillDestinationProject();
	}

	public void addOrUpdateCrossChargeScript() {
		if (!canExecute)
			return;
		projectCrossService.addOrUpdateCrossChargeScript();
		projectCrossService.addOrUpdateCrossChargeForReturnFromOutboundScript();
	}

	public void updatePartNumberImage() {
		if (!canExecute)
			return;
		partNumberService.updateImage();
	}

	public void updateFormulaAndType() {
		if (!canExecute)
			return;
		partNumberEquivalenceService.updateFormulaAndType();
	}

	public void updateInverseFormula() {
		if (!canExecute)
			return;
		partNumberEquivalenceService.updateInverseFormula();
	}

	public void updateAllBoqStatusAndDeliveryStatusScript() {
		if (!canExecute)
			return;
		poService.updateAllBoqStatusAndDeliveryStatusScript();
	}

	public void updateIsStockEmptyScript() {
		if (!canExecute)
			return;
		customerService.updateIsStockEmptyScript();
	}

	public void createPackingForPartNumberWithoutPackingList() {
		if (!canExecute)
			return;
		packingService.createPackingForPartNumberWithoutPackingList();
	}

	public void updateHasPacking() {
		if (!canExecute)
			return;
		partNumberService.updateHasPacking();
	}

	public void generateQrKeyScript() {
		if (!canExecute)
			return;
		deliveryRequestService.generateQrKeyScript();
	}

	public void updateIsFullyReturnedForExistingOutbound() {
		if (!canExecute)
			return;
		deliveryRequestService.updateIsFullyReturnedForExistingOutbound();
	}

	public void generateForOutboundAssociatedWithInbound() {
		deliveryRequestExpiryDateService.generateForOutboundAssociatedWithInbound(inboundDeliveryRequestId);
	}
	
	public void updateDetailListPurchaseCostFromBoqMappingScript() {
		deliveryRequestService.updateDetailListPurchaseCostFromBoqMappingScript();
	}

	// GETTERS & SETTERS
	public Integer getInboundDeliveryRequestId() {
		return inboundDeliveryRequestId;
	}

	public void setInboundDeliveryRequestId(Integer inboundDeliveryRequestId) {
		this.inboundDeliveryRequestId = inboundDeliveryRequestId;
	}

}