package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.IssueCategory;
import ma.azdad.repos.IssueCategoryRepos;
import ma.azdad.utils.App;

@Component
public class IssueCategoryService extends GenericService<Integer, IssueCategory, IssueCategoryRepos> {

	@Cacheable("issueCategoryService.findAll")
	public List<IssueCategory> findAll() {
		return repos.findAll();
	}

	@Cacheable("issueCategoryService.findOne")
	public IssueCategory findOne(Integer id) {
		IssueCategory issueCategory = super.findOne(id);

		return issueCategory;
	}

	public List<IssueCategory> findByApp() {
		return repos.findByApp(App.ILOGISTICS);
	}

}
