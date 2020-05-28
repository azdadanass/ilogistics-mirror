package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Boq;
import ma.azdad.repos.BoqRepos;

@Component
@Transactional
public class BoqService {

	@Autowired
	private BoqRepos boqRepos;

	public Set<Integer> findPartNumberIdListByPoAndHavingRemainingQuantity(Integer poId) {
		return boqRepos.findPartNumberIdListByPoAndHavingRemainingQuantity(poId);
	}

	public List<Boq> findByPoAndPartNumber(Integer poId, Integer partNumberId) {
		return boqRepos.findByPoAndPartNumber(poId, partNumberId);
	}

	public Double getUsedQuantity(Integer boqId) {
		return boqRepos.getUsedQuantity(boqId);
	}

	public List<Boq> findByPoAndPartNumber(Integer poId, List<Integer> partNumberListId) {
		if (partNumberListId == null || partNumberListId.isEmpty())
			return new ArrayList<>();
		return boqRepos.findByPoAndPartNumber(poId, partNumberListId);
	}

	public void updateTotalUsedQuantity(Set<Integer> idList) {
		System.out.println("updateTotalUsedQuantity " + idList);
		if (idList == null || idList.isEmpty())
			return;
		boqRepos.updateTotalUsedQuantity(idList);
	}

	public Set<Integer> getAssociatedBoqIdListWithDeliveryRequest(Integer deliveryRequestId) {
		return boqRepos.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequestId);
	}

	public Long countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(Integer podetailsId) {
		return ObjectUtils.firstNonNull(boqRepos.countByPodetailsAndTotalQuantityGreatherThanTotalUsedQuantity(podetailsId), 0l);
	}

}
