package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ma.azdad.model.Text;
import ma.azdad.repos.TextRepos;

@Component
public class TextService extends GenericService<Integer, Text, TextRepos> {

	@Autowired
	TextRepos textRepos;

	@Value("${applicationCode}")
	public String applicationCode;

	public List<String> findValueByBeanName(String beanName) {
		return textRepos.findValueByAppAndBeanName(applicationCode, beanName);
	}

	public List<String> findValueByBeanNameAndType(String beanName, String type) {
		return textRepos.findValueByAppAndBeanNameAndType(applicationCode, beanName, type);
	}

	public List<Text> findByBeanNameAndType(String beanName, String type) {
		return textRepos.findByAppAndBeanNameAndType(applicationCode, beanName, type);
	}

	public List<String> findValueListByBeanNameAndType(String beanName, String type) {
		return textRepos.findValueListByAppAndBeanNameAndType(applicationCode, beanName, type);
	}

	@Override
	public Text save(Text a) {
		a.setApp(applicationCode);
		return super.save(a);
	}
}
