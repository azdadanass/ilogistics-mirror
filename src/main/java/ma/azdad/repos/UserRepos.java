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

	@Query("select new User(username,fullName,photo,email,job,phone,cin) from User ")
	public List<User> find();

	List<User> findByInternal(Boolean internal);

	@Modifying
	@Query("update User set password = ?2 where username = ?1")
	public void updatePassword(String username, String password);

	@Query("select new User(username,fullName) from User ")
	public List<User> findLight();

	@Query("select new User(username,fullName,photo,email,job) from User ")
	public List<User> findLight2();

	@Query("select new User(user.username,user.fullName) from UserRole where role = ?1")
	public List<User> findLightByRole(Role role);

	@Query("select new User(username,fullName) from User where username in (?1)")
	public List<User> findLightByUsernameList(List<String> list);

	@Query("select new User(a.username,a.fullName) from User a,Affectation b where a.username = b.user.username and b.lineManager.username = ?1 and a.contractActive = ?2")
	public List<User> findLightByLineManagerAndStatus(String lineManagerUsername, Boolean contractActive);

	@Query("select new User(a.username,a.fullName) from User a where a.contractActive = ?1")
	public List<User> findLightByStatus(Boolean contractActive);

	public Long countByUsername(String username);

	User findByLogin(String login);

	String customerName = " (select b.name from Customer b where b.id = a.customer.id) ";
	String supplierName = " (select b.name from Supplier b where b.id = a.supplier.id) ";
	String select1 = "select new User(a.id,a.photo,a.fullName,a.job, a.email, a.phone,a.active,a.companyType, a.company, " + customerName + ", " + supplierName + ") ";

	@Query(select1 + " from User a where a.internal = ?1")
	List<User> findLight(Boolean interna);

	@Query(select1 + " from User a where a.internal = ?1 and a.user.username = ?2")
	List<User> findLightByUser(Boolean internal, String userUsername);

	@Query(select1 + " from User a where a.id in (?1)")
	public List<User> findLight(List<Integer> idList);

	@Query(select1 + " from User a where a.user.username = ?1")
	public List<User> findLight(String username);

	@Query(select1 + " from User a where a.companyType = ?1 and a.customer.id = ?2")
	public List<User> findLightByCustomer(CompanyType companyType, Integer customerId);

	@Query(select1 + " from User a where a.companyType = ?1 and a.supplier.id = ?2")
	public List<User> findLightBySupplier(CompanyType companyType, Integer supplierId);

	@Query(select1 + " from User a where a.companyType = ?1 and a.company = ?2")
	public List<User> findLightByCompany(CompanyType companyType, String company);

	public List<User> findByJob(String job);

	@Query("select count(*) from User where cin = ?1")
	public Long countByCin(String cin);

	@Query("select count(*) from User where cin = ?1 and username != ?2")
	public Long countByCin(String cin, String username);

	@Query("select count(*) from User where email = ?1")
	public Long countByEmail(String email);

	@Query("select count(*) from User where email = ?1 and username != ?2")
	public Long countByEmail(String email, String username);

	@Query("select count(*) from User where phone = ?1")
	public Long countByPhone(String phone);

	@Query("select count(*) from User where phone = ?1 and username != ?2")
	public Long countByPhone(String phone, String username);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2")
	public Long countByFirstNameAndLastName(String firstName, String lastName);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2 and username != ?3")
	public Long countByFirstNameAndLastName(String firstName, String lastName, String username);

	@Query(select1 + " from User a where a.transporter.id = ?1")
	public List<User> findLightByTransporter(Integer transporterId);

	@Query("select a.customer.id from User a where a.id = ?1")
	public Integer findCustomerId(Integer id);

	@Query("from User a where a.customer.id = ?1")
	public List<User> findByCustomer(Integer customerId);

	@Query("from User a where a.supplier.id = ?1")
	public List<User> findBySupplier(Integer supplierId);

	public List<User> findByCompany(String company);

	@Query("select new User(a.id,a.fullName) from User a where a.customer.id = (select b.customer.id from Project b where b.id = ?1)")
	public List<User> findLightByProject(Integer projectId);

	User findByEmail(String email);

	User findByPhone(String phone);

}
