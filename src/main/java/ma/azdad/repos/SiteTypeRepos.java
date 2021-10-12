package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SiteType;

@Repository
public interface SiteTypeRepos extends JpaRepository<SiteType, Integer> {

	@Query("from SiteType where category.id = ?1")
	public List<SiteType> findByCategory(Integer categoryId);
}
