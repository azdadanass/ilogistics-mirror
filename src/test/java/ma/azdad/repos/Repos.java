package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.GenericModel;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	DeliveryRequestRepos drr;

	
	@Test
	@Transactional
	public void test() throws Exception {
		
		

	}

}
