package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.AppLink;
import ma.azdad.repos.AppLinkRepos;

@Component
public class AppLinkService extends GenericService<Integer, AppLink, AppLinkRepos> {

	@Autowired
	AppLinkRepos appLinkRepos;

	@Override
	public AppLink findOne(Integer id) {
		AppLink appLink = super.findOne(id);
		return appLink;
	}

	public List<AppLink> findByDeliveryRequest(Integer deliveryRequestId, Boolean cost) {
		if (cost)
			return appLinkRepos.findCostsByDeliveryRequest(deliveryRequestId);
		else
			return appLinkRepos.findRevenuesByDeliveryRequest(deliveryRequestId);
	}

	public List<AppLink> findByTransportationRequest(Integer transportationRequestId) {
		return appLinkRepos.findByTransportationRequest(transportationRequestId);
	}

	public List<AppLink> findByWarehouse(Integer warehouseId) {
		return appLinkRepos.findByWarehouse(warehouseId);
	}

	public void deleteByDeliveryRequest(Integer deliveryRequestId) {
		appLinkRepos.deleteByDeliveryRequest(deliveryRequestId);
	}
	
	public List<AppLink> findByTransportationJob(Integer transportationJobId){
		return repos.findByTransportationJob(transportationJobId);
	}
}
