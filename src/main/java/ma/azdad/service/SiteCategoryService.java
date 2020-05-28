package ma.azdad.service;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.SiteCategory;

@Component
@Transactional
public class SiteCategoryService extends GenericService<SiteCategory> {

	@Override
	public SiteCategory findOne(Integer id) {
		SiteCategory siteCategory = super.findOne(id);
		Hibernate.initialize(siteCategory.getTypeList());
		return siteCategory;
	}

}

