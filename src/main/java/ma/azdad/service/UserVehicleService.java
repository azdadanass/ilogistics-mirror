package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.UserVehicle;
import ma.azdad.repos.UserVehicleRepos;

@Component
public class UserVehicleService extends GenericService<Integer, UserVehicle, UserVehicleRepos> {

	@Cacheable("userVehicleService.findAll")
	public List<UserVehicle> findAll() {
		return repos.findAll();
	}

	public UserVehicle findOne(Integer id) {
		UserVehicle userVehicle = super.findOne(id);
		initialize(userVehicle.getUser());
		initialize(userVehicle.getVehicle());
		return userVehicle;
	}

}
