package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.IssueCategory;
import ma.azdad.model.IssueParentType;

@Repository
public interface IssueCategoryRepos extends JpaRepository<IssueCategory, Integer> {

	@Query("from IssueCategory where project.id = ?1 and parentType = ?2")
	List<IssueCategory> findByProjectAndParenType(Integer projectId, IssueParentType parentType);

	@Query("from IssueCategory where project.id = ?1 and parentType in (?2)")
	List<IssueCategory> findByProjectAndParenType(Integer projectId, List<IssueParentType> parentTypeList);
	
	@Query("select count(*) from IssueCategory where project.id = ?1 and parentType = ?2")
	Long countByProject(Integer projectId,IssueParentType parentType);

}
