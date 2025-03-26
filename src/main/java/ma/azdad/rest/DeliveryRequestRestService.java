package ma.azdad.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.service.DeliveryRequestService;

@RestController
public class DeliveryRequestRestService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DeliveryRequestService deliveryRequestService;

	@RequestMapping("/rest/deliveryRequest/calculatePendingJrMapping/{id}")
	public void calculatePendingJrMapping(@PathVariable Integer id) {
		deliveryRequestService.calculatePendingJrMapping(id);
	}

}
