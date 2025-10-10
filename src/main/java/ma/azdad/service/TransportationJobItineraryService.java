package ma.azdad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.DriverLocation;
import ma.azdad.model.DriverLocationDto;
import ma.azdad.model.GenericPlace;
import ma.azdad.model.Stop;
import ma.azdad.model.StopType;
import ma.azdad.model.TransportationJobItinerary;
import ma.azdad.model.TransportationJobItineraryDto;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.repos.StopRepos;
import ma.azdad.repos.TransportationJobItineraryRepos;

@Service
public class TransportationJobItineraryService {

    @Autowired
    private TransportationJobItineraryRepos repos;
    
    @Autowired
    private TransportationRequestService transportationRequestService;
    

    @Autowired
    private StopRepos stopRepos;

  
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
    
    public List<TransportationJobItineraryDto> getRequestItinerary(Integer id) {
        List<TransportationJobItinerary> locations = repos.findByTransportationRequestIdOrderByTimestampAsc(id);  

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
    
    public List<TransportationJobItineraryDto> getEstimatedItinerary(Integer requestId) {
        List<TransportationJobItineraryDto> locations = new ArrayList<>();
        TransportationRequest transportationRequest = transportationRequestService.findOne(requestId);

        GenericPlace origin = !transportationRequest.getDeliveryRequest().getIsOutbound()
                ? transportationRequest.getDeliveryRequest().getOrigin()
                : transportationRequest.getDeliveryRequest().getWarehouse();

        GenericPlace destination = !transportationRequest.getDeliveryRequest().getIsInbound()
                ? transportationRequest.getDeliveryRequest().getDestination()
                : transportationRequest.getDeliveryRequest().getWarehouse();

        Date pickupDate = transportationRequest.getPickupDate() != null
                ? transportationRequest.getPickupDate()
                : (transportationRequest.getPlannedPickupDate() != null
                        ? transportationRequest.getPlannedPickupDate()
                        : transportationRequest.getNeededPickupDate());

        Date deliveryDate = transportationRequest.getDeliveryDate() != null
                ? transportationRequest.getDeliveryDate()
                : (transportationRequest.getPlannedDeliveryDate() != null
                        ? transportationRequest.getPlannedDeliveryDate()
                        : transportationRequest.getNeededDeliveryDate());

        locations.add(new TransportationJobItineraryDto(
                requestId,
                origin.getLatitude(),
                origin.getLongitude(),
                null,
                Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED,
                        TransportationRequestStatus.APPROVED).contains(transportationRequest.getStatus())
                                ? transportationRequest.getNeededPickupDate()
                                : pickupDate,
                null,
                origin.getName(),
                Arrays.asList(TransportationRequestStatus.PICKEDUP,
                        TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED)
                        .contains(transportationRequest.getStatus())
                                ? false
                                : true,
                Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED,
                        TransportationRequestStatus.APPROVED)
                        .contains(transportationRequest.getStatus())
                                ? true
                                : false,
                StopType.PICKUP.getValue(),
              transportationRequest.getPickupDuration2()));

        List<Stop> stops = stopRepos.findByTransportationJobIdOrderByDateAsc(
                transportationRequest.getTransportationJob().getId());

        // --- ADD ONLY VALID STOPS (BETWEEN PICKUP & DELIVERY DATES) ---
        for (Stop stop : stops) {
            if (stop.getDate() != null && pickupDate != null && deliveryDate != null) {
                // Keep only stops whose date is between pickup and delivery
                if (stop.getDate().after(pickupDate) && stop.getDate().before(deliveryDate)) {

                    GenericPlace place = stop.getSite() != null
                            ? stop.getSite()
                            : stop.getWarehouse();

                    if (place == null)
                        continue; // skip invalid stop

                    locations.add(new TransportationJobItineraryDto(
                            requestId,
                            place.getLatitude(),
                            place.getLongitude(),
                            null,
                            stop.getDate(),
                            null,
                            place.getName(),
                            stop.getExpected(),
                            false,
                            StopType.STOP.getValue(),stop.getDuration()));
                }
            }
        }

        locations.add(new TransportationJobItineraryDto(
                requestId,
                destination.getLatitude(),
                destination.getLongitude(),
                null,
                Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED,
                        TransportationRequestStatus.APPROVED).contains(transportationRequest.getStatus())
                                ? transportationRequest.getNeededDeliveryDate()
                                : deliveryDate,
                null,
                destination.getName(),
                Arrays.asList(TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED)
                        .contains(transportationRequest.getStatus())
                                ? false
                                : true,
                Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED,
                        TransportationRequestStatus.APPROVED)
                        .contains(transportationRequest.getStatus())
                                ? true
                                : false,
                StopType.DELIVERY.getValue(),
                transportationRequest.getDeliveryDuration2()));

        return locations;
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
