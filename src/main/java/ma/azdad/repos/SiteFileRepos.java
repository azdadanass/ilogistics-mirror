package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SiteFile;

@Repository
public interface SiteFileRepos extends JpaRepository<SiteFile, Integer> {

}

