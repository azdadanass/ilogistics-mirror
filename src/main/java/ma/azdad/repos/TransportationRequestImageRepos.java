package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.azdad.model.TransportationRequestImage;

public interface TransportationRequestImageRepos extends JpaRepository<TransportationRequestImage, Integer>{
	
	public List<TransportationRequestImage> findByTypeAndTransportationRequestId(String type,Integer id);

}
