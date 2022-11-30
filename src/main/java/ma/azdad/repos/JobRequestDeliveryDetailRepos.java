package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.JobRequestDeliveryDetail;

@Repository
public interface JobRequestDeliveryDetailRepos extends JpaRepository<JobRequestDeliveryDetail, Integer> {

	String select1 = "select new JobRequestDeliveryDetail(a.installedQuantity,a.isSerialNumberRequired,a.deliveryRequestDetail.id,a.deliveryRequestDetail.partNumber.name,a.deliveryRequestDetail.partNumber.image,a.deliveryRequestDetail.partNumber.description,a.deliveryRequestDetail.deliveryRequest.id,a.deliveryRequestDetail.deliveryRequest.referenceNumber,a.deliveryRequestDetail.deliveryRequest.type,a.jobRequest.id,a.jobRequest.site.name,a.jobRequest.team.name)";

	@Query(select1 + " from JobRequestDeliveryDetail a where a.jobRequest.project.id = ?1 and a.installedQuantity > 0")
	public List<JobRequestDeliveryDetail> findInstalledByProject(Integer projectId);

	@Query("select count(*) from JobRequestDeliveryDetail where deliveryRequestDetail.deliveryRequest.id = ?1")
	Long countByDeliveryRequest(Integer deliveryRequestId);
}
