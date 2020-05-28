package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.VehicleHistory;


@Repository
public interface VehicleHistoryRepos extends JpaRepository<VehicleHistory, Integer> {

	
}

