package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.repos.DelegationRepos;

@Component
@Transactional
public class DelegationService {

	@Autowired
	DelegationRepos repos;

	public List<Integer> findDelegatedProjects(String username) {
		List<Integer> result = new ArrayList<Integer>();
		result.add(0);
		result.addAll(repos.findDelegatedProjects(username, "Active", "PM"));
		return result;
	}
	
	public List<Integer> findDelegatedLobs(String username) {
		List<Integer> result = new ArrayList<Integer>();
		result.add(0);
		result.addAll(repos.findDelegatedLobs(username));
		return result;
	}
	
	public List<User> findDelegateUserListByProject(Integer projectId){
		return repos.findDelegateUserListByProject(projectId);
	}
}