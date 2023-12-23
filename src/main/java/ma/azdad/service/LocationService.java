package ma.azdad.service;

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

}
