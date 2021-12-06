package ma.azdad.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.CostType;
import ma.azdad.model.Po;
import ma.azdad.model.PoDeliveryStatus;
import ma.azdad.model.PoStatus;
import ma.azdad.model.Podetails;
import ma.azdad.model.RevenueType;
import ma.azdad.repos.PoRepos;
import ma.azdad.repos.PodetailsRepos;

@Component
@Transactional
public class PoService {

	@Autowired
	private PoRepos poRepos;

	@Autowired
	private PodetailsRepos podetailsRepos;

	@Autowired
	private BoqMappingService boqMappingService;

	@Autowired
	private BoqService boqService;

	public Po findOne(Integer id) {
		return poRepos.findById(id).get();
	}

	public List<Po> findByTypeAndProjectAndNotDelivered(String type, Integer projectId) {
		return poRepos.findByTypeAndProjectAndNotDeliveryStatus(type, projectId, PoDeliveryStatus.DELIVERED, Arrays.asList(PoStatus.REJECTED, PoStatus.CLOSED));
	}

	public void updateDeliveryStatus(Integer poId) {
		PoDeliveryStatus deliveryStatus = null;
		if (boqMappingService.countByPo(poId) == 0)
			deliveryStatus = PoDeliveryStatus.PENDING;
		else {
			List<Podetails> podetailsList = podetailsRepos.findByPo(poId);
			Boolean ibuy = poRepos.getIbuy(poId);
			if (!ibuy)
				if (podetailsList.stream().filter(i -> RevenueType.GOODS_SUPPLY.equals(i.getRevenueType()) && (!i.getIsBoqMapped() || boqService.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(i.getIdpoDetails()) > 0)).count() > 0)
					deliveryStatus = PoDeliveryStatus.IN_PROGRESS;
				else
					deliveryStatus = PoDeliveryStatus.DELIVERED;
			else {
				if (podetailsList.stream().filter(i -> CostType.PROJECT_GOODS_PURCHASE.equals(i.getCostType()) && (!i.getIsBoqMapped() || boqService.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(i.getIdpoDetails()) > 0)).count() > 0)
					deliveryStatus = PoDeliveryStatus.IN_PROGRESS;
				else
					deliveryStatus = PoDeliveryStatus.DELIVERED;
			}

		}
		poRepos.updateDeliveryStatus(poId, deliveryStatus);
	}

	public void updateAllDeliveryStatusScript() {
		Set<Integer> sourceList = new HashSet<>();
		sourceList.addAll(poRepos.findPoIdListContainingGoodsSupply(RevenueType.GOODS_SUPPLY));
		sourceList.addAll(poRepos.findPoIdListContainingProjectGoodsPurchase(CostType.PROJECT_GOODS_PURCHASE));

		for (Integer poId : sourceList)
			updateDeliveryStatus(poId);

	}

}
