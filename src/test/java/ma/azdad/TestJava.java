package ma.azdad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class TestJava {

	@Test
	public void test() throws InterruptedException, IOException {
		List<String> aaaaaaa;
		if ((aaaaaaa = secondCondtion()) != null) {
			System.out.println(aaaaaaa);
		}

	}

	public List<String> secondCondtion() {
		if (status())
			return file();

		return null;
	}

	public Boolean status() {
		return true;
	}

	public List<String> file() {
		List<String> result = new ArrayList<String>();
		result.add("sdqgqsdgqsdg");
		if (CollectionUtils.isEmpty(result))
			return null;
		return result;
	}

}