package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.InternalPartNumber;

@Repository
public interface InternalPartNumberRepos extends JpaRepository<InternalPartNumber, Integer> {

}
