package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestPaymentStatus;
import ma.azdad.model.TransportationRequestStatus;

@Repository
public interface TransportationRequestRepos extends JpaRepository<TransportationRequest, Integer> {

	String originName = "(select b.name from Site b where a.deliveryRequest.origin.id = b.id)";
	String destinationName = "(select b.name from Site b where a.deliveryRequest.destination.id = b.id)";
	String warehouseName = "(select b.name from Warehouse b where a.deliveryRequest.warehouse.id = b.id)";
	
	
	
	String transporterType = "(select b.type from Transporter b where b.id = a.transportationJob.transporter.id)";
	String transporterPrivateFirstName = "(select b.privateFirstName from Transporter b where b.id = a.transportationJob.transporter.id)";
	String transporterPrivateLastName = "(select b.privateLastName from Transporter b where b.id = a.transportationJob.transporter.id)";
	String transporterSupplierName = "(select b.supplier.name from Transporter b where b.id = a.transportationJob.transporter.id)";
	
	
	
	String approverFullName = " (select b.fullName from User b where b.username = a.user3.username) ";

	String originId = "(select b.id from Site b where a.deliveryRequest.origin.id = b.id)";
	String destinationId = "(select b.id from Site b where a.deliveryRequest.destination.id = b.id)";
	String warehouseId = "(select b.id from Warehouse b where a.deliveryRequest.warehouse.id = b.id)";
	String destinationProjectName = "(select b.name from Project b where b.id = a.deliveryRequest.destinationProject.id)";

	String c1 = "select new  TransportationRequest(a.id,a.reference,a.status,a.neededPickupDate,a.neededDeliveryDate,a.deliveryRequest.id,a.deliveryRequest.reference,a.deliveryRequest.smsRef,a.deliveryRequest.requester.username,a.deliveryRequest.requester.fullName," + originName + ", " + destinationName+ ", " + warehouseName + ", " + transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName + ","
			+ transporterSupplierName + ")";

	String select1 = "select new TransportationRequest(a.id,a.reference,a.status,a.deliveryRequest.id,a.deliveryRequest.reference,a.deliveryRequest.type,a.deliveryRequest.smsRef,a.deliveryRequest.requester.username,a.deliveryRequest.requester.fullName,a.neededPickupDate,a.neededDeliveryDate,a.deliveryDate," + originName + "," + destinationName+ ", " + warehouseName + "," + transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName
			+ "," + transporterSupplierName + "," + approverFullName + ",a.cost,a.totalAppLinkCost,a.paymentStatus," + destinationProjectName + ") ";
	String select2 = "select count(*) ";
	String select3 = "select new TransportationRequest(a.id,a.reference,a.status,a.deliveryRequest.reference,a.deliveryRequest.type,a.deliveryRequest.smsRef,a.deliveryRequest.requester.username,a.deliveryRequest.requester.fullName,a.neededPickupDate,a.neededDeliveryDate," + originName + "," + destinationName+ ", " + warehouseName + "," + transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName + ","
			+ transporterSupplierName + "," + originId + "," + destinationId + "," + warehouseId + ") ";

	@Query(c1 + "from TransportationRequest a" + " where a.deliveryRequest.requester.username = ?2 or a.deliveryRequest.project.manager.username = ?2 or a.deliveryRequest.project.costcenter.lob.manager.username = ?2 or a.deliveryRequest.project.id in (?1)" + " order by a.neededPickupDate")
	public List<TransportationRequest> findLight(List<Integer> assignedProjectList, String username);

	@Query(c1 + "from TransportationRequest a order by a.neededPickupDate")
	public List<TransportationRequest> findLight();

	@Query(c1 + "from TransportationRequest a where a.status = ?1 order by a.neededPickupDate")
	public List<TransportationRequest> findLight(TransportationRequestStatus status);

	@Query(select2 + "from TransportationRequest a where a.status = ?1 order by a.neededPickupDate")
	public Long count(TransportationRequestStatus status);

