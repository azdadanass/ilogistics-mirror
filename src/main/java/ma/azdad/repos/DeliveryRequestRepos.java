package ma.azdad.repos;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.IssueStatus;
import ma.azdad.model.Po;
import ma.azdad.model.User;

@Repository
public interface DeliveryRequestRepos extends JpaRepository<DeliveryRequest, Integer> {

	String destinationProjectCustomerName = "(select b.customer.name from Project b where a.destinationProject.id = b.id)";
	String endCustomerName = "(select b.name from Customer b where a.endCustomer.id = b.id)";
	String poNumero = "(select b.numero from Po b where a.po.id = b.id)";
	String deliverToCompanyName = "(select b.name from Company b where a.deliverToCompany.id = b.id)";
	String deliverToCustomerName = "(select b.name from Customer b where a.deliverToCustomer.id = b.id)";
	String deliverToSupplierName = "(select b.name from Supplier b where a.deliverToSupplier.id = b.id)";
	String toUserFullName = "(select b.fullName from User b where a.toUser.username = b.username)";
	String originName = "(select b.name from Site b where a.origin.id = b.id)";
	String destinationName = "(select b.name from Site b where a.destination.id = b.id)";
	String customerName = "(select b.name from Customer b where a.customer.id = b.id)";
	String supplierName = "(select b.name from Supplier b where a.supplier.id = b.id)";
	String companyName = "(select b.name from Company b where a.company.id = b.id)";
	String warehouse = "(select b from Warehouse b where b.id = a.warehouse.id)";
	String destinationProjectName = "(select b.name from Project b where b.id = a.destinationProject.id)";
	String transporterName1 = "(select concat(b.firstName,' ',b.lastName) from Transporter b where a.transporter.id = b.id)";
	String transporterName2 = "(select (select c.name from Supplier c where b.supplier.id = c.id) from Transporter b where a.transporter.id = b.id)";
	String transportationRequestNumber = "(select count(*) from TransportationRequest b where b.deliveryRequest.id = a.id)";
	String c1 = "select new DeliveryRequest(id,description,referenceNumber,reference,priority,a.requester,a.project,a.type,a.inboundType,a.isForReturn,a.isForTransfer,a.sdm," //
			+ "a.status,a.originNumber,a.date4,a.neededDeliveryDate,a.returnReason," + originName + "," + destinationName + ",a.ownerType," + customerName + "," + supplierName + "," + companyName
			+ "," + warehouse + "," + destinationProjectName + "," + transporterName1 + "," + transporterName2 + "," + transportationRequestNumber
			+ ",a.transportationNeeded,a.smsRef,a.containsBoqMapping,a.missingPo,a.missingOutboundDeliveryNote," + poNumero + ",a.deliverToCompanyType," + deliverToCompanyName + ","
			+ deliverToCustomerName + "," + deliverToSupplierName + "," + toUserFullName + "," + endCustomerName + ",a.project.customer.name," + destinationProjectCustomerName + ") ";

	String c2 = "select new DeliveryRequest(a.id,a.reference,a.type,a.status,a.date4,a.requester.username,a.requester.photo,a.warehouse.name,a.project.name,a.destinationProject.name,"//
			+ "a.deliverToCompanyType," + deliverToCompanyName + "," + deliverToCustomerName + "," + deliverToSupplierName + "," + toUserFullName + ")";

	@Query("select id from DeliveryRequest")
	List<Integer> findIdList();

	List<DeliveryRequest> findByStatus(DeliveryRequestStatus status);

	@Query("select id from DeliveryRequest where sdm is true or ism is true")
	List<Integer> findBySdmOrIsmIdlist();

	@Query("select distinct a.deliveryRequest.id from DeliveryRequestDetail a where a.partNumber.id = ?1 and (a.deliveryRequest.sdm is true or a.deliveryRequest.ism is true)")
	List<Integer> findBySdmOrIsmAndHavingPartNumberIdlist(Integer partNumberId);

	@Query("select type from DeliveryRequest where id = ?1")
	DeliveryRequestType findType(Integer id);

