package ma.azdad.mobile.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.Stop;
import ma.azdad.mobile.model.Token;
import ma.azdad.mobile.model.TransportationJob;
import ma.azdad.model.Role;
import ma.azdad.model.TransportationJobItineraryDto;
import ma.azdad.repos.TransportationJobItineraryRepos;
import ma.azdad.service.TokenService;
import ma.azdad.service.TransportationJobItineraryService;
import ma.azdad.service.TransportationJobService;

@RestController
@RequestMapping("/mobile/tj")
public class TransportationJobController {

	@Autowired
	TokenService tokenService;

	@Autowired
	TransportationJobService transportationJobService;
	
	@Autowired
    private TransportationJobItineraryService itineraryService;
	
	@Autowired
    private TransportationJobItineraryRepos repo;

	@GetMapping("/findone/{key}/{id}")
	public TransportationJob findOneLightMobile(@PathVariable String key, @PathVariable Integer id) {
		System.out.println("/findone/{key}/{id}");
		Token token = tokenService.getBykey(key);
		return transportationJobService.findOneMobile(id);
	}

	@GetMapping("/findbystatus/{key}/{state}")
	public List<TransportationJob> findLightByUserMobile(@PathVariable String key,
			@PathVariable Integer state) {
		System.out.println("/mobile/tj/findbystatus/{key}/{state}");
		Token token = tokenService.getBykey(key);
		if (token.getRoleList().contains(Role.ROLE_ILOGISTICS_TM)) {
			return transportationJobService.findByUser1MobileByStatus(state, token.getUsername());
		} else {
			return transportationJobService.findByDriverMobileByStatus(state, token.getUsername());

		}
	}
	@GetMapping("/start/{key}/{id}/{lat}/{lng}")
	public void start(@PathVariable String key,@PathVariable Integer id,@PathVariable Double lat,
            @PathVariable Double lng) {
		System.out.println("/mobile/tj/start/{key}");
		Token token = tokenService.getBykey(key);
		transportationJobService.startMobile(id, token.getUsername(), lat, lng);
	}
	
	@GetMapping("/findtjstops/{key}/{id}")
	public List<Stop> findTjStops(@PathVariable String key,
			@PathVariable Integer id) {
		System.out.println("/mobile/tj/findtjstops/{key}/{id}");
		Token token = tokenService.getBykey(key);
		return transportationJobService.findTjStopsMobile(id);
	}
	
	 @GetMapping("/itenerary/{jobId}")
	    public ResponseEntity<List<TransportationJobItineraryDto>> getJobItenerary(@PathVariable Integer jobId) {
	    	List<TransportationJobItineraryDto> locations = itineraryService.getJobItinerary(jobId);
	        if (locations == null) {
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(locations);
	    }
	    
	    @PostMapping("/updatePoint/{pointId}")
	    public ResponseEntity<Void> updatePointDistances(
	            @PathVariable Integer pointId,
	            @RequestBody DistanceUpdateRequest req) {
	        System.out.println("/updatePoint/{pointId}");
	    	repo.findById(pointId).ifPresent(point -> {
	            point.setDistanceFromPrevious(req.getDistanceFromPrevious());
	            point.setCumulativeDistance(req.getCumulativeDistance());
	            repo.save(point);
	        });
	        
	        return ResponseEntity.ok().build();
	    }
	    
	    // Mise � jour localisation du chauffeur
		@GetMapping("/driverLocation/{lat}/{lng}/{key}")
	    public ResponseEntity<Void> updateDriverLocation(
	            @PathVariable String key,
	            @PathVariable Double lat,
	            @PathVariable Double lng) {
			System.out.println("/driverLocation/");
			Token token = tokenService.getBykey(key);
	       
	        transportationJobService.updateDriverLocation(lat, lng, token.getUser());
	        return ResponseEntity.ok().build();
	    }
		
		@GetMapping("/jobItinerary/{lat}/{lng}/{key}")
	    public ResponseEntity<Void> updateJobItinerary(
	            @PathVariable String key,
	            @PathVariable Double lat,
	            @PathVariable Double lng) {
			System.out.println("/jobItinerary/");
			Token token = tokenService.getBykey(key);
	       
	        transportationJobService.updateJobItinerary(lat, lng, token.getUser());
	        return ResponseEntity.ok().build();
	    }

	    public static class DistanceUpdateRequest {
	        private double distanceFromPrevious;
	        private double cumulativeDistance;

	        public double getDistanceFromPrevious() { return distanceFromPrevious; }
	        public void setDistanceFromPrevious(double d) { this.distanceFromPrevious = d; }
	        public double getCumulativeDistance() { return cumulativeDistance; }
	        public void setCumulativeDistance(double c) { this.cumulativeDistance = c; }
	    }


}
