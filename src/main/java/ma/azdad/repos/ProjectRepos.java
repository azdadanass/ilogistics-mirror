
package ma.azdad.repos;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectCustomerType;
import ma.azdad.model.ProjectManagerType;

@Repository
public interface ProjectRepos extends JpaRepository<Project, Integer> {
	String select1 = "select new Project(id,name,type,startDate,endDate,customer.id) ";
	String c2 = "select new Project(id,name,type,subType,status,startDate,endDate,customer.name,companyWarehousing,companyStockManagement,customerWarehousing,customerStockManagement,supplierWarehousing,supplierStockManagement,sdm,ism,manager.fullName) ";

	@Query("select new Project(id,name,type) from Project ")
	public List<Project> findLight();
	
	@Query("from Project where companyWarehousing is true")
	List<Project> findByCompanyWarehousing();

//	@Query("select new Project(id,name) from Project where id in (?1)")
//	public List<Project> findLight(List<Integer> idList);

	@Query("select new Project(id,name) from Project where id in (?1)")
	public List<Project> findLight(Collection<Integer> idList);

	@Query(select1 + "from Project where status = ?1")
	public List<Project> findLight(String status);

	@Query("select new Project(id,name) from Project where id in (?1) and customer.id = ?2 and (companyWarehousing is true or supplierWarehousing is true or customerWarehousing is true)")
	public List<Project> findLightByIdListAndCustomer(Collection<Integer> idList, Integer customerId);

	@Query(c2 + "from Project where manager.username = ?1 order by customerWarehousing desc")
	public List<Project> findLightByManager(String managerUsername);
	
	@Query(c2 + "from Project where manager.username = ?1 and status = ?2 order by customerWarehousing desc")
	public List<Project> findLightByManager(String managerUsername,String status);

	@Query(select1 + "from Project where manager.username = ?1 and status = ?2")
	public List<Project> findLightByManagerAndStatus(String managerUsername, String status);

	String cond1 = " (manager.username = ?1 or id in (?2) or id in (select b.project.id from ProjectAssignment b where b.user.username = ?1 and current_date between b.startDate and  b.endDate)) ";

	@Query(select1 + " from Project where " + cond1 + " and status = ?3")
	public List<Project> findByResourceAndStatus(String username,List<Integer> delegatedProjectList, String status);

	@Query(select1 + " from Project where " + cond1 + " and status = ?3 and (companyWarehousing is true or customerWarehousing is true or supplierWarehousing is true)")
	public List<Project> findByResourceAndStatusAndHavingWarehousing(String username,List<Integer> delegatedProjectList, String status);

	@Query(select1 + " from Project where " + cond1 + " and status = ?3 and type != ?4")
	public List<Project> findByResourceAndStatusAndNotType(String username,List<Integer> delegatedProjectList, String status, String type);

	@Query(select1 + " from Project where " + cond1 + " and id in (?3) ")
	public List<Project> findByResourceAndInProjectList(String username,List<Integer> delegatedProjectList, Set<Integer> projectList);

	@Query("select a.inboundDeliveryRequest.project.id from StockRow a where a.inboundDeliveryRequest is not null group by a.partNumber.id,a.inboundDeliveryRequest.project.id having sum(a.quantity) > 0 ")
	public List<Integer> findNonEmptyProjectList();

	@Query("select id from Project where status = 'Open' and (manager.username = ?1 or id in (select b.project.id from ProjectAssignment b where b.user.username = ?1 and current_date between b.startDate and  b.endDate))")
	public List<Integer> findAssignedProjectIdListByResource(String username);

	@Query("select type from Project where id = ?1")
	public String getType(Integer projectId);

	@Query("select customerType from Project where id = ?1")
	public ProjectCustomerType getCustomerType(Integer projectId);

	@Query("select subType from Project where id = ?1")
	public String getSubType(Integer projectId);

