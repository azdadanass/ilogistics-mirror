package ma.azdad.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ma.azdad.model.TransportationJobItinerary;

public interface TransportationJobItineraryRepos extends JpaRepository<TransportationJobItinerary, Integer> {
	
	List<TransportationJobItinerary> findByTransportationJobIdOrderByTimestampAsc(Integer jobId);





    

}
