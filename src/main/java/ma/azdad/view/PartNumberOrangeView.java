package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberOrange;
import ma.azdad.service.CacheService;
import ma.azdad.service.PartNumberOrangeService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PartNumberOrangeView extends GenericViewOld<PartNumberOrange> {

	@Autowired
	PartNumberOrangeService service;

	@Autowired
	CacheService cacheService;

	@Override
	@PostConstruct
	public void init() {
		cacheService.evictCachePrefix("partNumberOrangeView");
	}

	@Cacheable("partNumberOrangeView.findAll")
	public List<PartNumberOrange> findAll() {
		return service.findAll();
	}

}