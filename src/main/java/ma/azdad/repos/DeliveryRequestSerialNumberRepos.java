package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.StockRowStatus;

@Repository
public interface DeliveryRequestSerialNumberRepos extends JpaRepository<DeliveryRequestSerialNumber, Integer> {
	
	String c1 = "select new DeliveryRequestSerialNumber(a.id,a.packingNumero,a.serialNumber,a.box,a.inboundStockRow.partNumber.name,a.inboundStockRow.partNumber.description,a.inboundStockRow.status,a.inboundStockRow.deliveryRequest.id,a.inboundStockRow.deliveryRequest.reference,a.packingDetail.type,a.inboundStockRow.location.name) ";
	

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1 or a.outboundDeliveryRequest.id = ?1")
	List<DeliveryRequestSerialNumber> findByDeliveryRequest(Integer deliveryRequestId);
	
	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequestDetail.id = ?1")
	List<DeliveryRequestSerialNumber> findByInboundDeliveryRequestDetail(Integer inboundDeliveryRequestId);
	
	
	@Query("select max(a.packingNumero) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequestDetail.id = ?1 and a.packingDetail.id = ?2")
	Integer findMaxPackingNumero(Integer inboundDeliveryRequestDetailId,Integer packingDetailId);
	
//	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.id = ?1  and a.packingDetail.id = ?2")
//	Long countByInboundStockRowAndPackingDetail(Integer inboundStockRowId,Integer packingDetailId);
	
	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.id = ?1")
	Long countByInboundStockRow(Integer inboundStockRowId);
	
	@Query("from DeliveryRequestSerialNumber a where a.outboundDeliveryRequest.id  = ?1 and a.inboundStockRow.partNumber.id = ?2 and a.serialNumber is not null and a.serialNumber != ''")
	List<DeliveryRequestSerialNumber> findByOutboundDeliveryRequestAndPartNumber(Integer outboundDeliveryRequestId,Integer partNumberId);
	
	@Query("from DeliveryRequestSerialNumber a where a.outboundDeliveryRequest.id  = ?1 and a.inboundStockRow.partNumber.id = ?2 and a.serialNumber is not null and a.serialNumber != '' and a.serialNumber not in (?3)")
	List<DeliveryRequestSerialNumber> findNotUsedByOutboundDeliveryRequestAndPartNumber(Integer outboundDeliveryRequestId,Integer partNumberId,List<String> usedSnList);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1 or a.outboundDeliveryRequest.id = ?1")
	Long countByDeliveryRequest(Integer deliveryRequestId);

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequestDetail.id in (select distinct b.inboundDeliveryRequestDetail.id from StockRow b where deliveryRequest.id = ?1 ) ")
	public List<DeliveryRequestSerialNumber> findInboundSerialNumberByOutboundDeliveryRequest(Integer outboundDeliveryRequestId);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2")
	public Long countByPartNumberAndInboundDeliveryRequest(Integer partNumberId, Integer inboundDeliveryRequestId);

	@Query("from DeliveryRequestSerialNumber a where a.serialNumber is not null and a.serialNumber != '' and a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2 and a.inboundStockRow.status = ?3 and a.inboundStockRow.location.id = ?4 and a.packingDetail.id = ?5 and outboundDeliveryRequest is null and a.id not in (?6)")
	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(Integer partNumberId, Integer inboundDeliveryRequestId,
			StockRowStatus status, Integer locationId, Integer packingDetailId, List<Integer> exculdeList);

	@Query("select a.packingNumero from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2 and a.serialNumber = ?3")
	public Integer findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(Integer partNumberId, Integer inboundDeliveryRequestId, String serialNumber);

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2 and a.packingNumero = ?3")
	public List<DeliveryRequestSerialNumber> findByPartNumberAndInboundDeliveryRequestAndPackingNumero(Integer partNumberId, Integer inboundDeliveryRequestId, Integer packingNumero);

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequestDetail.id = ?1 and a.packingDetail.id = ?2 and a.serialNumber is not null and a.serialNumber != '' and a.outboundDeliveryRequest is null")
	public List<DeliveryRequestSerialNumber> findRemainingOutbound(Integer deliveryRequestDetailId, Integer packingDetailId);

	@Query("SELECT a FROM DeliveryRequestSerialNumber a " + "JOIN a.inboundStockRow s " + "WHERE s.inboundDeliveryRequest.id IN "
			+ "(SELECT sr.inboundDeliveryRequest.id FROM StockRow sr WHERE sr.deliveryRequest.id = :deliveryRequestId) " + "AND a.serialNumber = :serialNumber "
			+ "AND a.outboundDeliveryRequest IS NULL")
	List<DeliveryRequestSerialNumber> findRemainingOutboundMobile(@Param("deliveryRequestId") Integer deliveryRequestId, @Param("serialNumber") String serialNumber);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1")
	public Long countByInboundDeliveryRequest(Integer deliveryRequestId);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.outboundDeliveryRequest.id = ?1")
	public Long countByOutboundDeliveryRequest(Integer deliveryRequestId);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1 and (a.serialNumber is null or serialNumber = '')  ")
	public Long countByInboundDeliveryRequestAndEmpty(Integer deliveryRequestId);
	
