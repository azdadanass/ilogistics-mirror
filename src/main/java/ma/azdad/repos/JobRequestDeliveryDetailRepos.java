package ma.azdad.repos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.JobRequestDeliveryDetail;

@Repository
public interface JobRequestDeliveryDetailRepos extends JpaRepository<JobRequestDeliveryDetail, Integer> {
	String deliverToCompanyName = "(select b.name from Company b where b.id = a.deliveryRequestDetail.deliveryRequest.deliverToCompany.id)";
	String deliverToCustomerName = "(select b.name from Customer b where b.id = a.deliveryRequestDetail.deliveryRequest.deliverToCustomer.id)";
	String deliverToSupplierName = "(select b.name from Supplier b where b.id = a.deliveryRequestDetail.deliveryRequest.deliverToSupplier.id)";
	String toUserFullName = "(select b.fullName from User b where b.username = a.deliveryRequestDetail.deliveryRequest.toUser.username)";

	String c1 = "select new JobRequestDeliveryDetail(a.installedQuantity,a.isSerialNumberRequired,a.deliveryRequestDetail.id," //
			+ "a.deliveryRequestDetail.partNumber.id,a.deliveryRequestDetail.partNumber.name,a.deliveryRequestDetail.partNumber.image,a.deliveryRequestDetail.partNumber.description,"//
			+ "a.deliveryRequestDetail.deliveryRequest.id,a.deliveryRequestDetail.deliveryRequest.referenceNumber,a.deliveryRequestDetail.deliveryRequest.type,"//
			+ "a.jobRequest.id,a.jobRequest.site.name,a.jobRequest.team.name,"//
			+ "a.deliveryRequestDetail.deliveryRequest.deliverToCompanyType," + deliverToCompanyName + "," + deliverToCustomerName + "," + deliverToSupplierName + ","
			+ toUserFullName + ") ";

	@Query(c1 + "from JobRequestDeliveryDetail a where a.jobRequest.project.id = ?1 and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByProject(Integer projectId);

	@Query(c1
			+ "from JobRequestDeliveryDetail a left join a.deliveryRequestDetail.deliveryRequest.company as company1 left join a.deliveryRequestDetail.inboundDeliveryRequest.company as company2 " //
			+ "where (company1.id = ?1 or company2.id = ?1) "//
			+ "and a.deliveryRequestDetail.deliveryRequest.id in (?2) " //
			+ "and a.deliveryRequestDetail.partNumber.id in (?3) " //
			+ "and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByCompanyOwner(Integer companyId, Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList);

	@Query(c1
			+ "from JobRequestDeliveryDetail a left join a.deliveryRequestDetail.deliveryRequest.customer as customer1 left join a.deliveryRequestDetail.inboundDeliveryRequest.customer as customer2 " //
			+ "where (customer1.id = ?1 or customer2.id = ?1) "//
			+ "and a.deliveryRequestDetail.deliveryRequest.id in (?2) " //
			+ "and a.deliveryRequestDetail.partNumber.id in (?3) " //
			+ "and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByCustomerOwner(Integer customerId, Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList);

	@Query("select count(*) from JobRequestDeliveryDetail where deliveryRequestDetail.deliveryRequest.id = ?1")
	Long countByDeliveryRequest(Integer deliveryRequestId);
	
	@Modifying
	@Query("delete from JobRequestDeliveryDetail where deliveryRequestDetail.id in (select distinct b.id from DeliveryRequestDetail b where b.deliveryRequest.id = ?1)")
	void deleteByDeliveryRequest(Integer deliveryRequestId);
}
