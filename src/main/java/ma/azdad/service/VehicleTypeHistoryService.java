package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.model.VehicleType;
import ma.azdad.model.VehicleTypeHistory;

@Component
@Transactional
public class VehicleTypeHistoryService extends GenericService<VehicleTypeHistory> {

	public void created(VehicleType vehicleType, User user) {
		try {
			VehicleTypeHistory vehicleTypeHistory = new VehicleTypeHistory(new Date(), "Created", vehicleType, user);
			save(vehicleTypeHistory);
		} catch (Exception e) {
			log.error("error creating vehicleTypeHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(VehicleType vehicleType, User user) {
		try {
			// TODO fill Description
			VehicleTypeHistory vehicleTypeHistory = new VehicleTypeHistory(new Date(), "Edited", vehicleType, user);
			save(vehicleTypeHistory);
		} catch (Exception e) {
			log.error("error creating vehicleTypeHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
