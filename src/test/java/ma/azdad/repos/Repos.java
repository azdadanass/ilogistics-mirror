package ma.azdad.repos;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.DeliveryRequestService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	DeliveryRequestService drs;

	
	@Test
	@Transactional
	public void test() throws Exception {
		
		System.out.println(drs.findByMissingOutbondDeliveryNoteFile("k.jabrane", Arrays.asList(-1), Arrays.asList(-1)).size());
		System.out.println(drs.countByMissingOutbondDeliveryNoteFile("k.jabrane", Arrays.asList(-1), Arrays.asList(-1)));

	}

}
