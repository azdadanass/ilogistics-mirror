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

	@Autowired
	DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;

	@Autowired
	EmailService emailService;

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
	public void updateAllIlogisticsStatusAndGoodsDeliveryStatusScript() {
		poService.updateAllIlogisticsStatusAndGoodsDeliveryStatusScript();
	}

	@Scheduled(cron = "00 40 00 * * *")
	public void updateCountIssuesScript() {
		deliveryRequestService.updateCountIssuesScript();
	}

	@Scheduled(cron = "00 00 05 * * *")
	public void updatePartNumberPricingQuantities() {
		partNumberPricingService.updateQuantities();
	}

	@Scheduled(cron = "00 10 05 * * *")
	public void calculatePendingJrMappingScript() {
		deliveryRequestService.calculatePendingJrMappingScript();
	}

	@Scheduled(cron = "00 20 05 * * *")
	public void calculateHavingRunningStockScript() {
		deliveryRequestService.calculateHavingRunningStockScript();
	}

	@Scheduled(cron = "00 30 05 * * *")
	public void calculateMissingExpiryScript() {
		deliveryRequestService.calculateMissingExpiryScript();
	}

	@Scheduled(cron = "00 40 05 * * *")
	public void calculateMissingSerialNumberScript() {
		deliveryRequestService.calculateMissingSerialNumberScript();
	}

	@Scheduled(cron = "00 40 05 * * *")
	public void sendDeliveryRequestDeliveryOverdueNotification() {
		emailService.sendDeliveryRequestDeliveryOverdueNotification();
	}

	@Scheduled(cron = "00 45 05 * * *")
	public void sendDeliveryRequestPendingAcknowledgementNotification() {
		emailService.sendDeliveryRequestPendingAcknowledgementNotification();
	}

//	@Scheduled(cron = "00 * 01-05 * * *")
//	public void automaticFillOutboundSerialNumberScript() {
//		deliveryRequestSerialNumberService.automaticFillOutboundSerialNumberScript(200);
//	}

}