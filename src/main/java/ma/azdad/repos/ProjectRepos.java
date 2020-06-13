
package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectManagerType;

@Repository
public interface ProjectRepos extends JpaRepository<Project, Integer> {
	String select1 = "select new Project(id,name,type,startDate,endDate,customer.id) ";
	String select2 = "select new Project(id,name,type,subType,startDate,endDate,customer.name,customerWarehousing,customerStockManagement,sdm) ";

	@Query("select new Project(id,name,type) from Project ")
	public List<Project> findLight();

	@Query("select new Project(id,name) from Project where id in (?1)")
	public List<Project> findLight(List<Integer> idList);

	@Query(select1 + "from Project where status = ?1")
	public List<Project> findLight(String status);

	@Query(select2 + "from Project where manager.username = ?1 order by customerWarehousing desc")
	public List<Project> findLightByManager(String managerUsername);

	@Query(select1 + "from Project where manager.username = ?1 and status = ?2")
	public List<Project> findLightByManagerAndStatus(String managerUsername, String status);

	@Query("select new Project(a.project.id,a.project.name,a.project.type) from AssignmentDetail a where a.assignment.id = ?1")
	public List<Project> findLightByAssignment(Integer assignmentId);

	// @Query("select new Project(id,name,type) from Project where status =
	// 'Open' and (manager.username = ?1 or id in (select b.project.id from
	// AssignmentDetail b where b.assignment.delegate.username = ?1 and
	// current_date between b.assignment.startDate and b.assignment.endDate))")
	// public List<Project> findLightByResource(String username);

	String cond1 = " (manager.username = ?1 or id in (select b.project.id from AssignmentDetail b where b.assignment.user.username = ?1 and current_date between b.assignment.startDate and  b.assignment.endDate)) ";

	@Query(select1 + " from Project where " + cond1 + " and status = ?2")
	public List<Project> findByResourceAndStatus(String username, String status);

	@Query(select1 + " from Project where " + cond1 + " and status = ?2 and type != ?3")
	public List<Project> findByResourceAndStatusAndNotType(String username, String status, String type);

	@Query(select1 + " from Project where " + cond1 + " and id in (?2) ")
	public List<Project> findByResourceAndInProjectList(String username, Set<Integer> projectList);

	@Query("select a.inboundDeliveryRequest.project.id from StockRow a where a.inboundDeliveryRequest is not null group by a.partNumber.id,a.inboundDeliveryRequest.project.id having sum(a.quantity) > 0 ")
	public List<Integer> findNonEmptyProjectList();

	@Query("select id from Project where status = 'Open' and (manager.username = ?1 or id in (select b.project.id from AssignmentDetail b where b.assignment.user.username = ?1 and current_date between b.assignment.startDate and  b.assignment.endDate))")
	public List<Integer> findAssignedProjectIdListByResource(String username);

	@Query("select type from Project where id = ?1")
	public String getType(Integer projectId);

	@Query("select subType from Project where id = ?1")
	public String getSubType(Integer projectId);

	@Query("SELECT new Project(id,name,type,manager.fullName) FROM Project ")
	public List<Project> find();

	@Query("select a.customer.id from Project a where a.id = ?1")
	public Integer getCustomerId(Integer customerId);

	@Query("select count(*) from ProjectManager a where a.project.id = ?1 and a.user.username = ?2 and a.type = ?3")
	Long countByManagerType(Integer projectId, String userUsername, ProjectManagerType type);

	@Query("select a.project.id from  ProjectManager a where a.user.username = ?1 and a.type = ?2")
	List<Integer> findIdListByManagerType(String username, ProjectManagerType type);

}
