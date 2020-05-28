package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransporterHistory;


@Repository
public interface TransporterHistoryRepos extends JpaRepository<TransporterHistory, Integer> {

	
}

