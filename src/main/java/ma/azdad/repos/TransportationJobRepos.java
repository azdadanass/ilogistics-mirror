package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobStatus;

@Repository
public interface TransportationJobRepos extends JpaRepository<TransportationJob, Integer> {
	String transporterName1 = "(select concat(b.firstName,' ',b.lastName) from Transporter b where a.transporter.id = b.id)";
	String transporterName2 = "(select (select c.name from Supplier c where b.supplier.id = c.id) from Transporter b where a.transporter.id = b.id)";
	String select = "select new TransportationJob(a.id,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost, " + transporterName1 + "," + transporterName2 + ") ";
	String select2 = "select new TransportationJob(a.id,a.startDate,a.endDate,a.status,a.realCost,a.estimatedCost, " + transporterName1 + "," + transporterName2 + ",a.driver.username,a.vehicle.matricule) ";

	@Query(select + "from TransportationJob a order by a.id desc")
	public List<TransportationJob> find();
	
	@Query(select2 + "from TransportationJob a order by a.id desc")
	public List<TransportationJob> findMobile();

	@Query(select + "from TransportationJob a where a.status = ?1 order by a.id desc")
	public List<TransportationJob> find(TransportationJobStatus status);
	
	@Query(select2 + "from TransportationJob a where a.status = ?1 order by a.id desc")
	public List<TransportationJob> findMobile(TransportationJobStatus status);

	@Query(select + "from TransportationJob a where a.status in (?1) order by a.id desc")
	public List<TransportationJob> find(List<TransportationJobStatus> status);
	
	@Query(select2 + "from TransportationJob a where a.status in (?1) order by a.id desc")
	public List<TransportationJob> findMobile(List<TransportationJobStatus> status);

	@Query("select a.transportationJob  from TransportationRequest a where a.id in (?1) group by a.transportationJob.id")
	public List<TransportationJob> findByTransportationRequestList(List<Integer> transportationRequestIdList);
}
