package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberType;

@Repository
public interface PartNumberTypeRepos extends JpaRepository<PartNumberType, Integer> {

	@Query("from PartNumberType where category.id = ?1")
	public List<PartNumberType> findByCategory(Integer categoryId);

	@Query("select count(*) from PartNumberType where name = ?1 and category.id = ?2")
	public Long countByNameAndCategory(String name, Integer categoryId);

	@Query("select count(*) from PartNumberType where name = ?1 and category.id = ?2 and id != ?3")
	public Long countByNameAndCategory(String name, Integer categoryId, Integer id);

	@Query("select distinct name from PartNumberType where category.id = ?1")
	List<String> findNameList(Integer categoryId);

	@Query("from PartNumberType where category.id = ?1 and name = ?2")
	PartNumberType findByName(Integer categoryId, String name);

}
