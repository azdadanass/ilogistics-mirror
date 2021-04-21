package ma.azdad.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowStatus;
import ma.azdad.repos.DeliveryRequestSerialNumberRepos;

@Component
public class DeliveryRequestSerialNumberService extends GenericService<Integer, DeliveryRequestSerialNumber, DeliveryRequestSerialNumberRepos> {

	public static Comparator<DeliveryRequestSerialNumber> COMPARATOR = Comparator.comparingInt(DeliveryRequestSerialNumber::getNotNullInboundStockRowId).thenComparingInt(DeliveryRequestSerialNumber::getTmpPartNumberId).thenComparingInt(DeliveryRequestSerialNumber::getPackingNumero).thenComparingInt(DeliveryRequestSerialNumber::getNotNullId);

	@Autowired
	DeliveryRequestSerialNumberRepos deliveryRequestSerialNumberRepos;

	@Override
	public DeliveryRequestSerialNumber findOne(Integer id) {
		DeliveryRequestSerialNumber deliveryRequestSerialNumber = super.findOne(id);
		// Hibernate.initialize(deliveryRequestSerialNumber.get..);
		return deliveryRequestSerialNumber;
	}

	public List<DeliveryRequestSerialNumber> findByDeliveryRequest(Integer deliveryRequestId) {
		List<DeliveryRequestSerialNumber> result = deliveryRequestSerialNumberRepos.findByDeliveryRequest(deliveryRequestId);

		UtilsFunctions.separatorTop();
		System.out.println(result);
		UtilsFunctions.separatorBottom();
		result.forEach(i -> {
			i.init();
			Hibernate.initialize(i.getPackingDetail());
			Hibernate.initialize(i.getInboundStockRow());
		});
		result.sort(COMPARATOR);
		return result;
	}

	public void save(PackingDetail packingDetail, StockRow inboundStockRow) {
		int n = (int) (inboundStockRow.getQuantity() / packingDetail.getParent().getQuantity());
		for (int i = 0; i < n; i++)
			for (int j = 0; j < packingDetail.getQuantity(); j++)
				save(new DeliveryRequestSerialNumber(i + 1, packingDetail, inboundStockRow));
	}

	public List<DeliveryRequestSerialNumber> findInboundSerialNumberByOutboundDeliveryRequest(Integer outboundDeliveryRequestId) {
		return deliveryRequestSerialNumberRepos.findInboundSerialNumberByOutboundDeliveryRequest(outboundDeliveryRequestId);
	}

	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(Integer partNumberId, Integer inboundDeliveryRequestId, StockRowStatus status, Integer locationId, Integer packingDetailId, List<Integer> exculdeList) {

		return deliveryRequestSerialNumberRepos.findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(partNumberId, inboundDeliveryRequestId, status, locationId, packingDetailId, !exculdeList.isEmpty() ? exculdeList : Arrays.asList(-1));
	}

	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(DeliveryRequestSerialNumber current, List<Integer> exculdeList) {
		return findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(current.getTmpPartNumber().getId(), current.getTmpInboundDeliveryRequest().getId(), current.getTmpStockRowStatus(), current.getTmpLocation().getId(), current.getPackingDetail().getId(), exculdeList);
	}

	public Integer findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(Integer partNumberId, Integer inboundDeliveryRequestId, String serialNumber) {
		return deliveryRequestSerialNumberRepos.findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(partNumberId, inboundDeliveryRequestId, serialNumber);
	}

	public List<DeliveryRequestSerialNumber> findByPartNumberAndInboundDeliveryRequestAndPackingNumero(Integer partNumberId, Integer inboundDeliveryRequestId, Integer packingNumero) {
		List<DeliveryRequestSerialNumber> result = deliveryRequestSerialNumberRepos.findByPartNumberAndInboundDeliveryRequestAndPackingNumero(partNumberId, inboundDeliveryRequestId, packingNumero);
		result.forEach(i -> i.init());
		return result;
	}

	public Long countByInboundDeliveryRequestAndEmpty(Integer deliveryRequestId) {
		return deliveryRequestSerialNumberRepos.countByInboundDeliveryRequestAndEmpty(deliveryRequestId);
	}

	public Long countByPartNumberAndInboundDeliveryRequest(Integer partNumberId, Integer inboundDeliveryRequestId) {
		return deliveryRequestSerialNumberRepos.countByPartNumberAndInboundDeliveryRequest(partNumberId, inboundDeliveryRequestId);
	}

}
