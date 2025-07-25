package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobStatus;

@Repository
public interface TransportationJobRepos extends JpaRepository<TransportationJob, Integer> {

	String transporterType = "(select b.type from Transporter b where b.id = a.transporter.id)";
	String transporterPrivateFirstName = "(select b.privateFirstName from Transporter b where b.id = a.transporter.id)";
	String transporterPrivateLastName = "(select b.privateLastName from Transporter b where b.id = a.transporter.id)";
	String transporterSupplierName = "(select b.supplier.name from Transporter b where b.id = a.transporter.id)";
	String vehicleMatricule = "(select b.matricule from Vehicle b where b.id = a.vehicle.id)";

	String c1 = "select new TransportationJob(a.id,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost,a.latitude,a.longitude, " + transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName
			+ "," + transporterSupplierName + ") ";
	String c2 = "select new TransportationJob(a.id,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost,a.latitude,a.longitude, " + transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName
			+ "," + transporterSupplierName + ",a.driver.username,a.vehicle.matricule) ";

	@Query(c1 + "from TransportationJob a order by a.id desc")
	public List<TransportationJob> find();

	@Query(c1 + "from TransportationJob a where a.id in (?1) order by a.id desc")
	public List<TransportationJob> findByIdList(List<Integer> idList);

	@Query(c2 + "from TransportationJob a order by a.id desc")
	public List<TransportationJob> findMobile();

	@Query(c1 + "from TransportationJob a where a.status = ?1 order by a.id desc")
	public List<TransportationJob> find(TransportationJobStatus status);

	@Query(c2 + "from TransportationJob a where a.status = ?1 order by a.id desc")
	public List<TransportationJob> findMobile(TransportationJobStatus status);

	@Query(c1 + "from TransportationJob a where a.status in (?1) order by a.id desc")
	public List<TransportationJob> find(List<TransportationJobStatus> status);

	@Query(c2 + "from TransportationJob a where a.status in (?1) order by a.id desc")
	public List<TransportationJob> findMobile(List<TransportationJobStatus> status);

	@Query("select a.transportationJob  from TransportationRequest a where a.id in (?1) group by a.transportationJob.id")
	public List<TransportationJob> findByTransportationRequestList(List<Integer> transportationRequestIdList);

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 and a.status = 'EDITED' order by a.id desc")
	public List<TransportationJob> findToAssign1(String user1Username);

	// to correct
	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 and a.status = 'ASSIGNED1' order by a.id desc")
	public List<TransportationJob> findToAssign2(String user1Username);

}
