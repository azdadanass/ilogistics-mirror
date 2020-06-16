package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.AssignmentDetail;

@Repository
public interface AssignmentDetailRepos extends JpaRepository<AssignmentDetail, Integer> {

	String constructor1 = "select new AssignmentDetail(id,a.assignment.startDate,a.assignment.endDate,a.assignment.assignator.photo,a.assignment.assignator.fullName,a.assignment.user.photo,a.assignment.user.fullName,a.project.name)";

	@Query(constructor1 + "from AssignmentDetail a where a.assignment.assignator.username = ?1 and current_date between a.assignment.startDate and a.assignment.endDate")
	List<AssignmentDetail> findByAssignatorAndActive(String username);

	@Query(constructor1 + "from AssignmentDetail a where a.assignment.assignator.username = ?1 and current_date not between a.assignment.startDate and a.assignment.endDate")
	List<AssignmentDetail> findByAssignatorAndInactive(String username);

	@Query(constructor1 + "from AssignmentDetail a where a.assignment.user.username = ?1 and current_date between a.assignment.startDate and a.assignment.endDate")
	List<AssignmentDetail> findByUserAndActive(String username);

	@Query(constructor1 + "from AssignmentDetail a where a.assignment.user.username = ?1 and current_date not between a.assignment.startDate and a.assignment.endDate")
	List<AssignmentDetail> findByUserAndInactive(String username);

}
