package ma.azdad.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.DriverLocation;
import ma.azdad.model.DriverLocationDto;
import ma.azdad.model.TransportationJobItinerary;
import ma.azdad.model.TransportationJobItineraryDto;
import ma.azdad.repos.TransportationJobItineraryRepos;

@Service
public class TransportationJobItineraryService {

    @Autowired
    private TransportationJobItineraryRepos repos;

  
    public List<TransportationJobItineraryDto> getJobItinerary(Integer id) {
        List<TransportationJobItinerary> locations = repos.findByTransportationJobIdOrderByTimestampAsc(id);  

        return locations.stream()
            .map(loc -> new TransportationJobItineraryDto(
                loc.getId(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getTransportationJob().getDriver() != null ? loc.getTransportationJob().getDriver().getUsername() : null,
                loc.getTimestamp(),
                loc.getTransportationJob().getDriver().getPhoto()
            ))
            .collect(Collectors.toList());
    }

    public void savePoint(TransportationJobItinerary point) {
        repos.save(point);
    }

    public void saveAll(List<TransportationJobItinerary> points) {
        repos.saveAll(points);
    }

    public void deleteAll() {
        repos.deleteAll();
    }
}
