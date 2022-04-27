package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.InternalPartNumber;
import ma.azdad.repos.InternalPartNumberRepos;
import ma.azdad.service.CacheService;
import ma.azdad.service.InternalPartNumberService;

@ManagedBean
@Component
@Scope("view")
public class InternalPartNumberView extends GenericView<Integer, InternalPartNumber, InternalPartNumberRepos, InternalPartNumberService> {

	@Autowired
	InternalPartNumberService service;

	@Autowired
	CacheService cacheService;

	@Override
	@PostConstruct
	public void init() {
		cacheService.evictCache("internalPartNumberView");
	}

	@Cacheable("internalPartNumberView.findAll")
	public List<InternalPartNumber> findAll() {
		return service.findAll();
	}

}