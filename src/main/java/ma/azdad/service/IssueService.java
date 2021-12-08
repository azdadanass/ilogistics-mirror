package ma.azdad.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Issue;
import ma.azdad.model.IssueComment;
import ma.azdad.model.IssueStatus;
import ma.azdad.repos.IssueRepos;

@Component
public class IssueService extends GenericService<Integer, Issue, IssueRepos> {

	@Autowired
	ProjectService projectService;

	@Override
	@Cacheable("issueService.findAll")
	public List<Issue> findAll() {
		return repos.findAll();
	}

	@Cacheable("issueService.findByProject")
	public List<Issue> findByProject(Integer projectId) {
		return repos.findByProject(projectId);
	}

	@Cacheable("issueService.findToConfirm")
	public List<Issue> findToConfirm(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return repos.findToConfirm(username, projectList, IssueStatus.RAISED);
	}

	@Cacheable("issueService.countToConfirm")
	public Long countToConfirm(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return repos.countToConfirm(username, projectList, IssueStatus.RAISED);
	}

	@Cacheable("issueService.findToAssign")
	public List<Issue> findToAssign(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return repos.findToAssign(username, projectList, IssueStatus.CONFIRMED);
	}

	@Cacheable("issueService.countToAssign")
	public Long countToAssign(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return repos.countToAssign(username, projectList, IssueStatus.CONFIRMED);
	}

	@Cacheable("issueService.findToResolve")
	public List<Issue> findToResolve(String username) {
		return repos.findToResolve(username, IssueStatus.ASSIGNED);
	}

	@Cacheable("issueService.countToResolve")
	public Long countToResolve(String username) {
		return repos.countToResolve(username, IssueStatus.ASSIGNED);
	}

	@Cacheable("issueService.findToAcknowledge")
	public List<Issue> findToAcknowledge(String username) {
		return repos.findToAcknowledge(username, IssueStatus.RESOLVED);
	}

	@Cacheable("issueService.countToAcknowledge")
	public Long countToAcknowledge(String username) {
		return repos.countToAcknowledge(username, IssueStatus.RESOLVED);
	}

	@Override
	@Cacheable("issueService.findOne")
	public Issue findOne(Integer id) {
		Issue issue = super.findOne(id);
		initialize(issue.getFileList());
		initialize(issue.getHistoryList());
		Hibernate.initialize(issue.getDeliveryRequest().getProject());
		Hibernate.initialize(issue.getUser1());
		Hibernate.initialize(issue.getCompany());
		Hibernate.initialize(issue.getCustomer());
		Hibernate.initialize(issue.getSupplier());
		Hibernate.initialize(issue.getOwnershipUser());
		for (IssueComment comment : issue.getCommentList())
			Hibernate.initialize(comment.getUser());
		return issue;
	}

}
