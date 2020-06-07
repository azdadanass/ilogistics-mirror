package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.AssignmentDetail;
import ma.azdad.model.Role;
import ma.azdad.model.User;

@Repository
public interface AssignmentDetailRepos extends JpaRepository<AssignmentDetail, Integer> {

	@Query("select distinct a.assignment.user from AssignmentDetail a where a.project.id =?1 and current_date between a.assignment.startDate and a.assignment.endDate and (select count(*) from UserRole b where b.user.username = a.assignment.user.username and  b.role = ?2) > 0 and a.assignment.user.internal = ?3 ")
	List<User> findByProjectAssignmentAndUserRole(Integer projectId, Role userRole, Boolean internal);

}
