package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PackingDetailType;
import ma.azdad.model.PartNumberClass;

@Repository
public interface PackingDetailTypeRepos extends JpaRepository<PackingDetailType, Integer> {

	@Query("select count(*) from PackingDetailType where name = ?1 and partNumberClass = ?2")
	Long countByNameAndClass(String name,PartNumberClass partNumberClass);

	@Query("select name from PackingDetailType where partNumberClass = ?1 and active is true order by partNumberClass")
	List<String> findNameListByClassAndActive(PartNumberClass partNumberClass);

}