	@Query(c1 + "from TransportationRequest a where a.status in (?1) order by a.neededPickupDate")
	public List<TransportationRequest> findLight(List<TransportationRequestStatus> status);

	@Query(c1 + "from TransportationRequest a where a.status = ?2 and (a.deliveryRequest.requester.username = ?1 or (a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?1)  or a.deliveryRequest.project.id in (?3))")
	public List<TransportationRequest> findLight(String username, TransportationRequestStatus status, List<Integer> assignedProjectList);

	@Query(c1 + "from TransportationRequest a where a.status in (?2) and (a.deliveryRequest.requester.username = ?1 or (a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?1)  or a.deliveryRequest.project.id in (?3))")
	public List<TransportationRequest> findLight(String username, List<TransportationRequestStatus> status, List<Integer> assignedProjectList);

	@Query(c1 + "from TransportationRequest a where a.deliveryRequest.deliverToSupplier.id = ?1 and a.deliveryRequest.destinationProject.id in (?2)")
	public List<TransportationRequest> findLightBySupplierUser(Integer supplierId, List<Integer> assignedProjectList);

	@Query(c1 + "from TransportationRequest a where a.deliveryRequest.deliverToSupplier.id = ?1 and a.deliveryRequest.destinationProject.id in (?2) and a.status in (?3)")
	public List<TransportationRequest> findLightBySupplierUser(Integer supplierId, List<Integer> assignedProjectList, List<TransportationRequestStatus> status);

	@Query(c1 + "from TransportationRequest a where a.deliveryRequest.requester.username = ?1 and a.status = ?2 order by a.neededPickupDate")
	public List<TransportationRequest> findLightByRequester(String username, TransportationRequestStatus status);

	@Query(select2 + "from TransportationRequest a where a.deliveryRequest.requester.username = ?1 and a.status = ?2 order by a.neededPickupDate")
	public Long countByRequester(String username, TransportationRequestStatus status);

	@Query(c1 + "from TransportationRequest a where a.deliveryRequest.project.manager.username = ?1 and a.status = ?2 order by a.neededPickupDate")
	public List<TransportationRequest> findLightByProjectManager(String username, TransportationRequestStatus status);

	@Query(select2 + "from TransportationRequest a where a.deliveryRequest.project.manager.username = ?1 and a.status = ?2 order by a.neededPickupDate")
	public Long countByProjectManager(String username, TransportationRequestStatus status);

	@Query("from TransportationRequest where deliveryRequest.id = ?1")
	public TransportationRequest findByDeliveryRequest(Integer deliveryRequestId);
	
	@Query("select id from TransportationRequest where deliveryRequest.id = ?1")
	public Integer findIdByDeliveryRequest(Integer deliveryRequestId);

	@Query("select a from TransportationRequest a where a.id in (select b.id from TransportationRequest b where b.deliveryRequest.origin.id = ?1) or a.id in (select b.id from TransportationRequest b where b.deliveryRequest.destination.id = ?1)")
	public List<TransportationRequest> findAssociatedWithSite(Integer siteId);
	
	public Integer countByTransportationJob(TransportationJob transportationJob);

	// TR PAYMENT LISTS
	@Query(select1 + "from TransportationRequest a where a.transportationJob.status = ?1 ")
	public List<TransportationRequest> findByPaymentStatus(TransportationJobStatus transportationJobStatus);

	@Query(select1 + "from TransportationRequest a where a.transportationJob.status = ?1 and (a.deliveryRequest.requester.username = ?2 or a.deliveryRequest.project.manager.username = ?2) ")
	public List<TransportationRequest> findByPaymentStatus(TransportationJobStatus transportationJobStatus, String username);

	@Query(select1 + "from TransportationRequest a where a.transportationJob.status = ?1 and a.paymentStatus = ?2 ")
	public List<TransportationRequest> findByPaymentStatus(TransportationJobStatus transportationJobStatus, TransportationRequestPaymentStatus paymentStatus);

