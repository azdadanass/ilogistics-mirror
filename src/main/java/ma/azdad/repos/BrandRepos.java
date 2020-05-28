package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Brand;

@Repository
public interface BrandRepos extends JpaRepository<Brand, Integer> {

	public Long countByName(String name);

	@Query("select count(*) from Brand where name = ?1 and id != ?2")
	public Long countByName(String name, Integer id);

	@Query("select distinct name from Brand")
	List<String> findNameList();

	Brand findByName(String name);
}
