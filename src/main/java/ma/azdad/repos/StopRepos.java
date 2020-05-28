package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Stop;

@Repository
public interface StopRepos extends JpaRepository<Stop, Integer> {

}
