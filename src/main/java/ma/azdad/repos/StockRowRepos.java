package ma.azdad.repos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CompanyType;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.Location;
import ma.azdad.model.Project;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowStatus;
import ma.azdad.model.Warehouse;

@Repository
public interface StockRowRepos extends JpaRepository<StockRow, Integer> {

	String brandName = "(select b.name from PartNumberBrand b where b.id = a.partNumber.brand.id)";
	String externalRequesterFullName = "(select b.fullName from User b where b.username = a.deliveryRequest.externalRequester.username)";
	String destinationName = "(select b.name from Site b where b.id = a.deliveryRequest.destination.id)";
	String originName = "(select b.name from Site b where b.id = a.deliveryRequest.origin.id)";
	String projectName = "(select b.name from Project b where b.id = a.deliveryRequest.project.id)";
	String destinationProjectName = "(select b.name from Project b where b.id = a.deliveryRequest.destinationProject.id)";
	String destinationProjectCustomerName = "(select b.name from Customer b where b.id = (select c.customer.id from Project c where c.id = a.deliveryRequest.destinationProject.id))";
	String deliverToCompanyName = "(select b.name from Company b where b.id = a.deliveryRequest.deliverToCompany.id)";
	String deliverToCustomerName = "(select b.name from Customer b where b.id = a.deliveryRequest.deliverToCustomer.id)";
	String deliverToSupplierName = "(select b.name from Supplier b where b.id = a.deliveryRequest.deliverToSupplier.id)";
	String toUserFullName = "(select b.fullName from User b where b.username = a.deliveryRequest.toUser.username)";
	String toUserEmail = "(select b.email from User b where b.username = a.deliveryRequest.toUser.username)";
	String toUserPhone = "(select b.phone from User b where b.username = a.deliveryRequest.toUser.username)";
	String toUserCin = "(select b.cin from User b where b.username = a.deliveryRequest.toUser.username)";

	String poNumero = "(select concat(b.numeroInvoice,'-',(select c.project.customer.abbreviation from Po c where c.id = a.deliveryRequest.po.id)) from Po b where b.id = a.deliveryRequest.po.id)";
	String inboundPoNumero1 = "(select concat(b.numero,'-',(select c.supplier.name from Po c where c.id = a.deliveryRequest.inboundPo.id)) from Po b where b.id = a.deliveryRequest.inboundPo.id)";
	String inboundPoNumero2 = "(select concat(b.numero,'-',(select c.supplier.name from Po c where c.id = a.inboundDeliveryRequest.po.id)) from Po b where b.id = a.inboundDeliveryRequest.po.id)";
	String endCustomerName = "(select b.name from Customer b where b.id = a.deliveryRequest.endCustomer.id)";

	String c1 = "select new StockRow(a.id,a.quantity,a.status,a.partNumber,a.inboundDeliveryRequest,a.location) ";
	String c2 = "select new StockRow(sum(a.quantity),a.status, a.originNumber, a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription, a.inboundDeliveryRequest,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.location,a.packing) ";
	String c3 = "select new StockRow(sum(a.quantity),a.deliveryRequest,a.status,a.originNumber,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.inboundDeliveryRequest,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.location)";
	String c4 = "select new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,sum(case when a.quantity > 0 then a.quantity else 0 end),sum(case when a.quantity < 0 then a.quantity else 0 end))";
	String c5 = "select  new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.deliveryRequest) ";
	String c6 = " select new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.image,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription) ";
	String c7 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.location) ";
	String c8 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest) ";
	String c9 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.inboundDeliveryRequest,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription) ";
//	String c10 = " select new StockRow(sum(a.quantity),a.inboundDeliveryRequest,a.partNumber.id,a.partNumber.name,a.partNumber.image,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription) ";
	String c10 = " select new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.image,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.inboundDeliveryRequest.id,a.inboundDeliveryRequest.reference,a.inboundDeliveryRequest.type,a.inboundDeliveryRequest.date4,a.inboundDeliveryRequest.approximativeStoragePeriod,a.inboundDeliveryRequest.project.name,a.inboundDeliveryRequest.warehouse.name) ";
	String c12 = "select new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.status,a.inboundDeliveryRequest.date4,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,sum(a.deliveryRequestDetail.unitCost*a.quantity))";
	String c13 = " select new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.project.id,a.deliveryRequest.project.name,destinationProject.customer.id) ";
	String c15 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.inboundDeliveryRequest,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,sum(a.deliveryRequestDetail.unitCost*a.quantity),a.deliveryRequest.deliverToCompanyType,(select b.name from Company b where a.deliveryRequest.deliverToCompany.id = b.id),(select b.name from Customer b where a.deliveryRequest.deliverToCustomer.id = b.id),(select b.name from Supplier b where a.deliveryRequest.deliverToSupplier.id = b.id),a.deliveryRequest.deliverToOther) ";
	String c16 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.inboundDeliveryRequest) ";
	// mobile
	String cm1 = " select new ma.azdad.mobile.model.StockRow(a.id,a.partNumber.name,a.partNumber.image,a.location,a.status,a.state,a.partNumber.description,sum(a.quantity),"
			+ "a.deliveryRequest.project.name,a.deliveryRequest.warehouse.name,sum(case when a.quantity > 0 then a.quantity else 0 end),sum(case when a.quantity < 0 then a.quantity else 0 end),"
			+ "a.deliveryRequest.reference,a.deliveryRequest.id,a.creationDate) ";

	String drd_c5 = "select new DeliveryRequestDetail(sum(a.quantity),a.status,a.originNumber,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.inboundDeliveryRequest,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.packing) ";
	String drd_c6 = "select new DeliveryRequestDetail(sum(-a.quantity) - COALESCE((select sum(b.quantity) from StockRow b where b.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and b.partNumber.id = a.partNumber.id),0),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.deliveryRequestDetail.unitPrice,a.deliveryRequestDetail.priceCurrency.id,a.inboundDeliveryRequest.ownerType,a.inboundDeliveryRequest.company.id,a.inboundDeliveryRequest.customer.id,a.inboundDeliveryRequest.supplier.id) ";
	String drd_c7 = "select new DeliveryRequestDetail(sum(-a.quantity) - COALESCE((select sum(b.quantity) from StockRow b where b.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and b.partNumber.id = a.partNumber.id),0),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.deliveryRequestDetail.unitPrice,a.deliveryRequestDetail.priceCurrency.id,a.inboundDeliveryRequest.ownerType,a.inboundDeliveryRequest.company.id,a.inboundDeliveryRequest.customer.id,a.inboundDeliveryRequest.supplier.id) ";

	String c22 = "select new StockRow(sum(a.quantity),a.status,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.deliveryRequestDetail.unitPrice,a.originNumber,a.deliveryRequestDetail.priceCurrency.id,a.deliveryRequest.project.name,a.deliveryRequest.warehouse.name,a.partNumber.id,a.partNumber.name,a.partNumber.image,a.partNumber.description,"
			+ brandName
			+ ",a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.inboundType,a.deliveryRequest.reference,a.deliveryRequest.smsRef,a.deliveryRequest.date4,a.deliveryRequest.sdm,a.deliveryRequest.ism,a.deliveryRequest.outboundType,a.deliveryRequest.returnReason,"
			+ destinationProjectCustomerName + "," + destinationName + "," + originName + "," + destinationProjectName + ",a.deliveryRequest.deliverToCompanyType ," + deliverToCompanyName + " ,"
			+ deliverToCustomerName + "," + deliverToSupplierName + ",a.deliveryRequest.deliverToOther," + toUserFullName + "," + poNumero + "," + inboundPoNumero1 + "," + endCustomerName + ") ";
	String c23 = "select new StockRow(sum(a.quantity),a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription,a.status,a.inboundDeliveryRequest.date4,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,sum(a.deliveryRequestDetail.unitCost*a.quantity),a.deliveryRequest.project.name)";

	String c24 = "select new StockRow(sum(a.quantity),a.status,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id," //
			+ "a.deliveryRequestDetail.unitPrice,a.deliveryRequestDetail.priceCurrency.id,a.deliveryRequest.project.name,a.deliveryRequest.warehouse.name,"//
			+ "a.partNumber.id,a.partNumber.name,a.partNumber.image,a.partNumber.description," + brandName
			+ ",a.deliveryRequest.id,a.deliveryRequest.type,a.deliveryRequest.inboundType,a.deliveryRequest.reference,a.deliveryRequest.smsRef,a.deliveryRequest.date4,"//
			+ "a.deliveryRequest.sdm,a.inboundDeliveryRequest.id,a.inboundDeliveryRequest.reference,a.inboundDeliveryRequest.originNumber," + destinationProjectCustomerName + "," + destinationName
			+ "," + originName + "," + destinationProjectName + ",a.deliveryRequest.deliverToCompanyType ," + deliverToCompanyName + " ," + deliverToCustomerName + "," + deliverToSupplierName
			+ ",a.deliveryRequest.deliverToOther," + toUserFullName + "," + toUserEmail + "," + toUserPhone + "," + toUserCin + "," + poNumero + ",a.inboundDeliveryRequest.po.id," + inboundPoNumero2
			+ "," + endCustomerName + ") ";
	
	
	String c25 = "select new StockRow(sum(a.quantity),a.status,a.state,a.creationDate,a.deliveryRequest.type,a.partNumber.id,a.partNumber.name,a.partNumber.description,a.location.id,a.location.name) ";
	String c26 = "select new StockRow(a.quantity,a.status, a.originNumber, a.partNumber.id,a.partNumber.name,a.partNumber.description,a.partNumber.industryName,a.partNumber.categoryName,a.partNumber.typeName,a.partNumber.brandName,a.partNumber.internalPartNumberName,a.partNumber.internalPartNumberDescription, a.inboundDeliveryRequest,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.costCurrency.id,a.location,a.packing) ";

	@Query("from StockRow a where (a.deliveryRequest.requester.username = ?1 or a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.warehouse.id in (?2) or a.deliveryRequest.project.id in (?3))")
	public List<StockRow> findByResource(String username, List<Integer> warehouseList, List<Integer> assignedProjectList);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Query(drd_c5
			+ " from StockRow a where a.deliveryRequest.project.id = ?1 and a.deliveryRequest.warehouse.id = ?2 group by a.status,a.originNumber,a.partNumber.id,a.inboundDeliveryRequest.id having sum(a.quantity) != 0")
	public List<DeliveryRequestDetail> findRemainingByProjectAndWarehouse(Integer projectId, Integer warehouseId);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	String select2 = "select new StockRow(sum(a.quantity),a.status, a.originNumber, a.partNumber, a.inboundDeliveryRequest,a.deliveryRequestDetail.unitCost,a.location,a.packing) ";

