package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.LocationService;
import ma.azdad.service.PackingService;
import ma.azdad.service.TransportationJobService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	LocationService locationService;
	
	@Autowired
	DeliveryRequestService deliveryRequestService;
	
	@Autowired
	TransportationJobService transportationJobService;
	
	@Autowired
	PackingService packingService;

	@Test
	@Transactional
	public void test() throws Exception {
		transportationJobService.calculateEstimatedCostsScript();
	}

}
