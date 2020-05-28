package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.VehicleFile;

@Repository
public interface VehicleFileRepos extends JpaRepository<VehicleFile, Integer> {

}

