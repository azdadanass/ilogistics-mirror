package ma.azdad.repos;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	StockRowRepos stockRowRepos;

	@Test
	@Transactional
	public void test() throws Exception {
		
		System.out.println(stockRowRepos.findDeliveryListsByCompanyOwner2("m.bougri", Arrays.asList(-1),  Arrays.asList(-1), 1));
		
	}

}
