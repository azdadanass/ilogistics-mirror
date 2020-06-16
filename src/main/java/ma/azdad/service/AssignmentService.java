package ma.azdad.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Assignment;
import ma.azdad.repos.AssignmentDetailRepos;
import ma.azdad.repos.AssignmentRepos;

@Component
@Transactional
public class AssignmentService extends GenericService<Assignment> {

	@Autowired
	AssignmentRepos repos;

	@Autowired
	AssignmentDetailRepos assignmentDetailRepos;

	@Override
	public Assignment findOne(Integer id) {
		Assignment assignment = super.findOne(id);
		Hibernate.initialize(assignment.getDetailList());
		Hibernate.initialize(assignment.getAssignator());
		return assignment;
	}

	public List<Assignment> find(String username, Boolean assignator, Boolean active) {
		if (assignator)
			return active ? repos.findByAssignatorAndActive(username) : repos.findByAssignatorAndInactive(username);
		else
			return active ? repos.findByUserAndActive(username) : repos.findByUserAndInactive(username);
	}

}
