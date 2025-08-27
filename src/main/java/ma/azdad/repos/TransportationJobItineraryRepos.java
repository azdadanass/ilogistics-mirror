package ma.azdad.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ma.azdad.model.TransportationJobItinerary;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequestStatus;

public interface TransportationJobItineraryRepos extends JpaRepository<TransportationJobItinerary, Integer> {
	
	List<TransportationJobItinerary> findByTransportationJobIdOrderByTimestampAsc(Integer jobId);
	
	List<TransportationJobItinerary> findByTransportationJobIdAndTransportationJobStatus(Integer jobId,TransportationJobStatus status);

	List<TransportationJobItinerary> findByTransportationJobIdAndTransportationRequestStatus(Integer jobId,TransportationRequestStatus status);

	
	






    

}
