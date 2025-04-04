package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.EmailService;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	DeliveryRequestService deliveryRequestService;
	
	@Autowired
	DeliveryRequestRepos deliveryRequestRepos;
	
	@Autowired
	EmailService emailService;

	@Test
	@Transactional
	public void test() throws Exception {
		
		String old  = deliveryRequestService.generateEmailNotification(deliveryRequestService.findOne(24387), "AA", true);
		System.out.println(old);
		
		System.out.println(deliveryRequestService.findDeliveryOverdue());
		emailService.sendDeliveryRequestDeliveryOverdueNotification(deliveryRequestService.findOne(18383));
		emailService.sendDeliveryRequestDeliveryOverdueNotification(deliveryRequestService.findOne(24387));
		
		Thread.sleep(3*1000);
	}

}
