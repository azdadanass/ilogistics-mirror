package ma.azdad.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.PartNumberPricingService;

@RestController
public class PartNumberPricingRestService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PartNumberPricingService partNumberPricingService;

	@Autowired
	DeliveryRequestDetailService deliveryRequestDetailService;

	@RequestMapping("/rest/partNumberPricing/updateQuantities")
	public String updateQuantities() {
		log.info("updateQuantities");
		partNumberPricingService.updateQuantities();
		return "SUCCESS";
	}

	@RequestMapping("/rest/partNumberPricing/costHistory/{partNumberId}/{companyId}")
	public List<DeliveryRequestCostHistory> findCostHistory(@PathVariable Integer partNumberId, @PathVariable Integer companyId) {
		List<DeliveryRequestDetail> data = deliveryRequestDetailService.findByPartNumberAndTypeAndProjectTypeStockAndProjectCompanyAndDeliveryRequestStatus(partNumberId, DeliveryRequestType.INBOUND, companyId, Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED));
		return data.stream().map(deliveryRequestDetail -> new DeliveryRequestCostHistory(deliveryRequestDetail)).collect(Collectors.toList());
	}

}
