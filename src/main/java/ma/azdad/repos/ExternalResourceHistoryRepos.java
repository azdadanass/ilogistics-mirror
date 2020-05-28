package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ExternalResourceHistory;


@Repository
public interface ExternalResourceHistoryRepos extends JpaRepository<ExternalResourceHistory, Integer> {

	
}

