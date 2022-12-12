package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Currency;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.JobRequestStatus;

@Repository
public interface DeliveryRequestDetailRepos extends JpaRepository<DeliveryRequestDetail, Integer> {

	String usedQuantity3 = " COALESCE((select sum(b.installedQuantity) from JobRequestDeliveryDetail b where b.deliveryRequestDetail.id = a.id and b.jobRequest.status not in (?4)),0) ";
	String toUserFullName = "(select b.fullName from User b where b.username = a.deliveryRequest.toUser.username)";
	String deliverToCompanyName = "(select b.name from Company b where b.id = a.deliveryRequest.deliverToCompany.id)";
	String deliverToCustomerName = "(select b.name from Customer b where b.id = a.deliveryRequest.deliverToCustomer.id)";
	String deliverToSupplierName = "(select b.name from Supplier b where b.id = a.deliveryRequest.deliverToSupplier.id)";

	String c1 = "select new DeliveryRequestDetail(sum(a.quantity), a.status, a.originNumber, a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription, a.inboundDeliveryRequest,a.unitCost,a.costCurrency.id) ";
	String c2 = "select new DeliveryRequestDetail(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.unitCost,a.costCurrency.id) ";
	String c3 = "select new DeliveryRequestDetail(a.id,a.partNumber.id,a.partNumber.name,a.partNumber.image,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.reference,a.deliveryRequest.date1,a.quantity, "
			+ usedQuantity3 + ",a.deliveryRequest.deliverToCompanyType," + deliverToCompanyName + "," + deliverToCustomerName + "," + deliverToSupplierName + "," + toUserFullName
			+ ")";
	String c4 = "select new DeliveryRequestDetail(a.id,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.reference,a.unitCost,a.costCurrency.id,a.purchaseCost,a.purchaseCurrency.id,a.deliveryRequest.date4,a.deliveryRequest.project.name,(select b.numeroIbuy from Po b where b.id = a.deliveryRequest.po.id),(select b.date from Po b where b.id = a.deliveryRequest.po.id),(select b.currency.name from Po b where b.id = a.deliveryRequest.po.id),(select b.supplier.name from Po b where b.id = a.deliveryRequest.po.id)) ";

	String c8 = "select new DeliveryRequestDetail(sum(a.quantity),a.status,a.deliveryRequest.id,a.deliveryRequest.reference,a.deliveryRequest.type,a.deliveryRequest.neededDeliveryDate,a.deliveryRequest.project.name,a.deliveryRequest.project.subType,a.deliveryRequest.warehouse.name)";

	String c9 = "select new DeliveryRequestDetail(a.id,a.quantity,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.reference,a.deliveryRequest.status,a.deliveryRequest.neededDeliveryDate)";

	@Query(c1
			+ " from DeliveryRequestDetail a where a.deliveryRequest.project.id = ?1 and a.deliveryRequest.warehouse.id = ?2 and  a.deliveryRequest.type = ?3 and a.deliveryRequest.status in (?4) group by a.status, a.originNumber, a.partNumber.id, a.inboundDeliveryRequest.id")
	public List<DeliveryRequestDetail> findByProjectAndWarehouseAndTypeAndStatus(Integer projectId, Integer warehouseId, DeliveryRequestType outbound,
			List<DeliveryRequestStatus> status);

	@Query(c1
			+ " from DeliveryRequestDetail a where a.deliveryRequest.id != ?5 and a.deliveryRequest.project.id = ?1 and a.deliveryRequest.warehouse.id = ?2 and  a.deliveryRequest.type = ?3 and a.deliveryRequest.status in (?4) group by a.status, a.originNumber, a.partNumber.id, a.inboundDeliveryRequest.id")
	public List<DeliveryRequestDetail> findByProjectAndWarehouseAndTypeAndStatus(Integer projectId, Integer warehouseId, DeliveryRequestType outbound,
			List<DeliveryRequestStatus> status, Integer id);

	@Query(c1
			+ " from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status in (?2) and a.deliveryRequest.id != ?3  group by a.status, a.originNumber, a.partNumber.id, a.inboundDeliveryRequest.id")
	public List<DeliveryRequestDetail> findByOutboundDeliveryRequestReturnAndStatus(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> status, Integer id);