	@Query(select1 + "from TransportationRequest a where a.transportationJob.status = ?1 and a.paymentStatus = ?2  and (a.deliveryRequest.requester.username = ?3 or a.deliveryRequest.project.manager.username = ?3)")
	public List<TransportationRequest> findByPaymentStatus(TransportationJobStatus transportationJobStatus, TransportationRequestPaymentStatus paymentStatus, String username);

	// @Query(select1 + "from TransportationRequest a where
	// a.transportationJob.status = ?1 and a.totalAppLinkCost = 0 ")
	// public List<TransportationRequest> findPending(TransportationJobStatus
	// transportationJobStatus);
	//
	// @Query(select1
	// + "from TransportationRequest a where a.transportationJob.status = ?1 and
	// a.totalAppLinkCost = 0 and (a.deliveryRequest.requester.username = ?2 or
	// a.deliveryRequest.project.manager.username = ?2) ")
	// public List<TransportationRequest> findPending(TransportationJobStatus
	// transportationJobStatus, String username);
	//
	// @Query(select1 + "from TransportationRequest a where
	// a.transportationJob.status = ?1 and abs(a.cost-a.totalAppLinkCost) >= 0.01 ")
	// public List<TransportationRequest> findInProgress(TransportationJobStatus
	// transportationJobStatus);
	//
	// @Query(select1
	// + "from TransportationRequest a where a.transportationJob.status = ?1 and
	// abs(a.cost-a.totalAppLinkCost) >= 0.01 and
	// (a.deliveryRequest.requester.username = ?2 or
	// a.deliveryRequest.project.manager.username = ?2 ) ")
	// public List<TransportationRequest> findInProgress(TransportationJobStatus
	// transportationJobStatus, String username);
	//
	// @Query(select1 + "from TransportationRequest a where
	// a.transportationJob.status = ?1 and abs(a.cost-a.totalAppLinkCost) < 0.01 ")
	// public List<TransportationRequest> findPaid(TransportationJobStatus
	// transportationJobStatus);
	//
	// @Query(select1
	// + "from TransportationRequest a where a.transportationJob.status = ?1 and
	// abs(a.cost-a.totalAppLinkCost) < 0.01 and
	// (a.deliveryRequest.requester.username = ?2 or
	// a.deliveryRequest.project.manager.username = ?2 )")
	// public List<TransportationRequest> findPaid(TransportationJobStatus
	// transportationJobStatus, String username);

	// ASSIGN TO TR JOB
	@Query(select3 + " from TransportationRequest a where a.transportationJob is null and a.status = ?1 and a.deliveryRequest.status in (?2) order by a.neededPickupDate")
	public List<TransportationRequest> findByNotHavingTransportationJob(TransportationRequestStatus status, List<DeliveryRequestStatus> deliveryRequestStatus);

	// CORRECTION
	@Query(" from TransportationRequest a where a.transportationJob is null and a.status in (?1) order by a.neededPickupDate")
	public List<TransportationRequest> findByNotHavingTransportationJob(List<TransportationRequestStatus> status);

	// UPDATE TR PAYMENT STATUS
	@Query("select a.totalAppLinkCost from TransportationRequest a where a.id = ?1")
	public Double getTotalAppLinkCost(Integer transportationRequestId);

	@Query("select a.paymentStatus from TransportationRequest a where a.id = ?1")
	public TransportationRequestPaymentStatus getPaymentStatus(Integer transportationRequestId);

	@Query("select a.cost from TransportationRequest a where a.id = ?1")
	public Double getCost(Integer transportationRequestId);

	@Query("select a.transportationJob.status from TransportationRequest a where a.id = ?1")
	public TransportationJobStatus getTransportationJobStatus(Integer transportationRequestId);

	@Modifying
	@Query("update TransportationRequest a set a.paymentStatus = ?2 where a.id = ?1")
	public void updatePaymentStatus(Integer transportationRequestId, TransportationRequestPaymentStatus paymentStatus);

	@Query("select a.id from TransportationRequest a where a.transportationJob.status = ?1")
	public List<Integer> findIdList(TransportationJobStatus transportationJobStatus);

}
