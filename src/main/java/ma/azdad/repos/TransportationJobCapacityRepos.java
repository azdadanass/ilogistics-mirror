package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.azdad.model.TransportationJobCapacity;

public interface TransportationJobCapacityRepos extends JpaRepository<TransportationJobCapacity, Integer> {

	void deleteByTransportationJobIdAndType(Integer id, String type);

	@Query("SELECT MAX(c.cumulativeWeight) FROM TransportationJobCapacity c "
			+ "WHERE c.transportationJob.id = :jobId AND c.type = :type")
	Double findMaxCumulativeWeightByTransportationJobIdAndType(@Param("jobId") Integer jobId,
			@Param("type") String type);

	@Query("SELECT MAX(c.cumulativeVolume) FROM TransportationJobCapacity c "
			+ "WHERE c.transportationJob.id = :jobId AND c.type = :type")
	Double findMaxCumulativeVolumeByTransportationJobIdAndType(@Param("jobId") Integer jobId,
			@Param("type") String type);

}
