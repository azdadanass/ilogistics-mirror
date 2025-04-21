package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.EmailService;
import ma.azdad.service.StockRowService;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	StockRowService stockRowService; 
	
	@Autowired
	DeliveryRequestService deliveryRequestService; 
	

	@Autowired
	DeliveryRequestRepos deliveryRequestRepos; 
	
	@Autowired
	EmailService emailService; 
	

	@Test
	@Transactional
	public void test() throws Exception {
//		emailService.sendDeliveryRequestPendingAcknowledgementNotification();
		deliveryRequestService.ackOldDeliveryRequestsScript();
		System.out.println(deliveryRequestService.findPendingAcknowledgementIdList().size());
	}

}
