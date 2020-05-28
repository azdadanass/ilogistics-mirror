package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Packing;

@Repository
public interface PackingRepos extends JpaRepository<Packing, Integer> {

	@Query("from Packing where partNumber.id = ?1")
	public List<Packing> findByPartNumber(Integer partNumberId);

	@Query("from Packing where partNumber.id = ?1 and active = true")
	public List<Packing> findByPartNumberAndActive(Integer partNumberId);

	@Query("select count(*) from Packing where partNumber.id = ?1")
	public Long countByPartNumber(Integer partNumberId);

	@Query("select count(*) from Packing where partNumber.id = ?1 and name = ?2 ")
	public Long countByPartNumberAndName(Integer partNumberId, String name);

	@Query("select count(*) from Packing where partNumber.id = ?1 and name = ?2 and id != ?3")
	public Long countByPartNumberAndName(Integer partNumberId, String name, Integer id);

	@Modifying
	@Query("update Packing set active = ?2 where id = ?1")
	public void updateActive(Integer id, Boolean active);

	@Modifying
	@Query("update Packing set name = ?2 where id = ?1")
	public void updateName(Integer id, String name);

}
