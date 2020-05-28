package ma.azdad.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class FileReaderService {

	@Autowired
	private ResourceLoader resourceLoader;

	static String prefix = "${", suffix = "}";

	//example path : classpath:hs/t1.js
	public String readFile(String path, Map<String, String> params) {
		try {
			Resource res = resourceLoader.getResource(path);
			String result = IOUtils.toString(res.getInputStream());
			if (params != null)
				for (String key : params.keySet()) {
					String value = params.get(key);
					result = result.replace(prefix + key + suffix, value != null ? value : "");
				}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getVariables(String path) {

		String fileText = readFile(path);
		Pattern MY_PATTERN = Pattern.compile("[${][^}]*[}]");
		Matcher m = MY_PATTERN.matcher(fileText);
		while (m.find()) {
			String s = m.group(0);
			System.out.println(s);
		}
	}

	public String readFile(String path) {
		return readFile(path, null);
	}

}
