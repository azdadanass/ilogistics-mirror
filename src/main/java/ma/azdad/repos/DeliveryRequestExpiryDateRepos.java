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
	
	@Query("select new DeliveryRequestExpiryDate(sum(a.quantity)-coalesce((select sum(b.quantity) from  DeliveryRequestExpiryDate b where (b.stockRow.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 or b.stockRow.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1) and b.stockRow.partNumber.id = ?2 and b.expiryDate = a.expiryDate),0),a.expiryDate) from DeliveryRequestExpiryDate a where a.stockRow.deliveryRequest.id = ?1 and a.stockRow.partNumber.id = ?2 group by a.expiryDate order by a.expiryDate")
	List<DeliveryRequestExpiryDate> findRemainingQuantityByOutboundDeliveryRequestAndPartNumberGroupByExpiryDate(Integer outboundDeliveryRequestId,Integer partNumberId);
	
	@Query("select count(*) from DeliveryRequestExpiryDate where stockRow.id = ?1")
	Long countByStockRow(Integer stockRowId);
	
	
	// delivery reporting
	String c1 = "select new DeliveryRequestExpiryDate(sum(a.quantity),a.expiryDate,"
			+ "a.stockRow.partNumber.name,a.stockRow.partNumber.description,a.stockRow.partNumber.brandName,a.stockRow.deliveryRequest.project.name," //
			+ "a.stockRow.deliveryRequest.reference,a.stockRow.deliveryRequest.date4,"
			+ "a.stockRow.deliveryRequest.deliverToCompanyType,"
			+ "(select b.name from Company b where b.id = a.stockRow.deliveryRequest.deliverToCompany.id),"//
			+ "(select b.name from Customer b where b.id = a.stockRow.deliveryRequest.deliverToCustomer.id),"//
			+ "(select b.name from Supplier b where b.id = a.stockRow.deliveryRequest.deliverToSupplier.id),"//
			+ "a.stockRow.deliveryRequest.deliverToOther,"//
			+ "(select b.name from Site b where b.id = a.stockRow.deliveryRequest.destination.id),"//
			+ "(select b.name from Customer b where b.id = (select c.customer.id from Project c where c.id = a.stockRow.deliveryRequest.destinationProject.id)),"//
			+ "(select b.name from Project b where b.id = a.stockRow.deliveryRequest.destinationProject.id),"//
			+ "(select b.name from Customer b where b.id = a.stockRow.deliveryRequest.endCustomer.id),"//
			+ "(select concat(b.numeroInvoice,'-',(select c.project.customer.abbreviation from Po c where c.id = a.stockRow.deliveryRequest.po.id)) from Po b where b.id = a.stockRow.deliveryRequest.po.id),"//
			+ "(select b.fullName from User b where b.username = a.stockRow.deliveryRequest.toUser.username),"//
			+ "a.stockRow.deliveryRequest.warehouse.name"//
			+ ") ";
	
	@Query(c1+"from DeliveryRequestExpiryDate a left join a.stockRow.deliveryRequest.warehouse as warehouse left join a.stockRow.deliveryRequest.company as company1 left join a.stockRow.inboundDeliveryRequest.company as company2 where (a.stockRow.deliveryRequest.project.manager.username = ?1 or a.stockRow.deliveryRequest.project.costcenter.lob.manager.username = ?1 or a.stockRow.deliveryRequest.project.costcenter.lob.bu.director.username = ?1 or warehouse.id in (?2) or a.stockRow.deliveryRequest.project.id in (?3)) and (company1.id = ?4 or company2.id = ?4) group by a.stockRow.deliveryRequest.id,a.stockRow.partNumber.id,a.expiryDate")
	List<DeliveryRequestExpiryDate> findDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);
	
}
