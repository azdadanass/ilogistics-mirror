package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.JobRequestHistory;

@Repository
public interface JobRequestHistoryRepos extends JpaRepository<JobRequestHistory, Integer> {

}

