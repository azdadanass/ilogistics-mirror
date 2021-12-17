package ma.azdad.repos;

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
	}

}
