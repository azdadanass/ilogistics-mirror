package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.StockRowDetail;
import ma.azdad.repos.StockRowDetailRepos;

@Component
public class StockRowDetailService extends GenericService<Integer, StockRowDetail, StockRowDetailRepos> {

	@Cacheable("stockRowDetailService.findAll")
	public List<StockRowDetail> findAll() {
		return repos.findAll();
	}

	public StockRowDetail findOne(Integer id) {
		StockRowDetail stockRowDetail = super.findOne(id);
		
		
		
		
		return stockRowDetail;
	}

	public List<StockRowDetail>  findByDeliveryRequestDetailListAndNotFullyUsed(List<Integer> deliveryRequestDetailIdList){
		List<StockRowDetail> result =  repos.findByDeliveryRequestDetailListAndNotFullyUsed(deliveryRequestDetailIdList);
		result.forEach(i->{
			initialize(i.getStockRow().getDeliveryRequestDetail());
			i.getStockRow().getPacking().getDetailList().forEach(j->initialize(j));
		});
		return result;
	}
}
