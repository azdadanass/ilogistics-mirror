package ma.azdad.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ProjectAssignment;
import ma.azdad.model.Team;
import ma.azdad.model.TeamDetail;
import ma.azdad.repos.TeamRepos;

@Component

public class TeamService extends GenericServiceOld<Team> {

	@Autowired
	TeamRepos teamRepos;

	@Override
	@Transactional
	public Team findOne(Integer id) {
		Team team = super.findOne(id);
		Hibernate.initialize(team.getFileList());
		Hibernate.initialize(team.getHistoryList());
		Hibernate.initialize(team.getDetailList());
		Hibernate.initialize(team.getAssignmentList());
		for (TeamDetail detail : team.getDetailList())
			Hibernate.initialize(detail.getUser());
		for (ProjectAssignment assignment : team.getAssignmentList())
			Hibernate.initialize(assignment.getProject());
		team.init();
		return team;
	}

	public List<Team> findActiveByUser(String username) {
		return teamRepos.find(username, true);
	}

	public List<Team> findActive(Boolean internal) {
		return teamRepos.find(internal, true);
	}

	public List<Team> find(Integer state, String username, List<Integer> projectIdList) {
		switch (state) {
		case 1:
			return teamRepos.find(username);
		case 2:
			return teamRepos.findByProjectList(projectIdList);
		case 3:
			return teamRepos.find();
		default:
			return null;
		}
	}

	public List<Team> findByTeamLeaderSupplier(Integer supplierId, Integer state, String username, List<Integer> projectIdList) {
		switch (state) {
		case 1:
			return teamRepos.findByTeamLeaderSupplierAndUser(supplierId, username);
		case 2:
			return teamRepos.findByTeamLeaderSupplierAndProjectList(supplierId, projectIdList);
		case 3:
			return teamRepos.findByTeamLeaderSupplier(supplierId);
		default:
			return null;
		}
	}

	public List<Team> findByProjectList(List<Integer> projectIdList) {
		if (projectIdList == null || projectIdList.isEmpty())
			return null;
		return teamRepos.findByProjectList(projectIdList);
	}

	public List<Team> findByProjectAndActive(Integer projectId) {
		return teamRepos.findByProjectAndActive(projectId);
	}

	public List<Team> findByProjectAndSupplierAndActive(Integer projectId, Integer supplierId) {
		return teamRepos.findByProjectAndSupplierAndActive(projectId, supplierId);
	}

	public List<Team> findActiveBySupplier(Integer supplierId) {
		return teamRepos.findActiveBySupplier(supplierId);
	}

	public List<Integer> findIdListByTeamLeader(String username) {
		return teamRepos.findIdListByTeamLeader(username);
	}

	public void calculateKeyScript() {
		findAll().forEach(i -> {
			i.calculateMembersKey();
			save(i);
		});
	}

	public Long countByMembersKey(String membersKey, Integer id) {
		return teamRepos.countByMembersKey(membersKey, id);
	}
}
