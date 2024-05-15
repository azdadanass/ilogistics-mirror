package ma.azdad.repos;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.model.IssueParentType;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.IssueCategoryService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	IssueCategoryService ics;

	
	@Test
	@Transactional
	public void test() throws Exception {
		
	System.out.println(ics.findByProjectAndParenType(552, IssueParentType.DN));

	}

}
