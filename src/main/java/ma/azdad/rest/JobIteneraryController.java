package ma.azdad.rest;

import com.fasterxml.jackson.core.JsonProcessingException;


import com.fasterxml.jackson.databind.ObjectMapper;

import ma.azdad.model.DriverLocationDto;
import ma.azdad.model.GenericPlace;
import ma.azdad.model.Stop;
import ma.azdad.model.StopType;
import ma.azdad.model.TransportationJobItinerary;
import ma.azdad.model.TransportationJobItineraryDto;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.TransportationJob;
import ma.azdad.repos.StopRepos;

import java.io.IOException;
import ma.azdad.repos.TransportationJobItineraryRepos;
import ma.azdad.service.StopService;
import ma.azdad.service.TransportationJobItineraryService;
import ma.azdad.service.TransportationRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


import java.util.*;

@RestController
@RequestMapping("/api/job")
public class JobIteneraryController {

	@Autowired
    private TransportationJobItineraryService itineraryService;
	
	@Autowired
    private TransportationJobItineraryRepos repo;
	
	@Autowired
    private TransportationRequestService transportationRequestService;
	
	
	@Autowired
	private StopRepos stopRepos;

    @GetMapping("/itinerary/{jobId}")
    public ResponseEntity<List<TransportationJobItineraryDto>> getJobItenerary(@PathVariable Integer jobId) {
    	List<TransportationJobItineraryDto> locations = itineraryService.getJobItinerary(jobId);
        if (locations == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locations);
    }
    
    @GetMapping("/estimated-itinerary/{jobId}")
    public ResponseEntity<List<TransportationJobItineraryDto>> getEstimatedJobItenerary(@PathVariable Integer jobId) {
    	List<TransportationJobItineraryDto> locations = new ArrayList<>();
    	List<Stop> stops = stopRepos.findByTransportationJobIdOrderByDateAsc(jobId);
    	for (Stop stop : stops) {
    	    double lat = stop.getSite() != null ? stop.getSite().getLatitude()
    	               : stop.getWarehouse() != null ? stop.getWarehouse().getLatitude() : 0.0;

    	    double lng = stop.getSite() != null ? stop.getSite().getLongitude()
    	               : stop.getWarehouse() != null ? stop.getWarehouse().getLongitude() : 0.0;
    	    
    	    String name = stop.getSite() != null ? stop.getSite().getName()
                    : stop.getWarehouse() != null ? stop.getWarehouse().getName() : "Unknown";

    	    locations.add(new TransportationJobItineraryDto(
    	        jobId,
    	        lat,
    	        lng,
    	        null,
    	        stop.getDate(),
    	        null,
    	        name,
    	        stop.getExpected(),
    	        false,
    	        stop.getType().getValue()
    	    ));
    	}

        if (stops == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locations);
    }
    
    @GetMapping("/itinerary/tr/{requestId}")
    public ResponseEntity<List<TransportationJobItineraryDto>> getRequestItenerary(@PathVariable Integer requestId) {
    	List<TransportationJobItineraryDto> locations = itineraryService.getRequestItinerary(requestId);
        if (locations == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locations);
    }
    
    @GetMapping("/estimated-itinerary/tr/{requestId}")
    public ResponseEntity<List<TransportationJobItineraryDto>> getEstimatedRequestItinerary(
            @PathVariable Integer requestId) {
        List<TransportationJobItineraryDto> locations = itineraryService.getEstimatedItinerary(requestId);
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

    public static class DistanceUpdateRequest {
        private double distanceFromPrevious;
        private double cumulativeDistance;

        public double getDistanceFromPrevious() { return distanceFromPrevious; }
        public void setDistanceFromPrevious(double d) { this.distanceFromPrevious = d; }
        public double getCumulativeDistance() { return cumulativeDistance; }
        public void setCumulativeDistance(double c) { this.cumulativeDistance = c; }
    }


}
