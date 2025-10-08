package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.ZoneHeightService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	ZoneHeightService zoneHeightService;

	@Test
	@Transactional
	public void test() throws Exception {
		zoneHeightService.updateFillPercentage(243);
	}

}
