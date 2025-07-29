package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Stop;

@Repository
public interface StopRepos extends JpaRepository<Stop, Integer> {
	
	
	@Query("from Stop where transportationJob.id in (?1)")
	List<Stop> findByTransportationJobList(List<Integer> transportationJobIdList);

}
