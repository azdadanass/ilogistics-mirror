package ma.azdad.repos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Customer;
import ma.azdad.utils.LabelValue;

@Repository
public interface CustomerRepos extends JpaRepository<Customer, Integer> {

	String c1 = "select new Customer(id,name,photo) ";

	@Query("select new ma.azdad.utils.LabelValue(name,id,'customer') from  Customer")
	public List<LabelValue> findLabelValueList();

	@Query(c1 + "from Customer where isCustomer is true")
	public List<Customer> findLight();

	@Query(c1 + "from Customer where id in (?1)")
	public List<Customer> findLight(Collection<Integer> idList);

	@Query(c1 + "from Customer where id in (?1) and category  = ?2")
	public List<Customer> findLight(Collection<Integer> idList, String category);

//	@Query("select new Customer(id,name,photo,isStockEmpty) from Customer where id in (?1) and isStockEmpty = ?2")
//	public List<Customer> findLight(List<Integer> idList, Boolean isStockEmpty);
//
//	@Query("select new Customer(id,name,photo,isStockEmpty) from Customer where id in (?1) and isStockEmpty = ?2  and category  = ?3")
//	public List<Customer> findLight(List<Integer> idList, Boolean isStockEmpty, String category);
//	@Modifying
//	@Query("update Customer set isStockEmpty = ?2 where id = ?1")
//	public void updateIsStockEmpty(Integer customerId, Boolean isStockEmpty);

	@Query("select a.project.customer.name from Po a where a.id = ?1")
	String findNameByPo(Integer poId);
	
	@Query(c1+"from Customer where id in (select distinct a.customer.id from ProjectAssignment a where a.project.id = ?1 and current_date between a.startDate and a.endDate)")
	List<Customer> findAssignedToProject(Integer projectId);

}
