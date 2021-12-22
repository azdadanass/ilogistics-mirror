package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestExpiryDate;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.StockRowStatus;

@Repository
public interface DeliveryRequestExpiryDateRepos extends JpaRepository<DeliveryRequestExpiryDate, Integer> {

	@Query("from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 ")
	public List<DeliveryRequestExpiryDate> findByDeliveryRequest(Integer deliveryRequestId);

	String remainingQuantity = " a.quantity - COALESCE((select sum(b.quantity) from DeliveryRequestExpiryDate b where b.stockRow.partNumber.id = ?1 and b.stockRow.inboundDeliveryRequest.id = ?2 and b.stockRow.deliveryRequest.type = ?3  and b.stockRow.status = ?4 and b.stockRow.location.id = ?5 and b.expiryDate = a.expiryDate),0) ";

	@Query("select expiryDate from DeliveryRequestExpiryDate a where a.stockRow.partNumber.id = ?1 and a.stockRow.deliveryRequest.id = ?2 and a.stockRow.status = ?4 and a.stockRow.location.id = ?5 and 0 <  " + remainingQuantity)
	public List<Date> findRemainingExpiryDateList(Integer partNumberId, Integer inboundDeliveryRequestId, DeliveryRequestType outboundDeliveryRequestType, StockRowStatus stockRowStatus, Integer locationId);

	@Query("select " + remainingQuantity + "  from DeliveryRequestExpiryDate a where a.stockRow.partNumber.id = ?1 and a.stockRow.deliveryRequest.id = ?2 and a.stockRow.status = ?4 and a.stockRow.location.id = ?5 and a.expiryDate = ?6")
	public Double findRemainingQuantity(Integer partNumberId, Integer inboundDeliveryRequestId, DeliveryRequestType outboundDeliveryRequestType, StockRowStatus stockRowStatus, Integer locationId, Date expiryDate);

	@Query("from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 and a.stockRow.partNumber.id = ?2 and a.stockRow.status = ?3 and a.stockRow.location.id = ?4")
	public List<DeliveryRequestExpiryDate> findByDeliveryRequestAndPartNumberAndStatusAndLocation(Integer delliveryRequestId, Integer partNumberId, StockRowStatus status, Integer locationId);

	@Query("select new DeliveryRequestExpiryDate(sum(a.quantity),a.expiryDate,a.stockRow.deliveryRequest.id,a.stockRow.inboundDeliveryRequest.id) from DeliveryRequestExpiryDate a where a.stockRow.partNumber.id = ?1 and a.stockRow.deliveryRequest.id in (?2) group by a.expiryDate,a.stockRow.deliveryRequest.id")
	public List<DeliveryRequestExpiryDate> findByPartNumberAndDeliveryRequestListGroupByExpiryDateAndDeliveryRequest(Integer partNumberId, List<Integer> deliveryRequestList);
}
