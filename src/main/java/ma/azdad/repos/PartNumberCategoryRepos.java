package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberCategory;

@Repository
public interface PartNumberCategoryRepos extends JpaRepository<PartNumberCategory, Integer> {

	@Query("from PartNumberCategory where industry.id = ?1")
	public List<PartNumberCategory> findByIndustry(Integer industryId);

	@Query("select count(*) from PartNumberCategory where name = ?1 and industry.id = ?2")
	public Long countByNameAndIndustry(String name, Integer industryId);

	@Query("select count(*) from PartNumberCategory where name = ?1 and industry.id = ?2 and id != ?3")
	public Long countByNameAndIndustry(String name, Integer industryId, Integer id);

	@Query("select distinct name from PartNumberCategory where industry.id = ?1")
	List<String> findNameList(Integer industryId);

	@Query("from PartNumberCategory where industry.id = ?1 and name = ?2")
	PartNumberCategory findByName(Integer industryId, String name);
}
