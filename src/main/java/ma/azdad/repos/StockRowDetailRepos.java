package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.StockRowDetail;

@Repository
public interface StockRowDetailRepos extends JpaRepository<StockRowDetail, Integer> {

	@Query("from StockRowDetail a where a.stockRow.deliveryRequestDetail.id in (?1) and a.quantity > a.usedQuantity")
	List<StockRowDetail> findByDeliveryRequestDetailListAndNotFullyUsed(List<Integer> deliveryRequestDetailIdList);
	
	
	@Query("select id,usedQuantity from StockRowDetail where id in (?1)")
	List<Object[]> findUsedQunantityMap(List<Integer> idList);

}
