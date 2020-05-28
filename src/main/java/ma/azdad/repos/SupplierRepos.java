package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Supplier;
import ma.azdad.utils.LabelValue;

@Repository
public interface SupplierRepos extends JpaRepository<Supplier, Integer> {

	@Query("select new ma.azdad.utils.LabelValue(name,id,'supplier') from  Supplier")
	public List<LabelValue> findLabelValueList();
	
	@Query("select new Supplier(id,name) from Supplier")
	public List<Supplier> findLight();
}
