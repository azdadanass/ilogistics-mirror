package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SerialNumber;

@Repository
public interface SerialNumberRepos extends JpaRepository<SerialNumber, Integer> {

	@Query("from SerialNumber a where a.jobRequest.id = ?1 and a.deliveryRequestDetail.id = ?2")
	public List<SerialNumber> findByJobRequestAndDeliveryRequestDetail(Integer jobRequestId, Integer deliveryRequestDetailId);

}
