package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestExpiryDate;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.PartNumber;
import ma.azdad.model.StockRowStatus;
import ma.azdad.utils.Pair;

@Repository
public interface DeliveryRequestExpiryDateRepos extends JpaRepository<DeliveryRequestExpiryDate, Integer> {

	@Query("from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 ")
	public List<DeliveryRequestExpiryDate> findByDeliveryRequest(Integer deliveryRequestId);

	String remainingQuantity = " a.quantity - COALESCE((select sum(b.quantity) from DeliveryRequestExpiryDate b where b.stockRow.partNumber.id = ?1 and b.stockRow.inboundDeliveryRequest.id = ?2 and b.stockRow.deliveryRequest.type = ?3  and b.stockRow.status = ?4 and b.stockRow.location.id = ?5 and b.expiryDate = a.expiryDate),0) ";

	@Query("select expiryDate from DeliveryRequestExpiryDate a where a.stockRow.partNumber.id = ?1 and a.stockRow.deliveryRequest.id = ?2 and a.stockRow.status = ?4 and a.stockRow.location.id = ?5 and 0 <  "
			+ remainingQuantity)
	public List<Date> findRemainingExpiryDateList(Integer partNumberId, Integer inboundDeliveryRequestId, DeliveryRequestType outboundDeliveryRequestType, StockRowStatus stockRowStatus,
			Integer locationId);

	@Query("select " + remainingQuantity
			+ "  from DeliveryRequestExpiryDate a where a.stockRow.partNumber.id = ?1 and a.stockRow.deliveryRequest.id = ?2 and a.stockRow.status = ?4 and a.stockRow.location.id = ?5 and a.expiryDate = ?6")
	public Double findRemainingQuantity(Integer partNumberId, Integer inboundDeliveryRequestId, DeliveryRequestType outboundDeliveryRequestType, StockRowStatus stockRowStatus, Integer locationId,
			Date expiryDate);

//	@Query("from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 and a.stockRow.partNumber.id = ?2 and a.stockRow.status = ?3 and a.stockRow.location.id = ?4")
//	public List<DeliveryRequestExpiryDate> findByDeliveryRequestAndPartNumberAndStatusAndLocation(Integer delliveryRequestId, Integer partNumberId, StockRowStatus status, Integer locationId);
	
	@Query("select distinct a.expiryDate from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 and a.stockRow.partNumber.id = ?2 and a.stockRow.status = ?3 and a.stockRow.location.id = ?4")
	public List<Date> findExpiryDateByDeliveryRequestAndPartNumberAndStatusAndLocation(Integer delliveryRequestId, Integer partNumberId, StockRowStatus status, Integer locationId);

	@Query("select new DeliveryRequestExpiryDate(sum(a.quantity),a.expiryDate,a.stockRow.deliveryRequest.id,a.stockRow.inboundDeliveryRequest.id) from DeliveryRequestExpiryDate a where a.stockRow.partNumber.id = ?1 and a.stockRow.deliveryRequest.id in (?2) group by a.expiryDate,a.stockRow.deliveryRequest.id,a.stockRow.inboundDeliveryRequest.id")
	public List<DeliveryRequestExpiryDate> findByPartNumberAndDeliveryRequestListGroupByExpiryDateAndDeliveryRequestAndInboundDeliveryRequest(Integer partNumberId, List<Integer> deliveryRequestList);

	@Query("select count(*) from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 and a.expiryDate = ?2")
	public Long countByDeliveryRequestAndDate(Integer deliveryRequestId, Date expiryDate);
	
	@Query("select a.stockRow.partNumber.id,sum(quantity) from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 and a.expiryDate is not null group by a.stockRow.partNumber.id")
	public List<Object[]> findQuantityMap(Integer deliveryRequest);
	
	@Query("select new DeliveryRequestExpiryDate(a.stockRow.partNumber.id,a.stockRow.partNumber.name,a.stockRow.partNumber.description,a.stockRow.partNumber.image,sum(case when a.stockRow.deliveryRequest.type = 'INBOUND' then a.quantity else -a.quantity end),a.expiryDate) from DeliveryRequestExpiryDate a where a.stockRow.inboundDeliveryRequest.id = ?1  group by a.stockRow.inboundDeliveryRequestDetail,a.expiryDate having sum(case when a.stockRow.deliveryRequest.type = 'INBOUND' then a.quantity else -a.quantity end) > 0")
	List<DeliveryRequestExpiryDate> findByInboundDeliveryRequest(Integer inboundDeliveryRequestId);
}
