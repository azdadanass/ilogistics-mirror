package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.StockRowService;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	StockRowService stockRowService; 
	
	@Autowired
	DeliveryRequestService deliveryRequestService; 
	

	@Test
	@Transactional
	public void test() throws Exception {
		DeliveryRequest deliveryRequest = deliveryRequestService.findOne(24400);
		System.out.println(stockRowService.generateStockRowFromOutboundDeliveryRequest(deliveryRequest, "DEFAULT"));
		System.out.println(stockRowService.generateStockRowFromOutboundDeliveryRequest(deliveryRequest, "FIFO"));
		
		
	}

}
