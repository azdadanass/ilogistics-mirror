package ma.azdad.repos;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.StockRowService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	StockRowService stockRowService;

	@Test
	@Transactional
	public void test() throws Exception {

		System.out.println(stockRowRepos.findStockHistoryByPartNumberAndCompanyOwner("m.bougri", Arrays.asList(1), Arrays.asList(-1), 1, 2603).stream().filter(i -> i.getDeliveryRequestId().equals(9688)).count());
		stockRowRepos.findStockHistoryByPartNumberAndCompanyOwner("m.bougri", Arrays.asList(1), Arrays.asList(-1), 1, 2603).stream().filter(i -> i.getDeliveryRequestId().equals(9688)).forEach(i -> System.out.println(i.getInboundDeliveryRequestId()));
	}

}
