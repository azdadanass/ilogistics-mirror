package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Boq;

@Repository
public interface BoqRepos extends JpaRepository<Boq, Integer> {

	String c1 = "select new Boq(a.id,a.reference,a.quantity,a.unitPrice,a.totalPrice,a.totalQuantity,a.totalUsedQuantity,a.podetails.reference,a.podetails.description,a.podetails.unit,a.partNumber.name,a.partNumber.description,a.partNumber.image,COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id and b.deliveryRequest.status in ('DELIVRED','ACKNOWLEDGED')),0)-COALESCE((select sum(b.quantity) from StockRow b where b.partNumber.id = a.partNumber.id and b.deliveryRequest.outboundDeliveryRequestReturn.po.id = ?1),0)) ";
	String c2 = "select new Boq(sum(a.totalQuantity),(select sum(b.quantity) from StockRow b where b.deliveryRequest.po.id = a.podetails.po.id and b.partNumber.id = a.partNumber.id)+(select sum(b.quantity) from StockRow b where b.partNumber.id = a.partNumber.id and b.deliveryRequest.outboundDeliveryRequestReturn.po.id = ?1),a.partNumber.name,a.partNumber.description,a.partNumber.image) ";
	String c3 = "select new Boq(a.partNumber.name,a.partNumber.description,a.partNumber.image,sum(a.totalQuantity),sum(a.totalUsedQuantity) - (select sum(b.quantity) from StockRow b where b.partNumber.id = a.partNumber.id and b.deliveryRequest.outboundDeliveryRequestReturn.po.id = ?1)) ";

	@Query("select a.partNumber.id from Boq a where a.podetails.po.id = ?1 and a.totalQuantity > a.totalUsedQuantity group by a.partNumber.id")
	public Set<Integer> findPartNumberIdListByPoAndHavingRemainingQuantity(Integer poId);

	@Query("from Boq a where a.podetails.po.id = ?1 and (a.partNumber.id = ?2 or (select count(*) from PartNumberEquivalenceDetail b where b.partNumber.id = ?2 and b.parent.partNumber.id = a.partNumber.id and b.parent.active is true) > 0) ")
	public List<Boq> findByPoAndPartNumber(Integer poId, Integer partNumberId);

	@Query("from Boq a where a.podetails.po.id = ?1 and a.partNumber.id in (?2) ")
	public List<Boq> findByPoAndPartNumber(Integer poId, List<Integer> partNumberListId);

	@Query("select COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id),0) from Boq a where a.id = ?1 ")
	public Double getUsedQuantity(Integer boqId);

	@Modifying
	@Query("update Boq a set a.totalUsedQuantity = COALESCE((select sum(b.quantity) from BoqMapping b where b.boq.id = a.id),0) where a.id in (?1)")
	public void updateTotalUsedQuantity(Set<Integer> idList);

	@Query("select a.boq.id from BoqMapping a where a.deliveryRequest.id = ?1 group by a.boq.id")
	public Set<Integer> getAssociatedBoqIdListWithDeliveryRequest(Integer deliveryRequestId);

	@Query("select count(*) from Boq a where a.podetails.id = ?1 and  totalQuantity > totalUsedQuantity ")
	public Long countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(Integer podetailsId);

	@Query("select count(*) from Boq a where a.podetails.po.id = ?1")
	Long countByPo(Integer poId);

	@Query(c1 + "from Boq a where a.podetails.po.id = ?1")
	public List<Boq> findByPo(Integer poId);
	
	@Query(c1 + "from Boq a where a.podetails.po.id = ?1")
	public List<Boq> findByPoGroupByPartNumber(Integer poId);
	
	@Query(c2 + "from Boq a where a.podetails.po.id = ?1 group by a.partNumber.id")
	public List<Boq> findDeliveredSummaryByPo(Integer poId);
	
	@Query(c3 + "from Boq a where a.podetails.po.id = ?1 group by a.partNumber.id")
	public List<Boq> findBoqSummaryByPo(Integer poId);
}
