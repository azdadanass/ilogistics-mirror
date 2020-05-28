package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberHistory;


@Repository
public interface PartNumberHistoryRepos extends JpaRepository<PartNumberHistory, Integer> {

	
}

