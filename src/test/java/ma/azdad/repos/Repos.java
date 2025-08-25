package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.TransportationRequestState;
import ma.azdad.service.DriverLocationService;
import ma.azdad.service.TransportationRequestService;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	DriverLocationService driverLocationService;
	

	@Test
	@Transactional
	public void test() throws Exception {
		System.out.println(driverLocationService.getLastLocation("a.azdad").getLatitude());
	}

}
