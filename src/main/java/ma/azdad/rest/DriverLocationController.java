package ma.azdad.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.model.DriverLocation;
import ma.azdad.model.DriverLocationDto;
import ma.azdad.service.DriverLocationService;

@RestController
@RequestMapping("/api/driver-locations")
public class DriverLocationController {

    @Autowired
    private DriverLocationService driverLocationService;

    @GetMapping("/last/{username}")
    public ResponseEntity<DriverLocationDto> getLastLocation(@PathVariable String username) {
    	DriverLocationDto location = driverLocationService.getLastLocationDto(username);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }
    
    @GetMapping("/drivers/{jobId}/{type}")
    public ResponseEntity<List<DriverLocationDto>> getDriversLocation(@PathVariable Integer jobId,@PathVariable String type) {
    	System.out.println(" jobId :"+jobId);
    	List<DriverLocationDto> locations = driverLocationService.getDriversLocation(jobId,type);
    	System.out.println("size : "+locations.size());

        if (locations == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locations);
    }
}
 