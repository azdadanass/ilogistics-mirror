package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BaseFile;

@Repository
public interface BaseFileRepos extends JpaRepository<BaseFile, Integer> {

}



