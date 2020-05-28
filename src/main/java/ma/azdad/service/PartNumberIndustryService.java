package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberIndustry;
import ma.azdad.repos.PartNumberIndustryRepos;

@Component
@Transactional
public class PartNumberIndustryService extends GenericService<PartNumberIndustry> {

	@Autowired
	PartNumberIndustryRepos partNumberIndustryRepos;

	@Override
	public PartNumberIndustry findOne(Integer id) {
		PartNumberIndustry partNumberIndustry = super.findOne(id);
		// Hibernate.initialize(partNumberIndustry.get..);
		return partNumberIndustry;
	}

	public Boolean isNameExists(String name, Integer id) {
		return ObjectUtils.firstNonNull(id != null ? partNumberIndustryRepos.countByName(name, id) : partNumberIndustryRepos.countByName(name), 0l) > 0;
	}

	public List<String> findNameList() {
		return partNumberIndustryRepos.findNameList();
	}

	public PartNumberIndustry findByName(String name) {
		return partNumberIndustryRepos.findByName(name);
	}

}
