package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.StockRowStatus;

@Repository
public interface DeliveryRequestSerialNumberRepos extends JpaRepository<DeliveryRequestSerialNumber, Integer> {

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1 or a.outboundDeliveryRequest.id = ?1")
	List<DeliveryRequestSerialNumber> findByDeliveryRequest(Integer deliveryRequestId);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1 or a.outboundDeliveryRequest.id = ?1")
	Long countByDeliveryRequest(Integer deliveryRequestId);

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequestDetail.id in (select distinct b.inboundDeliveryRequestDetail.id from StockRow b where deliveryRequest.id = ?1 ) ")
	public List<DeliveryRequestSerialNumber> findInboundSerialNumberByOutboundDeliveryRequest(Integer outboundDeliveryRequestId);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2")
	public Long countByPartNumberAndInboundDeliveryRequest(Integer partNumberId, Integer inboundDeliveryRequestId);

	@Query("from DeliveryRequestSerialNumber a where a.serialNumber is not null and a.serialNumber != '' and a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2 and a.inboundStockRow.status = ?3 and a.inboundStockRow.location.id = ?4 and a.packingDetail.id = ?5 and outboundDeliveryRequest is null and a.id not in (?6)")
	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(Integer partNumberId, Integer inboundDeliveryRequestId, StockRowStatus status, Integer locationId, Integer packingDetailId, List<Integer> exculdeList);

	@Query("select a.packingNumero from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2 and a.serialNumber = ?3")
	public Integer findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(Integer partNumberId, Integer inboundDeliveryRequestId, String serialNumber);

	@Query("from DeliveryRequestSerialNumber a where a.inboundStockRow.partNumber.id = ?1 and a.inboundStockRow.deliveryRequest.id = ?2 and a.packingNumero = ?3")
	public List<DeliveryRequestSerialNumber> findByPartNumberAndInboundDeliveryRequestAndPackingNumero(Integer partNumberId, Integer inboundDeliveryRequestId, Integer packingNumero);

	// INBOUND TESTS
	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1")
	public Long countByInboundDeliveryRequest(Integer deliveryRequestId);

	@Query("select count(*) from DeliveryRequestSerialNumber a where a.inboundStockRow.deliveryRequest.id = ?1 and (a.serialNumber is null or serialNumber = '')  ")
	public Long countByInboundDeliveryRequestAndEmpty(Integer deliveryRequestId);

	// OUTBOUND TESTS

}
