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

	@Override
	public Vehicle findOne(Integer id) {
		Vehicle vehicle = super.findOne(id);
		Hibernate.initialize(vehicle.getFileList());
		Hibernate.initialize(vehicle.getHistoryList());
		return vehicle;
	}

	public List<Vehicle> findLightByTransporter(Integer transporterId) {
		return vehicleRepos.findLightByTransporter(transporterId);
	}

}
