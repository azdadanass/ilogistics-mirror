package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.VehicleBrandType;
import ma.azdad.repos.VehicleBrandTypeRepos;

@Component
public class VehicleBrandTypeService extends GenericService<Integer, VehicleBrandType, VehicleBrandTypeRepos> {

	@Cacheable("vehicleBrandTypeService.findAll")
	public List<VehicleBrandType> findAll() {
		return repos.findAll();
	}

	public VehicleBrandType findOne(Integer id) {
		VehicleBrandType vehicleBrandType = super.findOne(id);

		return vehicleBrandType;
	}

	public List<VehicleBrandType> findByBrand(Integer brandId) {
		return repos.findByBrand(brandId);
	}

}
