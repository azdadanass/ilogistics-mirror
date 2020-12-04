package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ExternalResource;

@Repository
public interface ExternalResourceRepos extends JpaRepository<ExternalResource, Integer> {

//	String customerName = " (select b.name from Customer b where b.id = a.customer.id) ";
//	String supplierName = " (select b.name from Supplier b where b.id = a.supplier.id) ";
//	String select1 = "select new ExternalResource(a.id,a.photo,a.fullName,a.job, a.email, a.phone,a.active,a.companyType, a.company, " + customerName + ", " + supplierName + ") ";
//
//	@Query(select1 + " from ExternalResource a")
//	public List<ExternalResource> findLight();
//
//	@Query(select1 + " from ExternalResource a where a.id in (?1)")
//	public List<ExternalResource> findLight(List<Integer> idList);
//
//	@Query(select1 + " from ExternalResource a where a.user.username = ?1")
//	public List<ExternalResource> findLight(String username);
//
//	@Query(select1 + " from ExternalResource a where a.companyType = ?1 and a.customer.id = ?2")
//	public List<ExternalResource> findLightByCustomer(CompanyType companyType, Integer customerId);
//
//	@Query(select1 + " from ExternalResource a where a.companyType = ?1 and a.supplier.id = ?2")
//	public List<ExternalResource> findLightBySupplier(CompanyType companyType, Integer supplierId);
//
//	@Query(select1 + " from ExternalResource a where a.companyType = ?1 and a.company = ?2")
//	public List<ExternalResource> findLightByDeliverToOther(CompanyType companyType, String company);
//
//	public List<ExternalResource> findByJob(String job);
//
//	@Query("select count(*) from ExternalResource where cin = ?1")
//	public Long countByCin(String cin);
//
//	@Query("select count(*) from ExternalResource where cin = ?1 and id != ?2")
//	public Long countByCin(String cin, Integer id);
//
//	@Query("select count(*) from ExternalResource where email = ?1")
//	public Long countByEmail(String email);
//
//	@Query("select count(*) from ExternalResource where email = ?1 and id != ?2")
//	public Long countByEmail(String email, Integer id);
//
//	@Query("select count(*) from ExternalResource where phone = ?1")
//	public Long countByPhone(String phone);
//
//	@Query("select count(*) from ExternalResource where phone = ?1 and id != ?2")
//	public Long countByPhone(String phone, Integer id);
//
//	@Query("select count(*) from ExternalResource where firstName = ?1 and lastName = ?2")
//	public Long countByFirstNameAndLastName(String firstName, String lastName);
//
//	@Query("select count(*) from ExternalResource where firstName = ?1 and lastName = ?2 and id != ?3")
//	public Long countByFirstNameAndLastName(String firstName, String lastName, Integer id);
//
//	@Query(select1 + " from ExternalResource a where a.transporter.id = ?1")
//	public List<ExternalResource> findLightByTransporter(Integer transporterId);
//
//	@Query("select a.customer.id from ExternalResource a where a.id = ?1")
//	public Integer findCustomerId(Integer id);
//
//	@Query("from ExternalResource a where a.customer.id = ?1")
//	public List<ExternalResource> findByCustomer(Integer customerId);
//
//	@Query("from ExternalResource a where a.supplier.id = ?1")
//	public List<ExternalResource> findBySupplier(Integer supplierId);
//
//	public List<ExternalResource> findByCompany(String company);
//
//	@Query("select new ExternalResource(a.id,a.fullName) from ExternalResource a where a.customer.id = (select b.customer.id from Project b where b.id = ?1)")
//	public List<ExternalResource> findLightByProject(Integer projectId);
//
//	@Query("from ExternalResource where email = ?1")
//	public ExternalResource findByEmail(String email);

}
