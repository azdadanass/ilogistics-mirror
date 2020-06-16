package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Assignment;

@Repository
public interface AssignmentRepos extends JpaRepository<Assignment, Integer> {

	String c1 = "select new Assignment(id,startDate,endDate,a.assignator.photo,assignator.fullName,a.user.photo, a.user.fullName) ";

	@Query(c1 + "from Assignment a where a.assignator.username = ?1 and current_date between startDate and endDate")
	List<Assignment> findByAssignatorAndActive(String username);

	@Query(c1 + "from Assignment a where a.assignator.username = ?1 and current_date not between startDate and endDate")
	List<Assignment> findByAssignatorAndInactive(String username);

	@Query(c1 + "from Assignment a where a.user.username = ?1 and current_date between startDate and endDate")
	List<Assignment> findByUserAndActive(String username);

	@Query(c1 + "from Assignment a where a.user.username = ?1 and current_date not between startDate and endDate")
	List<Assignment> findByUserAndInactive(String username);
}