	@Query("select count(*) from DeliveryRequestSerialNumber a where a.packingDetail.id = ?1")
	public Long countByPackingDetail(Integer packginDetailId);
	
	
	@Modifying
	@Query("delete from DeliveryRequestSerialNumber where inboundStockRow.id in (select sr.id from StockRow sr where sr.deliveryRequest.id = ?1)")
	void deleteByInboundDeliveryRequest(Integer inboundDeliveryRequestId);
	
	@Query("from DeliveryRequestSerialNumber a where serialNumber is not null and serialNumber != '' and outboundDeliveryRequest is null and (select sum(b.quantity) from StockRow b where b.inboundDeliveryRequestDetail.id = a.inboundStockRow.deliveryRequestDetail.id) = 0")
	List<DeliveryRequestSerialNumber> automaticFillOutboundSerialNumberQuery1();
	
	@Query("select distinct a.deliveryRequest from StockRow a where a.inboundDeliveryRequestDetail.id = ?1 and a.deliveryRequest.type = 'OUTBOUND' and a.deliveryRequest.missingSerialNumber is true")
	List<DeliveryRequest> automaticFillOutboundSerialNumberQuery2(Integer inboundDeliveryRequestDetailId);
	
	@Query("select count(*) from DeliveryRequestSerialNumber a where a.outboundDeliveryRequest.id = ?1 and a.inboundStockRow.deliveryRequestDetail.id = ?2 and a.packingDetail.id = ?3")
	Long countByOutboundDelievryRequestAndInboundeDeliveryDetailAndPackingDetail(Integer outboundDeliveryRequestId,Integer inboundDeliveryDetailId,Integer packingDetailId);
	
	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequestDetail.id = ?1 and a.inboundStockRow.location.id = ?2 and a.packingDetail.id = ?3 and a.serialNumber is not null and a.serialNumber != '' and a.outboundDeliveryRequest is null")
	List<DeliveryRequestSerialNumber> findHavingSerialNumberAndNoOutbound(Integer inboundDeliveryRequestDetailId,Integer locationId,Integer packingDetailId);
	
	
	// reporting
	
	
	@Query(c1+"from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.company.id = ?2 and (a.inboundStockRow.deliveryRequest.project.manager.username = ?3 or a.inboundStockRow.deliveryRequest.project.costcenter.lob.manager.username = ?3 or a.inboundStockRow.deliveryRequest.project.costcenter.lob.bu.director.username = ?3 or a.inboundStockRow.deliveryRequest.project.id in (?4) or a.inboundStockRow.deliveryRequest.warehouse.id in (?5)) and a.outboundDeliveryRequest is null and a.serialNumber is not null and a.serialNumber != '' ")
	List<DeliveryRequestSerialNumber> findCurrentInventoryByPartNumber(Integer partNumberId,Integer companyId,String username, List<Integer> projectList, List<Integer> warehouseList);
	
	@Query(c1+"from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.project.id = ?1 and a.inboundStockRow.deliveryRequest.company.id = ?2 and (a.inboundStockRow.deliveryRequest.project.manager.username = ?3 or a.inboundStockRow.deliveryRequest.project.costcenter.lob.manager.username = ?3 or a.inboundStockRow.deliveryRequest.project.costcenter.lob.bu.director.username = ?3 or a.inboundStockRow.deliveryRequest.project.id in (?4) or a.inboundStockRow.deliveryRequest.warehouse.id in (?5)) and a.outboundDeliveryRequest is null and a.serialNumber is not null and a.serialNumber != '' ")
	List<DeliveryRequestSerialNumber> findCurrentInventoryByProject(Integer projectId,Integer companyId,String username, List<Integer> projectList, List<Integer> warehouseList);
	
	@Query(c1+"from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.warehouse.id = ?1 and a.inboundStockRow.deliveryRequest.company.id = ?2 and (a.inboundStockRow.deliveryRequest.project.manager.username = ?3 or a.inboundStockRow.deliveryRequest.project.costcenter.lob.manager.username = ?3 or a.inboundStockRow.deliveryRequest.project.costcenter.lob.bu.director.username = ?3 or a.inboundStockRow.deliveryRequest.project.id in (?4) or a.inboundStockRow.deliveryRequest.warehouse.id in (?5)) and a.outboundDeliveryRequest is null and a.serialNumber is not null and a.serialNumber != '' ")
	List<DeliveryRequestSerialNumber> findCurrentInventoryByWarehouse(Integer warehouseId,Integer companyId,String username, List<Integer> projectList, List<Integer> warehouseList);

}
