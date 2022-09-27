package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import ma.azdad.model.BoqMapping;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.repos.BoqMappingRepos;

@Component
public class BoqMappingService extends GenericService<Integer, BoqMapping, BoqMappingRepos> {

	@Override
	public BoqMapping findOne(Integer id) {
		BoqMapping boqMapping = super.findOne(id);
		// Hibernate.initialize(boqMapping.get..);
		return boqMapping;
	}

	public List<BoqMapping> findRemaining(Integer poId) {
		return repos.findRemaining(poId);
	}

	public Long countByPo(Integer poId) {
		return ObjectUtils.firstNonNull(repos.countByPo(poId), 0l);
	}

	public Long countDeliveryRequestsByRelatedToPoAndNotInStatus(Integer poId, List<DeliveryRequestStatus> notInStatus) {
		return ObjectUtils.firstNonNull(repos.countDeliveryRequestsByRelatedToPoAndNotInStatus(poId, notInStatus), 0l);
	}

	public Long countDeliveryRequestsByRelatedToPoAndInStatus(Integer poId, List<DeliveryRequestStatus> inStatus) {
		return ObjectUtils.firstNonNull(repos.countDeliveryRequestsByRelatedToPoAndInStatus(poId, inStatus), 0l);
	}

//	public Double findTotalQuantityByPoDetailAndPartNumberAndDeliveryRequestDelivered(Integer poDetailId, Integer partNumberId) {
//		return ObjectUtils.firstNonNull(repos.findTotalQuantityByPoDetailAndPartNumberAndDeliveryRequestDelivered(poDetailId, partNumberId), 0.0);
//	}
}
