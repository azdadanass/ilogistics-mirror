package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Boq;
import ma.azdad.repos.BoqRepos;

@Component
@Transactional
public class BoqService extends GenericService<Integer, Boq, BoqRepos> {

	public Set<Integer> findPartNumberIdListByPoAndHavingRemainingQuantity(Integer poId) {
		return repos.findPartNumberIdListByPoAndHavingRemainingQuantity(poId);
	}

	public List<Boq> findByPoAndPartNumber(Integer poId, Integer partNumberId) {
		return repos.findByPoAndPartNumber(poId, partNumberId);
	}

	public Double getUsedQuantity(Integer boqId) {
		return repos.getUsedQuantity(boqId);
	}

	public List<Boq> findByPoAndPartNumber(Integer poId, List<Integer> partNumberListId) {
		if (partNumberListId == null || partNumberListId.isEmpty())
			return new ArrayList<>();
		return repos.findByPoAndPartNumber(poId, partNumberListId);
	}

	public void updateTotalUsedQuantity(Set<Integer> idList) {
		System.out.println("updateTotalUsedQuantity " + idList);
		if (idList == null || idList.isEmpty())
			return;
		repos.updateTotalUsedQuantity(idList);
	}

	public Set<Integer> getAssociatedBoqIdListWithDeliveryRequest(Integer deliveryRequestId) {
		return repos.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequestId);
	}

	public Long countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(Integer podetailsId) {
		return ObjectUtils.firstNonNull(repos.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(podetailsId), 0l);
	}

	public Long countByPo(Integer poId) {
		return ObjectUtils.firstNonNull(repos.countByPo(poId), 0l);
	}

	public List<Boq> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}

	public List<Boq> findDeliveredSummaryByPo(Integer poId){
		return repos.findDeliveredSummaryByPo(poId);
	}
	
	public List<Boq> findBoqSummaryByPo(Integer poId){
		return repos.findBoqSummaryByPo(poId);
	}

}
