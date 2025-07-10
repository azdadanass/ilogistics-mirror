package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.IssueType;
import ma.azdad.repos.IssueTypeRepos;

@Component
public class IssueTypeService extends GenericService<Integer, IssueType, IssueTypeRepos> {

	@Cacheable("issueTypeService.findAll")
	public List<IssueType> findAll() {
		return repos.findAll();
	}

	@Cacheable("issueTypeService.findOne")
	public IssueType findOne(Integer id) {
		IssueType issueType = super.findOne(id);

		return issueType;
	}

	public List<IssueType> findByCategory(Integer categoryId) {
		return repos.findByCategory(categoryId);
	}
	
	public List<IssueType> findByCategoryAndName(Integer categoryId,String name) {
		return repos.findByCategoryAndName(categoryId,name);
	}


}
