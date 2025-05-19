package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ProjectAssignment;
import ma.azdad.model.ProjectAssignmentType;
import ma.azdad.model.Supplier;
import ma.azdad.model.User;
import ma.azdad.repos.ProjectAssignmentRepos;

@Component
@Transactional
public class ProjectAssignmentService extends GenericService<Integer, ProjectAssignment, ProjectAssignmentRepos> {

	@Override
	public ProjectAssignment findOne(Integer id) {
		ProjectAssignment model = super.findOne(id);
		initialize(model.getProject());
		initialize(model.getSupplier());
		initialize(model.getCustomer());
		initialize(model.getTeam());
		initialize(model.getUser());
		initialize(model.getParent());
		return model;
	}

	public List<ProjectAssignment> find(List<Integer> projectAssignmentList, List<Integer> delegatedProjectList, ProjectAssignmentType type) {
		return repos.find(projectAssignmentList, delegatedProjectList, type);
	}

	public List<ProjectAssignment> findBySupplierAndProjectList(Integer supplierId, List<Integer> projectAssignmentList) {
		return repos.findBySupplierAndProjectList(supplierId, projectAssignmentList);
	}

	public List<ProjectAssignment> findByCustomerAndProjectList(Integer customerId, List<Integer> projectAssignmentList) {
		return repos.findByCustomerAndProjectList(customerId, projectAssignmentList);
	}

	public List<ProjectAssignment> findByUser(String userUsername) {
		return repos.findByUser(userUsername);
	}

	public List<ProjectAssignment> findInternalTeamsAssignement(List<Integer> projectAssignmentList, List<Integer> delegatedProjectList) {
		return repos.findInternalTeamsAssignement(projectAssignmentList, delegatedProjectList);
	}

	public List<ProjectAssignment> findByParentAndType(Integer parentId, ProjectAssignmentType type) {
		return repos.findByParentAndType(parentId, type);
	}

	public Boolean isOverlap(ProjectAssignment projectAssignment) {
		switch (projectAssignment.getType()) {
		case INTERNAL:
		case EXTERNAL_PM:
			return repos.countByUserAndOverlapsWidthDates(projectAssignment.getUserUsername(), projectAssignment.getProjectId(), projectAssignment.getId(), projectAssignment.getStartDate(),
					projectAssignment.getEndDate()) > 0;
		case INTERNAL_TEAM:
		case EXTERNAL_TEAM:
			return repos.countByTeamAndOverlapsWidthDates(projectAssignment.getTeamId(), projectAssignment.getProjectId(), projectAssignment.getId(), projectAssignment.getStartDate(),
					projectAssignment.getEndDate()) > 0;
		case SUPPLIER:
			return repos.countBySupplierAndOverlapsWidthDates(projectAssignment.getSupplierId(), projectAssignment.getProjectId(), projectAssignment.getId(), projectAssignment.getStartDate(),
					projectAssignment.getEndDate()) > 0;
		case CUSTOMER:
			return repos.countByCustomerAndOverlapsWidthDates(projectAssignment.getCustomerId(), projectAssignment.getProjectId(), projectAssignment.getId(), projectAssignment.getStartDate(),
					projectAssignment.getEndDate()) > 0;
		default:
			return null;
		}
	}

	public void updateParentIdScript() {
		for (ProjectAssignment pa : findAll()) {
			if (ProjectAssignmentType.EXTERNAL_PM.equals(pa.getType()) || (ProjectAssignmentType.EXTERNAL_TEAM.equals(pa.getType()) && pa.getTeam().getTeamLeader().getSupplier() != null)) {
				Supplier supplier = ProjectAssignmentType.EXTERNAL_PM.equals(pa.getType()) ? pa.getUser().getSupplier() : pa.getTeam().getTeamLeader().getSupplier();
				ProjectAssignment parent = repos.findByProjectAndSupplier(pa.getProjectId(), supplier.getId());
				if (parent == null) {
					parent = new ProjectAssignment();
					parent.setStartDate(pa.getStartDate());
					parent.setEndDate(pa.getEndDate());
					parent.setType(ProjectAssignmentType.SUPPLIER);
					parent.setSupplier(supplier);
					parent.setProject(pa.getProject());
					parent = repos.save(parent);
				}
				pa.setParent(parent);
				repos.save(pa);
			}
		}

	}

	public Long countActiveByProjectAndUser(Integer projectId, String username) {
		return ObjectUtils.firstNonNull(repos.countActiveByProjectAndUser(projectId, username), 0l);
	}

	public Boolean isUserHavingActiveAssignmentInProject(Integer projectId, String username) {
		return countActiveByProjectAndUser(projectId, username) > 0;
	}
	
	public List<User> findCompanyUserListAssignedToProject(Integer projectId){
		return repos.findCompanyUserListAssignedToProject(projectId);
	}
	
	public List<User> findUserListAssignedToProject(Integer projectId){
		return repos.findUserListAssignedToProject(projectId);
	}
	

}
