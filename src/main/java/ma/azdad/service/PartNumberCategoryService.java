package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberCategory;
import ma.azdad.repos.PartNumberCategoryRepos;

@Component
@Transactional
public class PartNumberCategoryService extends GenericService<Integer, PartNumberCategory, PartNumberCategoryRepos> {

	@Autowired
	PartNumberCategoryRepos partNumberCategoryRepos;

	@Override
	public PartNumberCategory findOne(Integer id) {
		PartNumberCategory partNumberCategory = super.findOne(id);
		// Hibernate.initialize(partNumberCategory.get..);
		return partNumberCategory;
	}

	public Boolean isNameAndIndustryExists(String name, Integer industryId, Integer id) {
		return ObjectUtils.firstNonNull(id != null ? partNumberCategoryRepos.countByNameAndIndustry(name, industryId, id) : partNumberCategoryRepos.countByNameAndIndustry(name, industryId), 0l) > 0;
	}

	public List<PartNumberCategory> findByIndustry(Integer industryId) {
		return partNumberCategoryRepos.findByIndustry(industryId);
	}

	public List<String> findNameList(Integer industryId) {
		return partNumberCategoryRepos.findNameList(industryId);
	}

	public PartNumberCategory findByName(Integer industryId, String name) {
		return partNumberCategoryRepos.findByName(industryId, name);
	}

}
