package ma.azdad.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.TransportationJob;

@Transactional
@Rollback(false)
public class Service extends GenericTest {

	@Autowired
	TransportationJobService tjs;

	@Test
	public void testExample() throws Exception {

		TransportationJob tj1 = tjs.findOne(2);

		tj1.generatePathList();

	}

}