	@Query(c1
			+ " from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and a.deliveryRequest.status in (?2) and a.deliveryRequest.id != ?3  group by a.status, a.originNumber, a.partNumber.id, a.inboundDeliveryRequest.id")
	public List<DeliveryRequestDetail> findByOutboundDeliveryRequestTransferAndStatus(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> status, Integer id);

	@Query(c2 + " from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status in (?2) group by a.partNumber.id")
	public List<DeliveryRequestDetail> findByOutboundDeliveryRequestReturnAndStatus(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> status);

	@Query(c2 + " from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and a.deliveryRequest.status in (?2) group by a.partNumber.id")
	public List<DeliveryRequestDetail> findByOutboundDeliveryRequestTransferAndStatus(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> status);

	@Query("select new DeliveryRequestDetail(a.quantity,a.partNumber,a.deliveryRequest,a.packing) from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status not in (?2) ")
	public List<DeliveryRequestDetail> findReturnedDetailList(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> notInStatus);

	@Query("select new DeliveryRequestDetail(sum(a.quantity),a.partNumber) from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status not in (?2) group by a.partNumber.id")
	public List<DeliveryRequestDetail> findReturnedDetailListGroupByPartNumber(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> notInStatus);

	@Query("select count(*) from DeliveryRequestDetail a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status not in (?2) ")
	public Long countReturnedDetailList(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> status);

	@Query("select a.id from DeliveryRequestDetail a where  a.deliveryRequest.type = ?1 and a.partNumber.id =?2  and a.inboundDeliveryRequest.id = ?3 ")
	public List<Integer> findIdList(DeliveryRequestType type, Integer partNumberId, Integer inboundDeliveryRequestId);

	@Query("select a.id from DeliveryRequestDetail a where a.partNumber.id =?1 and a.deliveryRequest.outboundDeliveryRequestReturn.id = (select b.deliveryRequest.id from DeliveryRequestDetail b where b.id = ?2)")
	public List<Integer> findIdListByPartNumberAndOutboundDeliveryRequestReturn(Integer partNumberId, Integer outboundDeliveryRequestDetailId);

	@Query("select a.id from DeliveryRequestDetail a where a.partNumber.id =?1 and a.deliveryRequest.outboundDeliveryRequestTransfer.id = (select b.deliveryRequest.id from DeliveryRequestDetail b where b.id = ?2)")
	public List<Integer> findIdListByPartNumberAndOutboundDeliveryRequestTransfer(Integer partNumberId, Integer outboundDeliveryRequestDetailId);

	// UPDATE UNIT COST
	@Modifying
	@Query("update DeliveryRequestDetail set unitCost = ?2 where id = ?1 ")
	public void updateUnitCost(Integer id, Double unitCost);

	@Modifying
	@Query("update DeliveryRequestDetail set unitCost = ?2 where id in (?1) ")
	public void updateUnitCost(List<Integer> idList, Double unitCost);

	// UPDATE UNIT PRICE
	@Modifying
	@Query("update DeliveryRequestDetail set unitPrice = ?2 where id = ?1 ")
	public void updateUnitPrice(Integer id, Double unitPrice);

	@Modifying
	@Query("update DeliveryRequestDetail set purchaseCost = ?2 where id = ?1 ")
	public void updatePurchaseCost(Integer id, Double purchaseCost);

	@Modifying
	@Query("update DeliveryRequestDetail a set a.purchaseCurrency = ?2 where a.deliveryRequest.id = ?1 ")
	public void updatePurchaseCurrencyByDeliveryRequest(Integer deliveryRequestId, Currency purchaseCurrency);

	@Modifying
	@Query("update DeliveryRequestDetail a set a.priceCurrency = ?2 where a.deliveryRequest.id = ?1 ")
	public void updatePriceCurrencyByDeliveryRequest(Integer deliveryRequestId, Currency purchaseCurrency);

	@Modifying
	@Query("update DeliveryRequestDetail set purchaseCost = 0 where deliveryRequest.id = ?1 ")
	public void clearPurchaseCostByDeliveryRequest(Integer deliveryRequestId);

	@Modifying
	@Query("update DeliveryRequestDetail set unitPrice = ?2 where id in (?1) ")
	public void updateUnitPrice(List<Integer> idList, Double unitPrice);

	// reporting

//	String select3 = "select new DeliveryRequestDetail(a.id,a.partNumber.name,a.partNumber.description,a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.referenceNumber,a.quantity, " + usedQuantity3 + "," + toUserFullName + ")";

