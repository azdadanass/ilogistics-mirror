package ma.azdad.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import ma.azdad.model.GenericBean;

public class GenericServiceOld<A extends GenericBean> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected JpaRepository<A, Integer> repos;

	@Autowired
	protected CacheService cacheService;

	public A findOneLight(Integer id) {
		return repos.findById(id).get();
	}

	@Transactional
	public A findOne(Integer id) {
		return repos.findById(id).get();
	}

	public List<A> findAll() {
		return repos.findAll();
	}

	public A save(A a) {
		cacheEvict();
		return repos.save(a);
	}

	public A saveAndRefresh(A a) {
		A ca = save(a);
		return findOne(ca.id());
	}

	public void delete(Integer id) {
		cacheEvict();
		try {
			repos.deleteById(id);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			log.error(dataIntegrityViolationException.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void delete(A a) {
		cacheEvict();
		delete(a.id());
	}

	public Long count() {
		return repos.count();
	}

	public void flush() {
		repos.flush();
	}

	public void cacheEvict() {
		String className2 = getParameterClassName().substring(0, 1).toLowerCase() + getParameterClassName().substring(1);
		cacheService.evictCachePrefix(className2 + "Service");
	}

	@SuppressWarnings("unchecked")
	public String getParameterClassName() {
		return ((Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
	}

	public Boolean exists(Integer id) {
		return repos.existsById(id);
	}

}