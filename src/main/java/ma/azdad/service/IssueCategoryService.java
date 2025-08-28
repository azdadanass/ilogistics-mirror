package ma.azdad.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.IssueCategory;
import ma.azdad.model.IssueParentType;
import ma.azdad.model.IssueType;
import ma.azdad.repos.IssueCategoryRepos;

@Component
public class IssueCategoryService extends GenericService<Integer, IssueCategory, IssueCategoryRepos> {
	
	@Autowired
	ProjectService projectService;

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
		return repos.findByProjectAndParenType(projectId, Arrays.asList(IssueParentType.DN,IssueParentType.TR));
	}

	public List<IssueCategory> findByProjectAndParenType(Integer projectId, IssueParentType parentType) {
		return repos.findByProjectAndParenType(projectId, parentType);
	}
	
	public List<IssueCategory> findByProjectAndParenTypeAndName(Integer projectId, IssueParentType parentType,String name) {
		return repos.findByProjectAndParenTypeAndName(projectId, parentType,name);
	}
	
	
	public void addDefaultIssueCategory(Integer projectId) {
		if(repos.countByProject(projectId, IssueParentType.DN)>0)
			return;
		
		String[] cats = { "Delivery Issue", "Time issue", "Resource issue", "Transportation Issue", "Other" };
		String[][] types = { {"Wrong Delivery","Missing Items","Damaged Items","Faulty Items","Damaged Packing / Bad Packing Quality","Other"},
			{"Warehouse Delivery delay","Other"}, //
			{"Bad Behaviors","Other"}, //
			{"Bad behaviors","Transportation delay","Bad Loading / Unloading","Inappropriate transportation means","Other"}, //
			{"Other"} 
		};
		
		for (int i = 0; i < cats.length; i++) {
			IssueCategory ic = new IssueCategory();
			ic.setName(cats[i]);
			ic.setProject(projectService.findOneLight(projectId));
			ic.setParentType(IssueParentType.DN);
			for (String type : types[i]) {
				IssueType it = new IssueType();
				it.setName(type);
				ic.addType(it);
			}
			repos.save(ic);
		}
	}
	
	public void addDefaultIssueCategoryScript() {
		projectService.findByCompanyWarehousing().forEach(i->addDefaultIssueCategory(i.getId()));
	}
	

}
