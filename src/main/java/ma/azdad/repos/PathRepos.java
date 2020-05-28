package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Path;

@Repository
public interface PathRepos extends JpaRepository<Path, Integer> {

}
