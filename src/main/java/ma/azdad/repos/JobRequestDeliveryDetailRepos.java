package ma.azdad.repos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
			+ "a.deliveryRequestDetail.partNumber.name,a.deliveryRequestDetail.partNumber.image,a.deliveryRequestDetail.partNumber.description,"//
			+ "a.deliveryRequestDetail.deliveryRequest.id,a.deliveryRequestDetail.deliveryRequest.referenceNumber,a.deliveryRequestDetail.deliveryRequest.type,"//
			+ "a.jobRequest.id,a.jobRequest.site.name,a.jobRequest.team.name,"//
			+ "a.deliveryRequestDetail.deliveryRequest.deliverToCompanyType," + deliverToCompanyName + "," + deliverToCustomerName + "," + deliverToSupplierName + ","
			+ toUserFullName + ") ";

	@Query(c1 + "from JobRequestDeliveryDetail a where a.jobRequest.project.id = ?1 and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalledByProject(Integer projectId);

	@Query(c1 + "from JobRequestDeliveryDetail a where " //
			+ "a.deliveryRequestDetail.deliveryRequest.id in (?1) " //
			+ "and a.deliveryRequestDetail.partNumber.id in (?2) " //
			+ "and a.installedQuantity > 0")
	List<JobRequestDeliveryDetail> findInstalled(Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList);

	@Query("select count(*) from JobRequestDeliveryDetail where deliveryRequestDetail.deliveryRequest.id = ?1")
	Long countByDeliveryRequest(Integer deliveryRequestId);
}
