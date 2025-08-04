package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.UserVehicle;

@Repository
public interface UserVehicleRepos extends JpaRepository<UserVehicle, Integer> {

}

