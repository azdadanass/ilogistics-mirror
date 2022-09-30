package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.Po;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	PoRepos poRepos;

	@Autowired
	StockRowRepos stockRowRepos;
	
	@Autowired
	BoqRepos boqRepos;

	
	@Test
	@Transactional
	public void test() throws Exception {
		
		poRepos.findById(6).get().equals(new Po());
		System.out.println("--------------");
		poRepos.findAll().get(0).equals(new Po());
		
		System.out.println(poRepos.findById(6).get().getSupplier().getClass().getSimpleName());
		System.out.println(poRepos.findById(6).get().getProject().getClass().getSimpleName());

	}

}
