package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ZoneHeight;

@Repository
public interface ZoneHeightRepos extends JpaRepository<ZoneHeight, Integer> {

	@Query("from ZoneHeight a where a.column.line.location.id = ?1")
	List<ZoneHeight> findByLocation(Integer locationId);

}
