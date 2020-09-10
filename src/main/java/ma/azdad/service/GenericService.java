package ma.azdad.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import ma.azdad.model.GenericBean;

@Transactional
public class GenericService<A extends GenericBean, B extends JpaRepository<A, Integer>> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected B repos;

	@Autowired
	protected CacheService cacheService;

	public A findOne(Integer id) {
		return repos.findById(id).get();
	}

	public A findOneLight(Integer id) {
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
		save(a);
		return findOne(a.id());
	}

	public void delete(Integer id) throws DataIntegrityViolationException, Exception {
		cacheEvict();
		repos.deleteById(id);
	}

	public void delete(A a) throws DataIntegrityViolationException, Exception {
		cacheEvict();
		repos.delete(a);
	}

	public Long count() {
		return repos.count();
	}

	public Boolean exists(Integer id) {
		return repos.existsById(id);
	}

	public A saveAndFlush(A a) {
		cacheEvict();
		return repos.saveAndFlush(a);
	}

	public void flush() {
		repos.flush();
	}

	public void cacheEvict() {
		String className2 = getParameterClassName().substring(0, 1).toLowerCase() + getParameterClassName().substring(1);
		cacheService.evictCachePrefix(className2 + "Service");
	}

	public void initialize(Object proxy) {
		Hibernate.initialize(proxy);
	}

	@SuppressWarnings("unchecked")
	public String getParameterClassName() {
		return ((Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
	}

}