package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.StockRowDetail;

@Repository
public interface StockRowDetailRepos extends JpaRepository<StockRowDetail, Integer> {
	
	String c1 = "select new StockRowDetail(a.id,a.quantity,a.usedQuantity,a.stockRow.status,a.packingDetail.name,a.packingDetail.type,a.stockRow.partNumber.name,a.stockRow.partNumber.image,a.stockRow.partNumber.description,a.stockRow.deliveryRequest.id,a.stockRow.deliveryRequest.reference) ";

	@Query("from StockRowDetail a where a.stockRow.deliveryRequestDetail.id in (?1) and a.quantity > a.usedQuantity")
	List<StockRowDetail> findByDeliveryRequestDetailListAndNotFullyUsed(List<Integer> deliveryRequestDetailIdList);
	
	@Query("select id,usedQuantity from StockRowDetail where id in (?1)")
	List<Object[]> findUsedQunantityMap(List<Integer> idList);
	
	
	@Query(c1+" from StockRowDetail a where a.zoneHeight.id = ?1 and a.quantity > a.usedQuantity")
	List<StockRowDetail> findRemainingByZoneHight(Integer zoneHeightId);

}
