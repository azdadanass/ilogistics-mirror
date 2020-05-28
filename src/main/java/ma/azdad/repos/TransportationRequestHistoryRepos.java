package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransportationRequestHistory;


@Repository
public interface TransportationRequestHistoryRepos extends JpaRepository<TransportationRequestHistory, Integer> {

	
}

