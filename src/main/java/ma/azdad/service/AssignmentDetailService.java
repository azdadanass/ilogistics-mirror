package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.AssignmentDetail;
import ma.azdad.repos.AssignmentDetailRepos;

@Component
@Transactional
public class AssignmentDetailService extends GenericService<AssignmentDetail> {

	@Autowired
	AssignmentDetailRepos assignmentDetailRepos;

	public List<AssignmentDetail> find(String username, Boolean assignator, Boolean active) {
		if (assignator)
			return active ? assignmentDetailRepos.findByAssignatorAndActive(username) : assignmentDetailRepos.findByAssignatorAndInactive(username);
		else
			return active ? assignmentDetailRepos.findByUserAndActive(username) : assignmentDetailRepos.findByUserAndInactive(username);
	}

}
