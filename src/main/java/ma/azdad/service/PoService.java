package ma.azdad.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.CostType;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Po;
import ma.azdad.model.PoIlogisticsStatus;
import ma.azdad.model.PoDeliveryStatus;
import ma.azdad.model.PoFile;
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
		Po po = repos.findById(id).get();
		Hibernate.initialize(po.getProject().getCustomer());
		Hibernate.initialize(po.getSupplier());
		Hibernate.initialize(po.getCompany());
		Hibernate.initialize(po.getCurrency());
		Hibernate.initialize(po.getFileList());
		return po;
	}

	public List<Po> findByTypeAndProjectAndNotMapped(String type, Integer projectId) {
		return repos.findByTypeAndProjectAndNotIlogisticsStatus(type, projectId, PoIlogisticsStatus.COMPLETED, Arrays.asList(PoStatus.REJECTED, PoStatus.CLOSED));
	}

	public void updateIlogisticsStatus(Integer poId) {
		System.out.println("updateIlogisticsStatus poId: " + poId);
		PoIlogisticsStatus ilogisticsStatus = null;
		if (boqService.countByPo(poId) == 0)
			ilogisticsStatus = null;
		else if (boqMappingService.countByPo(poId) == 0 && boqService.countByPo(poId) > 0)
			ilogisticsStatus = PoIlogisticsStatus.PENDING;
		else {
			List<Podetails> podetailsList = podetailsRepos.findByPoAndHavingBoq(poId);
			Boolean ibuy = repos.getIbuy(poId);
			if (!ibuy)
				if (podetailsList.stream().filter(i -> RevenueType.GOODS_SUPPLY.equals(i.getRevenueType())
						&& (!i.getIsBoqMapped() || boqService.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(i.getIdpoDetails()) > 0)).count() > 0)
					ilogisticsStatus = PoIlogisticsStatus.IN_PROGRESS;
				else
					ilogisticsStatus = PoIlogisticsStatus.COMPLETED;
			else {
				if (podetailsList.stream().filter(i -> CostType.PROJECT_GOODS_PURCHASE.equals(i.getCostType())
						&& (!i.getIsBoqMapped() || boqService.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(i.getIdpoDetails()) > 0)).count() > 0)
					ilogisticsStatus = PoIlogisticsStatus.IN_PROGRESS;
				else
					ilogisticsStatus = PoIlogisticsStatus.COMPLETED;
			}

		}
		repos.updateIlogisticsStatus(poId, ilogisticsStatus);
		cacheService.evictCache("poService");
		cacheService.evictCacheOthers("poService");
	}

	public void updateAllIlogisticsStatusAndDeliveryStatusScript() {
		Set<Integer> sourceList = new HashSet<>();
		sourceList.addAll(repos.findPoIdListContainingGoodsSupply(RevenueType.GOODS_SUPPLY));
		sourceList.addAll(repos.findPoIdListContainingProjectGoodsPurchase(CostType.PROJECT_GOODS_PURCHASE));

		for (Integer poId : sourceList) {
			updateIlogisticsStatus(poId);
			updateDeliveryStatus(poId);
		}

	}

	public void updateDeliveryStatus(Integer poId) {
		PoDeliveryStatus deliveryStatus = null;
		PoIlogisticsStatus ilogisticsStatus = repos.getIlogisticsStatus(poId);
		if (PoIlogisticsStatus.COMPLETED.equals(ilogisticsStatus)) {
			if (boqMappingService.countDeliveryRequestsByRelatedToPoAndNotInStatus(poId, // means all dn are DELIVRED
					Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)) == 0)
				deliveryStatus = PoDeliveryStatus.DELIVRED;
			else if (boqMappingService.countDeliveryRequestsByRelatedToPoAndInStatus(poId, // at least one dn DELIVRED or PARTIALLY_DELIVRED
					Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)) > 0)
				deliveryStatus = PoDeliveryStatus.PARTIALLY_DELIVRED;
		} else if (PoIlogisticsStatus.IN_PROGRESS.equals(ilogisticsStatus))
			if (boqMappingService.countDeliveryRequestsByRelatedToPoAndInStatus(poId, // at least one dn DELIVRED or PARTIALLY_DELIVRED
					Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)) > 0)
				deliveryStatus = PoDeliveryStatus.PARTIALLY_DELIVRED;
		repos.updateDeliveryStatus(poId, deliveryStatus);
		cacheService.evictCache("poService");
		cacheService.evictCacheOthers("poService");
	}

	public List<Po> find(Boolean ibuy, Integer companyId, String username, List<Integer> assignedProjectList, PoDeliveryStatus deliveryStatus) {
		if (ibuy)
			if (deliveryStatus == null)
				return repos.findSupplierPoList(companyId, username, assignedProjectList);
			else if (PoDeliveryStatus.PENDING.equals(deliveryStatus))
				return repos.findSupplierPoListByDeliveryStatusNull(companyId, username, assignedProjectList);
			else
				return repos.findSupplierPoListByDeliveryStatus(companyId, username, assignedProjectList, deliveryStatus);
		else if (deliveryStatus == null)
			return repos.findCustomerPoList(companyId, username, assignedProjectList);
		else if (PoDeliveryStatus.PENDING.equals(deliveryStatus))
			return repos.findCustomerPoListByDeliveryStatusNull(companyId, username, assignedProjectList);
		else
			return repos.findCustomerPoListByDeliveryStatus(companyId, username, assignedProjectList, deliveryStatus);
	}
	
	public List<PoFile> findFileList(Integer id){
		return repos.findFileList(id);
	}

}
