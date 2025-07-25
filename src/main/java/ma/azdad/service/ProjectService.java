package ma.azdad.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectCustomerType;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.model.ProjectStatus;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.User;
import ma.azdad.repos.IssueRepos;
import ma.azdad.repos.ProjectRepos;

@Component
public class ProjectService extends GenericService<Integer, Project, ProjectRepos> {

	@Autowired
	IssueRepos issueRepos;

	public Project findOne(Integer id) {
		Project p = repos.findById(id).get();
		Hibernate.initialize(p.getManager());
		Hibernate.initialize(p.getCustomer());
		Hibernate.initialize(p.getCostcenter());
		Hibernate.initialize(p.getCostcenter().getLob());
		Hibernate.initialize(p.getCostcenter().getLob().getManager());
		Hibernate.initialize(p.getCurrency());
		Hibernate.initialize(p.getContract());
		Hibernate.initialize(p.getManagerList());
		Hibernate.initialize(p.getPreferredWarehouse());
		Hibernate.initialize(p.getPreferredLocation());
		return p;
	}

	public Project findOneLight(Integer id) {
		Project p = repos.findById(id).get();
		return p;
	}

	public Project findOne2(Integer id) {
		Project p = repos.findById(id).get();
		Hibernate.initialize(p.getManager());
		Hibernate.initialize(p.getCustomer());
		Hibernate.initialize(p.getCostcenter());
		Hibernate.initialize(p.getCostcenter().getLob());
		Hibernate.initialize(p.getCostcenter().getLob().getManager());
		Hibernate.initialize(p.getPreferredWarehouse());
		Hibernate.initialize(p.getPreferredLocation());
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

	public List<Project> findByCompanyWarehousing() {
		return repos.findByCompanyWarehousing();
	}

	public List<Project> findLightByManager(String managerUsername, String status) {
		if (StringUtils.isBlank(status))
			return repos.findLightByManager(managerUsername);
		return repos.findLightByManager(managerUsername, status);
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

	public List<Project> findLightByResource(String username, List<Integer> delegatedProjectList) {
		return repos.findByResourceAndStatus(username, delegatedProjectList, "Open");
	}

	public List<Project> findInboundProjectList(String username, List<Integer> delegatedProjectList) {
		return repos.findByResourceAndStatusAndHavingWarehousing(username, delegatedProjectList, "Open");
	}

	public List<Project> findXboundProjectList(String username, List<Integer> delegatedProjectList) {
		return repos.findByResourceAndStatusAndNotType(username, delegatedProjectList, "Open", ProjectTypes.STOCK.getValue());
	}

	public List<Project> findOutboundProjectList(String username, List<Integer> delegatedProjectList) {
		return repos.findByResourceAndInProjectList(username, delegatedProjectList, findNonEmptyProjectList());
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

	public Integer getCustomerId(Integer id) {
		return repos.getCustomerId(id);
	}

	@Cacheable("projectService.getSdm")
	public Boolean getSdm(Integer id) {
		return repos.getSdm(id);
	}

	public Boolean isStockProject(Integer projectId) {
		return ProjectTypes.STOCK.getValue().equals(getType(projectId));
	}

	public Boolean isDeliveryProject(Integer projectId) {
		return ProjectTypes.DELIVERY.getValue().equals(getType(projectId));
	}

	public Boolean isCustomerProject(Integer projectId) {
		return ProjectCustomerType.CUSTOMER.equals(repos.getCustomerType(projectId));
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

	@Cacheable("projectService.countByManagerAndProjectAndManagerType")
	public Long countByManagerAndProjectAndManagerType(String username, Integer projectId, ProjectManagerType type) {
		return repos.countByManagerAndProjectAndManagerType(username, projectId, type);
	}

	public Boolean isHardwareManager(String username, Integer projectId) {
		return countByManagerAndProjectAndManagerType(username, projectId, ProjectManagerType.HARDWARE_MANAGER) > 0;
	}

	public Integer findCustomerId(Integer id) {
		return repos.findCustomerId(id);
	}

	public Integer findCompanyId(Integer id) {
		return repos.findCompanyId(id);
	}

	public Boolean isQualityManager(String username, Integer projectId) {
		return countByManagerAndProjectAndManagerType(username, projectId, ProjectManagerType.QUALITY_MANAGER) > 0;
	}

	@Cacheable("projectService.findIdListByManagerAndManagerType")
	public List<Integer> findIdListByManagerAndManagerType(String username, ProjectManagerType type) {
		return repos.findIdListByManagerAndManagerType(username, type);
	}

	@Cacheable("projectService.findIdListByQualityManager")
	public List<Integer> findIdListByQualityManager(String username) {
		return findIdListByManagerAndManagerType(username, ProjectManagerType.QUALITY_MANAGER);
	}

	@Cacheable("projectService.findIdListByDelegation")
	public List<Integer> findIdListByDelegation(String username) {
		return repos.findIdListByDelegation(username);
	}

	public List<Project> findProjectListHavingIssues(String username, Collection<Integer> userProjectIdList, Collection<Integer> lobIdList) {
		List<Integer> idList = issueRepos.findProjectIdList(username, userProjectIdList, lobIdList);
		if (!idList.isEmpty())
			return repos.findLight(idList);
		return new ArrayList<Project>();
	}

	public List<Project> findLightByIdListAndCustomer(Collection<Integer> idList, Integer customerId) {
		return repos.findLightByIdListAndCustomer(idList, customerId);
	}

	public List<Integer> findAllProjectIdListByResource(String username, List<Integer> delegatedProjectList) {
		List<Integer> result = new ArrayList<>();
		result.add(-1);
		result.addAll(repos.findAllProjectIdListByResource(username, delegatedProjectList));
		return result;
	}

	public void updateApproximativeStoragePeriod(Integer id, Integer approximativeStoragePeriod) {
		repos.updateApproximativeStoragePeriod(id, approximativeStoragePeriod);
	}
	
	public void updatePreferredWarehouse(Integer id,Integer preferredWarehouseId) {
		repos.updatePreferredWarehouse(id, preferredWarehouseId);
	}
	
	public void updatePreferredLocation(Integer id,Integer preferredLocationId) {
		repos.updatePreferredLocation(id, preferredLocationId);
	}
	
	public void updateCustomerWarehousing(Integer projectId, Boolean customerWarehousing) {
		repos.updateCustomerWarehousing(projectId, customerWarehousing);
		evictCache();
	}

	public void updateCustomerStockManagement(Integer projectId, Boolean customerStockManagement) {
		repos.updateCustomerStockManagement(projectId, customerStockManagement);
		evictCache();
	}

	public void updateCompanyWarehousing(Integer projectId, Boolean companyWarehousing) {
		repos.updateCompanyWarehousing(projectId, companyWarehousing);
		evictCache();
	}

	public void updateCompanyStockManagement(Integer projectId, Boolean companyStockManagement) {
		repos.updateCompanyStockManagement(projectId, companyStockManagement);
		evictCache();
	}

	public void updateSupplierWarehousing(Integer projectId, Boolean supplierWarehousing) {
		repos.updateSupplierWarehousing(projectId, supplierWarehousing);
		evictCache();
	}

	public void updateSupplierStockManagement(Integer projectId, Boolean supplierStockManagement) {
		repos.updateSupplierStockManagement(projectId, supplierStockManagement);
		evictCache();
	}
	
	public void updateSdm(Integer projectId, Boolean sdm) {
		repos.updateSdm(projectId, sdm);
		evictCache();
	}
	

	public void updateIsm(Integer projectId, Boolean sdm) {
		repos.updateIsm(projectId, sdm);
		evictCache();
	}
}
