package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Podetails;

@Repository
public abstract interface PodetailsRepos extends JpaRepository<Podetails, Integer> {

	@Query("from Podetails where po.id = ?1")
	public List<Podetails> findByPo(Integer poId);

	@Query("select distinct a.podetails from Boq a where a.podetails.po.id = ?1")
	public List<Podetails> findByPoAndHavingBoq(Integer poId);

}
