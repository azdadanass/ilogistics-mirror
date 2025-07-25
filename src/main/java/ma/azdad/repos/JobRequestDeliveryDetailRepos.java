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
	String deliverToCompanyName = "(select b.name from Company b where b.id = a.deliveryRequest.deliverToCompany.id)";
	String deliverToCustomerName = "(select b.name from Customer b where b.id = a.deliveryRequest.deliverToCustomer.id)";
	String deliverToSupplierName = "(select b.name from Supplier b where b.id = a.deliveryRequest.deliverToSupplier.id)";
	String toUserFullName = "(select b.fullName from User b where b.username = a.deliveryRequest.toUser.username)";
	String inboundPoNumero = "(select b.numero from Po b where b.id = a.deliveryRequest.inboundPo.id)";
	String teamName = "(select b.name from Team b where b.id = a.jobRequest.team.id)";

	String c1 = "select new JobRequestDeliveryDetail(a.quantity,a.installedQuantity,a.isSerialNumberRequired," //
			+ "a.partNumber.id,a.partNumber.name,a.partNumber.image,a.partNumber.description,"//
			+ "a.deliveryRequest.id,a.deliveryRequest.reference,a.deliveryRequest.type,"//
			+ "a.jobRequest.id,a.jobRequest.reference,a.jobRequest.status,a.jobRequest.site.name,"+teamName+","//
			+ "a.deliveryRequest.deliverToCompanyType," + deliverToCompanyName + "," + deliverToCustomerName + "," + deliverToSupplierName + "," + toUserFullName + ","
			+ inboundPoNumero + ") ";

	@Query(c1 + "from JobRequestDeliveryDetail a where a.jobRequest.project.id = ?1 and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByProject(Integer projectId);

	@Query(c1 + "from JobRequestDeliveryDetail a where a.deliveryRequest.id = ?1 and a.jobRequest.status not in ('REJECTED','CANCELED')")
	List<JobRequestDeliveryDetail> findByDeliveryRequest(Integer deliveryRequestId);

	@Query(c1 + "from JobRequestDeliveryDetail a where a.deliveryRequest.id = ?1 and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByDeliveryRequest(Integer deliveryRequest);

//	@Query(c1
//			+ "from JobRequestDeliveryDetail a left join a.deliveryRequest.company as company1 left join a.deliveryRequest.inboundDeliveryRequest.company as company2 " //
//			+ "where (company1.id = ?1 or company2.id = ?1) "//
//			+ "and a.deliveryRequest.id in (?2) " //
//			+ "and a.partNumber.id in (?3) " //
//			+ "and a.installedQuantity > 0")
//	List<JobRequestDeliveryDetail> findInstalledByCompanyOwner(Integer companyId, Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList);

	@Query(c1 + "from JobRequestDeliveryDetail a " //
			+ "where a.deliveryRequest.company.id = ?1 "//
			+ "and a.deliveryRequest.id in (?2) " //
			+ "and a.partNumber.id in (?3) " //
			+ "and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByCompanyOwner(Integer companyId, Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList);

	@Query(c1 + "from JobRequestDeliveryDetail a " //
			+ "where a.deliveryRequest.customer.id = ?1 "//
			+ "and a.deliveryRequest.id in (?2) " //
			+ "and a.partNumber.id in (?3) " //
			+ "and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByCustomerOwner(Integer customerId, Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList);

	@Query("select count(*) from JobRequestDeliveryDetail a where a.deliveryRequest.id = ?1")
	Long countByDeliveryRequest(Integer deliveryRequestId);

	@Modifying
	@Query("delete from JobRequestDeliveryDetail where deliveryRequest.id = ?1")
	void deleteByDeliveryRequest(Integer deliveryRequestId);

	@Query("select distinct a.jobRequest.id from JobRequestDeliveryDetail a where a.jobRequest.date6 is null and a.deliveryRequest.id = ?1")
	List<Integer> findNotStartedJobRequestIdListByDeliveryRequest(Integer deliveryRequest);

	@Modifying
	@Query("delete from JobRequestDeliveryDetail where deliveryRequest.id = ?1 and jobRequest.id in (?2)")
	void deleteByDeliveryRequestAndJobRequestList(Integer deliveryRequestId, List<Integer> jobRequestList);
}
