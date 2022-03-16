package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.PoService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	SiteRepos sr;

	@Test
	@Transactional
	public void test() throws Exception {

		System.out.println(sr.findByCategoryAndGoogleRegion(5, "Oriental").size());
		System.out.println(sr.findByCategoryAndGoogleRegion(null, "Oriental").size());
		System.out.println(sr.findByCategoryAndGoogleRegion(null, null).size());

	}

}
