package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.VehicleBrand;
import ma.azdad.repos.VehicleBrandRepos;

@Component
public class VehicleBrandService extends GenericService<Integer, VehicleBrand, VehicleBrandRepos> {

	@Cacheable("vehicleBrandService.findAll")
	public List<VehicleBrand> findAll() {
		return repos.findAll();
	}

	public VehicleBrand findOne(Integer id) {
		VehicleBrand vehicleBrand = super.findOne(id);
		initialize(vehicleBrand.getBrandTypeList());
		return vehicleBrand;
	}

}
