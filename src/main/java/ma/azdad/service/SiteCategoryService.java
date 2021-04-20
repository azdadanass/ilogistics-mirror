package ma.azdad.service;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.SiteCategory;
import ma.azdad.repos.SiteCategoryRepos;

@Component
@Transactional
public class SiteCategoryService extends GenericService<Integer, SiteCategory, SiteCategoryRepos> {

	@Override
	public SiteCategory findOne(Integer id) {
		SiteCategory siteCategory = super.findOne(id);
		Hibernate.initialize(siteCategory.getTypeList());
		return siteCategory;
	}

}
