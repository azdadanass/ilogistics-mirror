package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestFile;

@Repository
public interface DeliveryRequestFileRepos extends JpaRepository<DeliveryRequestFile, Integer> {

}

