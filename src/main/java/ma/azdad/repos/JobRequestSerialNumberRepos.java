package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.JobRequestSerialNumber;

@Repository
public interface JobRequestSerialNumberRepos extends JpaRepository<JobRequestSerialNumber, Integer> {
	
	String c1="select new JobRequestSerialNumber(a.id,a.serialNumber,a.longitude,a.latitude,a.entryMode,a.phoneModel,a.date,"//
			+ "a.packingDetail.parent.partNumber.name,a.packingDetail.parent.partNumber.description,a.packingDetail.parent.partNumber.brandName,a.deliveryRequest.project.name," //
			+ "a.deliveryRequest.reference,a.deliveryRequest.date4,"
			+ "a.deliveryRequest.deliverToCompanyType,"
			+ "(select b.name from Company b where b.id = a.deliveryRequest.deliverToCompany.id),"//
			+ "(select b.name from Customer b where b.id = a.deliveryRequest.deliverToCustomer.id),"//
			+ "(select b.name from Supplier b where b.id = a.deliveryRequest.deliverToSupplier.id),"//
			+ "a.deliveryRequest.deliverToOther,"//
			+ "(select b.name from Site b where b.id = a.deliveryRequest.destination.id),"//
			+ "(select b.name from Customer b where b.id = (select c.customer.id from Project c where c.id = a.deliveryRequest.destinationProject.id)),"//
			+ "(select b.name from Project b where b.id = a.deliveryRequest.destinationProject.id),"//
			+ "(select b.name from Customer b where b.id = a.deliveryRequest.endCustomer.id),"//
			+ "(select concat(b.numeroInvoice,'-',(select c.project.customer.abbreviation from Po c where c.id = a.deliveryRequest.po.id)) from Po b where b.id = a.deliveryRequest.po.id),"//
			+ "(select b.fullName from User b where b.username = a.deliveryRequest.toUser.username),"//
			+ "a.deliveryRequest.warehouse.name"//
			+ ") ";
	
	
	@Query(c1+"from JobRequestSerialNumber a left join a.deliveryRequest.warehouse as warehouse where (a.deliveryRequest.project.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.manager.username = ?1 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?1 or warehouse.id in (?2) or a.deliveryRequest.project.id in (?3)) and (a.deliveryRequest.company.id = ?4) and a.deliveryRequest.sdm is true")
	List<JobRequestSerialNumber> findDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId);

}

