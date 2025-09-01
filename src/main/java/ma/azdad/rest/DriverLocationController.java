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
    public ResponseEntity<DriverLocation> getLastLocation(@PathVariable String username) {
        DriverLocation location = driverLocationService.getLastLocation(username);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }
    
    @GetMapping("/drivers")
    public ResponseEntity<List<DriverLocationDto>> getDriversLocation() {
    	List<DriverLocationDto> locations = driverLocationService.getDriversLocation();
        if (locations == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locations);
    }
}
 