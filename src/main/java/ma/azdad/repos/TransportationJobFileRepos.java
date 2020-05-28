package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransportationJobFile;

@Repository
public interface TransportationJobFileRepos extends JpaRepository<TransportationJobFile, Integer> {

}
