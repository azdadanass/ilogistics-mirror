package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.SiteType;
import ma.azdad.repos.SiteTypeRepos;

@Component
@Transactional
public class SiteTypeService extends GenericService<Integer, SiteType, SiteTypeRepos> {

	@Autowired
	SiteTypeRepos siteTypeRepos;

	@Override
	public SiteType findOne(Integer id) {
		SiteType siteType = super.findOne(id);
		return siteType;
	}

	public List<SiteType> findByCategory(Integer categoryId) {
		return siteTypeRepos.findByCategory(categoryId);
	}
}
