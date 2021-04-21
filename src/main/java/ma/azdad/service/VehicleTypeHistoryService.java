package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.User;
import ma.azdad.model.VehicleType;
import ma.azdad.model.VehicleTypeHistory;
import ma.azdad.repos.VehicleTypeHistoryRepos;

@Component
public class VehicleTypeHistoryService extends GenericService<Integer, VehicleTypeHistory, VehicleTypeHistoryRepos> {

	public void created(VehicleType vehicleType, User user) {
		try {
			VehicleTypeHistory vehicleTypeHistory = new VehicleTypeHistory("Created", user, null, vehicleType);
			save(vehicleTypeHistory);
		} catch (Exception e) {
			log.error("error creating vehicleTypeHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(VehicleType vehicleType, User user) {
		try {
			// TODO fill Description
			VehicleTypeHistory vehicleTypeHistory = new VehicleTypeHistory("Edited", user, null, vehicleType);
			save(vehicleTypeHistory);
		} catch (Exception e) {
			log.error("error creating vehicleTypeHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
