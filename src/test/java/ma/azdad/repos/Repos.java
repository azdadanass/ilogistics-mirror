package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	DeliveryRequestRepos deliveryRequestRepos;
	

	@Test
	@Transactional
	public void test() throws Exception {
		deliveryRequestRepos.updateNumberOfItems();
		deliveryRequestRepos.updateGrossWeight();
		deliveryRequestRepos.updateNetWeight();
		deliveryRequestRepos.updateVolume();
	}

}
