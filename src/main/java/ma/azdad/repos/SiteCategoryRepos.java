package ma.azdad.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  ma.azdad.model.SiteCategory;


@Repository
public interface SiteCategoryRepos extends JpaRepository<SiteCategory, Integer> {

	
}

