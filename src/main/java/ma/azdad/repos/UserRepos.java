package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Role;
import ma.azdad.model.User;

@Repository
public interface UserRepos extends JpaRepository<User, String> {

	String c1 = "select new User(a.username,a.fullName) ";
	String c3 = "select new User(a.username,a.fullName,a.email,a.internal) ";
	String c6 = "select distinct new User(a.username,a.firstName,a.lastName,a.login,a.photo,a.email) ";


	@Query("select new User(username,fullName,photo,email,job,phone,cin) from User ")
	List<User> find();
	
	@Query(c3 + "from User a where a.active = ?1")
	List<User> find(Boolean active);
	
	@Query(c6 +" from User a where a.active = ?1")
	public List<User> findLightByStatus2(Boolean active);
	
	@Query(c3 + "from User a where  a.active = ?1 and a.username!=?2 order by a.internal desc,a.fullName asc")
	List<User> findLight2( Boolean active,String username);

	List<User> findByInternal(Boolean internal);

	@Query(c1 + "from User a where a.internal = ?1 and a.active = ?2")
	List<User> findLight(Boolean internal, Boolean active);
	
	@Query(c1 + "from User a where (a.internal = ?1 and a.active = ?2 and a.username!=?3) or a.username='e.r.bouougri' ")
	List<User> findLight2(Boolean internal, Boolean active,String username);

	@Modifying
	@Query("update User set password = ?2 where username = ?1")
	void updatePassword(String username, String password);

	@Query("select new User(username,fullName) from User ")
	List<User> findLight();

	@Query("select new User(username,fullName,photo,email,job) from User ")
	List<User> findLight2();

	@Query("select new User(user.username,user.fullName) from UserRole where role = ?1")
	List<User> findLightByRole(Role role);

	@Query("select new User(username,fullName) from User where username in (?1)")
	List<User> findLightByUsernameList(List<String> list);

	@Query("select new User(a.username,a.fullName) from User a,Affectation b where a.username = b.user.username and b.lineManager.username = ?1 and a.active = ?2")
	List<User> findLightByLineManagerAndStatus(String lineManagerUsername, Boolean active);

	@Query("select new User(a.username,a.fullName) from User a where a.active = ?1")
	List<User> findLightByStatus(Boolean active);

	Long countByUsername(String username);
	
	User findByUsername(String username);
	
	@Query(c6 + "from User a where a.username = ?1")
	User findByUsernameLight(String username);
	
	List<User> findByCin(String cin);
	
	User findByFullName(String fullName);

	User findByLogin(String login);

	String companyName = " (select b.name from Company b where b.id = a.company.id) ";
	String customerName = " (select b.name from Customer b where b.id = a.customer.id) ";
	String supplierName = " (select b.name from Supplier b where b.id = a.supplier.id) ";
	String select1 = "select new User(a.id,a.photo,a.fullName,a.cin,a.job, a.email, a.phone,a.active,a.companyType, " + companyName + ", " + customerName + ", " + supplierName
			+ ") ";

	@Query(select1 + "from User a where a.company.id = ?1 and a.active is true")
	List<User> findActiveByCompany(Integer companyId);
	
	@Query(select1 + "from User a where a.customer.id = ?1 and a.active is true")
	List<User> findActiveByCustomer(Integer customerId);

	@Query(select1 + "from User a where a.supplier.id = ?1 and a.active is true")
	List<User> findActiveBySupplier(Integer supplierId);

	@Query(select1 + " from User a where a.internal = ?1")
	List<User> findLight(Boolean interna);

	@Query(select1 + " from User a where a.internal = ?1 and a.user.username = ?2")
	List<User> findLightByUser(Boolean internal, String userUsername);

	@Query(select1 + " from User a where a.id in (?1)")
	List<User> findLight(List<Integer> idList);

	@Query(select1 + " from User a where a.user.username = ?1")
	List<User> findLight(String username);

	@Query(c1 + " from User a where a.company.id = ?1 and a.active = ?2")
	List<User> findLightByCompany(Integer companyId, Boolean active);

	@Query(select1 + " from User a where a.companyType = ?1 and a.customer.id = ?2")
	List<User> findLightByCustomer(CompanyType companyType, Integer customerId);

	@Query(select1 + " from User a where a.companyType = ?1 and a.supplier.id = ?2")
	List<User> findLightBySupplier(CompanyType companyType, Integer supplierId);

//	@Query(select1 + " from User a where a.companyType = ?1 and a.other = ?2")
//	List<User> findLightByOther(CompanyType companyType, String other);

	List<User> findByJob(String job);

	@Query("select count(*) from User where cin = ?1")
	Long countByCin(String cin);

	@Query("select count(*) from User where cin = ?1 and username != ?2")
	Long countByCin(String cin, String username);

	@Query("select count(*) from User where email = ?1")
	Long countByEmail(String email);

	@Query("select count(*) from User where email = ?1 and username != ?2")
	Long countByEmail(String email, String username);

	@Query("select count(*) from User where phone = ?1")
	Long countByPhone(String phone);

	@Query("select count(*) from User where phone = ?1 and username != ?2")
	Long countByPhone(String phone, String username);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2")
	Long countByFirstNameAndLastName(String firstName, String lastName);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2 and username != ?3")
	Long countByFirstNameAndLastName(String firstName, String lastName, String username);

	@Query(select1 + " from User a where a.transporter.id = ?1")
	List<User> findLightByTransporter(Integer transporterId);

	@Query("select a.customer.id from User a where a.id = ?1")
	Integer findCustomerId(Integer id);

