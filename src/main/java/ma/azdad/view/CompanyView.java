package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Company;
import ma.azdad.service.CompanyService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class CompanyView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public CompanyService companyService;

	@PostConstruct
	public void init() {

	}

	public List<Company> findAll() {
		return companyService.findAll();
	}

}