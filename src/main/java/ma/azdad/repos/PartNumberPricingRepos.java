package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberPricing;

@Repository
public interface PartNumberPricingRepos extends JpaRepository<PartNumberPricing, Integer> {
	String c1 = "select new PartNumberPricing(a.id,a.date,a.baseLineCost,a.baseLinePrice,a.maxAllowedDiscount,a.currency.currency,a.partNumber.id,a.partNumber.name) ";

	@Query(c1 + "from PartNumberPricing a")
	List<PartNumberPricing> findLight();

	@Query("select count(*) from PartNumberPricing a where a.partNumber.id = ?1 and a.date = ?2 and (?3 is null or a.id != ?3)")
	Long countByPartNumberAndDate(Integer partNumberId, Date date, Integer id);

}