	@Query(c1 + " from DeliveryRequest a"
			+ " where (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))"
			+ " order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLight(String username, List<Integer> warehouseList, List<Integer> projectList);

	@Query("select id from DeliveryRequest a where a.type = 'OUTBOUND' and a.inboundPo.id is null")
	List<Integer> findByOutboundWithoutInboundPoId();

	@Query(c1 + " from DeliveryRequest a"
			+ " where (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3)) and a.type = ?4"
			+ " order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLight(String username, List<Integer> warehouseList, List<Integer> projectList, DeliveryRequestType type);

	@Query(c1
			+ " from DeliveryRequest a where ((a.deliverToSupplier.id = ?1 and a.destinationProject.id in (?2) and (a.type = ?3 or ?3 is null)) or a.warehouse.id in (?4))  order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightBySupplierUser(Integer deliverToSupplierId, List<Integer> projectList, DeliveryRequestType type, List<Integer> warehouseList);

	@Query(c1
			+ " from DeliveryRequest a where ((a.deliverToSupplier.id = ?1 and a.destinationProject.id in (?2) and (a.type = ?3 or ?3 is null)) or a.warehouse.id in (?4)) and a.status in (?5)  order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightBySupplierUser(Integer deliverToSupplierId, List<Integer> projectList, DeliveryRequestType type, List<Integer> warehouseList,
			List<DeliveryRequestStatus> statusList);

	@Query(c1 + " from DeliveryRequest a"
			+ " where a.pendingJrMapping is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.project.id in (?2)) and a.type = ?3 and a.sdm = ?4 and a.ism = ?5"
			+ " order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findPendingJrMapping(String username, Collection<Integer> projectList, DeliveryRequestType type, Boolean sdm, Boolean ism);

	@Query("select count(*) from DeliveryRequest a"
			+ " where a.pendingJrMapping is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.project.id in (?2)) and a.type = ?3 and a.sdm = ?4 and a.ism = ?5"
			+ " order by a.neededDeliveryDate desc")
	public Long countPendingJrMapping(String username, Collection<Integer> projectList, DeliveryRequestType type, Boolean sdm, Boolean ism);

	@Query(c1 + " from DeliveryRequest a"
			+ " where a.havingRunningStock is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.project.id in (?2)) and a.type = ?3 and a.sdm = ?4 and a.ism = ?5"
			+ " order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findHavingRunningStock(String username, Collection<Integer> projectList, DeliveryRequestType type, Boolean sdm, Boolean ism);

	@Query("select count(*) from DeliveryRequest a"
			+ " where a.havingRunningStock is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.project.id in (?2)) and a.type = ?3 and a.sdm = ?4 and a.ism = ?5"
			+ " order by a.neededDeliveryDate desc")
	public Long countHavingRunningStock(String username, Collection<Integer> projectList, DeliveryRequestType type, Boolean sdm, Boolean ism);

	@Query(c1
			+ " from DeliveryRequest a where (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))  and a.type = ?4 and a.missingPo is true and a.status not in ('REJECTED','CANCELED') ")
	public List<DeliveryRequest> findByMissingPo(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type);

	@Query("select count(*) from DeliveryRequest a where (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))  and a.type = ?4 and a.missingPo is true and a.status not in ('REJECTED','CANCELED') ")
	public Long countByMissingPo(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type);

	@Query(c2
			+ "from DeliveryRequest a where a.missingOutboundDeliveryNote is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.toUser.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))  order by a.id desc")
	List<DeliveryRequest> findByMissingOutboundDeliveryNoteFile(String username, Collection<Integer> warehouseList, Collection<Integer> projectIdList);

	@Query("select count(*) from DeliveryRequest a where a.missingOutboundDeliveryNote is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.toUser.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))")
	Long countByMissingOutboundDeliveryNoteFile(String username, Collection<Integer> warehouseList, Collection<Integer> projectIdList);

	@Query(c2 + "from DeliveryRequest a where a.missingOutboundDeliveryNote is true and a.deliverToSupplier.id = ?1 and a.project.id in (?2) order by a.id desc")
	List<DeliveryRequest> findByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(Integer supplierId, Collection<Integer> projectIdList);

	@Query("select count(*) from DeliveryRequest a where a.missingOutboundDeliveryNote is true and a.deliverToSupplier.id = ?1 and a.project.id in (?2)")
	Long countByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(Integer supplierId, Collection<Integer> projectIdList);

	@Query(c2 + "from DeliveryRequest a where a.missingOutboundDeliveryNote is true and a.deliverToCustomer.id = ?1 and a.project.id in (?2) order by a.id desc")
	List<DeliveryRequest> findByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(Integer customerId, Collection<Integer> projectIdList);

	@Query("select count(*) from DeliveryRequest a where a.missingOutboundDeliveryNote is true and a.deliverToCustomer.id = ?1 and a.project.id in (?2)")
	Long countByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(Integer customerId, Collection<Integer> projectIdList);

	@Query(c1 + " from DeliveryRequest a "
			+ " where (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))"
			+ " and a.type = ?4 and a.po is not null and 0 = (select count(*) from BoqMapping b where b.deliveryRequest.id = a.id ) and a.status not in ('REJECTED','CANCELED') and (a.type = 'INBOUND' or a.isFullyReturned is null or a.isFullyReturned is false)")
	public List<DeliveryRequest> findByMissingBoqMapping(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type);

	@Query("select count(*) from DeliveryRequest a "
			+ " where (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))"
			+ " and a.type = ?4 and a.po is not null and 0 = (select count(*) from BoqMapping b where b.deliveryRequest.id = a.id ) and a.status not in ('REJECTED','CANCELED') and (a.type = 'INBOUND' or a.isFullyReturned is null or a.isFullyReturned is false)")
	public Long countByMissingBoqMapping(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type);

	@Query(c1
			+ " from DeliveryRequest a where a.status = ?2 and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?3) or a.project.id in (?4)) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLight(String username, DeliveryRequestStatus status, List<Integer> warehouseList, List<Integer> projectList);

	@Query(c1 + " from DeliveryRequest a where a.requester.username = ?1")
	public List<DeliveryRequest> findLightByRequester(String username);

	@Query(c1
			+ " from DeliveryRequest a where a.status in (?2) and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?3) or a.project.id in (?4)) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLight(String username, List<DeliveryRequestStatus> status, List<Integer> warehouseList, List<Integer> projectList);

	@Query("select count(*)  from DeliveryRequest a where a.status = ?2 and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?3) or a.project.id in (?4)) order by a.neededDeliveryDate desc")
	public Long count(String username, DeliveryRequestStatus status, List<Integer> warehouseList, List<Integer> assignedProjectList);

	@Query("select count(*)  from DeliveryRequest a where a.status in (?2) and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?3) or a.project.id in (?4)) order by a.neededDeliveryDate desc")
	public Long count(String username, List<DeliveryRequestStatus> status, List<Integer> warehouseList, List<Integer> assignedProjectList);

	@Query(c1
			+ " from DeliveryRequest a where a.type = ?2 and a.status = ?3 and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?4) or a.project.id in (?5)) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLight(String username, DeliveryRequestType type, DeliveryRequestStatus status, List<Integer> warehouseList, List<Integer> projectList);

	@Query(c1
			+ " from DeliveryRequest a where a.type = ?2 and a.status in (?3) and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?4) or a.project.id in (?5)) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLight(String username, DeliveryRequestType type, List<DeliveryRequestStatus> status, List<Integer> warehouseList, List<Integer> projectList);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.requester.username = ?2  order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByRequester(DeliveryRequestType type, String username);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.requester.username = ?2 and a.status = ?3 order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByRequester(DeliveryRequestType type, String username, DeliveryRequestStatus status);

	@Query(c1 + "from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and (a.requester.username = ?1 or a.toUser.username = ?1 or a.warehouse.id in (?2)) ")
	List<DeliveryRequest> findToAcknowledgeInternal(String username, List<Integer> warehouseList);

	@Query("select count(*) from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and (a.requester.username = ?1 or a.toUser.username = ?1 or a.warehouse.id in (?2)) ")
	Long countToAcknowledgeInternal(String username, List<Integer> warehouseList);

	@Query(c1 + "from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and (a.toUser.username = ?1 or (a.deliverToSupplier.id = ?2 and a.destinationProject.id in (?3)))")
	List<DeliveryRequest> findToAcknowledgeExternalSupplierUser(String username, Integer supplierId, List<Integer> projectIdList);

	@Query("select count(*) from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and (a.toUser.username = ?1 or (a.deliverToSupplier.id = ?2 and a.destinationProject.id in (?3)))")
	Long countToAcknowledgeExternalSupplierUser(String username, Integer supplierId, List<Integer> projectIdList);

	@Query(c1 + "from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and (a.toUser.username = ?1 or (a.deliverToCustomer.id = ?2 and a.destinationProject.id in (?3)))")
	List<DeliveryRequest> findToAcknowledgeExternalCustomerUser(String username, Integer customerId, List<Integer> projectIdList);

	@Query("select count(*) from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and (a.toUser.username = ?1 or (a.deliverToCustomer.id = ?2 and a.destinationProject.id in (?3)))")
	Long countToAcknowledgeExternalCustomerUser(String username, Integer customerId, List<Integer> projectIdList);

	@Query("select count(*)  from DeliveryRequest a where a.type = ?1 and a.requester.username = ?2 and a.status = ?3  order by a.neededDeliveryDate desc")
	public Long countByRequester(DeliveryRequestType type, String username, DeliveryRequestStatus status);

	@Query(c1
			+ " from DeliveryRequest a where a.type = ?1 and a.isForTransfer = true and (a.destinationProject.manager.username = ?2 or a.destinationProject.id in (?3)) and 0 = (select count(*) from DeliveryRequest b where b.outboundDeliveryRequestTransfer.id = a.id and b.status not in ('REJECTED','CANCELED')) and a.status in (?4)")
	public List<DeliveryRequest> findLightByIsForTransferAndDestinationProjectAndNotTransferredAndStatus(DeliveryRequestType type, String username, List<Integer> assignedProjectList,
			List<DeliveryRequestStatus> status);

	@Query("select count(*)  from DeliveryRequest a where a.type = ?1 and a.isForTransfer = true and (a.destinationProject.manager.username = ?2 or a.destinationProject.id in (?3)) and 0 = (select count(*) from DeliveryRequest b where b.outboundDeliveryRequestTransfer.id = a.id) and a.status in (?4) ")
	public long countByIsForTransferAndDestinationProjectAndNotTransferredAndStatus(DeliveryRequestType type, String username, List<Integer> assignedProjectList, List<DeliveryRequestStatus> status);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.isForReturn = true and a.isFullyReturned = false and a.requester.username = ?2 and a.status in (?3)")
	public List<DeliveryRequest> findLightByIsForReturnAndNotFullyReturned(DeliveryRequestType type, String username, List<DeliveryRequestStatus> status);

	@Query("select count(*)  from DeliveryRequest a where a.type = ?1 and a.isForReturn = true and a.isFullyReturned = false and a.requester.username = ?2 and a.status in (?3)")
	public Long countByIsForReturnAndNotFullyReturned(DeliveryRequestType type, String username, List<DeliveryRequestStatus> status);

	@Query(c1
			+ " from DeliveryRequest a where a.transportationNeeded = true and (select count(*) from TransportationRequest b where b.deliveryRequest.id = a.id)=0 and a.requester.username = ?1 and a.status not in (?2)")
	public List<DeliveryRequest> findLightByPendingTransportation(String username, List<DeliveryRequestStatus> notInStatus);

	@Query("select count(*)  from DeliveryRequest a where a.transportationNeeded = true and (select count(*) from TransportationRequest b where b.deliveryRequest.id = a.id)=0 and a.requester.username = ?1 and a.status not in (?2)")
	public Long countByPendingTransportation(String username, List<DeliveryRequestStatus> notInStatus);

	@Query(c1 + "from DeliveryRequest a where a.isSnRequired is true and a.missingSerialNumber is true and a.warehouse.id in (?1)")
	public List<DeliveryRequest> findLightByMissingSerialNumber(List<Integer> warehouseList);

	@Query("select count(*) from DeliveryRequest a where a.isSnRequired is true and a.missingSerialNumber is true and a.warehouse.id in (?1)")
	public Long countByMissingSerialNumber(List<Integer> warehouseList);

	@Query(c1 + "from DeliveryRequest a where a.missingExpiry = true and a.warehouse.id in (?1)")
	public List<DeliveryRequest> findLightByMissingExpiry(List<Integer> warehouseList);

	@Query("select count(*) from DeliveryRequest a where a.missingExpiry = true and a.warehouse.id in (?1)")
	public Long countByMissingExpiry(List<Integer> warehouseList);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.requester.username = ?2 and a.status in (?3) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByRequester(DeliveryRequestType type, String username, List<DeliveryRequestStatus> status);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.project.manager.username = ?2 order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByProjectManager(DeliveryRequestType type, String username);

	@Query(c1 + " from DeliveryRequest a where (a.project.manager.username = ?1 or a.project.id in (?2)) and a.status = ?3 order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightToApprovePm(String username, List<Integer> delegatedProjectList, DeliveryRequestStatus requested);

	@Query("select count(*) from DeliveryRequest a where (a.project.manager.username = ?1 or a.project.id in (?2)) and a.status = ?3 order by a.neededDeliveryDate desc")
	public Long countToApprovePm(String username, List<Integer> delegatedProjectList, DeliveryRequestStatus requested);

	@Query(c1 + " from DeliveryRequest a where a.status = ?2 and  a.project.id in (select b.project.id from ProjectManager b where b.user.username = ?1) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightToApproveHm(String username, DeliveryRequestStatus approved1);

	@Query("select count(*) from DeliveryRequest a where a.status = ?2 and  a.project.id in (select b.project.id from ProjectManager b where b.user.username = ?1) order by a.neededDeliveryDate desc")
	public Long countToApproveHm(String username, DeliveryRequestStatus approved1);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.project.manager.username = ?2 and a.status in (?3) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByProjectManager(DeliveryRequestType type, String username, List<DeliveryRequestStatus> status);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.warehouse.id in (?2) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByWarehouseList(DeliveryRequestType type, List<Integer> warehouseList);

	@Query(c1 + " from DeliveryRequest a where a.warehouse.id in (?1) and a.status in (?2) and a.type != ?3 order by a.priority desc,a.neededDeliveryDate")
	public List<DeliveryRequest> findLightByWarehouseList(List<Integer> warehouseList, List<DeliveryRequestStatus> statusList, DeliveryRequestType xbound);

	@Query("select count(*)  from DeliveryRequest a where a.warehouse.id in (?1) and a.status in (?2) and a.type != ?3")
	public Long countByWarehouseList(List<Integer> warehouseList, List<DeliveryRequestStatus> statusList, DeliveryRequestType xbound);

	@Query(c1 + " from DeliveryRequest a where a.type = ?1 and a.warehouse.id in (?2) and a.status in (?3) order by a.neededDeliveryDate desc")
	public List<DeliveryRequest> findLightByWarehouseList(DeliveryRequestType type, List<Integer> warehouseList, List<DeliveryRequestStatus> status);

	@Query("select COALESCE(max(referenceNumber),0) from DeliveryRequest where type = ?1 and referenceNumber != null")
	public Integer getMaxReferenceNumber(DeliveryRequestType type);

	@Query("select new DeliveryRequest(a.id,a.referenceNumber,a.type) from DeliveryRequest a where a.requester.username = ?1 and status = ?2 and a.id not in (select b.deliveryRequest.id  from TransportationRequest b) ")
	public List<DeliveryRequest> findByCanBeTransported(String username, DeliveryRequestStatus approved);

	public Long countByOriginNumber(String originNumber);

	public Long countByTypeAndReferenceNumber(DeliveryRequestType type, Integer referenceNumber);

	@Query("select id from DeliveryRequest where type = ?1 and referenceNumber = ?2")
	public Integer findIdByTypeAndReferenceNumber(DeliveryRequestType type, Integer referenceNumber);

	@Query("from DeliveryRequest where type = ?1 and referenceNumber = ?2")
	public DeliveryRequest findByTypeAndReferenceNumber(DeliveryRequestType type, Integer referenceNumber);

	@Query("select sum(a.quantity * a.partNumber.grossWeight) from DeliveryRequestDetail a where a.deliveryRequest.id = ?1")
	public Double getGrossWeight(Integer deliveryRequestId);

	// correction destination project
	@Query("from DeliveryRequest a where a.type = ?1 and a.project.type = ?2 and a.destinationProject is null")
	public List<DeliveryRequest> findByNotHavingDestinationProject(DeliveryRequestType type, String projectType);

	// correct cross charge
	@Query("select a.id from DeliveryRequest a where a.type = ?1 and a.status in (?2) and a.project.type = ?3 and a.destinationProject is not null and (select count(*) from ProjectCross b where b.deliveryRequest.id = a.id) = 0")
	public List<Integer> findByNotHavingCrossCharge(DeliveryRequestType type, List<DeliveryRequestStatus> status, String projectType);

	@Query("select new DeliveryRequest(a.id," + totalCostCoalesce + "," + totalCrossChargeCoalesce + "," + crossChargeId
			+ ") from DeliveryRequest a where a.type = ?1 and a.status in (?2)  and a.destinationProject is not null and a.project.id != a.destinationProject.id and  " + totalCostCoalesce + "!= "
			+ totalCrossChargeCoalesce)
	public List<DeliveryRequest> findToUpdateCrossCharge(DeliveryRequestType outbound, List<DeliveryRequestStatus> status);

	@Query("select new DeliveryRequest(a.id," + totalCostCoalesce + "," + totalCrossChargeCoalesce + "," + crossChargeId
			+ ") from DeliveryRequest a where a.status = ?1  and a.outboundDeliveryRequestReturn is not null and (select idprojectcross from ProjectCross b where b.deliveryRequest.id = a.outboundDeliveryRequestReturn.id) is not null and  "
			+ totalCostCoalesce + " != " + totalCrossChargeCoalesce)
	public List<DeliveryRequest> findToUpdateCrossChargeForReturnFromOutbound(DeliveryRequestStatus status);

	// DN Financial
	String from1 = " from DeliveryRequest a ";
	// working only inbound & outbound
	String usernameCondition = " (a.requester.username = ?1 or a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.costcenter.lob.bu.director.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3)) ";
	String companyCondition = " (a.project.costcenter.lob.bu.company.id = ?4) ";
	String projectCondition = " (a.project.type = ?5) ";
	String totalCost = " (select sum(b.quantity * b.unitCost) from DeliveryRequestDetail b where  b.deliveryRequest.id = a.id) ";
	String totalCostCoalesce = "COALESCE(" + totalCost + ",0)";
	String totalPrice = " (select sum(b.quantity * b.unitPrice) from DeliveryRequestDetail b where  b.deliveryRequest.id = a.id) ";
	String totalPriceCoalesce = "COALESCE(" + totalPrice + ",0)";
	String totalRevenue = " (select sum(b.amount*b.acceptance.oldInvoiceTerm.po.madConversionRate) from AppLink b where b.revenueType is not null and b.deliveryRequest.id = a.id) ";
	String totalCrossCharge = " (select sum(b.amount) from ProjectCross b where b.deliveryRequest.id = a.id) ";
	String totalCrossChargeCoalesce = "COALESCE(" + totalCrossCharge + ",0)";
	String associatedCostIbuy = " (select sum(b.amount*b.acceptance.oldInvoiceTerm.po.madConversionRate) from AppLink b where b.costType is not null and b.deliveryRequest.id = a.id) ";
	String associatedCostIexpense = " (select sum(b.amount*b.expensepayment.budgetdetail.budget.madConversionRate) from AppLink b where b.costType is not null and b.deliveryRequest.id = a.id) ";
	String crossChargeId = " (select b.idprojectcross from ProjectCross b where b.deliveryRequest.id = a.id) ";
	String select3 = "select new DeliveryRequest(a.id,a.reference,a.referenceNumber,a.type,a.status,a.project,a.destinationProject,a.destinationProject.customer.name,a.date4, " + totalCost + ","
			+ totalRevenue + ", " + totalCrossCharge + ",(select b.numeroInvoice from Po b where a.po.id = b.id)) ";
	String select4 = "select new DeliveryRequest(a.id,a.reference,a.referenceNumber,a.type,a.status,a.project,a.date4, " + totalCost + ", " + associatedCostIbuy + "," + associatedCostIexpense
			+ ",a.inboundType)";

	@Query(select3 + from1 + " where " + usernameCondition + " and " + companyCondition + " and " + projectCondition + " and a.type = ?6 and a.status not in (?7) order by date4 desc")
	public List<DeliveryRequest> findOutboundFinancialByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, String projectTypeStock,
			DeliveryRequestType outbound, List<DeliveryRequestStatus> excludeStatusList);

	@Query(select4 + from1 + " where " + usernameCondition
			+ " and a.company.id = ?4 and a.type = ?5 and a.inboundType = ?6 and a.status in (?7) and (select count(*) from AppLink b where b.deliveryRequest.id = a.id) > 0 order by date4 desc")
	public List<DeliveryRequest> findInboundFinancialByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, DeliveryRequestType inbound,
			InboundType inboundType, List<DeliveryRequestStatus> statusList);

	@Query("from DeliveryRequest a where a.outboundDeliveryRequestReturn.id = ?1 ")
	public List<DeliveryRequest> findByOutboundDeliveryRequestReturn(Integer outboundDeliveryRequestId);

	@Query("from DeliveryRequest a where a.outboundDeliveryRequestTransfer.id = ?1 ")
	public List<DeliveryRequest> findByOutboundDeliveryRequestTransfer(Integer outboundDeliveryRequestId);

	@Query("select count(*) from DeliveryRequest a where a.project.id = ?1 ")
	public Long countByProject(Integer projectId);

	@Modifying
	@Query("update DeliveryRequest set po = ?2 where id = ?1")
	public void updatePo(Integer id, Po po);

	@Modifying
	@Query("update DeliveryRequest set neededDeliveryDate = ?2 where id = ?1")
	public void updateNeededDeliveryDate(Integer id, Date neededDeliveryDate);

	@Modifying
	@Query("update DeliveryRequest set smsRef = ?2 where id = ?1")
	public void updateSmsRef(Integer id, String smsRef);

	@Modifying
	@Query("update DeliveryRequest set transportationNeeded = ?2 where id = ?1")
	public void updateTransportationNeeded(Integer id, Boolean transportationNeeded);

	@Query("select count(*) from DeliveryRequest where outboundDeliveryRequestTransfer.id = ?1")
	public Long countByOutboundDeliveryRequestTransfer(Integer outboundDeliveryRequestId);

	@Query("select count(*) from DeliveryRequest where outboundDeliveryRequestReturn.id = ?1")
	public Long countByOutboundDeliveryRequestReturn(Integer outboundDeliveryRequestId);

	@Query("select id from DeliveryRequest where qrKey is null")
	public List<Integer> findIdListWithoutQrKey();

	@Modifying
	@Query("update DeliveryRequest set qrKey = ?2 where id = ?1")
	public void updateQrKey(Integer id, String qrKey);

	@Modifying
	@Query("update DeliveryRequest set isSnRequired = ?2 where id = ?1")
	public void updateIsSnRequired(Integer id, Boolean isSnRequired);

	@Modifying
	@Query("update DeliveryRequest set isFullyReturned = ?2 where id = ?1")
	public void updateIsFullyReturned(Integer id, Boolean isFullyReturned);

	@Query("select distinct a.outboundDeliveryRequestReturn.id from DeliveryRequest a where a.outboundDeliveryRequestReturn.id is not null")
	public List<Integer> findOutboundDeliveryRequestReturnIdList();

	@Query("select new DeliveryRequest(id,referenceNumber,type) from DeliveryRequest where outboundDeliveryRequestTransfer.id = ?1")
	public DeliveryRequest findLightAssociatedInboundTransfer(Integer outboundDeliveryRequestId);

	@Query("select " + totalCostCoalesce + " from DeliveryRequest a where a.id = ?1")
	public Double getTotalCost(Integer id);

	@Query("select " + totalPriceCoalesce + " from DeliveryRequest a where a.id = ?1")
	public Double getTotalPrice(Integer id);

	@Query("select endCustomer.name from DeliveryRequest a where a.id = ?1 ")
	public String getEndCustomerName(Integer id);

	@Modifying
	@Query("update DeliveryRequest set missingSerialNumber = ?2 where id = ?1")
	public void updateMissingSerialNumber(Integer id, Boolean missingSerialNumber);

	@Modifying
	@Query("update DeliveryRequest set missingExpiry = ?2 where id = ?1")
	public void updateMissingExpiry(Integer id, Boolean missingExpiry);

	@Modifying
	@Query("update DeliveryRequest set isForTransfer = ?2 where id = ?1")
	public void updateIsForTransfer(Integer id, Boolean isForTransfer);

	@Modifying
	@Query("update DeliveryRequest set requestDate = ?2 where id = ?1")
	public void updateRequestDate(Integer id, Date requestDate);

	@Modifying
	@Query("update DeliveryRequest set requestFrom = ?2 where id = ?1")
	public void updateRequestFrom(Integer id, String requestFrom);

	@Modifying
	@Query("update DeliveryRequest set externalRequester = ?2 where id = ?1")
	public void updateExternalRequester(Integer id, User user);

	@Modifying
	@Query("update DeliveryRequest set sdm = ?2 where id = ?1")
	public void updateSdm(Integer id, Boolean sdm);

	// update countIssues
	@Modifying
	@Query("update DeliveryRequest a set a.countIssues1 = (select count(*) from Issue b where b.deliveryRequest.id = a.id and b.status in (?2) and b.blocking is true) where id = ?1")
	void updateCountIssues1(Integer id, List<IssueStatus> issueStatusList);

	@Modifying
	@Query("update DeliveryRequest a set a.countIssues2 = (select count(*) from Issue b where b.deliveryRequest.id = a.id and b.status in (?2) and b.blocking is false) where id = ?1")
	void updateCountIssues2(Integer id, List<IssueStatus> issueStatusList);

	@Modifying
	@Query("update DeliveryRequest a set a.countIssues3 = (select count(*) from Issue b where b.deliveryRequest.id = a.id and b.status in (?2)) where id = ?1")
	void updateCountIssues3(Integer id, List<IssueStatus> issueStatusList);

	@Query("select distinct deliveryRequest.id from BoqMapping where deliveryRequest.type = ?1")
	List<Integer> findIdByTypeAndHavingBoqMapping(DeliveryRequestType type);

	@Query("select status from DeliveryRequest where id = ?1")
	DeliveryRequestStatus findStatusById(Integer id);

	@Modifying
	@Query("update DeliveryRequest a set hardwareSwapInboundId = ?2,hardwareSwapInboundStatus = ?3 where id = ?1")
	void updateHardwareSwapInboundIdAndStatus(Integer outboundId, Integer inboundId, DeliveryRequestStatus inboundStatus);

	@Query("select count(*) from DeliveryRequest a where a.hardwareSwapInboundId = ?1")
	Long countByHardwareSwapInboundId(Integer inboundId);

	@Modifying
	@Query("update DeliveryRequest a set pendingJrMapping = ?2 where id  = ?1")
	void updatePendingJrMapping(Integer id, Boolean pendingJrMapping);

	@Modifying
	@Query("update DeliveryRequest a set havingRunningStock = ?2 where id  = ?1")
	void updateHavingRunningStock(Integer id, Boolean havingRunningStock);

	@Query("select a.id from DeliveryRequest a where a.status in ('APPROVED2','PARTIALLY_DELIVRED') and a.type in ('INBOUND','OUTBOUND') and a.neededDeliveryDate < current_date")
	List<Integer> findDeliveryOverdue();

	@Query("select a.requester.fullName from DeliveryRequest a where a.id = ?1")
	String findRequesterFullName(Integer id);
	
	
	@Query("from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' ")
	List<DeliveryRequest> ackOldDeliveryRequestsScript();

	@Query("select id from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and datediff(current_date,a.date4) > 5 ")
	List<Integer> findPendingAcknowledgementIdList();
	
	@Query("select count(*) from DeliveryRequest a where a.type = 'OUTBOUND' and a.status = 'DELIVRED' and datediff(current_date,a.date4) > 5 and a.requester.username = ?1")
	Long countPendingAcknowledgementIdList(String requesterUsername);

	// mobile

	String cm1 = "select new ma.azdad.mobile.model.DeliveryRequest(" + "a.id, a.reference, a.type, a.neededDeliveryDate, a.date4, "
			+ "a.inboundType, a.status, a.isForReturn, a.isForTransfer, a.requester.fullName, " + "a.project.id, a.project.name, "
			+ "a.destinationProject.id, (select b.name from Project b where b.id = a.destinationProject.id), " + "a.warehouse.id, a.warehouse.name, "
			+ "a.destination.id, (select b.name from Site b where b.id = a.destination.id), " + "a.origin.id, (select b.name from Site b where b.id = a.origin.id), "
			+ "a.requester.photo, a.deliveryDate, a.transportationNeeded, a.isSnRequired, " + "company.name, supplier.name, customer.name, a.ownerType,a.approximativeStoragePeriod) " + // NOTE: space at the end
			"from DeliveryRequest a " + "left join a.company company " + "left join a.supplier supplier " + "left join a.customer customer ";

	@Query(cm1 + "  where a.id = ?1")
	ma.azdad.mobile.model.DeliveryRequest findOneLightMobile(Integer id);

	@Query(cm1 + " where a.warehouse.id in (?1) and a.status in (?2) and a.type != ?3 order by a.priority desc,a.neededDeliveryDate")
	public List<ma.azdad.mobile.model.DeliveryRequest> findLightByWarehouseListMobile(List<Integer> warehouseList, List<DeliveryRequestStatus> status, DeliveryRequestType xbound);

	@Query("select count(*) from DeliveryRequest a where a.warehouse.id in (?1) and a.status in (?2) and a.type != ?3 order by a.priority desc,a.neededDeliveryDate")
	public Long countByWarehouseListMobile(List<Integer> warehouseList, List<DeliveryRequestStatus> status, DeliveryRequestType xbound);

	@Query(cm1 + " where a.warehouse.id in (?1) and a.status in (?2) and a.type != ?3 order by a.priority desc,a.neededDeliveryDate")
	public List<ma.azdad.mobile.model.DeliveryRequest> findLightNewByWarehouseListMobile(List<Integer> warehouseList, List<DeliveryRequestStatus> status, DeliveryRequestType xbound);

	@Query(cm1 + "  where a.warehouse.id in (?1) and a.status in (?2) and a.type != ?3 order by a.date4 desc")
	public List<ma.azdad.mobile.model.DeliveryRequest> findLightDeliveredByWarehouseListMobile(List<Integer> warehouseList, List<DeliveryRequestStatus> status, DeliveryRequestType xbound);

	@Query(cm1 + " where a.missingSerialNumber = true and a.warehouse.id in (?1)")
	public List<ma.azdad.mobile.model.DeliveryRequest> findLightByMissingSerialNumberMobile(List<Integer> warehouseList);

	@Query("select count(*) from DeliveryRequest a where a.missingSerialNumber = true and a.warehouse.id in (?1)")
	public Long countByMissingSerialNumberMobile(List<Integer> warehouseList);

	@Query(cm1 + " where a.missingExpiry = true and a.warehouse.id in (?1)")
	public List<ma.azdad.mobile.model.DeliveryRequest> findLightByMissingExpiryMobile(List<Integer> warehouseList);

	@Query("select count(*) from DeliveryRequest a where a.missingExpiry = true and a.warehouse.id in (?1)")
	public Long countByMissingExpiryMobile(List<Integer> warehouseList);

	@Query(cm1
			+ " where a.missingOutboundDeliveryNote is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.toUser.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))  order by a.id desc")
	public List<ma.azdad.mobile.model.DeliveryRequest> findByMissingOutboundDeliveryNoteFileMobile(String username, Collection<Integer> warehouseList, Collection<Integer> projectIdList);

	@Query("select count(*) from DeliveryRequest a where a.missingOutboundDeliveryNote is true and (a.requester.username = ?1 or a.project.manager.username = ?1 or a.toUser.username = ?1 or a.warehouse.id in (?2) or a.project.id in (?3))  order by a.id desc")
	public Long countByMissingOutboundDeliveryNoteFileMobile(String username, Collection<Integer> warehouseList, Collection<Integer> projectIdList);

	@Query("select new ma.azdad.mobile.model.DeliveryRequestHistory(a.id,a.date,a.status,a.description,u.fullName,u.photo) from DeliveryRequestHistory a left join a.user as u where a.parent.id = ?1")
	List<ma.azdad.mobile.model.DeliveryRequestHistory> findHistoryListMobile(Integer id);

	@Query("select distinct a.deliveryRequest.id from DeliveryRequestDetail a where a.partNumber.expirable is true and a.deliveryRequest.status not in ('REJECTED','CANCELED')")
	List<Integer> findByHavingExpirableItems();

	@Query("select distinct a.deliveryRequest.id from DeliveryRequestDetail a where a.partNumber.id = ?1")
	List<Integer> findByHavingPartNumber(Integer partNumberId);

}
