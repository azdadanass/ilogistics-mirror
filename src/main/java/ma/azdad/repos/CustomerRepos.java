package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Customer;
import ma.azdad.utils.LabelValue;

@Repository
public interface CustomerRepos extends JpaRepository<Customer, Integer> {

	@Query("select new ma.azdad.utils.LabelValue(name,id,'customer') from  Customer")
	public List<LabelValue> findLabelValueList();

	@Query("select new Customer(id,name,photo,isStockEmpty) from Customer")
	public List<Customer> findLight();

	@Query("select new Customer(id,name,photo,isStockEmpty) from Customer where id in (?1)")
	public List<Customer> findLight(List<Integer> idList);

	@Query("select new Customer(id,name,photo,isStockEmpty) from Customer where id in (?1) and isStockEmpty = ?2")
	public List<Customer> findLight(List<Integer> idList, Boolean isStockEmpty);

	@Query("select new Customer(id,name,photo,isStockEmpty) from Customer where id in (?1) and isStockEmpty = ?2  and category  = ?3")
	public List<Customer> findLight(List<Integer> idList, Boolean isStockEmpty, String category);

	@Modifying
	@Query("update Customer set isStockEmpty = ?2 where id = ?1")
	public void updateIsStockEmpty(Integer customerId, Boolean isStockEmpty);

	@Query("select a.project.customer.name from Po a where a.idpo = ?1")
	String findNameByPo(Integer poId);

}
