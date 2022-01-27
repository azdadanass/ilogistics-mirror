package ma.azdad.repos;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.StockRowService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	StockRowService stockRowService;

	@Autowired
	DeliveryRequestDetailService drds;

	@Test
	@Transactional
	public void test() throws Exception {
		
		System.out.println(drds.findPendingQuantityByCompanyOwnerGroupByPartNumber("m.bougri", Arrays.asList(-1), Arrays.asList(-1), 1));
	}

}