//	@Query(c2
//			+ " from StockRow a where a.deliveryRequest.project.id = ?1 and a.deliveryRequest.warehouse.id = ?2 and a.partNumber.id = ?3 and a.status = ?4 and a.originNumber = ?5 and a.inboundDeliveryRequest.id = ?6 group by a.location.id having sum(a.quantity) != 0 order by sum(a.quantity) ")
//	public List<StockRow> findRemainingToPrepare(Integer projectId, Integer warehouseId, Integer partNumberId, StockRowStatus status, String originNumber, Integer inboundDeliveryRequestId);

	@Query(c2 + "from StockRow a where a.inboundDeliveryRequestDetail.id = ?1 and a.status = ?2 group by a.location.id having sum(a.quantity) != 0 order by sum(a.quantity)")
	public List<StockRow> findRemainingToPrepare(Integer inboundDeliveryRequestDetailId, StockRowStatus status);
	
	@Query(c26 + "from StockRow a where a.inboundDeliveryRequestDetail.id = ?1 and a.status = ?2 order by a.creationDate")
	public List<StockRow> findByInboundDeliveryRequestDetailAndStatus(Integer inboundDeliveryRequestDetailId, StockRowStatus status);

//	// FIFO no sense : we know already inbound delivery date
//	@Query(c2 + "from StockRow a where a.inboundDeliveryRequestDetail.id = ?1 and a.status = ?2 group by a.location.id, having sum(a.quantity) != 0 order by a.inboundDeliveryRequestDetail.deliveryRequest.date4,sum(a.quantity)")
//	public List<StockRow> findRemainingToPrepareMethod1(Integer inboundDeliveryRequestDetailId, StockRowStatus status);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	String select3 = "select new StockRow(sum(a.quantity),a.deliveryRequest,a.status,a.originNumber,a.partNumber,a.inboundDeliveryRequest,a.deliveryRequestDetail.unitCost,a.location)";

	@Query(c3 + "from StockRow a where a.deliveryRequest.id = ?1 group by a.status,a.partNumber.id,a.location.id")
	public List<StockRow> findByDeliveryRequest(Integer deliveryRequestId);

	@Query(c3
			+ " from StockRow a where (a.deliveryRequest.requester.username = ?1 or a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.warehouse.id in (?2) or a.deliveryRequest.project.id in (?3)) group by a.status,a.partNumber.id,a.location.id having sum(a.quantity) != 0")
	public List<StockRow> getStockSituationByResource(String username, List<Integer> warehouseList, List<Integer> assignedProjectList);

//	@Query(c3 + " from StockRow a where a.inboundDeliveryRequest.id = ?1 group by a.status,a.partNumber.id,a.location.id having sum(a.quantity) != 0")
//	public List<StockRow> getStockSituationByInboundDeliveryRequest(Integer deliveryRequestId);
	
	@Query(c25 + " from StockRow a where a.inboundDeliveryRequest.id = ?1 group by a.status,a.partNumber.id,a.location.id,a.deliveryRequest.type,date(a.creationDate) order by a.creationDate")
	public List<StockRow> getStockSituationByInboundDeliveryRequest(Integer deliveryRequestId);

	@Query(cm1 + " from StockRow a where a.inboundDeliveryRequest.id = ?1 group by a.status,a.partNumber.id,a.location.id having sum(a.quantity) != 0")
	public List<ma.azdad.mobile.model.StockRow> getStockSituationByInboundDeliveryRequestMobile(Integer deliveryRequestId);
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Query(c4 + " from StockRow a where a.inboundDeliveryRequest.id = ?1 group by a.partNumber.id")
	public List<StockRow> findByInboundDeliveryRequest(Integer deliveryRequestId);

	@Query(cm1 + " from StockRow a where a.inboundDeliveryRequest.id = ?1 group by a.partNumber.id")
	public List<ma.azdad.mobile.model.StockRow> findByInboundDeliveryRequestMobile(Integer deliveryRequestId);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Query(c5 + "from StockRow a where a.inboundDeliveryRequest.id = ?1 and a.quantity < 0 group by a.partNumber.id,a.deliveryRequest.id")
	public List<StockRow> findAttachedOutboundDeliveryRequestList(Integer deliveryRequestId);

	@Query(cm1 + "from StockRow a where a.inboundDeliveryRequest.id = ?1 and a.quantity < 0 group by a.partNumber.id,a.deliveryRequest.id")
	public List<ma.azdad.mobile.model.StockRow> findAttachedOutboundDeliveryRequestListMobile(Integer deliveryRequestId);

	// UPDATE UNIT COST
