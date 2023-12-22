package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.Location;
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

}
