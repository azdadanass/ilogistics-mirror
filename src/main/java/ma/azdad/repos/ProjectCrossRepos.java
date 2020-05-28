package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ProjectCross;

@Repository
public interface ProjectCrossRepos extends JpaRepository<ProjectCross, Integer> {

	@Query("from ProjectCross a where a.deliveryRequest.id = ?1")
	public ProjectCross findByDeliveryRequest(Integer deliveryRequestId);

	@Query("select count(*) from ProjectCross a where a.deliveryRequest.id = ?1")
	public Long countByDeliveryRequest(Integer deliveryRequestId);

	@Modifying
	@Query("update ProjectCross a set a.amount = ?2,a.cashAmount = ?2 * 1.2  where a.id = ?1")
	public void updateAmount(Integer id, Double amount);

	@Modifying
	@Query("delete from ProjectCross a where a.deliveryRequest.id = ?1")
	public void deleteByDeliveryRequest(Integer deliveryRequestId);

}
