package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	StockRowRepos srr;

	@Test
	@Transactional
	public void test() throws Exception {

		srr.count();
		start();

		System.out.println(srr.getFinancialSituation2(1));
		
	}

}
