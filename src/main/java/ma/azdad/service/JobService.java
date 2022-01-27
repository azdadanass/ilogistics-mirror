package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class JobService {

	@Autowired
	PartNumberService partNumberService;

	@Autowired
	SiteService siteService;

	@Autowired
	ProjectCrossService projectCrossService;

	@Autowired
	TransportationRequestService transportationRequestService;

	@Autowired
	PartNumberEquivalenceService partNumberEquivalenceService;
	
	@Autowired
	PartNumberPricingService partNumberPricingService;

	@Autowired
	PoService poService;

	@Autowired
	DeliveryRequestService deliveryRequestService;

	@Async
	@Scheduled(cron = "0 34 19 * * *")
	public void test() {
	}

	@Async
	@Scheduled(cron = "0 15 05 * * *")
	public void updateSitesGoogleGeocodeData() {
		siteService.updateGoogleGeocodeData();
	}

	@Async
	@Scheduled(cron = "0 00 03 * * *")
	public void updatePaymentStatus() {
		transportationRequestService.updatePaymentStatus();
	}

	@Async
	@Scheduled(cron = "0 00 04 * * *")
	public void addOrUpdateCrossChargeScript() {
		projectCrossService.addOrUpdateCrossChargeScript();
		projectCrossService.addOrUpdateCrossChargeForReturnFromOutboundScript();
	}

	@Scheduled(cron = "0 30 04 * * *")
	public void updateImage() {
		partNumberService.updateImage();
	}

	@Scheduled(cron = "0 35 04 * * *")
	public void updateFormulaAndType() {
		partNumberEquivalenceService.updateFormulaAndType();
	}

	@Scheduled(cron = "0 40 04 * * *")
	public void updateInverseFormula() {
		partNumberEquivalenceService.updateInverseFormula();
	}

	@Scheduled(cron = "0 45 04 * * *")
	public void updateAllPoDeliveryStatusScript() {
		poService.updateAllDeliveryStatusScript();
	}

	@Scheduled(cron = "00 40 00 * * *")
	public void updateCountIssuesScript() {
		deliveryRequestService.updateCountIssuesScript();
	}
	
	@Scheduled(cron = "00 00 05 * * *")
	public void updatePartNumberPricingQuantities() {
		partNumberPricingService.updateQuantities();
	}

}