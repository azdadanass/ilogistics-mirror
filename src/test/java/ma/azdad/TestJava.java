package ma.azdad;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

public class TestJava {

	@Test
	public void test() throws InterruptedException, IOException {

		Set<String> keySet = new HashSet<String>();
		System.out.println(keySet.add("a"));
		System.out.println(keySet.add("a"));

	}

}