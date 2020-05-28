package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Vehicle;

@Repository
public interface VehicleRepos extends JpaRepository<Vehicle, Integer> {

	@Query("select new Vehicle(a.id,a.fromMyTools,a.matricule,(select b.matricule from Tool b where b.id = a.tool.id)) from Vehicle a where a.transporter.id = ?1")
	public List<Vehicle> findLightByTransporter(Integer transporterId);
}
