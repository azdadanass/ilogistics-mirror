package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Assignment;

@Repository
public interface AssignmentRepos extends JpaRepository<Assignment, Integer> {

	@Query("from Assignment a where a.assignator.username = ?1")
	public List<Assignment> findByAssignator(String username);
}
