package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransporterFile;

@Repository
public interface TransporterFileRepos extends JpaRepository<TransporterFile, Integer> {

}

