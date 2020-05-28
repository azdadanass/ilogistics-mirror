package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.repos.DelegationRepos;

@Component
@Transactional
public class DelegationService {

	@Autowired
	DelegationRepos delegationRepos;

	public List<Integer> findDelegatedProjects(String username) {
		List<Integer> result = new ArrayList<Integer>();
		result.add(0);
		result.addAll(delegationRepos.findDelegatedProjects(username, "Active", "PM"));
		return result;
	}
}