package ma.azdad.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.PartNumberPricing;
import ma.azdad.repos.PartNumberPricingRepos;

@Component
public class PartNumberPricingService extends GenericService<Integer, PartNumberPricing, PartNumberPricingRepos> {
	@Autowired
	CompanyService companyService;

	@Override
	@Cacheable("partNumberPricingService.findAll")
	public List<PartNumberPricing> findAll() {
		return repos.findAll();
	}

	@Override
	public PartNumberPricing findOne(Integer id) {
		PartNumberPricing partNumberPricing = super.findOne(id);
		initialize(partNumberPricing.getPartNumber());
		initialize(partNumberPricing.getCompany());
		initialize(partNumberPricing.getDetailList());
		initialize(partNumberPricing.getFileList());
		initialize(partNumberPricing.getHistoryList());
		initialize(partNumberPricing.getCommentList());
		return partNumberPricing;
	}

	@Cacheable("partNumberPricingService.findLight")
	public List<PartNumberPricing> findLight() {
		return repos.findLight();
	}

	public void updatePhysicalQuantity(Integer companyId) {
		repos.updatePhysicalQuantity(companyId);
		evictCache();
	}

	public void updatePendingQuantity(Integer companyId) {
		repos.updatePendingQuantity(companyId, Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		evictCache();
	}

	public void updateQuantities(Integer companyId) {
		updatePhysicalQuantity(companyId);
		updatePendingQuantity(companyId);
	}

	public void updateQuantities() {
		companyService.findIdList().forEach(id -> updateQuantities(id));
	}

}
