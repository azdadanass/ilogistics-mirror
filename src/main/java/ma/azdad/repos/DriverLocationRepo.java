package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DriverLocation;

@Repository
public interface DriverLocationRepo extends JpaRepository<DriverLocation, Integer> {

    DriverLocation findTopByDriverUsernameOrderByDateDesc(String driverUsername);

    List<DriverLocation> findByDriverOrderByDateDesc(String driverUsername);
    
    @Query("SELECT d FROM DriverLocation d JOIN FETCH d.driver")
    List<DriverLocation> getDriversLocation();
    
	@Query("from DriverLocation where driver.username = ?1 order by id desc")
	List<DriverLocation> findByUser(String username);
	
	

	@Modifying
	@Query("UPDATE DriverLocation SET date = ?7, latitude = ?2, longitude = ?3, googleAddress = ?4, googleCity = ?5, googleRegion = ?6 WHERE id = ?1")
	void updateGoogleGeocodeData2(
	    Integer siteId,
	    Double latitude,
	    Double longitude,
	    String googleAddress,
	    String googleCity,
	    String googleRegion,
	    Date date
	);

    
  

}
