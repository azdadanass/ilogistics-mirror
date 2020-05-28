package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SiteHistory;


@Repository
public interface SiteHistoryRepos extends JpaRepository<SiteHistory, Integer> {

	
}

