package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.PackingDetail;
import ma.azdad.repos.PackingDetailRepos;

@Component
public class PackingDetailService extends GenericService<Integer, PackingDetail, PackingDetailRepos> {

	@Autowired
	PackingDetailRepos repos;

	@Override
	public PackingDetail findOne(Integer id) {
		PackingDetail packingDetail = super.findOne(id);
		// Hibernate.initialize(packingDetail.get..);
		return packingDetail;
	}

	public List<PackingDetail> findByPartNumber(Integer partNumberId) {
		return repos.findByPartNumber(partNumberId);
	}

	public Long countByDeliveryRequestAndHasSerialNumber(Integer deliveryRequestId) {
		return ObjectUtils.firstNonNull(repos.countByDeliveryRequestAndHasSerialNumber(deliveryRequestId), 0l);
	}
	
	public Long countByPackingAndHasSerialnumber(Integer packingId) {
		return ObjectUtils.firstNonNull(repos.countByPackingAndHasSerialnumber(packingId), 0l);
	}

}
