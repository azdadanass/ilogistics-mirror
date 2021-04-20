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

import ma.azdad.model.GenericModel;

@Transactional
public class GenericService<ID, M extends GenericModel<ID>, R extends JpaRepository<M, ID>> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	private String modelClassName1 = ((Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getSimpleName();
	private String modelClassName2 = modelClassName1.substring(0, 1).toLowerCase() + modelClassName1.substring(1);

	@Autowired
	protected R repos;

	@Autowired
	protected CacheService cacheService;

	public M findOne(ID id) {
		return repos.findById(id).get();
	}

	public M findOneLight(ID id) {
		return repos.findById(id).get();
	}

	public List<M> findAll() {
		return repos.findAll();
	}

	public M save(M model) {
		cacheEvict();
		return repos.save(model);
	}

	public M saveAndRefresh(M model) {
		save(model);
		return findOne(model.id());
	}

	public void delete(ID id) throws DataIntegrityViolationException, Exception {
		cacheEvict();
		repos.deleteById(id);
	}

	public void delete(M model) throws DataIntegrityViolationException, Exception {
		cacheEvict();
		repos.delete(model);
	}

	public Long count() {
		return repos.count();
	}

	public Boolean exists(ID id) {
		return repos.existsById(id);
	}

	public void cacheEvict() {
		System.out.println(getClass().getSimpleName());
		cacheService.evictCachePrefix(modelClassName2 + "Service");
	}

	public void initialize(Object object) {
		Hibernate.initialize(object);
	}

	public void initialize(Object... objects) {
		for (int i = 0; i < objects.length; i++)
			Hibernate.initialize(objects[i]);
	}

	public void flush() {
		repos.flush();
	}

}
