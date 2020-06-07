package ma.azdad.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Assignment;
import ma.azdad.model.Role;
import ma.azdad.model.User;
import ma.azdad.repos.AssignmentDetailRepos;
import ma.azdad.repos.AssignmentRepos;

@Component
@Transactional
public class AssignmentService extends GenericService<Assignment> {

	@Autowired
	AssignmentRepos assignmentRepos;

	@Autowired
	AssignmentDetailRepos assignmentDetailRepos;

	@Override
	public Assignment findOne(Integer id) {
		Assignment assignment = super.findOne(id);
		Hibernate.initialize(assignment.getDetailList());
		Hibernate.initialize(assignment.getAssignator());
		return assignment;
	}

	public List<Assignment> findByAssignator(String username) {
		return assignmentRepos.findByAssignator(username);
	}

	public List<User> findByProjectAssignmentAndUserRoleAndInternal(Integer projectId) {
		return assignmentDetailRepos.findByProjectAssignmentAndUserRole(projectId, Role.ROLE_ILOGISTICS_USER, true);
	}

}
