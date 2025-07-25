package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.IssueFile;

@Repository
public interface IssueFileRepos extends JpaRepository<IssueFile, Integer> {

}