	@Query(c3
			+ "from DeliveryRequestDetail a where a.deliveryRequest.destinationProject.id = ?1 and a.deliveryRequest.type in (?2) and a.deliveryRequest.status in (?3) and a.quantity > "
			+ usedQuantity3)
	public List<DeliveryRequestDetail> findByDestinationProjectAndTypeAndStatus(Integer destinationProjectId, List<DeliveryRequestType> typeList,
			List<DeliveryRequestStatus> statusList, List<JobRequestStatus> notInJobRequestStatusList);

	@Query("select a.inboundDeliveryRequest.customer.id from DeliveryRequestDetail a where a.deliveryRequest.id = ?1 and a.inboundDeliveryRequest.customer is not null group by a.inboundDeliveryRequest.customer")
	public List<Integer> findOwnerCustomerList(Integer outboundDeliveryRequestId);

	@Query("select a.inboundDeliveryRequest.supplier.id from DeliveryRequestDetail a where a.deliveryRequest.id = ?1 and a.inboundDeliveryRequest.supplier is not null group by a.inboundDeliveryRequest.supplier")
	public List<Integer> findOwnerSupplierList(Integer outboundDeliveryRequestId);

	@Query("select a.inboundDeliveryRequest.company.id from DeliveryRequestDetail a where a.deliveryRequest.id = ?1 and a.inboundDeliveryRequest.company is not null group by a.inboundDeliveryRequest.company")
	public List<Integer> findOwnerCompanyList(Integer outboundDeliveryRequestId);

	@Query(c4
			+ " from DeliveryRequestDetail a where a.partNumber.id = ?1 and a.deliveryRequest.type = ?2 and a.deliveryRequest.inboundType = ?3 and a.deliveryRequest.company.id = ?4 and a.deliveryRequest.status in (?5) order by a.deliveryRequest.date4 desc")
	public List<DeliveryRequestDetail> findByPartNumberAndDeliveryRequestTypeAndCompany(Integer partNumberId, DeliveryRequestType deliveryRequestType, InboundType inboundType,
			Integer companyId, List<DeliveryRequestStatus> deliveryRequestStatus);

	@Query("select new DeliveryRequestDetail(0.0,sum(a.totalQuantity-a.totalUsedQuantity),a.partNumber) from Boq a where a.podetails.po.id = ?1 and a.totalQuantity > a.totalUsedQuantity group by a.partNumber")
	public List<DeliveryRequestDetail> findRemainingByPo(Integer poId);

	// EFFECTIVE STOCK
	String from1 = " from DeliveryRequestDetail a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.company as company1 left join a.inboundDeliveryRequest.company as company2 ";
	String from2 = " from DeliveryRequestDetail a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.customer as customer1 left join a.inboundDeliveryRequest.customer as customer2 ";
	String usernameCondition = " (a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?1 or warehouse.id in (?2) or a.deliveryRequest.project.id in (?3)) ";
	String companyCondition = " (company1.id = ?4 or company2.id = ?4 ) ";
	String customerCondition = " (customer1.id = ?4 or customer2.id = ?4 ) ";

//	@Query("select sum(a.quantity) " + from1 + " where  " + usernameCondition + " and " + companyCondition + "  and a.partNumber.id = ?5 and a.deliveryRequest.type = ?6 and a.deliveryRequest.status in (?7)")
//	public Double findPendingQuantityByCompanyOwnerAnPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> statusList);

//	@Query("select sum(a.quantity) " + from2 + " where  " + usernameCondition + " and " + customerCondition + "  and a.partNumber.id = ?5 and a.deliveryRequest.type = ?6 and a.deliveryRequest.status in (?7)")
//	public Double findPendingQuantityByCustomerOwnerAnPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> statusList);

