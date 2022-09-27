package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BoqMapping;
import ma.azdad.model.DeliveryRequestStatus;

@Repository
public interface BoqMappingRepos extends JpaRepository<BoqMapping, Integer> {

	@Query("select new BoqMapping(a,abs(a.quantity * a.podetails.unit - COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id),0))) from Boq a where a.podetails.po.idpo = ?1 and abs(a.quantity * a.podetails.unit - COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id),0)) > 0 ")
	public List<BoqMapping> findRemaining(Integer poId);

	@Query("select count(*) from BoqMapping where boq.podetails.po.idpo = ?1")
	Long countByPo(Integer poId);

	@Modifying
	@Query("delete from BoqMapping where boq.id in (select b.id from Boq b where b.podetails.po.idpo = ?1) ")
	void deleteByPo(Integer poId);

	@Query("select count(distinct a.deliveryRequest.id) from BoqMapping a where a.boq.podetails.po.idpo = ?1 and a.deliveryRequest.status in (?2)")
	Long countDeliveryRequestsByRelatedToPoAndInStatus(Integer poId, List<DeliveryRequestStatus> inStatus);

	@Query("select count(distinct a.deliveryRequest.id) from BoqMapping a where a.boq.podetails.po.idpo = ?1 and a.deliveryRequest.status not in (?2)")
	Long countDeliveryRequestsByRelatedToPoAndNotInStatus(Integer poId, List<DeliveryRequestStatus> notInStatus);
	
	
//	@Query("select sum(a.quantity) from BoqMapping a where a.boq.podetails.idpoDetails = ?1 and a.boq.partNumber.id = ?2 and a.deliveryRequest.status in ('DELIVRED','ACKNOWLEDGED')")
//	Double findTotalQuantityByPoDetailAndPartNumberAndDeliveryRequestDelivered(Integer poDetailId,Integer partNumberId);
	
}
