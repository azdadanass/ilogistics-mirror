package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.VehicleBrandType;

@Repository
public interface VehicleBrandTypeRepos extends JpaRepository<VehicleBrandType, Integer> {
	
	@Query("from VehicleBrandType where brand.id = ?1")
	List<VehicleBrandType> findByBrand(Integer brandId);
	

}