//	@Modifying
//	@Query("update StockRow set unitCost = ?2 where id = ?1 ")
//	public void updateUnitCost(Integer id, Double unitCost);

//	@Modifying
//	@Query("update StockRow set unitCost = ?2 where id in (?1) ")
//	public void updateUnitCost(List<Integer> idList, Double unitCost);

	// UPDATE UNIT COST
//	@Modifying
//	@Query("update StockRow set unitPrice = ?2 where id = ?1 ")
//	public void updateUnitPrice(Integer id, Double unitPrice);

//	@Modifying
//	@Query("update StockRow set unitPrice = ?2 where id in (?1) ")
//	public void updateUnitPrice(List<Integer> idList, Double unitPrice);

	@Query("select a.id from StockRow a where a.partNumber.id =?1  and a.inboundDeliveryRequest.id = ?2 ")
	public List<Integer> findIdListByPartNumberAndInboundDeliveryRequest(Integer partNumberId, Integer inboundDeliveryRequestId);

	@Query("select a.id from StockRow a where a.partNumber.id =?1  and a.deliveryRequest.id = ?2 ")
	public List<Integer> findIdListByPartNumberAndDeliveryRequest(Integer partNumberId, Integer deliveryRequestId);

	@Query("select a.id from StockRow a where a.deliveryRequest.type = ?1 and  a.deliveryRequest.id = ?2 and a.partNumber.id =?3")
	public List<Integer> findIdList(DeliveryRequestType type, Integer deliveryRequestId, Integer partNumberId);

	// UPDATE LOCATION

	@Modifying
	@Query("update StockRow a set a.location = ?1 where a.inboundDeliveryRequest.id = ?2 and a.partNumber.id = ?3 and a.location.id = ?4 and a.status = ?5")
	public void updateLocation(Location newLocation, Integer inboundDeliveryRequestId, Integer partNumberId, Integer LocationId, StockRowStatus status);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// REPORTING
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	String from1 = " from StockRow a left join a.deliveryRequest.warehouse as warehouse ";
	String from2 = " from StockRow a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.company as company1 left join a.inboundDeliveryRequest.company as company2 ";
	String from3 = " from StockRow a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.customer as customer1 left join a.inboundDeliveryRequest.customer as customer2  ";
	String from4 = " from StockRow a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.supplier as supplier1 left join a.inboundDeliveryRequest.supplier as supplier2  ";
//	String from2Test = " from StockRow a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.company as company1 left join a.inboundDeliveryRequest.company as company2 left join a.partNumber as partNumber";
	String usernameCondition = " (a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?1 or warehouse.id in (?2) or a.deliveryRequest.project.id in (?3)) ";
	String companyCondition = " (company1.id = ?4 or company2.id = ?4) ";
	String companyConditionNew = " (a.companyId = ?4 or a.inboundCompanyId = ?4) ";
	String customerCondition = " (customer1.id = ?4 or customer2.id = ?4 ) ";
