package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Company;
import ma.azdad.utils.LabelValue;

@Repository
public abstract interface CompanyRepos extends JpaRepository<Company, Integer> {

	@Query("select new ma.azdad.utils.LabelValue(name,id,'company') from  Company")
	public List<LabelValue> findLabelValueList();

	@Query("from Company where id in (?1)")
	public List<Company> find(List<Integer> idList);

}
