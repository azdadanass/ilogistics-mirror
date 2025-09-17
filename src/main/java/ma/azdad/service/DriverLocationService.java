package ma.azdad.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.DriverLocation;
import ma.azdad.model.DriverLocationDto;
import ma.azdad.model.Role;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobAssignmentType;
import ma.azdad.model.User;
import ma.azdad.repos.DriverLocationRepo;

@Service
public class DriverLocationService {

	@Autowired
	private DriverLocationRepo driverLocationRepo;
	@Autowired
	private TransportationJobService transportationJobService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransporterService transporterService;
	
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

	public List<DriverLocationDto> getDriversLocation(Integer id ,String type) {
	    List<DriverLocation> locations = driverLocationRepo.findAll();
	    TransportationJob transportationJob = transportationJobService.findOne(id);

	    Set<String> allowedUsernames = new HashSet<>();
	    if (type != null) {
	        TransportationJobAssignmentType assignmentType = TransportationJobAssignmentType.getByValue(type);
	        System.out.println("Resolved assignment type = " + assignmentType);

	        if (type.equals(TransportationJobAssignmentType.TRANSPORTER.getValue())) {
	            allowedUsernames.addAll(
	                transporterService.findLight(true).stream()
	                    .map(t -> t.getUser() != null ? t.getUser().getUsername() : null)
	                    .filter(Objects::nonNull)
	                    .collect(Collectors.toSet())
	            );
	        } 
	        else if (type.equals(TransportationJobAssignmentType.INTERNAL_DRIVER.getValue())) {
	            allowedUsernames.addAll(
	                userService.findActiveDriverList(true).stream()
	                    .map(User::getUsername)
	                    .filter(Objects::nonNull)
	                    .collect(Collectors.toSet())
	            );
	        } 
	        else if (type.equals(TransportationJobAssignmentType.EXTERNAL_DRIVER.getValue())) {
	            if (transportationJob != null) {
	                switch (transportationJob.getStatus()) {
	                    case EDITED:
	                        allowedUsernames.addAll(
	                            userService.findActiveDriverList(false).stream()
	                                .map(User::getUsername)
	                                .filter(Objects::nonNull)
	                                .collect(Collectors.toSet())
	                        );
	                        break;
	                    case ASSIGNED1:
	                        allowedUsernames.addAll(
	                            userService.findByRoleAndActiveAndTransporter(
	                                    Role.ROLE_ILOGISTICS_DRIVER,
	                                    transportationJob.getTransporterId()
	                            ).stream()
	                             .map(User::getUsername)
	                             .filter(Objects::nonNull)
	                             .collect(Collectors.toSet())
	                        );
	                        break;
	                    default:
	                        break;
	                }
	            }
	        }
	    }
	    System.out.println("internal "+allowedUsernames);

	    return locations.stream()
	        .filter(loc -> loc.getDriver() != null && allowedUsernames.contains(loc.getDriver().getUsername()))
	        .map(loc -> new DriverLocationDto(
	                loc.getLatitude(),
	                loc.getLongitude(),
	                loc.getDriver() != null ? loc.getDriver().getUsername() : null,
	                loc.getDate(),
	                loc.getDriver().getPhoto()
	        ))
	        .collect(Collectors.toList());
	}


}