	@Query("SELECT new Project(id,name,type,manager.fullName) FROM Project ")
	public List<Project> find();

	@Query("select a.customer.id from Project a where a.id = ?1")
	public Integer getCustomerId(Integer id);

	@Query("select a.sdm from Project a where a.id = ?1")
	public Boolean getSdm(Integer id);

	@Query("select count(*) from ProjectManager a where a.project.id = ?1 and a.user.username = ?2 and a.type = ?3")
	Long countByManagerType(Integer projectId, String userUsername, ProjectManagerType type);

	@Query("select a.project.id from  ProjectManager a where a.user.username = ?1 and a.type = ?2")
	List<Integer> findIdListByManagerType(String username, ProjectManagerType type);

	@Query("select count(*) from ProjectManager a where a.user.username = ?1 and a.project.id = ?2 and a.type = ?3")
	Long countByManagerAndProjectAndManagerType(String username, Integer projectId, ProjectManagerType type);

	@Query("select customer.id from Project where id = ?1")
	Integer findCustomerId(Integer id);

	@Query("select costcenter.lob.bu.company.id from Project where id = ?1")
	Integer findCompanyId(Integer id);

	@Query("select distinct a.project.id from ProjectManager a where a.user.username = ?1 and a.type = ?2")
	List<Integer> findIdListByManagerAndManagerType(String username, ProjectManagerType type);

	@Query("select distinct a.project.id  from DelegationDetail a where a.delegation.delegate.username = ?1 and a.delegation.status = 'Active' and a.type = 'PM'")
	List<Integer> findIdListByDelegation(String username);

	@Query("select id from Project where (companyWarehousing is true or supplierWarehousing is true or customerWarehousing is true) and status = 'Open' and (manager.username = ?1 or id in (?2) or id in (select b.project.id from ProjectAssignment b where b.user.username = ?1 and current_date between b.startDate and  b.endDate))")
	public List<Integer> findAllProjectIdListByResource(String username,List<Integer> delegatedProjectList);
	
	
	// inplace
	@Modifying
	@Query("update Project set approximativeStoragePeriod = ?2 where id = ?1")
	void updateApproximativeStoragePeriod(Integer id,Integer approximativeStoragePeriod);
	
	@Modifying
	@Query("update Project set preferredWarehouse.id = ?2 where id = ?1")
	void updatePreferredWarehouse(Integer id,Integer preferredWarehouseId);
	
	@Modifying
	@Query("update Project set preferredLocation.id = ?2 where id = ?1")
	void updatePreferredLocation(Integer id,Integer preferredLocationId);
	
	@Modifying
	@Query("update Project set customerWarehousing = ?2 where id = ?1")
	void updateCustomerWarehousing(Integer projectId, Boolean customerWarehousing);

	@Modifying
	@Query("update Project set customerStockManagement = ?2 where id = ?1")
	void updateCustomerStockManagement(Integer projectId, Boolean customerStockManagement);

	@Modifying
	@Query("update Project set companyWarehousing = ?2 where id = ?1")
	void updateCompanyWarehousing(Integer projectId, Boolean companyWarehousing);

	@Modifying
	@Query("update Project set companyStockManagement = ?2 where id = ?1")
	void updateCompanyStockManagement(Integer projectId, Boolean companyStockManagement);

	@Modifying
	@Query("update Project set supplierWarehousing = ?2 where id = ?1")
	void updateSupplierWarehousing(Integer projectId, Boolean supplierWarehousing);

	@Modifying
	@Query("update Project set supplierStockManagement = ?2 where id = ?1")
	void updateSupplierStockManagement(Integer projectId, Boolean supplierStockManagement);
	
	@Modifying
	@Query("update Project set sdm = ?2 where id = ?1")
	void updateSdm(Integer projectId, Boolean sdm);
	
	@Modifying
	@Query("update Project set ism = ?2 where id = ?1")
	void updateIsm(Integer projectId, Boolean ism);

}
