package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TeamDetail;
import ma.azdad.model.TeamDetailType;

@Repository
public interface TeamDetailRepos extends JpaRepository<TeamDetail, Integer> {

	@Query("select a.user.fullName from TeamDetail a where a.type = ?1 and a.user.username = ?2 and a.team.active = true group by a.user.fullName")
	public String findFullNameByActiveTeam(TeamDetailType teamLeaderType, String userUsername);

	@Query("select a.user.fullName from TeamDetail a where a.type = ?1 and a.user.username = ?2 and a.team.active = true and a.team.id != ?3 group by a.user.fullName")
	public String findFullNameByActiveTeam(TeamDetailType teamLeaderType, String userUsername, Integer teammId);

}
