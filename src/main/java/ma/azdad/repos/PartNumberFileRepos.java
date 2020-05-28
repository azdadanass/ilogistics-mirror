package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberFile;

@Repository
public interface PartNumberFileRepos extends JpaRepository<PartNumberFile, Integer> {

}

