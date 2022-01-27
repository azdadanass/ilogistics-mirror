package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.PartNumberPricingService;
import ma.azdad.service.StockRowService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	StockRowService stockRowService;

	@Autowired
	DeliveryRequestDetailService drds;
	
	@Autowired
	PartNumberPricingService pnps;

	@Test
	@Transactional
	public void test() throws Exception {
		
		System.out.println(stockRowRepos.findStockHistoryByPartNumberAndProjectStock(2605, 1));
	}

}
