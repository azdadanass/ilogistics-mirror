package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.ExternalResourceService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	ExternalResourceService ers;

	@Test
	@Transactional
	public void testExample() throws Exception {

		ers.exportToUserScript();

	}

}
