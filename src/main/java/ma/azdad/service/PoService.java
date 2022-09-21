package ma.azdad.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.CostType;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Po;
import ma.azdad.model.PoBoqStatus;
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
	private PoRepos repos;

	@Autowired
	private PodetailsRepos podetailsRepos;

	@Autowired
	private BoqMappingService boqMappingService;

	@Autowired
	private BoqService boqService;

	@Autowired
	private CacheService cacheService;

	public Po findOne(Integer id) {
		return repos.findById(id).get();
	}

	public List<Po> findByTypeAndProjectAndNotMapped(String type, Integer projectId) {
		return repos.findByTypeAndProjectAndNotBoqStatus(type, projectId, PoBoqStatus.MAPPED, Arrays.asList(PoStatus.REJECTED, PoStatus.CLOSED));
	}

	public void updateBoqStatus(Integer poId) {
		System.out.println("updateBoqStatus poId: " + poId);
		PoBoqStatus boqStatus = null;
		if (boqService.countByPo(poId) == 0)
			boqStatus = null;
		else if (boqMappingService.countByPo(poId) == 0 && boqService.countByPo(poId) > 0)
			boqStatus = PoBoqStatus.PENDING;
		else {
			List<Podetails> podetailsList = podetailsRepos.findByPoAndHavingBoq(poId);
			Boolean ibuy = repos.getIbuy(poId);
			if (!ibuy)
				if (podetailsList.stream().filter(i -> RevenueType.GOODS_SUPPLY.equals(i.getRevenueType())
						&& (!i.getIsBoqMapped() || boqService.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(i.getIdpoDetails()) > 0)).count() > 0)
					boqStatus = PoBoqStatus.IN_PROGRESS;
				else
					boqStatus = PoBoqStatus.MAPPED;
			else {
				if (podetailsList.stream().filter(i -> CostType.PROJECT_GOODS_PURCHASE.equals(i.getCostType())
						&& (!i.getIsBoqMapped() || boqService.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(i.getIdpoDetails()) > 0)).count() > 0)
					boqStatus = PoBoqStatus.IN_PROGRESS;
				else
					boqStatus = PoBoqStatus.MAPPED;
			}

		}
		repos.updateBoqStatus(poId, boqStatus);
		cacheService.evictCache("poService");
		cacheService.evictCacheOthers("poService");
	}

	public void updateAllBoqStatusAndDeliveryStatusScript() {
		Set<Integer> sourceList = new HashSet<>();
		sourceList.addAll(repos.findPoIdListContainingGoodsSupply(RevenueType.GOODS_SUPPLY));
		sourceList.addAll(repos.findPoIdListContainingProjectGoodsPurchase(CostType.PROJECT_GOODS_PURCHASE));

		for (Integer poId : sourceList) {
			updateBoqStatus(poId);
			updateDeliveryStatus(poId);
		}

	}

	public void updateDeliveryStatus(Integer poId) {
		PoDeliveryStatus deliveryStatus = null;
		PoBoqStatus boqStatus = repos.getBoqStatus(poId);
		if (PoBoqStatus.MAPPED.equals(boqStatus)) {
			if (boqMappingService.countDeliveryRequestsByRelatedToPoAndNotInStatus(poId, // means all dn are DELIVRED
					Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)) == 0)
				deliveryStatus = PoDeliveryStatus.DELIVRED;
			else if (boqMappingService.countDeliveryRequestsByRelatedToPoAndInStatus(poId, // at least one dn DELIVRED or PARTIALLY_DELIVRED
					Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)) > 0)
				deliveryStatus = PoDeliveryStatus.PARTIALLY_DELIVRED;
		} else if (PoBoqStatus.IN_PROGRESS.equals(boqStatus))
			if (boqMappingService.countDeliveryRequestsByRelatedToPoAndInStatus(poId, // at least one dn DELIVRED or PARTIALLY_DELIVRED
					Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)) > 0)
				deliveryStatus = PoDeliveryStatus.PARTIALLY_DELIVRED;
		repos.updateDeliveryStatus(poId, deliveryStatus);
		cacheService.evictCache("poService");
		cacheService.evictCacheOthers("poService");
	}

	public List<Po> find(Boolean ibuy,Integer companyId, String username, List<Integer> assignedProjectList) {
		if (ibuy)
			return repos.findSupplierPoList(companyId,username, assignedProjectList);
		else
			return repos.findCustomerPoList(companyId,username, assignedProjectList);
	}

}
