package ma.azdad.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import ma.azdad.utils.TemplateType;

@Component
public class ThymeLeafService {

	TemplateEngine htmlTemplateEngine;
	TemplateEngine textTemplateEngine;

	@PostConstruct
	public void init() {
		initHtmlTemplateEngine();
		initTextTemplateEngine();
	}

	public String generate(TemplateType templateType, String template, Map<String, Object> parameters) {
		Context context = new Context();
		parameters.forEach((k, v) -> context.setVariable(k, v));
		switch (templateType) {
		case HTML:
			return htmlTemplateEngine.process(template, context);
		case TEXt:
			return htmlTemplateEngine.process(template, context);
		default:
			return null;
		}
	}

	private void initHtmlTemplateEngine() {
		htmlTemplateEngine = new TemplateEngine();
		htmlTemplateEngine.setTemplateResolver(htmlTemplateResolver());
	}

	private void initTextTemplateEngine() {
		textTemplateEngine = new TemplateEngine();
		textTemplateEngine.setTemplateResolver(textTemplateResolver());
	}

	private FileTemplateResolver htmlTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setOrder(1);
		templateResolver.setPrefix(getTemplatePath() + "html/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		return templateResolver;
	}

	private FileTemplateResolver textTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setOrder(2);
		templateResolver.setPrefix(getTemplatePath() + "text/");
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		return templateResolver;
	}

	private String getTemplatePath() {
		return ThymeLeafService.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "templates/";
	}

	public void test() {
		List<Integer> l = Arrays.asList(1, 2, 3);

		Context context = new Context();

		context.setVariable("title", "User Page");
		context.setVariable("name", "John Doeeeeeeeeeeeeeeeeee");
		context.setVariable("list", l);
		context.setVariable("subscriptionDate", new Date());

		String s1 = htmlTemplateEngine.process("test.html", context);
		String s2 = textTemplateEngine.process("test.txt", context);

		System.out.println(s1);
		System.out.println(s2);
	}

}