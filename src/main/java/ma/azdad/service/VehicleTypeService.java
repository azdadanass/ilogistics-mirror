package ma.azdad.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.VehicleType;
import ma.azdad.repos.VehicleTypeRepos;

@Component
@Transactional
public class VehicleTypeService extends GenericServiceOld<VehicleType> {

	@Autowired
	VehicleTypeRepos vehicleTypeRepos;

	@Override
	public VehicleType findOne(Integer id) {
		VehicleType vehicleType = super.findOne(id);
		Hibernate.initialize(vehicleType.getHistoryList());
		return vehicleType;
	}

}
