package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TeamDetail;
import ma.azdad.model.TeamDetailType;
import ma.azdad.repos.TeamDetailRepos;

@Component
@Transactional
public class TeamDetailService extends GenericServiceOld<TeamDetail> {

	@Autowired
	TeamDetailRepos teamDetailRepos;

	@Override
	public TeamDetail findOne(Integer id) {
		TeamDetail teamDetail = super.findOne(id);
		// Hibernate.initialize(teamDetail.get..);
		return teamDetail;
	}

	public String findFullNameByActiveTeam(TeamDetail teamDetail, Integer teamId) {
		if (teamId == null)
			return teamDetailRepos.findFullNameByActiveTeam(TeamDetailType.TEAM_LEADER, teamDetail.getUserUsername());
		else
			return teamDetailRepos.findFullNameByActiveTeam(TeamDetailType.TEAM_LEADER, teamDetail.getUserUsername(), teamId);
	}

}
