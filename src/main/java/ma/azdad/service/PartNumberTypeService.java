package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberType;
import ma.azdad.repos.PartNumberTypeRepos;

@Component
@Transactional
public class PartNumberTypeService extends GenericServiceOld<PartNumberType> {

	@Autowired
	PartNumberTypeRepos partNumberTypeRepos;

	@Override
	public PartNumberType findOne(Integer id) {
		PartNumberType partNumberType = super.findOne(id);
		// Hibernate.initialize(partNumberType.get..);
		return partNumberType;
	}

	public Boolean isNameAndCategoryExists(String name, Integer categoryId, Integer id) {
		return ObjectUtils.firstNonNull(id != null ? partNumberTypeRepos.countByNameAndCategory(name, categoryId, id) : partNumberTypeRepos.countByNameAndCategory(name, categoryId), 0l) > 0;
	}

	public List<PartNumberType> findByCategory(Integer categoryId) {
		return partNumberTypeRepos.findByCategory(categoryId);
	}

	public List<String> findNameList(Integer categoryId) {
		return partNumberTypeRepos.findNameList(categoryId);
	}

	public PartNumberType findByName(Integer categoryId, String name) {
		return partNumberTypeRepos.findByName(categoryId, name);
	}

}
