package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.EmailService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	EmailService es;

	@Test
	@Transactional
	public void testExample() throws Exception {

		es.sendSimpleMail("a.azdad@3gcom-int.com", "test", "test test");

	}

}
