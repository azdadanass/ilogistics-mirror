package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
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
	public CompanyService service;

	@Autowired
	public CacheManager cacheManager;

	@PostConstruct
	public void init() {
		cacheManager.getCache("companyView.findAll").clear();
		cacheManager.getCache("companyView.findIdByProject").clear();
	}

	@Cacheable("companyView.findAll")
	public List<Company> findAll() {
		return service.findAll();
	}

	@Cacheable("companyView.findIdByProject")
	public Integer findIdByProject(Integer projectId) {
		return service.findIdByProject(projectId);
	}

	public List<Company> findLight() {
		return service.findLight();
	}
	
	public Company findCompanyUser(String username) {
		return service.findCompanyUser(username);
	}

}