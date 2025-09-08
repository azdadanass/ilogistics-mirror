package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.UserService;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	TransportationRequestService transportationRequestService;
	
	@Autowired
	UserService userService;
	

	@Test
	@Transactional
	public void test() throws Exception {
		transportationRequestService.sendPendingAckNotification();
		Thread.sleep(3*1000);
	}

}
