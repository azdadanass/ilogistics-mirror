package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.VehicleBrand;

@Repository
public interface VehicleBrandRepos extends JpaRepository<VehicleBrand, Integer> {
	
	
	

}

