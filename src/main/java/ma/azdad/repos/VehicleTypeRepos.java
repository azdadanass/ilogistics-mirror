package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.VehicleType;

@Repository
public interface VehicleTypeRepos extends JpaRepository<VehicleType, Integer> {

}
