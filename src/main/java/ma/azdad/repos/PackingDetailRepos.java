package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PackingDetail;

@Repository
public interface PackingDetailRepos extends JpaRepository<PackingDetail, Integer> {

	@Query("from PackingDetail where parent.partNumber.id = ?1")
	public List<PackingDetail> findByPartNumber(Integer partNumberId);

	@Query("select count(*) from DeliveryRequestDetail a where a.deliveryRequest.id = ?1 and (select count(*) from PackingDetail b where b.parent.id = a.packing.id and b.hasSerialnumber is true) > 0 ")
	Long countByDeliveryRequestAndHasSerialNumber(Integer deliveryRequestId);
	
	
	@Query("select count(*) from PackingDetail a where a.parent.id = ?1 and a.hasSerialnumber is true")
	Long countByPackingAndHasSerialnumber(Integer packingId);

}
