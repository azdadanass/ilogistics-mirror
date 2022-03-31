package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.IssueType;

@Repository
public interface IssueTypeRepos extends JpaRepository<IssueType, Integer> {
	
	@Query("from IssueType where category.id = ?1")
	List<IssueType> findByCategory(Integer categoryId);

}

