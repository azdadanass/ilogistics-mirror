package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	DeliveryRequestExpiryDateRepos deliveryRequestExpiryDateRepos;
	

	@Test
	@Transactional
	public void test() throws Exception {
		
		deliveryRequestExpiryDateRepos.findByInboundDeliveryRequest(8726).forEach(i->{
			System.out.println(i.getPartNumberId());
			System.out.println(i.getPartNumberName());
			System.out.println(i.getQuantity());
			System.out.println(i.getExpiryDate());
			
		});
		
		
		
	}

}
