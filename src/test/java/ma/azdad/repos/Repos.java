package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.StockRowState;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.LocationService;
import ma.azdad.service.PackingService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	LocationService locationService;
	
	@Autowired
	DeliveryRequestService deliveryRequestService;
	
	@Autowired
	PackingService packingService;

	@Test
	@Transactional
	public void test() throws Exception {
		DeliveryRequest dn =  deliveryRequestService.findOne(24879);
		dn.setStockRowState(StockRowState.NORMAL);
		
		System.out.println(locationService.findAvailableLocationList(dn, packingService.findOne(46)));
	}

}
