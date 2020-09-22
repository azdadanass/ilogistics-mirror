package ma.azdad.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.model.ProjectStatus;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.User;
import ma.azdad.repos.ProjectRepos;

@Component
@Transactional
public class ProjectService {

	@Autowired
	ProjectRepos repos;

	public Project findOne(Integer id) {
		Project p = repos.findById(id).get();
		Hibernate.initialize(p.getManager());
		Hibernate.initialize(p.getCustomer());
		Hibernate.initialize(p.getCostcenter());
		Hibernate.initialize(p.getCostcenter().getLob());
		Hibernate.initialize(p.getCostcenter().getLob().getManager());
		return p;
	}

	public Project findOne2(Integer id) {
		Project p = repos.findById(id).get();
		Hibernate.initialize(p.getManager());
		Hibernate.initialize(p.getCustomer());
		Hibernate.initialize(p.getCostcenter());
		Hibernate.initialize(p.getCostcenter().getLob());
		Hibernate.initialize(p.getCostcenter().getLob().getManager());
		p.getManagerList().forEach(i -> Hibernate.initialize(i.getUser()));
		return p;
	}

//	public Project findOne2(Integer id) {
//		Project p = repos.findOne(id);
//		Hibernate.initialize(p.getManager());
//		Hibernate.initialize(p.getCustomer());
//		Hibernate.initialize(p.getCostcenter());
//		Hibernate.initialize(p.getCostcenter().getLob());
//		Hibernate.initialize(p.getPartNumberThresholdList());
//		return p;
//	}

	public List<Project> findLightByManager(String managerUsername) {
		return repos.findLightByManager(managerUsername);
	}

	public List<Project> findLight() {
		return repos.findLight();
	}

	public List<Project> findLight(Collection<Integer> idList) {
		return repos.findLight(idList);
	}

	public List<Project> findLight(String status) {
		return repos.findLight(status);
	}

	public List<Project> findLightByManagerAndStatus(String managerUsername, String status) {
		return repos.findLightByManagerAndStatus(managerUsername, status);
	}

	public List<Project> findLightByManagerAndOpen(String managerUsername) {
		return findLightByManagerAndStatus(managerUsername, ProjectStatus.OPEN.getValue());
	}

	// public List<Project> findLightByResource(String username) {
	// return repos.findLightByResource(username);
	// }

	public List<Project> findLightByResource(String username) {
		return repos.findByResourceAndStatus(username, "Open");
	}

	public List<Project> findInboundProjectList(String username) {
		return repos.findByResourceAndStatus(username, "Open");
	}

	public List<Project> findXboundProjectList(String username) {
		return repos.findByResourceAndStatusAndNotType(username, "Open", ProjectTypes.STOCK.getValue());
	}

	public List<Project> findOutboundProjectList(String username) {
		return repos.findByResourceAndInProjectList(username, findNonEmptyProjectList());
	}

	public Set<Integer> findNonEmptyProjectList() {
		Set<Integer> result = new HashSet<>();
		result.add(-1);
		result.addAll(repos.findNonEmptyProjectList());
		return result;
	}

	public List<Integer> findAssignedProjectIdListByResource(String username) {
		List<Integer> result = new ArrayList<>();
		result.add(-1);
		result.addAll(repos.findAssignedProjectIdListByResource(username));
		return result;
	}

	public String getType(Integer projectId) {
		return repos.getType(projectId);
	}

	public String getSubType(Integer projectId) {
		return repos.getSubType(projectId);
	}

	@Cacheable("projectService.getManagerFullNameMap")
	public Map<Integer, Project> getManagerFullNameMap() {
		return repos.find().stream().collect(Collectors.toMap(i -> i.getId(), i -> i));
	}

	public Integer getCustomerId(Integer customerId) {
		return repos.getCustomerId(customerId);
	}

	public Boolean isStockProject(Integer projectId) {
		return ProjectTypes.STOCK.getValue().equals(getType(projectId));
	}

	public Boolean isDeliveryProject(Integer projectId) {
		return ProjectTypes.DELIVERY.getValue().equals(getType(projectId));
	}

	public Boolean isDstrProject(Integer projectId) {
		return "DSTR".equals(getSubType(projectId));
	}

	public Project save(Project project) {
		return repos.save(project);
	}

	public Boolean isHardwareManager(Integer projectId, String userUsername) {
		return repos.countByManagerType(projectId, userUsername, ProjectManagerType.HARDWARE_MANAGER) > 0;
	}

	@Cacheable("projectService.findFirstManagerByType")
	public User findFirstManagerByType(Integer projectId, ProjectManagerType managerType) {
		if (projectId == null)
			return null;
		return repos.findById(projectId).get().getManagerList().stream().filter(i -> managerType.equals(i.getType())).map(i -> i.getUser()).findFirst().orElse(null);
	}

	public List<Integer> findIdListByManagerType(String username, ProjectManagerType type) {
		return repos.findIdListByManagerType(username, type);
	}
}
