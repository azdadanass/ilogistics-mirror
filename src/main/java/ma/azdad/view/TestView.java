package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.service.FileReaderService;

@ManagedBean
@Component

@Scope("view")
public class TestView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FileReaderService fs;

	@PostConstruct
	public void init() {
	}

	// @Transactional
	public void test() {
	}

}