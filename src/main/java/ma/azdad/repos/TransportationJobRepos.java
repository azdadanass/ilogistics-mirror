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

	String c1 = "select new TransportationJob(a.id,a.reference,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost,a.transporter.id, " + transporterType + ","
			+ transporterPrivateFirstName + "," + transporterPrivateLastName + "," + transporterSupplierName + ") ";
	String c2 = "select new TransportationJob(a.id,a.reference,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost,a.transporter.id, " + transporterType + ","
			+ transporterPrivateFirstName + "," + transporterPrivateLastName + "," + transporterSupplierName + ",a.driver.username," + vehicleMatricule + ") ";

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 order by a.id desc")
	public List<TransportationJob> findByUser1(String user1Username);

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 and a.status in (?2) order by a.id desc")
	public List<TransportationJob> findByUser1(String user1Username, List<TransportationJobStatus> statusList);

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

	@Query("select count(*) from TransportationJob a where a.user1.username = ?1 and a.status = 'EDITED' order by a.id desc")
	public Long countToAssign1(String user1Username);

	@Query(c1 + "from TransportationJob a where a.transporter.id = ?1 and a.status = 'ASSIGNED1' order by a.id desc")
	public List<TransportationJob> findToAssign2(Integer transporterId);

	@Query("select count(*) from TransportationJob a where a.transporter.id = ?1 and a.status = 'ASSIGNED1' order by a.id desc")
	public Long countToAssign2(Integer transporterId);

	@Query(c1 + "from TransportationJob a where a.driver.username = ?1")
	public List<TransportationJob> findByDriver(String driverUsername);

	@Query(c1 + "from TransportationJob a where a.driver.username = ?1 and a.status = ?2")
	public List<TransportationJob> findByDriver(String driverUsername, TransportationJobStatus status);

	@Query("select count(*) from TransportationJob a where a.driver.username = ?1 and a.status = ?2")
	public Long countByDriver(String driverUsername, TransportationJobStatus status);

}
