package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BoqMapping;

@Repository
public interface BoqMappingRepos extends JpaRepository<BoqMapping, Integer> {

	@Query("select new BoqMapping(a,abs(a.quantity * a.podetails.unit - COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id),0))) from Boq a where a.podetails.po.idpo = ?1 and abs(a.quantity * a.podetails.unit - COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id),0)) > 0 ")
	public List<BoqMapping> findRemaining(Integer poId);

	@Query("select count(*) from BoqMapping where boq.podetails.po.idpo = ?1")
	Long countByPo(Integer poId);

	@Modifying
	@Query("delete from BoqMapping where boq.id in (select b.id from Boq b where b.podetails.po.idpo = ?1) ")
	void deleteByPo(Integer poId);
}
