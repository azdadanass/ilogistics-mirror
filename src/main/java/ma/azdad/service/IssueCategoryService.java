package ma.azdad.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.IssueCategory;
import ma.azdad.model.IssueParentType;
import ma.azdad.repos.IssueCategoryRepos;

@Component
public class IssueCategoryService extends GenericService<Integer, IssueCategory, IssueCategoryRepos> {

	@Cacheable("issueCategoryService.findAll")
	public List<IssueCategory> findAll() {
		return repos.findAll();
	}

	@Cacheable("issueCategoryService.findOne")
	public IssueCategory findOne(Integer id) {
		IssueCategory issueCategory = super.findOne(id);
		initialize(issueCategory.getTypeList());
		return issueCategory;
	}

	public List<IssueCategory> findByProject(Integer projectId) {
		return repos.findByProjectAndParenType(projectId, Arrays.asList(IssueParentType.JR, IssueParentType.TICKET));
	}

	public List<IssueCategory> findByProjectAndParenType(Integer projectId, IssueParentType parentType) {
		return repos.findByProjectAndParenType(projectId, parentType);
	}

}
