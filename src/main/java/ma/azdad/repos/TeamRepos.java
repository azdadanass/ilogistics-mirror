package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Team;

@Repository
public interface TeamRepos extends JpaRepository<Team, Integer> {

	String teamSize = "(select count(*) from TeamDetail b where b.team.id = a.id)";
	String activeAssignements = "(select count(*) from ProjectAssignment b where b.team.id = a.id and current_date between b.startDate and b.endDate)";

	String select1 = " select new Team(a.id,a.name,a.category,a.type,a.active," + teamSize + "," + activeAssignements + ",a.teamLeader.fullName,a.teamLeader.photo) ";

	@Query(select1 + "from Team a")
	public List<Team> find();

	@Query(select1 + "from Team a where a.user.username = ?1")
	public List<Team> find(String username);

	@Query(select1 + "from Team a where a.user.username = ?1 and a.active = ?2")
	public List<Team> find(String username, Boolean active);

	@Query(select1 + "from Team a where a.teamLeader.internal = ?1 and a.active = ?2")
	public List<Team> find(Boolean internal, Boolean active);

	@Query(select1 + "from Team a where a.teamLeader.supplier.id = ?1 and a.active is true")
	public List<Team> findActiveBySupplier(Integer supplierId);

	@Query(select1 + "from Team a join a.assignmentList b where b.project.id in (?1) group by a.id")
	public List<Team> findByProjectList(List<Integer> projectIdList);

	@Query(select1 + "from Team a join a.assignmentList b where b.project.id = ?1 and current_date between b.startDate and b.endDate and a.active = true group by a.id")
	public List<Team> findByProjectAndActive(Integer projectId);

	@Query(select1 + "from Team a join a.assignmentList b where b.project.id = ?1 and current_date between b.startDate and b.endDate and a.active = true and a.teamLeader.supplier.id = ?2 group by a.id")
	public List<Team> findByProjectAndSupplierAndActive(Integer projectId, Integer supplierId);

	@Query("select  a.id from Team a where a.teamLeader.username = ?1")
	List<Integer> findIdListByTeamLeader(String username);

	@Query(select1 + "from Team a where a.teamLeader.supplier.id = ?1")
	public List<Team> findByTeamLeaderSupplier(Integer supplierId);

	@Query(select1 + "from Team a where a.teamLeader.supplier.id = ?1 and a.user.username = ?2")
	public List<Team> findByTeamLeaderSupplierAndUser(Integer supplierId, String username);

	@Query(select1 + "from Team a join a.assignmentList b where a.teamLeader.supplier.id = ?1 and b.project.id in (?2) group by a.id")
	public List<Team> findByTeamLeaderSupplierAndProjectList(Integer supplierId, List<Integer> projectIdList);

	@Query("select count(*) from Team where membersKey = ?1 and (?2 is null or id != ?2)")
	Long countByMembersKey(String membersKey, Integer id);
}
