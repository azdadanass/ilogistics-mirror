package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Location;

@Repository
public interface LocationRepos extends JpaRepository<Location, Integer> {

	@Query("select count(*) from Location where warehouse.id = ?1 and name = ?2")
	public Long countByWarehouseAndName(Integer warehouseId, String name);

	@Query("select count(*) from Location where warehouse.id = ?1 and name = ?2 and id != ?3")
	public Long countByWarehouseAndName(Integer warehouseId, String name, Integer id);
}