//	String select6 = " select new StockRow(sum(a.quantity),a.partNumber) ";
//	String select7 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.location) ";
//	String select8 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest) ";
//	String select9 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.inboundDeliveryRequest,a.partNumber) ";
//	String select10 = " select new StockRow(sum(a.quantity),a.inboundDeliveryRequest,a.partNumber) ";
//	String select15 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.inboundDeliveryRequest,a.partNumber,sum(a.deliveryRequestDetail.unitCost*a.quantity),(select b.name from Company b where a.deliveryRequest.deliverToCompany.id = b.id),(select b.name from Customer b where a.deliveryRequest.deliverToCustomer.id = b.id),(select b.name from Supplier b where a.deliveryRequest.deliverToSupplier.id = b.id),a.deliveryRequest.deliverToOther) ";
//	String select17 = " select new StockRow(sum(a.quantity),a.status,a.deliveryRequest,a.inboundDeliveryRequest) ";
	String projectCondition = "a.deliveryRequest.project.id = ?5";
	String warehouseCondition = "a.deliveryRequest.warehouse.id = ?5";
	String destinationCondition = "a.deliveryRequest.destination.id = ?5";
	String deliverToEntityCondition = " (?5 = (select b.name from Company b where a.deliveryRequest.deliverToCompany.id = b.id) or ?5 = (select b.name from Customer b where a.deliveryRequest.deliverToCustomer.id = b.id) or ?5 = (select b.name from Supplier b where a.deliveryRequest.deliverToSupplier.id = b.id) or ?5 = a.deliveryRequest.deliverToOther) ";
	String externalRequesterCondition = "a.deliveryRequest.externalRequester.id = ?5";
	String poCondition = "a.inboundDeliveryRequest.outboundDeliveryRequestTransfer.po.id = ?5";
	String destinationProjectCondition = "a.deliveryRequest.destinationProject.id = ?5";
	String yearCondition = "year(a.deliveryRequest.date4) = ?5";
	String yearAndMonthCondition = "concat(MONTHNAME(a.deliveryRequest.date4),'-',year(a.deliveryRequest.date4)) = ?5";

	@Query("select a.deliveryRequest.company.id " + from1 + "  where a.deliveryRequest.company is not null and " + usernameCondition + " group by a.deliveryRequest.company.id")
	public List<Integer> findCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList);

	@Query("select a.deliveryRequest.customer.id " + from1 + "  where a.deliveryRequest.customer is not null and " + usernameCondition + " group by a.deliveryRequest.customer.id")
	public List<Integer> f(String username, List<Integer> warehouseList, List<Integer> assignedProjectList);

	@Query(c6 + from2 + " where " + usernameCondition + " and " + companyCondition + "  group by a.partNumber.id")
	public List<StockRow> findByCompanyOwnerAndGroupByPartNumber2(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c6 + from3 + " where " + usernameCondition + " and " + customerCondition + "  group by a.partNumber.id")
	public List<StockRow> findByCustomerOwnerAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query(c6 + from3 + " where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2)  group by a.partNumber.id")
	public List<StockRow> findByCustomerOwnerAndGroupByPartNumber(Integer customerId, List<Integer> projectIdList);

	@Query(c6 + from4 + " where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2)  group by a.partNumber.id")
	public List<StockRow> findBySupplierOwnerAndGroupByPartNumber(Integer supplierId, List<Integer> projectIdList);

	@Query(c6 + from2 + " where " + usernameCondition + " and " + companyCondition + " and " + projectCondition + "  group by a.partNumber.id")
	public List<StockRow> findByCompanyOwnerAndProjectAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query(c6 + from3 + " where " + usernameCondition + " and " + customerCondition + " and " + projectCondition + "  group by a.partNumber.id")
	public List<StockRow> findByCustomerOwnerAndProjectAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	@Query(c6 + from3 + " where (customer1.id = ?1 or customer2.id = ?2) and a.deliveryRequest.project.id = ?2  group by a.partNumber.id")
	public List<StockRow> findByCustomerOwnerAndProjectAndGroupByPartNumber(Integer customerId, Integer projectId);

	@Query(c6 + from4 + " where (supplier1.id = ?1 or supplier2.id = ?2) and a.deliveryRequest.project.id = ?2  group by a.partNumber.id")
	public List<StockRow> findBySupplierOwnerAndProjectAndGroupByPartNumber(Integer supplierId, Integer projectId);

	@Query(c6 + from2 + " where " + usernameCondition + " and " + companyCondition + " and " + warehouseCondition + "  group by a.partNumber.id")
	public List<StockRow> findByCompanyOwnerAndWarehouseAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer warehouseId);

	@Query(c6 + from3 + " where " + usernameCondition + " and " + customerCondition + " and " + warehouseCondition + "  group by a.partNumber.id")
	public List<StockRow> findByCustomerOwnerAndWarehouseAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer warehouseId);

	@Query(c6 + from3 + " where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.deliveryRequest.warehouse.id = ?3 group by a.partNumber.id")
	public List<StockRow> findByCustomerOwnerAndWarehouseAndGroupByPartNumber(Integer customerId, List<Integer> projectIdList, Integer warehouseId);

	@Query(c6 + from4 + " where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.deliveryRequest.warehouse.id = ?3 group by a.partNumber.id")
	public List<StockRow> findBySupplierOwnerAndWarehouseAndGroupByPartNumber(Integer supplierId, List<Integer> projectIdList, Integer warehouseId);

	@Query(c7 + from2 + " where " + usernameCondition + " and " + companyCondition
			+ " and a.partNumber.id = ?5  group by a.deliveryRequest.project.id,a.deliveryRequest.warehouse.id,a.status,a.location.id having sum(a.quantity) != 0")
	public List<StockRow> findCurrentStockByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId);

	@Query(c7 + from3 + " where " + usernameCondition + " and " + customerCondition
			+ " and a.partNumber.id = ?5  group by a.deliveryRequest.project.id,a.deliveryRequest.warehouse.id,a.status,a.location.id having sum(a.quantity) != 0")
	public List<StockRow> findCurrentStockByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId);

	@Query(c7 + from3
			+ " where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.partNumber.id = ?3  group by a.deliveryRequest.project.id,a.deliveryRequest.warehouse.id,a.status,a.location.id having sum(a.quantity) != 0")
	public List<StockRow> findCurrentStockByPartNumberAndCustomerOwner(Integer customerId, List<Integer> projectIdList, Integer partNumberId);

	@Query(c7 + from4
			+ " where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.partNumber.id = ?3  group by a.deliveryRequest.project.id,a.deliveryRequest.warehouse.id,a.status,a.location.id having sum(a.quantity) != 0")
	public List<StockRow> findCurrentStockByPartNumberAndSupplierOwner(Integer supplierId, List<Integer> projectIdList, Integer partNumberId);

	@Query(c16 + from2 + " where " + usernameCondition + " and " + companyCondition + " and a.partNumber.id = ?5 group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId);

	@Query(c9 + from2 + " where " + usernameCondition + " and " + companyCondition
			+ " and a.partNumber.id = ?5 and a.deliveryRequest.project.id = ?6 and a.deliveryRequest.type = ?7  group by a.deliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId,
			Integer pojectId, DeliveryRequestType outbound);

	@Query(c16 + from3 + " where " + usernameCondition + " and " + customerCondition + " and a.partNumber.id = ?5  group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId);

	@Query(c16 + from3
			+ " where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.partNumber.id = ?3  group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(Integer customerId, List<Integer> projectIdList, Integer partNumberId);

	@Query(c16 + from4
			+ " where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.partNumber.id = ?3  group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndSupplierOwner(Integer supplierId, List<Integer> projectIdList, Integer partNumberId);

	@Query(c9 + from3 + " where " + usernameCondition + " and " + customerCondition
			+ " and a.partNumber.id = ?5 and a.deliveryRequest.project.id = ?6 and a.deliveryRequest.type = ?7  group by a.deliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId,
			Integer pojectId, DeliveryRequestType outbound);

	@Query(c9 + from2 + " where " + usernameCondition + " and " + companyCondition + " and a.deliveryRequest.project.id = ?5 and a.quantity < 0  group by a.partNumber.id,a.status")
	public List<StockRow> findStockHistoryByCompanyOwnerGroupByPartNumberAndStatus(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			Integer projectId);

	@Query(c9 + from3 + " where " + usernameCondition + " and " + customerCondition + " and a.deliveryRequest.project.id = ?5 and a.quantity < 0  group by a.partNumber.id,a.status")
	public List<StockRow> findStockHistoryByCustomerOwnerGroupByPartNumberAndStatus(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			Integer projectId);

	@Query(c9 + from2 + " where " + usernameCondition + " and " + companyCondition + " and " + projectCondition + "  group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByProjectAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query(c9 + from3 + " where " + usernameCondition + " and " + customerCondition + " and " + projectCondition + "  group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByProjectAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	@Query(c9 + from3 + " where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id = ?2 group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByProjectAndCustomerOwner(Integer customerId, Integer projectId);

	@Query(c9 + from4 + " where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id = ?2 group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByProjectAndSupplierOwner(Integer supplierId, Integer projectId);

	@Query(c9 + from2 + " where " + usernameCondition + " and " + companyCondition + " and " + warehouseCondition + "  group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByWarehouseAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer warehouseId);

	@Query(c9 + from3 + " where " + usernameCondition + " and " + customerCondition + " and " + warehouseCondition + "  group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByWarehouseAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer warehouseId);

	@Query(c9 + from3
			+ " where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.deliveryRequest.warehouse.id = ?3 group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByWarehouseAndCustomerOwner(Integer customerId, List<Integer> projectIdList, Integer warehouseId);

	@Query(c9 + from4
			+ " where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2) and a.deliveryRequest.warehouse.id = ?3 group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByWarehouseAndSupplierOwner(Integer supplierId, List<Integer> projectIdList, Integer warehouseId);

	@Query(c9 + from1 + " where " + usernameCondition
			+ " and  a.inboundDeliveryRequest.company.id = ?4 and a.inboundDeliveryRequest.project.type = ?5 and a.deliveryRequest.destinationProject.customer.id = ?6"
			+ " and a.quantity < 0  group by a.deliveryRequest.id,a.status,a.partNumber.id order by a.deliveryRequest.date4")
	public List<StockRow> findStockHistoryByDestinationCustomerAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			String projectTypeStock, Integer customerId);

	@Query("select a.deliveryRequest.project.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition + " group by a.deliveryRequest.project.id")
	public List<Integer> findProjectIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,true) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " group by a.deliveryRequest.project.id having sum(a.quantity) > 0")
	public List<Project> findProjectListByCompanyOwnerAndHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,false) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " group by a.deliveryRequest.project.id having sum(a.quantity) = 0")
	public List<Project> findProjectListByCompanyOwnerAndNotHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select a.deliveryRequest.project.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.project.type = ?5   group by a.deliveryRequest.project.id")
	public List<Integer> findProjectIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, String projectType);

	@Query("select a.deliveryRequest.project.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition + " group by a.deliveryRequest.project.id")
	public List<Integer> findProjectIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,true) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " group by a.deliveryRequest.project.id having sum(a.quantity) > 0")
	public List<Project> findProjectListByCustomerOwnerAndHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,false) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " group by a.deliveryRequest.project.id having sum(a.quantity) = 0")
	public List<Project> findProjectListByCustomerOwnerAndNotHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,true) " + from3
			+ "where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2) group by a.deliveryRequest.project.id having sum(a.quantity) > 0")
	public List<Project> findProjectListByCustomerOwnerAndHavingStock(Integer customerId, List<Integer> projectIdList);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,false) " + from3
			+ "where (customer1.id = ?1 or customer2.id = ?1) and a.deliveryRequest.project.id in (?2) group by a.deliveryRequest.project.id having sum(a.quantity) = 0")
	public List<Project> findProjectListByCustomerOwnerAndNotHavingStock(Integer customerId, List<Integer> projectIdList);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,true) " + from4
			+ "where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2) group by a.deliveryRequest.project.id having sum(a.quantity) > 0")
	public List<Project> findProjectListBySupplierOwnerAndHavingStock(Integer supplierId, List<Integer> projectIdList);

	@Query("select distinct new Project(a.deliveryRequest.project.id,a.deliveryRequest.project.name,false) " + from4
			+ "where (supplier1.id = ?1 or supplier2.id = ?1) and a.deliveryRequest.project.id in (?2) group by a.deliveryRequest.project.id having sum(a.quantity) = 0")
	public List<Project> findProjectListBySupplierOwnerAndNotHavingStock(Integer supplierId, List<Integer> projectIdList);

	@Query("select distinct new Warehouse(a.deliveryRequest.warehouse.id,a.deliveryRequest.warehouse.name,true) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.warehouse is not null group by a.deliveryRequest.warehouse.id having sum(a.quantity) > 0")
	public List<Warehouse> findWarehouseListByCompanyOwnerAndHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select distinct new Warehouse(a.deliveryRequest.warehouse.id,a.deliveryRequest.warehouse.name,false) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.warehouse is not null group by a.deliveryRequest.warehouse.id having sum(a.quantity) = 0")
	public List<Warehouse> findWarehouseListByCompanyOwnerAndNotHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select a.deliveryRequest.warehouse.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition + " group by a.deliveryRequest.warehouse.id")
	public List<Integer> findWarehouseIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select distinct new Warehouse(a.deliveryRequest.warehouse.id,a.deliveryRequest.warehouse.name,true) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ "and a.deliveryRequest.warehouse is not null group by a.deliveryRequest.warehouse.id having sum(a.quantity) > 0")
	public List<Warehouse> findWarehouseListByCustomerOwnerAndHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select distinct new Warehouse(a.deliveryRequest.warehouse.id,a.deliveryRequest.warehouse.name,false) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ "and a.deliveryRequest.warehouse is not null group by a.deliveryRequest.warehouse.id having sum(a.quantity) = 0")
	public List<Warehouse> findWarehouseListByCustomerOwnerAndNotHavingStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select a.deliveryRequest.destination.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.destination is not null and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.destination.id ")
	public List<Integer> findDestinationIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query("select a.deliveryRequest.destination.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.destination is not null and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.destination.id ")
	public List<Integer> findDestinationIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	@Query("select a.deliveryRequest.externalRequester.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.externalRequester is not null and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.externalRequester.id ")
	public List<Integer> findExternalRequesterIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedExternalRequesterList, Integer companyId, Integer projectId);

	@Query("select a.deliveryRequest.destinationProject.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.destinationProject is not null and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.destinationProject.id ")
	public List<Integer> findDestinationProjectIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query("select a.deliveryRequest.externalRequester.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.externalRequester is not null and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.externalRequester.id ")
	public List<Integer> findExternalRequesterIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedExternalRequesterList, Integer customerId, Integer projectId);

	@Query("select distinct a.deliveryRequest.po.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.po is not null and a.deliveryRequest.project.id = ?5 and a.deliveryRequest.type = ?6")
	public List<Integer> findPoIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId,
			DeliveryRequestType outbound);

	@Query("select distinct a.deliveryRequest.po.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.po is not null and a.deliveryRequest.project.id = ?5 and a.deliveryRequest.type = ?6 ")
	public List<Integer> findPoIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId,
			DeliveryRequestType outbound);

	@Query("select distinct a.partNumber.id " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.po is not null and a.deliveryRequest.project.id = ?5 and a.deliveryRequest.type = ?6")
	public List<Integer> findPartNumberIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId,
			DeliveryRequestType outbound);

	@Query("select distinct a.partNumber.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.po is not null and a.deliveryRequest.project.id = ?5 and a.deliveryRequest.type = ?6")
	public List<Integer> findPartNumberIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId,
			DeliveryRequestType outbound);

	@Query("select a.deliveryRequest.destinationProject.id " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.destinationProject is not null and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.destinationProject.id ")
	public List<Integer> findDestinationProjectIdListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	@Query("select year(a.deliveryRequest.date4) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.date4 is not null and a.deliveryRequest.project.id = ?5 group by year(a.deliveryRequest.date4) ")
	public List<Integer> findYearListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query("select year(a.deliveryRequest.date4) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.date4 is not null and a.deliveryRequest.project.id = ?5 group by year(a.deliveryRequest.date4) ")
	public List<Integer> findYearListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	@Query("select concat(MONTHNAME(a.deliveryRequest.date4),'-',year(a.deliveryRequest.date4))  " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.date4 is not null and a.deliveryRequest.project.id = ?5 group by year(a.deliveryRequest.date4),month(a.deliveryRequest.date4) ")
	public List<String> findYearAndMonthListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query("select concat(MONTHNAME(a.deliveryRequest.date4),'-',year(a.deliveryRequest.date4)) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.date4 is not null and a.deliveryRequest.project.id = ?5 group by year(a.deliveryRequest.date4),month(a.deliveryRequest.date4)")
	public List<String> findYearAndMonthListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	////////////////////////////////////////////

	@Query("select (select b from Project b where b.id = a.deliveryRequest.project.id) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " group by a.deliveryRequest.project.id")
	public List<Project> findProjectListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select (select b.name from Company b where a.deliveryRequest.deliverToCompany.id = b.id) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.project.id = ?5  group by a.deliveryRequest.deliverToCompany.name ")
	public List<String> findDeliverToCompanyNameListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query("select (select b.name from Customer b where a.deliveryRequest.deliverToCustomer.id = b.id) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.deliverToCompanyType = ?5 and a.deliveryRequest.project.id = ?6 group by a.deliveryRequest.deliverToCustomer.name ")
	public List<String> findDeliverToCustomerNameListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, CompanyType companyTypeCustomer,
			Integer projectId);

	@Query("select (select b.name from Supplier b where a.deliveryRequest.deliverToSupplier.id = b.id) " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.deliverToCompanyType = ?5 and a.deliveryRequest.project.id = ?6 group by a.deliveryRequest.deliverToSupplier.name ")
	public List<String> findDeliverToSupplierNameListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, CompanyType companyTypeSupplier,
			Integer projectId);

	@Query("select a.deliveryRequest.deliverToOther " + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " and a.deliveryRequest.deliverToCompanyType = ?5 and a.deliveryRequest.project.id = ?6 group by a.deliveryRequest.deliverToOther ")
	public List<String> findDeliverToOtherNameListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, CompanyType companyTypeOther,
			Integer projectId);

	@Query(c15 + from2 + " where " + usernameCondition + " and " + companyCondition + " and " + deliverToEntityCondition
			+ " and a.quantity < 0 and a.deliveryRequest.project.id = ?6 and a.deliveryRequest.destinationProject.type != ?7  group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByDeliverToEntityAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyOwnerId,
			String deliverToName, Integer projectId, String projectTypeStock);

	@Query(c22 + from2
			+ "where (company1.id = ?1 or company2.id = ?1 ) and  a.deliveryRequest.outboundDeliveryRequestReturn.id in (?2) and a.partNumber.id in (?3) group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice ")
	public List<StockRow> findStockHistoryByCompanyOwnerAndOutboundDeliveryRequestReturn(Integer companyId, List<Integer> outboundSrouceList, List<Integer> partNumberList);

	@Query(c22 + from3
			+ "where (customer1.id = ?1 or customer2.id = ?1 ) and  a.deliveryRequest.outboundDeliveryRequestReturn.id in (?2) and a.partNumber.id in (?3) group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice ")
	public List<StockRow> findStockHistoryByCustomerOwnerAndOutboundDeliveryRequestReturn(Integer customerId, List<Integer> outboundSrouceList, List<Integer> partNumberList);

	//

	@Query("select (select b from Project b where b.id = a.deliveryRequest.project.id) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " group by a.deliveryRequest.project.id")
	public List<Project> findProjectListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select (select b.name from Company b where a.deliveryRequest.deliverToCompany.id = b.id) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.project.id = ?5 group by a.deliveryRequest.deliverToCompany.name ")
	public List<String> findDeliverToCompanyNameListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId);

	@Query("select (select b.name from Customer b where a.deliveryRequest.deliverToCustomer.id = b.id) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.deliverToCompanyType = ?5 and a.deliveryRequest.project.id = ?6 group by a.deliveryRequest.deliverToCustomer.name ")
	public List<String> findDeliverToCustomerNameListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			CompanyType companyTypeCustomer, Integer projectId);

	@Query("select (select b.name from Supplier b where a.deliveryRequest.deliverToSupplier.id = b.id) " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.deliverToCompanyType = ?5 and a.deliveryRequest.project.id = ?6 group by a.deliveryRequest.deliverToSupplier.name ")
	public List<String> findDeliverToSupplierNameListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			CompanyType companyTypeSupplier, Integer projectId);

	@Query("select a.deliveryRequest.deliverToOther " + from3 + "  where  " + usernameCondition + " and " + customerCondition
			+ " and a.deliveryRequest.deliverToCompanyType = ?5 and a.deliveryRequest.project.id = ?6 group by a.deliveryRequest.deliverToOther ")
	public List<String> findDeliverToOtherNameListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, CompanyType companyTypeOther,
			Integer projectId);

	@Query(c15 + from3 + " where " + usernameCondition + " and " + customerCondition + " and " + deliverToEntityCondition
			+ " and a.quantity < 0 and a.deliveryRequest.project.id = ?6 and a.deliveryRequest.destinationProject.type != ?7  group by a.deliveryRequest.id,a.status,a.partNumber.id")
	public List<StockRow> findStockHistoryByDeliverToEntityAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerOwnerId,
			String deliverToName, Integer projectId, String projectTypeStock);

	////////////////////////////////////////////
	@Query("select a.deliveryRequest.destinationProject.customer.id " + from1 + "  where  " + usernameCondition + " and a.inboundDeliveryRequest.company.id = ?4 "
			+ " and a.deliveryRequest.destinationProject is not null and a.inboundDeliveryRequest.project.type = ?5 group by a.deliveryRequest.destinationProject.customer.id")
	public List<Integer> findDestinationCustomerIdListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationCustomerList, Integer companyId,
			String projectTypeStock);

	@Query(c10 + from1 + "  where  " + usernameCondition
			+ " and (a.companyId = ?4 or a.inboundCompanyId = ?4) and datediff(current_date,a.inboundDeliveryRequest.date4) > a.inboundDeliveryRequest.approximativeStoragePeriod group by a.deliveryRequest.id,a.partNumber.id having sum(a.quantity) > 0")
	public List<StockRow> findOverdueByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c10 + from1 + "  where  " + usernameCondition
			+ " and (a.customerId = ?4 or a.inboundCustomerId = ?4) and  datediff(current_date,a.inboundDeliveryRequest.date4) > a.inboundDeliveryRequest.approximativeStoragePeriod group by a.inboundDeliveryRequest.id,a.partNumber.id having sum(a.quantity) > 0")
	public List<StockRow> findOverdueByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	// only company
	@Query(c6 + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " group by a.partNumber.id having sum(a.quantity) > (select b.stockMax from PartNumber b where b.id = a.partNumber.id )")
	public List<StockRow> findMaxStockThreshold(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c6 + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " group by a.partNumber.id having sum(a.quantity) < (select b.stockMin from PartNumber b where b.id = a.partNumber.id )")
	public List<StockRow> findMinStockThreshold(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	//// Cost Center Financial//////////////

	@Query(c12 + from2 + "  where  " + usernameCondition + " and " + companyCondition + " and " + projectCondition
			+ " group by a.partNumber,a.status,a.inboundDeliveryRequest.date4 having sum(a.quantity) > 0")
	public List<StockRow> getCostCenterFinancialSituation(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId);

	@Query(c23 + from2 + "  where  " + usernameCondition + " and " + companyCondition
			+ " group by a.partNumber,a.status,a.deliveryRequest.project.id,a.deliveryRequestDetail.unitCost,a.inboundDeliveryRequest.date4 having sum(a.quantity) > 0")
	public List<StockRow> getFinancialSituationOld(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c23 + from1 + " where  " + usernameCondition + " and " + companyConditionNew
			+ " group by a.partNumber,a.status,a.deliveryRequest.project.id,a.deliveryRequestDetail.unitCost,a.inboundDeliveryRequest.date4 having sum(a.quantity) > 0")
	public List<StockRow> getFinancialSituation(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c23 + from2
			+ "  where  (company1.id = ?1 or company2.id = ?1 ) group by a.partNumber,a.status,a.deliveryRequest.project.id,a.deliveryRequestDetail.unitCost,a.inboundDeliveryRequest.date4 having sum(a.quantity) > 0")
	public List<StockRow> getFinancialSituationOld(Integer companyId);

	@Query(c23 + "from StockRow a "
			+ "  where  (a.companyId = ?1 or a.inboundCompanyId = ?1) group by a.partNumber,a.status,a.deliveryRequest.project.id,a.deliveryRequestDetail.unitCost,a.inboundDeliveryRequest.date4 having sum(a.quantity) > 0")
	public List<StockRow> getFinancialSituation(Integer companyId);

	// fast moving items

//	@Query(c13 + from2 + "  where  " + usernameCondition + " and " + companyCondition + " and a.partNumber.stockItem = true group by a.partNumber.id,a.deliveryRequest.id")
//	public List<StockRow> findByCompanyOwnerGroupbyPartNumberAndDeliveryRequestOld(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c13 + "from StockRow a left join a.deliveryRequest.warehouse as warehouse left join a.deliveryRequest.destinationProject as destinationProject  where  " + usernameCondition
			+ " and (a.companyId = ?4 or a.inboundCompanyId = ?4) and a.partNumber.stockItem = true group by a.partNumber.id,a.deliveryRequest.id")
	public List<StockRow> findByCompanyOwnerGroupbyPartNumberAndDeliveryRequest(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	// CHART
//	@Query("select sum(a.quantity * a.deliveryRequestDetail.unitCost) from StockRow a where a.deliveryRequestDetail.unitCost is not null and a.inboundCompanyId = ?1 and date(a.creationDate) <= date(?2)")
//	public Double getTotalCostBeforeDate(Integer companyId, Date maxDate);

	@Query("select year(a.creationDate),month(a.creationDate),sum(a.quantity * a.deliveryRequestDetail.unitCost) from StockRow a where a.deliveryRequestDetail.unitCost is not null and a.inboundCompanyId = ?1 group by year(a.creationDate),month(a.creationDate) order by year(a.creationDate),month(a.creationDate)")
	public List<Object[]> getTotalCostPerYearAndMonth(Integer companyId);

	@Query("select sum(a.quantity * a.deliveryRequestDetail.unitCost) " + from1 + " where a.deliveryRequestDetail.unitCost is not null and " + usernameCondition
			+ " and a.inboundDeliveryRequest.company.id = ?4 and date(a.creationDate) <= date(?5)")
	public Double getTotalCostBeforeDate(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Date maxDate);

	@Query("select year(a.creationDate),month(a.creationDate),sum(a.quantity * a.deliveryRequestDetail.unitCost) " + from1 + " where a.deliveryRequestDetail.unitCost is not null and "
			+ usernameCondition + " and a.inboundCompanyId = ?4 group by year(a.creationDate),month(a.creationDate) order by year(a.creationDate),month(a.creationDate)")
	public List<Object[]> getTotalCostPerYearAndMonth(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query("select sum(a.quantity * a.deliveryRequestDetail.unitCost) " + from1 + " where a.deliveryRequestDetail.unitCost is not null and " + usernameCondition
			+ " and a.inboundDeliveryRequest.company.id = ?4 and " + projectCondition + "  and date(a.creationDate) <= date(?6)")
	public Double getProjectTotalCostBeforeDate(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId, Date maxDate);

	// RETURN FROM OUTBOUND

	@Query(drd_c6
			+ " from StockRow a where a.deliveryRequest.id = ?1 group by a.partNumber.id having (sum(-a.quantity) - COALESCE((select sum(b.quantity) from StockRow b where b.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and b.partNumber.id = a.partNumber.id),0)) > 0")
	public List<DeliveryRequestDetail> findRemainingByOutboundDeliveryRequestReturn(Integer outboundDeliveryRequestId);

	// TRANSFER FROM OUTBOUND
//	String select16 = "select new DeliveryRequestDetail(sum(-a.quantity) - COALESCE((select sum(b.quantity) from StockRow b where b.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and b.partNumber.id = a.partNumber.id),0),a.partNumber,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice) ";

	@Query(drd_c7
			+ " from StockRow a where a.deliveryRequest.id = ?1 group by a.partNumber.id having (sum(-a.quantity) - COALESCE((select sum(b.quantity) from StockRow b where b.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and b.partNumber.id = a.partNumber.id),0)) > 0")
	public List<DeliveryRequestDetail> findRemainingByOutboundDeliveryRequestTransfer(Integer outboundDeliveryRequestId);

	@Query("select a.partNumber.id " + from3 + " where " + usernameCondition + " and " + customerCondition + "  group by a.partNumber.id having sum(a.quantity) > 0")
	public Set<Integer> findInStockByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query("select a.partNumber.id " + from3 + " where (customer1.id = ?1 or customer2.id = ?1) group by a.partNumber.id having sum(a.quantity) > 0")
	public Set<Integer> findInStockByCustomerOwner(Integer customerId);

	@Query("select customer1.id,customer2.id " + from3 + " where " + usernameCondition)
	public List<Object[]> findCustomerIdList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList);

	@Query("select customer1.id,customer2.id " + from3 + " where " + usernameCondition + " group by a.partNumber.id,customer1.id,customer2.id having sum(a.quantity) > 0")
	public List<Object[]> findCustomerIdListWithStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList);

	@Query("select MONTHNAME(date1) from DeliveryRequest where id = 2")
	public List<String> test();

	@Query("select distinct a.deliveryRequest.id from StockRow a where a.inboundDeliveryRequest.id = ?1 and a.deliveryRequest.type = 'OUTBOUND'")
	public List<Integer> findAssociatedOutboundWithInbound(Integer inboundDeliveryRequestId);

	@Query("select distinct a.inboundDeliveryRequest.id from StockRow a where a.deliveryRequest.id = ?1")
	public List<Integer> findAssociatedInboundWithOutbound(Integer outboundDeliveryRequestId);

	@Query(c1 + "from StockRow a where  a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status not in (?2)")
	public List<StockRow> findReturnedStockRowList(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> notInStatus);

	@Query("select count(*) from StockRow a where  a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1 and a.deliveryRequest.status not in (?2)")
	public Long countReturnedStockRowList(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> notInStatus);

	@Query(c1 + " from StockRow a where  a.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and a.deliveryRequest.status not in (?2)")
	public List<StockRow> findTransferredStockRowList(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> notInStatus);

	@Query("select count(*) from StockRow a where  a.deliveryRequest.outboundDeliveryRequestTransfer.id = ?1 and a.deliveryRequest.status not in (?2)")
	public Long countTransferredStockRowList(Integer outboundDeliveryRequestId, List<DeliveryRequestStatus> notInStatus);

	@Query(c16
			+ "from StockRow a where a.partNumber.id = ?1 and a.deliveryRequest.project.type = 'Stock' and a.deliveryRequest.project.costcenter.lob.bu.company.id = ?2 group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status")
	public List<StockRow> findStockHistoryByPartNumberAndProjectStock(Integer partNumberId, Integer companyId);

	@Query("select distinct a.inboundDeliveryRequest.ownerType from StockRow a  where a.deliveryRequest.id = ?1")
	List<CompanyType> findOwnerTypeListByDeliveryRequest(Integer deliveryRequestId);

	@Query(c22 + from2 + "where" + usernameCondition + " and " + companyCondition
			+ " and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c22 + from3 + "where" + usernameCondition + " and " + customerCondition
			+ " and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findDeliveryListsByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query(c24 + from2 + "where" + usernameCondition + " and " + companyCondition
			+ " and a.quantity < 0 group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findDeliveryListsByCompanyOwner2(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c24 + from3 + "where" + usernameCondition + " and " + customerCondition
			+ " and a.quantity < 0 group by a.deliveryRequest.id,a.inboundDeliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findDeliveryListsByCustomerOwner2(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query(c22 + from3
			+ "where (customer1.id = ?1 or customer2.id = ?2) and a.deliveryRequest.project.id in (?2) and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findDeliveryListsByCustomerOwner(Integer customerId, List<Integer> projectIdList);

	@Query(c22 + from2 + "where" + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.sdm is true and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findSdmDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c22 + from3 + "where" + usernameCondition + " and " + customerCondition
			+ "and a.deliveryRequest.sdm is true and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findSdmDeliveryListsByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query(c22 + from3
			+ "where (customer1.id = ?1 or customer2.id = ?2) and a.deliveryRequest.project.id in (?2) and a.deliveryRequest.sdm is true and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findSdmDeliveryListsByCustomerOwner(Integer customerId, List<Integer> projectIdList);

	@Query(c22
			+ "from StockRow a where a.deliveryRequest.sdm is true and a.deliveryRequest.deliverToSupplier.id = ?1 and a.deliveryRequest.project.id in (?2) and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findSdmDeliveryListsByDeliverToSupplier(Integer supplierId, List<Integer> projectIdList);

	@Query(c22 + from2 + "where" + usernameCondition + " and " + companyCondition
			+ "and a.deliveryRequest.ism is true and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findIsmDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	@Query(c22 + from3 + "where" + usernameCondition + " and " + customerCondition
			+ "and a.deliveryRequest.ism is true and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findIsmDeliveryListsByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId);

	@Query(c22 + from3
			+ "where (customer1.id = ?1 or customer2.id = ?2) and a.deliveryRequest.project.id in (?2) and a.deliveryRequest.ism is true and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findIsmDeliveryListsByCustomerOwner(Integer customerId, List<Integer> projectIdList);

	@Query(c22
			+ "from StockRow a where a.deliveryRequest.ism is true and a.deliveryRequest.deliverToSupplier.id = ?1 and a.deliveryRequest.project.id in (?2) and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findIsmDeliveryListsByDeliverToSupplier(Integer supplierId, List<Integer> projectIdList);

	@Query(c22
			+ "from StockRow a where a.deliveryRequest.deliverToSupplier.id = ?1 and a.deliveryRequest.project.id in (?2) and a.quantity < 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findDeliveryListsByDeliverToSupplier(Integer supplierId, List<Integer> projectIdList);

	@Query(c22 + "from StockRow a where a.deliveryRequest.po.id = ?1 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findByPo(Integer poId);

	@Query(c22
			+ "from StockRow a where a.deliveryRequest.po.id = ?1 and a.deliveryRequest.status in ('DELIVRED','ACKNOWLEDGED') and (a.deliveryRequest.type = 'INBOUND' or a.deliveryRequest.isFullyReturned is null or a.deliveryRequest.isFullyReturned is false) and (select count(*) from BoqMapping b where b.deliveryRequest.id = a.deliveryRequest.id) = 0 group by a.deliveryRequest.id,a.status,a.partNumber.id,a.deliveryRequestDetail.unitCost,a.deliveryRequestDetail.unitPrice")
	List<StockRow> findByPoAndDeliveredWithoutBoqMapping(Integer poId);

	@Query("select a.partNumber.id,sum(case when a.deliveryRequest.type = 'INBOUND' then a.quantity else -a.quantity end) from StockRow a where a.deliveryRequest.id = ?1 and a.partNumber.expirable is true group by a.partNumber.id")
	List<Object[]> findByDeliveryRequestAndExpirableMap(Integer deliveryRequestId);

//	@Modifying
//	@Query("update StockRow a set a.deliveryRequestDetail.unitPrice = ?1 where a.partNumber.id = ?2 and a.deliveryRequest.id in (select distinct b.id from DeliveryRequest b where b.outboundDeliveryRequestReturn.id = ?3)")
//	void updateUnitPriceByPartNumberAndOutboundDeliveryRequestReturn(Double unitPrice, Integer partNumberId, Integer outboundDeliveryRequestReturnId);

	@Query("select a.partNumber.id,sum(a.quantity) " + from2 + "where" + usernameCondition + " and " + companyCondition + " and a.deliveryRequest.project.subType = 'Stock' group by a.partNumber.id")
	List<Object[]> findProjectStockGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

	// PN Reporting quantities
	@Query("select sum(a.quantity)" + from2 + "where a.partNumber.id = ?5 and" + usernameCondition + "and" + companyCondition)
	Double findPhysicalInventoryByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId);

	@Query("select sum(a.quantity)" + from2 + "where a.partNumber.id = ?5 and" + usernameCondition + "and" + companyCondition + "and a.deliveryRequest.project.subType = 'Stock'")
	Double findStockInventoryByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId);

	@Query("select sum(a.quantity)" + from3 + "where a.partNumber.id = ?5 and" + usernameCondition + "and" + customerCondition)
	Double findPhysicalInventoryByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId);

	@Query("select sum(a.quantity)" + from3 + "where a.partNumber.id = ?5 and" + usernameCondition + "and" + customerCondition + "and a.deliveryRequest.project.subType = 'Stock'")
	Double findStockInventoryByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId);

	@Query("select new StockRow(sum(a.quantity),a.partNumber) from StockRow a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1  group by a.partNumber.id")
	public List<StockRow> findReturnedStockRowListGroupByPartNumber(Integer outboundDeliveryRequestId);

	@Query("select a.partNumber.id,sum(a.quantity) from StockRow a where a.deliveryRequest.id = ?1 group by a.partNumber.id")
	List<Object[]> findQuantityByDeliveryRequestGroupByPartNumber(Integer deliveryRequest);

	@Query("select a.partNumber.id,sum(a.quantity) from StockRow a where a.deliveryRequest.outboundDeliveryRequestReturn.id = ?1  and COALESCE(a.deliveryRequest.outboundDeliveryRequestReturn.returnReason,'') !='Hardware Swap' group by a.partNumber.id")
	List<Object[]> findReturnedQuantityByOutboundDeliveryRequestGroupByPartNumber(Integer outboundDeliveryRequestId);

	@Query("select sum(a.quantity) from StockRow a where a.deliveryRequest.id = ?1 and a.partNumber.id = 2")
	Double findQuantityByDeliveryRequestAndPartNumber(Integer deliveryRequest, Integer partNumberId);

	@Query("select sum(a.quantity) from StockRow a where a.deliveryRequestDetail.id = ?1")
	Double findQuantityByDeliveryRequestDetail(Integer deliveryRequestDetail);

	@Modifying
	@Query("update StockRow a set a.companyId = (select b.company.id from DeliveryRequest b where a.deliveryRequest.id = b.id),a.customerId = (select b.customer.id from DeliveryRequest b where a.deliveryRequest.id = b.id),a.supplierId = (select b.supplier.id from DeliveryRequest b where a.deliveryRequest.id = b.id)  where a.deliveryRequest.id = ?1")
	void updateOwnerId(Integer deliveryRequestId);

	@Modifying
	@Query("update StockRow a set a.inboundCompanyId = (select b.company.id from DeliveryRequest b where a.inboundDeliveryRequest.id = b.id),a.inboundCustomerId = (select b.customer.id from DeliveryRequest b where a.inboundDeliveryRequest.id = b.id),a.inboundSupplierId = (select b.supplier.id from DeliveryRequest b where a.inboundDeliveryRequest.id = b.id)  where a.deliveryRequest.id = ?1")
	void updateInboundOwnerId(Integer deliveryRequestId);

	@Query("select sum(-a.quantity * (select sum(pd.quantity) from PackingDetail pd where pd.parent.id = a.packing.id and pd.hasSerialnumber is true) / a.packing.quantity) from StockRow a where a.deliveryRequest.id = ?1 and (select count(*) from DeliveryRequestSerialNumber b where b.inboundStockRow.deliveryRequestDetail = a.inboundDeliveryRequestDetail.id and b.serialNumber is not null and b.serialNumber!='') > 0  ")
	Double findTotalRequiringSerialNumberInOurbound(Integer outboundDelievryRequestId);

	@Query("select distinct a.deliveryRequest.id from StockRow a where (select count(*) from PackingDetail b where b.parent.id = a.packing.id and b.hasSerialnumber is true) > 0")
	List<Integer> findDeliveryRequestListHavingPackingDetailRequiringSerialNumber();
	
	@Query("select a.inboundDeliveryRequest.id from StockRow a where a.inboundDeliveryRequest.id in (select b.id from DeliveryRequest b where b.type = 'INBOUND' and b.missingSerialNumber is true and (select count(*) from DeliveryRequestSerialNumber c where c.inboundStockRow.deliveryRequest.id = b.id and c.serialNumber is not null and c.serialNumber != '')=0)  group by a.inboundDeliveryRequest.id having sum(a.quantity) = 0" )
	List<Integer> clearMissingSerialNumberBacklogQuery();
	
}
