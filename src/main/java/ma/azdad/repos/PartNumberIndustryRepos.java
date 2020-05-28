package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberIndustry;

@Repository
public interface PartNumberIndustryRepos extends JpaRepository<PartNumberIndustry, Integer> {

	public Long countByName(String name);

	@Query("select count(*) from PartNumberIndustry where name = ?1 and id != ?2")
	public Long countByName(String name, Integer id);

	@Query("select distinct name from PartNumberIndustry")
	List<String> findNameList();

	PartNumberIndustry findByName(String name);

}
