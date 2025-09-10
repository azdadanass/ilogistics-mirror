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
		
	   return  driverLocationRepo.findTopByDriverUsernameOrderByDateDesc(driverUsername);
	   
	}

	public DriverLocationDto getLastLocationDto(String driverUsername) {
	    DriverLocation loc = driverLocationRepo.findTopByDriverUsernameOrderByDateDesc(driverUsername);

	    if (loc == null) {
	        return null; 
	    }

	    return new DriverLocationDto(
	        loc.getLatitude(),
	        loc.getLongitude(),
	        loc.getDriver() != null ? loc.getDriver().getUsername() : null,
	        loc.getDate(),
	        loc.getDriver() != null ? loc.getDriver().getPhoto() : null
	    );
	}

	public List<DriverLocationDto> getDriversLocation() {
		List<DriverLocation> locations = driverLocationRepo.findAll(); // or your custom query

		return locations.stream()
				.map(loc -> new DriverLocationDto(loc.getLatitude(), loc.getLongitude(), loc.getDriver() != null ? loc.getDriver().getUsername() : null, loc.getDate(), loc.getDriver().getPhoto()))
				.collect(Collectors.toList());
	}

}