	@Query("from User a where a.customer.id = ?1")
	List<User> findByCustomer(Integer customerId);

	@Query("from User a where a.supplier.id = ?1")
	List<User> findBySupplier(Integer supplierId);

	@Query("from User a where ((a.customer is not null and a.customer.id = ?1) or (a.supplier is not null and a.supplier.id = ?2)) and (select count(*) from  ProjectAssignment b where b.user.username = a.username and b.project.id = ?3 and current_date between b.startDate and b.endDate) > 0")
	List<User> findByCustomerOrSupplierAndHavingAssignement(Integer customerId, Integer supplierId, Integer projectId);

	List<User> findByCompany(String company);

	@Query("select new User(a.id,a.fullName) from User a where a.customer.id = (select b.customer.id from Project b where b.id = ?1)")
	List<User> findLightByProject(Integer projectId);

	User findByEmail(String email);

	User findByPhone(String phone);

	@Query("select distinct a.user from ProjectAssignment a where a.project.id =?1 and current_date between a.startDate and a.endDate and (select count(*) from UserRole b where b.user.username = a.user.username and  b.role = ?2) > 0 and a.user.internal = ?3 ")
	List<User> findByProjectAssignmentAndUserRole(Integer projectId, Role userRole, Boolean internal);

	@Query("select distinct a.user from ProjectAssignment a where a.project.id =?1 and current_date between a.startDate and a.endDate and a.user.internal = ?2")
	List<User> findByProjectAssignment(Integer projectId, Boolean internal);
	
	@Query("select distinct a.user from ProjectAssignment a where a.project.id =?1 and current_date between a.startDate and a.endDate and a.user.active is true")
	List<User> findByActiveAndProjectAssignment(Integer projectId);

	@Query("select distinct a.delegation.delegate from DelegationDetail a where a.project.id =?1 and current_date between a.delegation.startDate and a.delegation.endDate and a.delegation.delegate.internal = ?2")
	List<User> findByProjectDelegation(Integer projectId, Boolean internal);

	@Query("select a.project.costcenter.lob.manager from DeliveryRequest a where a.id=  ?1")
	User findLobManagerByDeliveryRequest(Integer deliveryRequestId);

	// security
	@Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.login = ?2")
	@Modifying
	void updateFailedAttempts(int failAttempts, String login);

	// external
	@Query("select new User(a.username,a.fullName) from User a where a.internal is false and a.ilogistics is true and a.active is true")
	public List<User> findExternalActive();

	@Query("select new User(a.username,a.fullName) from User a where a.active is true and a.companyType = ?1 and (a.company.id = ?2 or a.customer.id = ?3 or a.supplier.id = ?4)")
	List<User> findActiveByCompanyType(CompanyType companyType, Integer companyId, Integer customerId, Integer supplierId);

	@Query("select distinct a.user from ProjectAssignment a where a.project.id =?1 and current_date between a.startDate and a.endDate and (select count(*) from UserRole b where b.user.username = a.user.username and  b.role = ?2) > 0 and a.user.supplier.id = ?3 and a.user.active is true")
	List<User> findActiveByProjectAssignmentAndUserRoleAndSupplier(Integer projectId, Role userRole, Integer supplierId);

	@Query(select1 + "from User a where a.supplier.id = ?1 and (select count(*) from UserRole b where b.user.username = a.username and b.role = ?2) > 0 and a.active is true")
	List<User> findLightBySupplierAndHasRole(Integer supplierId, Role role);

	@Query(select1 + "from User a where a.customer.id = ?1 and (select count(*) from UserRole b where b.user.username = a.username and b.role = ?2) > 0 and a.active is true")
	List<User> findLightByCustomerAndHasRole(Integer customerId, Role role);
	
	@Query("select new ma.azdad.mobile.model.User(a.username,a.firstName,a.lastName,a.login,a.photo,a.email) from User a where username = ?1")
	ma.azdad.mobile.model.User findOneMobile(String username);
	
	
	@Query("select distinct a.user from UserRole a where a.user.active is true and a.role = ?1")
	List<User> findByRole(Role role);
	
	@Query(c1
			+ "from User a where a.companyType = 'COMPANY' and a.company.id = ?2 and (select count(*) from ProjectAssignment b where b.user.username = a.username and b.project.id = ?1 and current_date between b.startDate and b.endDate) > 0")
	List<User> findByAssignementAndCompany(Integer projectId, Integer companyId);

	@Query(c1
			+ "from User a where a.companyType = 'CUSTOMER' and a.customer.id = ?2 and (select count(*) from ProjectAssignment b where b.user.username = a.username and b.project.id = ?1 and current_date between b.startDate and b.endDate) > 0")
	List<User> findByAssignementAndCustomer(Integer projectId, Integer customerId);

	@Query(c1
			+ "from User a where a.companyType = 'SUPPLIER' and a.supplier.id = ?2 and (select count(*) from ProjectAssignment b where b.user.username = a.username and b.project.id = ?1 and current_date between b.startDate and b.endDate) > 0")
	List<User> findByAssignementAndSupplier(Integer projectId, Integer supplierId);
	
	@Query("select a.companyType from User a where a.username = ?1")
	CompanyType findCompanyType(String username);
	
	@Query("select a.company.name from User a where a.username = ?1")
	String findCompanyName(String username);

	@Query("select a.customer.name from User a where a.username = ?1")
	String findCustomerName(String username);

	@Query("select a.supplier.name from User a where a.username = ?1")
	String findSupplierName(String username);
}
