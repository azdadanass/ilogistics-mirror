package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Company;
import ma.azdad.model.Currency;
import ma.azdad.utils.LabelValue;

@Repository
public abstract interface CompanyRepos extends JpaRepository<Company, Integer> {

	@Query("select new ma.azdad.utils.LabelValue(name,id,'company') from  Company")
	public List<LabelValue> findLabelValueList();

	@Query("from Company where id in (?1)")
	public List<Company> find(List<Integer> idList);
	
	@Query("select id from Company")
	public List<Integer> findIdList();

	@Query("select a.costcenter.lob.bu.company.id from Project a where a.id = ?1")
	Integer findIdByProject(Integer projectId);

	@Query("select new Company(id,name) from Company")
	List<Company> findLight();
	
	@Query("select a.company from User a where a.username = ?1")
	Company findCompanyUser(String username);
	
	@Query("select a.accountingCurrency from Company a where a.id = ?1")
	Currency findAccountingCurrency(Integer id);
	
	@Query("select new Company(a.costcenter.lob.bu.company.id,a.costcenter.lob.bu.company.name) from Project a where a.id = ?1")
	Company findLightByProject(Integer projectId);

}
