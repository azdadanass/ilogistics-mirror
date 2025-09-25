package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Vehicle;

@Repository
public interface VehicleRepos extends JpaRepository<Vehicle, Integer> {

	String c1 = "select new Vehicle(a.id,a.fromMyTools,a.matricule,(select b.matricule from Tool b where b.id = a.tool.id),a.vehicleType.maxWeight,a.vehicleType.maxVolume) ";

	@Query(c1 + "from Vehicle a where a.transporter.id = ?1")
	public List<Vehicle> findLightByTransporter(Integer transporterId);

	@Query("from Vehicle a where a.id in (select b.vehicle.id from UserVehicle b where b.active is true and b.user.username = ?1) and a.transporter.id = (select b.transporter.id from User b where b.username = ?1) and a.active is true")
	public List<Vehicle> findActiveByDriver(String driverUsername);

	@Modifying
	@Query("update Vehicle set active = ?2 where id = ?1")
	public void updateActive(Integer id, Boolean active);
}
