package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.IssueCategory;
import ma.azdad.utils.App;

@Repository
public interface IssueCategoryRepos extends JpaRepository<IssueCategory, Integer> {
	
	@Query("from IssueCategory where app = ?1")
	List<IssueCategory> findByApp(App app);

}

