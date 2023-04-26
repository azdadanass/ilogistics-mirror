package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.JobRequest;

@Repository
public interface JobRequestRepos extends JpaRepository<JobRequest, Integer> {

}