	@Query("select a.partNumber.id,sum(a.quantity) " + from1 + " where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.type = ?5 and a.deliveryRequest.status in (?6) and a.deliveryRequest.project.subType = 'Stock' group by a.partNumber.id")
	public List<Object[]> findPendingQuantityByCompanyOwnerAndProjectSubTypeStockGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList,
			Integer companyId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> statusList);

	@Query("select a.partNumber.id,sum(a.quantity) " + from2 + " where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.type = ?5 and a.deliveryRequest.status in (?6) group by a.partNumber.id")
	public List<Object[]> findPendingQuantityByCustomerOwnerGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> statusList);

	@Query("select sum(a.quantity) " + from1 + " where  a.deliveryRequest.project.id = ?6 and " + usernameCondition + " and " + companyCondition
			+ "  and a.partNumber.id = ?5 and  a.deliveryRequest.type = ?7 and a.deliveryRequest.status in (?8)")
	public Double findPendingQuantityByCompanyOwnerAnPartNumberAndProject(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			Integer partNumberId, Integer projectId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> statusList);

	@Query("select sum(a.quantity) " + from2 + " where  a.deliveryRequest.project.id = ?6 and " + usernameCondition + " and " + customerCondition
			+ "  and a.partNumber.id = ?5 and a.deliveryRequest.type = ?7 and a.deliveryRequest.status in (?8)")
	public Double findPendingQuantityByCustomerOwnerAnPartNumberAndProject(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			Integer partNumberId, Integer projectId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> statusList);

	@Query(c4
			+ " from DeliveryRequestDetail a where a.partNumber.id = ?1 and a.deliveryRequest.type = ?2 and a.deliveryRequest.project.type = 'Stock' and a.deliveryRequest.project.costcenter.lob.bu.company.id = ?3 and a.deliveryRequest.status in (?4)")
	public List<DeliveryRequestDetail> findByPartNumberAndTypeAndProjectTypeStockAndProjectCompanyAndDeliveryRequestStatus(Integer partNumberId,
			DeliveryRequestType deliveryRequestType, Integer companyId, List<DeliveryRequestStatus> deliveryRequestStatus);

	@Query("from DeliveryRequestDetail a where a.deliveryRequest.id = ?1 and a.partNumber.id = ?2")
	public List<DeliveryRequestDetail> findByDeliveryRequestAndPartNumber(Integer deliveryRequestId, Integer partNumberId);

	@Modifying
	@Query("update DeliveryRequestDetail a set a.unitPrice = ?1 where a.partNumber.id = ?2 and a.deliveryRequest.id in (select distinct b.id from DeliveryRequest b where b.outboundDeliveryRequestReturn.id = ?3)")
	void updateUnitPriceByPartNumberAndOutboundDeliveryRequestReturn(Double unitPrice, Integer partNumberId, Integer outboundDeliveryRequestReturnId);

	@Query(c8 + from1 + "where" + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.type = ?5 and a.partNumber.id = ?6 and a.deliveryRequest.date4 is null and a.deliveryRequest.status not in ('REJECTED','CANCELED') group by a.deliveryRequest.id,a.status")
	List<DeliveryRequestDetail> findByCompanyOwnerAndPartNumberAndNotDelivered(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			DeliveryRequestType type, Integer partNumberId);

	@Query("select a.partNumber.id,sum(a.quantity) " + from1 + "where" + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.type = 'INBOUND'and a.deliveryRequest.date4 is null and a.deliveryRequest.status not in ('REJECTED','CANCELED') group by a.partNumber.id")
	List<Object[]> findForecastQuantityGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c9
			+ " from DeliveryRequestDetail a where  a.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and a.deliveryRequest.date4 is null and a.deliveryRequest.status not in ('REJECTED','CANCELED')")
	List<DeliveryRequestDetail> findTransferredAndPendingDetailList(Integer outboundDeliveryRequestId);

	@Query(c9
			+ " from DeliveryRequestDetail a where  a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.date4 is null and a.deliveryRequest.status not in ('REJECTED','CANCELED')")
	List<DeliveryRequestDetail> findReturnedAndPendingDetailList(Integer outboundDeliveryRequestId);

	// PN Reporting quantities
	@Query("select sum(a.quantity)" + from1 + " where  " + usernameCondition + " and " + companyCondition
			+ " and a.partNumber.id = ?5 and a.deliveryRequest.type = ?6 and a.deliveryRequest.status in (?7)")
	Double findPendingByCompanyOwnerAndPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			Integer partNumberId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> deliveryRequestStatusList);
	
	@Query("select sum(a.quantity)" + from1 + " where  " + usernameCondition + " and " + companyCondition
			+ " and a.partNumber.id = ?5 and a.deliveryRequest.type = ?6 and a.deliveryRequest.status in (?7) and a.deliveryRequest.project.subType = 'Stock'")
	Double findPendingStockByCompanyOwnerAndPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			Integer partNumberId, DeliveryRequestType deliveryRequestType, List<DeliveryRequestStatus> deliveryRequestStatusList);
}
