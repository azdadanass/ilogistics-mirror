package ma.azdad.service;

import java.util.ArrayList;
import java.util.Collection;
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
import ma.azdad.repos.ProjectRepos;

@Component
public class IssueService extends GenericService<Integer, Issue, IssueRepos> {

	@Autowired
	IssueRepos issueRepos;

	@Autowired
	ProjectRepos projectRepos;

	@Autowired
	ProjectService projectService;

	@Override
	public Issue findOne(Integer id) {
		Issue issue = super.findOne(id);

		initialize(issue.getDeliveryRequest().getProject().getCustomer());
		if (issue.getDeliveryRequest().getWarehouse() != null)
			issue.getDeliveryRequest().getWarehouse().getManagerList().forEach(i -> initialize(i.getUser()));
		initialize(issue.getAssignatorSupplier());
		initialize(issue.getAssignator());

		initialize(issue.getConfirmatorSupplier());
		initialize(issue.getConfirmator());

		Hibernate.initialize(issue.getFileList());
		Hibernate.initialize(issue.getHistoryList());
		Hibernate.initialize(issue.getUser1());
		Hibernate.initialize(issue.getCompany());
		Hibernate.initialize(issue.getCustomer());
		Hibernate.initialize(issue.getSupplier());
		Hibernate.initialize(issue.getOwnershipUser());
		for (IssueComment comment : issue.getCommentList())
			Hibernate.initialize(comment.getUser());
		issue.getToNotifyList().forEach(i -> {
			initialize(i.getInternalResource().getCompany());
			initialize(i.getInternalResource().getCustomer());
			initialize(i.getInternalResource().getSupplier());
		});
		return issue;
	}

	public List<Issue> findByProject(Integer projectId) {
		return issueRepos.findByProject(projectId);
	}

	public List<Issue> findByProject(List<Integer> projectIdList) {
		if (projectIdList.isEmpty())
			return new ArrayList<Issue>();
		return issueRepos.findByProject(projectIdList);
	}

	@Cacheable("issueService.findDeliveryRequestIssueListByUser")
	public List<Issue> findDeliveryRequestIssueListByUser(String username, Collection<Integer> projectIdList, Collection<Integer> lobIdList) {
		return repos.findDeliveryRequestIssueListByUser(username, projectIdList, lobIdList);
	}

	public List<Issue> findToConfirm(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return issueRepos.findToConfirm(username, projectList, IssueStatus.RAISED);
	}

	public Long countToConfirm(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return issueRepos.countToConfirm(username, projectList, IssueStatus.RAISED);
	}

	public List<Issue> findToAssign(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return issueRepos.findToAssign(username, projectList, IssueStatus.CONFIRMED);
	}

	public Long countToAssign(String username) {
		Set<Integer> projectList = new HashSet<Integer>();
		projectList.add(0);
		projectList.addAll(projectService.findIdListByDelegation(username));
		projectList.addAll(projectService.findIdListByQualityManager(username));
		return issueRepos.countToAssign(username, projectList, IssueStatus.CONFIRMED);
	}

	public List<Issue> findToResolve(String username) {
		return issueRepos.findToResolve(username, IssueStatus.ASSIGNED);
	}

	public Long countToResolve(String username) {
		return issueRepos.countToResolve(username, IssueStatus.ASSIGNED);
	}

	public List<Issue> findToAcknowledge(String username) {
		return issueRepos.findToAcknowledge(username, IssueStatus.RESOLVED);
	}

	public Long countToAcknowledge(String username) {
		return issueRepos.countToAcknowledge(username, IssueStatus.RESOLVED);
	}

}
