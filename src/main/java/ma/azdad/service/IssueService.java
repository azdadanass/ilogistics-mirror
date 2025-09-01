package ma.azdad.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

		switch (issue.getParentType()) {
		case DN:
			initialize(issue.getDeliveryRequest().getProject().getCustomer());
			if (issue.getDeliveryRequest().getWarehouse() != null)
				issue.getDeliveryRequest().getWarehouse().getManagerList().forEach(i -> initialize(i.getUser()));
			initialize(issue.getDeliveryRequest().getProject().getManager());
			issue.getDeliveryRequest().getProject().getManagerList().forEach(i->initialize(i.getUser()));
			break;
		case TR:
			initialize(issue.getTransportationRequest().getDeliveryRequest().getProject().getCustomer());
			if (issue.getTransportationRequest().getDeliveryRequest().getWarehouse() != null)
				issue.getTransportationRequest().getDeliveryRequest().getWarehouse().getManagerList().forEach(i -> initialize(i.getUser()));
			initialize(issue.getTransportationRequest().getDeliveryRequest().getProject().getManager());
			issue.getTransportationRequest().getDeliveryRequest().getProject().getManagerList().forEach(i->initialize(i.getUser()));
			break;

		default:
			break;
		}
		
		
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
		return issueRepos.findToConfirm(username);
	}

	public Long countToConfirm(String username) {
		return issueRepos.countToConfirm(username);
	}

	public List<Issue> findToAssign(String username) {
		return issueRepos.findToAssign(username);
	}

	public Long countToAssign(String username) {
		return issueRepos.countToAssign(username);
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
