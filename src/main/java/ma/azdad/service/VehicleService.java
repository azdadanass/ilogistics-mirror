package ma.azdad.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.Vehicle;
import ma.azdad.repos.VehicleRepos;

@Component
public class VehicleService extends GenericService<Integer, Vehicle, VehicleRepos> {

	@Autowired
	VehicleRepos vehicleRepos;
	@Autowired
	VehicleBrandTypeService vehicleBrandTypeService;

	@Override
	public Vehicle findOne(Integer id) {
		Vehicle vehicle = super.findOne(id);
		Hibernate.initialize(vehicle.getFileList());
		Hibernate.initialize(vehicle.getHistoryList());
		initialize(vehicle.getTransporter().getUserList());
		vehicle.getUserList().forEach(i -> initialize(i.getUser()));
		return vehicle;
	}

	public List<Vehicle> findLightByTransporter(Integer transporterId) {
		return vehicleRepos.findLightByTransporter(transporterId);
	}

	public void updateActive(Integer id, Boolean active) {
		evictCache();
		repos.updateActive(id, active);
	}
	
	public List<Vehicle> findActiveByDriver(String driverUsername){
		List<Vehicle> list =  repos.findActiveByDriver(driverUsername);
		for (Vehicle vehicle : list) {
			Hibernate.initialize(vehicle.getBrandType());
			Hibernate.initialize(vehicle.getBrandType().getBrand());
		}
		return list;
	}

}
