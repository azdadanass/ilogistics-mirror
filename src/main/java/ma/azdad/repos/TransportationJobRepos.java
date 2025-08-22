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

	String c1 = "select new TransportationJob(a.id,a.reference,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost,a.firstLatitude,a.firstLongitude,a.user1.photo,a.transporter.id, "
			+ transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName + "," + transporterSupplierName + ") ";
	String c2 = "select new TransportationJob(a.id,a.reference,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost,a.firstLatitude,a.firstLongitude,a.user1.photo,a.transporter.id, "
			+ transporterType + "," + transporterPrivateFirstName + "," + transporterPrivateLastName + "," + transporterSupplierName + ",a.driver.username," + vehicleMatricule + ") ";

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 order by a.id desc")
	public List<TransportationJob> findByUser1(String user1Username);

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 and a.status in (?2) order by a.id desc")
	public List<TransportationJob> findByUser1(String user1Username, List<TransportationJobStatus> statusList);

	@Query(c2 + "from TransportationJob a where a.user1.username = ?1 order by a.id desc")
	public List<TransportationJob> findByUser1Mobile(String user1Username);

	@Query(c2 + "from TransportationJob a where a.user1.username = ?1 and a.status in (?2) order by a.id desc")
	public List<TransportationJob> findByUser1Mobile(String user1Username, List<TransportationJobStatus> statusList);

	@Query(c1 + "from TransportationJob a order by a.id desc")
	public List<TransportationJob> find();

	@Query(c1 + "from TransportationJob a where a.id in (?1) order by a.id desc")
	public List<TransportationJob> findByIdList(List<Integer> idList);

	@Query(c2 + "from TransportationJob a order by a.id desc")
	public List<TransportationJob> findMobile();

	@Query(c1 + "from TransportationJob a where a.status = ?1 order by a.id desc")
	public List<TransportationJob> find(TransportationJobStatus status);

	@Query(c2 + "from TransportationJob a where a.status in (?1) and a.driver.username = ?2 order by a.id desc")
	public List<TransportationJob> find(List<TransportationJobStatus> status, String username);

	@Query(c2 + "from TransportationJob a where a.status = ?1 order by a.id desc")
	public List<TransportationJob> findMobile(TransportationJobStatus status);

	@Query(c1 + "from TransportationJob a where a.status in (?1) order by a.id desc")
	public List<TransportationJob> find(List<TransportationJobStatus> status);

	@Query(c2 + "from TransportationJob a where a.status in (?1) order by a.id desc")
	public List<TransportationJob> findMobile(List<TransportationJobStatus> status);

	@Query("select a.transportationJob  from TransportationRequest a where a.id in (?1) group by a.transportationJob.id")
	public List<TransportationJob> findByTransportationRequestList(List<Integer> transportationRequestIdList);

	@Query(c1 + "from TransportationJob a where (a.user1.username = ?1 or a.driver.username = ?1) and a.status = 'ASSIGNED2' order by a.id desc")
	public List<TransportationJob> findToAccept(String username);

	@Query("select count(*) from TransportationJob a where (a.user1.username = ?1 or a.driver.username = ?1) and a.status = 'ASSIGNED2' order by a.id desc")
	public Long countToAccept(String username);

	@Query(c1 + "from TransportationJob a where (a.user1.username = ?1 or a.driver.username = ?1) and a.status = 'ACCEPTED' order by a.id desc")
	public List<TransportationJob> findToStart(String username);

	@Query("select count(*) from TransportationJob a where (a.user1.username = ?1 or a.driver.username = ?1) and a.status = 'ACCEPTED' order by a.id desc")
	public Long countToStart(String username);

	@Query(c1 + "from TransportationJob a where (a.user1.username = ?1 or a.driver.username = ?1) and a.status in ('STARTED','IN_PROGRESS') order by a.id desc")
	public List<TransportationJob> findToComplete(String username);

	@Query("select count(*) from TransportationJob a where (a.user1.username = ?1 or a.driver.username = ?1) and a.status in ('STARTED','IN_PROGRESS') order by a.id desc")
	public Long countToComplete(String username);

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 and a.status = ?2 order by a.id desc")
	public List<TransportationJob> findByUser1AndStatus(String user1Username, TransportationJobStatus transportationJobStatus);

	@Query("select count(*) from TransportationJob a where a.user1.username = ?1 and a.status = ?2 order by a.id desc")
	public Long countByUser1AndStatus(String user1Username, TransportationJobStatus transportationJobStatus);

	@Query(c1 + "from TransportationJob a where a.user1.username = ?1 and a.status in (?2) order by a.id desc")
	public List<TransportationJob> findByUser1AndStatus(String user1Username, List<TransportationJobStatus> transportationJobStatusList);

	@Query("select count(*) from TransportationJob a where a.user1.username = ?1 and a.status in (?2) order by a.id desc")
	public Long countByUser1AndStatus(String user1Username, List<TransportationJobStatus> transportationJobStatusList);

	@Query(c1
			+ "from TransportationJob a where a.user1.username = ?1 and a.status = 'EDITED' and (select count(*) from TransportationRequest b where b.transportationJob.id = a.id) > 0 order by a.id desc")
	public List<TransportationJob> findToAssign1(String user1Username);

	@Query("select count(*) from TransportationJob a where a.user1.username = ?1 and a.status = 'EDITED' and (select count(*) from TransportationRequest b where b.transportationJob.id = a.id) > 0")
	public Long countToAssign1(String user1Username);

	@Query(c1 + "from TransportationJob a where a.transporter.id = ?1 and a.status = 'ASSIGNED1' order by a.id desc")
	public List<TransportationJob> findToAssign2(Integer transporterId);

	@Query("select count(*) from TransportationJob a where a.transporter.id = ?1 and a.status = 'ASSIGNED1'")
	public Long countToAssign2(Integer transporterId);

	@Query(c1 + "from TransportationJob a where a.driver.username = ?1 order by id desc")
	public List<TransportationJob> findByDriver(String driverUsername);

	@Query(c1 + "from TransportationJob a where a.driver.username = ?1 and a.status = ?2  order by id desc")
	public List<TransportationJob> findByDriver(String driverUsername, TransportationJobStatus status);

	@Query(c2 + "from TransportationJob a where a.driver.username = ?1 and a.status in (?2)  order by id desc")
	public List<TransportationJob> findByDriverMobile(String driverUsername, List<TransportationJobStatus> status);

	@Query(c2 + "from TransportationJob a where a.driver.username = ?1 order by id desc")
	public List<TransportationJob> findByDriverMobile(String driverUsername);

	@Query(c1 + "from TransportationJob a where a.driver.username = ?1 and a.status in (?2)  order by id desc")
	public List<TransportationJob> findByDriver(String driverUsername, List<TransportationJobStatus> statusList);

	@Query(c1 + "from TransportationJob a where a.transporter.id = ?1 order by id desc")
	public List<TransportationJob> findByTransporter(Integer transporterId);

	@Query(c1 + "from TransportationJob a where a.transporter.id = ?1 and a.status in (?2)  order by id desc")
	public List<TransportationJob> findByTransporter(Integer transporterId, List<TransportationJobStatus> statusList);

	@Query("select count(*) from TransportationJob a where a.driver.username = ?1 and a.status = ?2")
	public Long countByDriver(String driverUsername, TransportationJobStatus status);

	@Query("select new ma.azdad.mobile.model.TransportationJobHistory(a.id,a.date,a.status,a.description,u.fullName,u.photo) from TransportationJobHistory a left join a.user as u where a.parent.id = ?1")
	List<ma.azdad.mobile.model.TransportationJobHistory> findHistoryListMobile(Integer id);

	// reactivity and performance
	@Query("select count(*) from TransportationJob a where a.driver.username = ?1 and date4 is not null")
	Long countAcceptedByDriver(String driverUsername);

	@Query("select count(*) from TransportationJob a where a.driver.username = ?1 and date4 is not null and date4 < maxAcceptDate")
	Long countAcceptedWithinDeadLineByDriver(String driverUsername);

	@Query("select count(*) from TransportationJob a where a.driver.username = ?1 and date5 is not null")
	Long countStartedByDriver(String driverUsername);

	@Query("select count(*) from TransportationJob a where a.driver.username = ?1  and date5 is not null and date5 < maxStartDate")
	Long countStartedWithinDeadLineByDriver(String driverUsername);

}
