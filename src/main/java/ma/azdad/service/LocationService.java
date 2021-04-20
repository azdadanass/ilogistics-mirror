package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Location;
import ma.azdad.repos.LocationRepos;

@Component
@Transactional
public class LocationService extends GenericService<Integer, Location, LocationRepos> {

	@Autowired
	private LocationRepos locationRepos;

	@Override
	public Location findOne(Integer id) {
		Location location = super.findOne(id);
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
