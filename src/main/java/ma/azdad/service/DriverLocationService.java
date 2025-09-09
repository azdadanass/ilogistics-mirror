package ma.azdad.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.DriverLocation;
import ma.azdad.model.DriverLocationDto;
import ma.azdad.repos.DriverLocationRepo;

@Service
public class DriverLocationService {

	@Autowired
	private DriverLocationRepo driverLocationRepo;

	public DriverLocation getLastLocation(String driverUsername) {
		return driverLocationRepo.findTopByDriverUsernameOrderByDateDesc(driverUsername);
	}

	public List<DriverLocationDto> getDriversLocation() {
	    List<DriverLocation> locations = driverLocationRepo.findAll(); // ou ta query custom

	    return locations.stream()
	        .map(loc -> {
	            String photoUrl = loc.getDriver() != null ? loc.getDriver().getPhoto() : null;

	            System.out.println("Driver: " 
	                + (loc.getDriver() != null ? loc.getDriver().getUsername() : "unknown") 
	                + " | Photo URL: " + photoUrl);

	            return new DriverLocationDto(
	                loc.getLatitude(),
	                loc.getLongitude(),
	                loc.getDriver() != null ? loc.getDriver().getUsername() : null,
	                loc.getDate(),
	                photoUrl
	            );
	        })
	        .collect(Collectors.toList());
	}

}
