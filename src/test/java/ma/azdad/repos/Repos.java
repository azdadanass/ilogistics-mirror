package ma.azdad.repos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.GenericTest;
import ma.azdad.service.HighchartsService;
import ma.azdad.utils.Series;

@Rollback(false)
public class Repos extends GenericTest {

	
	@Autowired
	HighchartsService highchartsService;

	@Test
	@Transactional
	public void test() throws Exception {
		Double[] a = {1.0,2.0};
		Series[] s = {new Series("aaa","red", a)};
		
		
//		System.out.println(highchartsService.generatePieChart("s", "a",s));
		
	}

}
