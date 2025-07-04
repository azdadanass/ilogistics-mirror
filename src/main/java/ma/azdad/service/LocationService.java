package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyType;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Location;
import ma.azdad.model.StockRowState;
import ma.azdad.repos.LocationRepos;

@Component
public class LocationService extends GenericService<Integer, Location, LocationRepos> {

	@Autowired
	private LocationRepos locationRepos;
	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Override
	public Location findOne(Integer id) {
		Location location = super.findOne(id);
		location.getDetailList().forEach(detail->{
			initialize(detail.getCompany());
			initialize(detail.getCustomer());
			initialize(detail.getSupplier());
		});
		return location;
	}

	public Long countByWarehouseAndName(Integer warehouseId, String name, Integer id) {
		Long l = id == null ? locationRepos.countByWarehouseAndName(warehouseId, name) : locationRepos.countByWarehouseAndName(warehouseId, name, id);
		return l != null ? l : 0;
	}

	public Boolean isNameExists(Location location) {
		return countByWarehouseAndName(location.getWarehouse().getId(), location.getName(), location.getId()) > 0;
	}
	
	public List<Location> findByWarehouseAndStockRowStateAndOwner(Integer warehouseId, StockRowState stockRowState, CompanyType ownerType, Integer ownerId){
		return repos.findByWarehouseAndStockRowStateAndOwner(warehouseId, stockRowState, ownerType, ownerId);
	}
	
	public List<Location> findByWarehouseAndStockRowStateAndOwner(DeliveryRequest deliveryRequest){
		return findByWarehouseAndStockRowStateAndOwner(deliveryRequest.getWarehouseId(),deliveryRequest.getStockRowState(),deliveryRequest.getOwnerType(),deliveryRequest.getOwnerId());
	}
	
	public List<Location> findLightByWarehouse(Integer warehouseId){
		return repos.findLightByWarehouse(warehouseId);
	}
	
	//mobile
	@SuppressWarnings("null")
	public List<ma.azdad.mobile.model.Location> findByWarehouseAndStockRowStateAndOwnerMobile(Integer id,String state){
		DeliveryRequest deliveryRequest =deliveryRequestService.findOne(id);
		if(state.equals("Normal")) {
			deliveryRequest.setStockRowState(StockRowState.NORMAL);
		} else {
			deliveryRequest.setStockRowState(StockRowState.FAULTY);
		}
		List<Location> list =  findByWarehouseAndStockRowStateAndOwner(deliveryRequest);
		
		List<ma.azdad.mobile.model.Location> mobileList = new ArrayList<>() ;
		for (Location location : list) {
			mobileList.add(new ma.azdad.mobile.model.Location(location.getId(), location.getName()));
		}
		System.out.print("lcation size :"+mobileList.size());
		 return mobileList;
	}

}
