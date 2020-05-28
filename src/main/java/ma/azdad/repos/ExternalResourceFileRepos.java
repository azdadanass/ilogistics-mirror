package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ExternalResourceFile;

@Repository
public interface ExternalResourceFileRepos extends JpaRepository<ExternalResourceFile, Integer> {

}

