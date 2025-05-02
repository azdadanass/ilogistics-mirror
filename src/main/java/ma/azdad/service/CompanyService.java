package ma.azdad.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Company;
import ma.azdad.model.Currency;
import ma.azdad.repos.CompanyRepos;
import ma.azdad.utils.LabelValue;

@Component
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepos repos;

	public List<LabelValue> findLabelValueList() {
		return repos.findLabelValueList();
	}

	public Company findOne(Integer id) {
		return repos.findById(id).get();
	}

	public List<Company> find(List<Integer> idList) {
		if (idList == null || idList.isEmpty())
			return null;
		return repos.find(idList);
	}

	public List<Company> findAll() {
		return repos.findAll();
	}

	public Integer findIdByProject(Integer projectId) {
		return repos.findIdByProject(projectId);
	}

	@Cacheable("companyService.findLight")
	public List<Company> findLight() {
		return repos.findLight();
	}

	public List<Integer> findIdList() {
		return repos.findIdList();
	}

	public Company findCompanyUser(String username) {
		return repos.findCompanyUser(username);
	}

	public Currency findAccountingCurrency(Integer id) {
		return repos.findAccountingCurrency(id);
	}
	
	@Cacheable("companyService.findLightByProject")
	public Company findLightByProject(Integer projectId) {
		return repos.findLightByProject(projectId);
	}

	@Cacheable("companyService.findNameMap")
	public Map<Integer, String> findNameMap() {
		return repos.findLight().stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getName()));
	}
}
