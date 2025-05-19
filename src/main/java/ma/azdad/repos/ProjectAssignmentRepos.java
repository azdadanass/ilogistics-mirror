package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ProjectAssignment;
import ma.azdad.model.ProjectAssignmentType;
import ma.azdad.model.User;

@Repository
public interface ProjectAssignmentRepos extends JpaRepository<ProjectAssignment, Integer> {

	String c1 = "select new ProjectAssignment(a.id,a.type,a.startDate,a.endDate,a.project.name," //
			+ "(select b.username from User b where a.user.username = b.username),"//
			+ "(select b.fullName from User b where a.user.username = b.username),"//
			+ "(select b.active from User b where a.user.username = b.username),"//
			+ "(select b.name from Team b where a.team.id = b.id)"//
			+ ",(select b.type from Team b where a.team.id = b.id)"//
			+ ",(select b.active from Team b where a.team.id = b.id)"//
			+ ",(select b.name from Supplier b where a.supplier.id = b.id)"//
			+ ",(select b.photo from Supplier b where a.supplier.id = b.id)"//
			+ ",(select b.name from Customer b where a.customer.id = b.id)"//
			+ ",(select b.photo from Customer b where a.customer.id = b.id)"//
			+ ",(select b.teamLeader.username from Team b where a.team.id = b.id)) ";

	@Query(c1 + " from ProjectAssignment a where (a.project.id in (?1) or a.project.id in (?2)) and a.type = ?3")
	List<ProjectAssignment> find(List<Integer> projectAssignmentList, List<Integer> delegatedProjectList, ProjectAssignmentType type);

	@Query(c1 + " from ProjectAssignment a where a.supplier.id = ?1 and a.project.id in (?2)")
	List<ProjectAssignment> findBySupplierAndProjectList(Integer supplierId, List<Integer> projectAssignmentList);

	@Query(c1 + " from ProjectAssignment a where a.customer.id = ?1 and a.project.id in (?2)")
	List<ProjectAssignment> findByCustomerAndProjectList(Integer customerId, List<Integer> projectAssignmentList);

	@Query(c1 + " from ProjectAssignment a where a.user.username = ?1")
	List<ProjectAssignment> findByUser(String userUsername);

	@Query(c1 + " from ProjectAssignment a where (a.project.id in (?1) or a.project.id in (?2)) and a.team.teamLeader.internal is true")
	List<ProjectAssignment> findInternalTeamsAssignement(List<Integer> projectAssignmentList, List<Integer> delegatedProjectList);

	@Query(c1 + "from ProjectAssignment a where a.parent.id = ?1 and a.type = ?2")
	List<ProjectAssignment> findByParentAndType(Integer parentId, ProjectAssignmentType type);

	@Query("select count(distinct a.team.id) from ProjectAssignment a where a.project.id = ?1 and startDate < ?3 and endDate >= ?2") // the < is necessary
	Long countTeamsByProjectAndOverlapsWidthDates(Integer projectId, Date minDate, Date maxDate);

	@Query("select count(*) from ProjectAssignment a where a.user.username = ?1 and a.project.id = ?2 and (?3 is null or a.id != ?3) and startDate < ?5 and endDate >= ?4")
	Long countByUserAndOverlapsWidthDates(String userUsername, Integer projectId, Integer id, Date minDate, Date maxDate);

	@Query("select count(*) from ProjectAssignment a where a.team.id = ?1 and a.project.id = ?2 and (?3 is null or a.id != ?3) and startDate < ?5 and endDate >= ?4")
	Long countByTeamAndOverlapsWidthDates(Integer teamId, Integer projectId, Integer id, Date minDate, Date maxDate);

	@Query("select count(*) from ProjectAssignment a where a.supplier.id = ?1 and a.project.id = ?2 and (?3 is null or a.id != ?3) and startDate < ?5 and endDate >= ?4")
	Long countBySupplierAndOverlapsWidthDates(Integer supplierId, Integer projectId, Integer id, Date minDate, Date maxDate);

	@Query("select count(*) from ProjectAssignment a where a.customer.id = ?1 and a.project.id = ?2 and (?3 is null or a.id != ?3) and startDate < ?5 and endDate >= ?4")
	Long countByCustomerAndOverlapsWidthDates(Integer customerId, Integer projectId, Integer id, Date minDate, Date maxDate);

	@Query("from ProjectAssignment a where a.project.id = ?1 and a.supplier.id = ?2")
	ProjectAssignment findByProjectAndSupplier(Integer projectId, Integer supplierId);

	@Query("select count(*) from ProjectAssignment a where a.project.id = ?1 and a.user.username = ?2 and current_date between a.startDate and a.endDate")
	Long countActiveByProjectAndUser(Integer projectId, String username);
	
	@Query("select distinct a.customer.id from ProjectAssignment a where a.project.id = ?1 and current_date between a.startDate and a.endDate")
	List<Integer> findCustomerAssignedToProject(Integer projectId);
	
	@Query("select distinct a.user from ProjectAssignment a where a.project.id = ?1 and a.user.company.id = a.project.costcenter.lob.bu.company.id and a.user.active is true and current_date between a.startDate and a.endDate ")
	List<User> findCompanyUserListAssignedToProject(Integer projectId);
	
	@Query("select distinct a.user from ProjectAssignment a where a.project.id = ?1 and a.user.active is true and current_date between a.startDate and a.endDate ")
	List<User> findUserListAssignedToProject(Integer projectId);

}
