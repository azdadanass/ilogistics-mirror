package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BaseHistory;


@Repository
public interface BaseHistoryRepos extends JpaRepository<BaseHistory, Integer> {

	
}

