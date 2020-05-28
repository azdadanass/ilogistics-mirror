package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestHistory;


@Repository
public interface DeliveryRequestHistoryRepos extends JpaRepository<DeliveryRequestHistory, Integer> {

	
}

