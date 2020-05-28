package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransportationRequestFile;

@Repository
public interface TransportationRequestFileRepos extends JpaRepository<TransportationRequestFile, Integer> {

}

