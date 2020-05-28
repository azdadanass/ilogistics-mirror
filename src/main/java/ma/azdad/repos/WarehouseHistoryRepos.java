package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.WarehouseHistory;


@Repository
public interface WarehouseHistoryRepos extends JpaRepository<WarehouseHistory, Integer> {

	
}

