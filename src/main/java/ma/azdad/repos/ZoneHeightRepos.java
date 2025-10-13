package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ZoneHeight;

@Repository
public interface ZoneHeightRepos extends JpaRepository<ZoneHeight, Integer> {

	@Query("from ZoneHeight a where a.column.line.location.id = ?1")
	List<ZoneHeight> findByLocation(Integer locationId);
	
	@Query("select distinct a.zoneHeight.id from StockRowDetail a where a.stockRow.deliveryRequest.id = ?1 ")
	List<Integer> findIdListByDeliveryRequest(Integer deliveryRequestId);

	@Query("select column.line.location.slotSize from ZoneHeight where id = ?1")
	Double findSlotSize(Integer id);

	@Query("select sum( (quantity - usedQuantity) * packingDetail.volume / packingDetail.storageFactor) from StockRowDetail  where inboundStockRow is null and zoneHeight.id = ?1")
	Double findTotalUsedVolume(Integer id);
	
	@Query("select reference from ZoneHeight where id = ?1")
	String findReferenceById(Integer id);
	
	
	@Modifying
	@Query("update ZoneHeight set usedVolume = ?2 where id = ?1 ")
	void updateUsedVolume(Integer id, Double usedVolume);

	@Modifying
	@Query("update ZoneHeight set fillPercentage = ?2 where id = ?1 ")
	void updateFillPercentage(Integer id, Double fillPercentage);

}
