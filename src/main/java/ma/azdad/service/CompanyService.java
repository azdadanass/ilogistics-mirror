package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Company;
import ma.azdad.repos.CompanyRepos;
import ma.azdad.utils.LabelValue;

@Component
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepos companyRepos;

	public List<LabelValue> findLabelValueList() {
		return companyRepos.findLabelValueList();
	}

	public Company findOne(Integer id) {
		return companyRepos.findById(id).get();
	}

	public List<Company> find(List<Integer> idList) {
		if (idList == null || idList.isEmpty())
			return null;
		return companyRepos.find(idList);
	}

	public List<Company> findAll() {
		return companyRepos.findAll();
	}
}
