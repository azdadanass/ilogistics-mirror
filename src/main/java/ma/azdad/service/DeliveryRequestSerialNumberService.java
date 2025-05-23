package ma.azdad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowStatus;
import ma.azdad.repos.DeliveryRequestSerialNumberRepos;
import ma.azdad.view.DeliveryRequestSerialNumberView;
import ma.azdad.view.DeliveryRequestSerialNumberView.SerialNumberSummary;

@Component
public class DeliveryRequestSerialNumberService extends GenericService<Integer, DeliveryRequestSerialNumber, DeliveryRequestSerialNumberRepos> {

	public static Comparator<DeliveryRequestSerialNumber> COMPARATOR = Comparator.comparingInt(DeliveryRequestSerialNumber::getNotNullInboundStockRowId)
			.thenComparingInt(DeliveryRequestSerialNumber::getTmpPartNumberId).thenComparingInt(DeliveryRequestSerialNumber::getPackingNumero)
			.thenComparingInt(DeliveryRequestSerialNumber::getNotNullId);

	@Autowired
	DeliveryRequestSerialNumberRepos repos;

	@Autowired
	DeliveryRequestService deliveryRequestService;

	@Override
	public DeliveryRequestSerialNumber findOne(Integer id) {
		DeliveryRequestSerialNumber deliveryRequestSerialNumber = super.findOne(id);
		// Hibernate.initialize(deliveryRequestSerialNumber.get..);
		return deliveryRequestSerialNumber;
	}

	public List<DeliveryRequestSerialNumber> findByDeliveryRequest(Integer deliveryRequestId) {
		List<DeliveryRequestSerialNumber> result = repos.findByDeliveryRequest(deliveryRequestId);
		result.forEach(i -> {
			i.init();
			Hibernate.initialize(i.getPackingDetail());
			Hibernate.initialize(i.getInboundStockRow());
		});
		result.sort(COMPARATOR);
		return result;
	}
	
	public List<DeliveryRequestSerialNumber> findByInboundDeliveryRequestDetail(Integer inboundDeliveryRequestId){
		return repos.findByInboundDeliveryRequestDetail(inboundDeliveryRequestId);
	}
	
	public List<DeliveryRequestSerialNumber>  findNotUsedByOutboundDeliveryRequestAndPartNumber(Integer outboundDeliveryRequestId,Integer partNumberId,List<String> usedSnList){
		if(usedSnList.isEmpty())
			return repos.findByOutboundDeliveryRequestAndPartNumber(outboundDeliveryRequestId, partNumberId);
		return repos.findNotUsedByOutboundDeliveryRequestAndPartNumber(outboundDeliveryRequestId, partNumberId,usedSnList);
	}

	public void save(PackingDetail packingDetail, StockRow inboundStockRow) {
		int n = (int) (inboundStockRow.getQuantity() / packingDetail.getParent().getQuantity());
		for (int i = 0; i < n; i++)
			for (int j = 0; j < packingDetail.getQuantity(); j++)
				save(new DeliveryRequestSerialNumber(i + 1, packingDetail, inboundStockRow));
	}

	public List<DeliveryRequestSerialNumber> findInboundSerialNumberByOutboundDeliveryRequest(Integer outboundDeliveryRequestId) {
		return repos.findInboundSerialNumberByOutboundDeliveryRequest(outboundDeliveryRequestId);
	}

	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(Integer partNumberId, Integer inboundDeliveryRequestId,
			StockRowStatus status, Integer locationId, Integer packingDetailId, List<Integer> exculdeList) {

		return repos.findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(partNumberId, inboundDeliveryRequestId, status, locationId, packingDetailId,
				!exculdeList.isEmpty() ? exculdeList : Arrays.asList(-1));
	}

	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(DeliveryRequestSerialNumber current, List<Integer> exculdeList) {
		return findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(current.getTmpPartNumber().getId(), current.getTmpInboundDeliveryRequest().getId(),
				current.getTmpStockRowStatus(), current.getTmpLocation().getId(), current.getPackingDetail().getId(), exculdeList);
	}

	public Integer findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(Integer partNumberId, Integer inboundDeliveryRequestId, String serialNumber) {
		return repos.findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(partNumberId, inboundDeliveryRequestId, serialNumber);
	}

	public List<DeliveryRequestSerialNumber> findByPartNumberAndInboundDeliveryRequestAndPackingNumero(Integer partNumberId, Integer inboundDeliveryRequestId, Integer packingNumero) {
		List<DeliveryRequestSerialNumber> result = repos.findByPartNumberAndInboundDeliveryRequestAndPackingNumero(partNumberId, inboundDeliveryRequestId, packingNumero);
		result.forEach(i -> i.init());
		return result;
	}
	
	public Integer findMaxPackingNumero(Integer inboundDeliveryRequestDetailId,Integer packingDetailId) {
		return repos.findMaxPackingNumero(inboundDeliveryRequestDetailId, packingDetailId);
	}
	
//	public Long countByInboundStockRowAndPackingDetail(Integer inboundStockRowId,Integer packingDetailId) {
//		return ObjectUtils.firstNonNull(repos.countByInboundStockRowAndPackingDetail(inboundStockRowId,packingDetailId),0l);
//	}
	
	public Long countByInboundStockRow(Integer inboundStockRowId) {
		return ObjectUtils.firstNonNull(repos.countByInboundStockRow(inboundStockRowId),0l);
	}

	public Long countByInboundDeliveryRequestAndEmpty(Integer deliveryRequestId) {
		return repos.countByInboundDeliveryRequestAndEmpty(deliveryRequestId);
	}

	public Long countByPartNumberAndInboundDeliveryRequest(Integer partNumberId, Integer inboundDeliveryRequestId) {
		return repos.countByPartNumberAndInboundDeliveryRequest(partNumberId, inboundDeliveryRequestId);
	}

	public Long countByDeliveryRequest(Integer deliveryRequestId) {
		return ObjectUtils.firstNonNull(repos.countByDeliveryRequest(deliveryRequestId), 0l);
	}

	public Long countByInboundDeliveryRequest(Integer deliveryRequestId) {
		return ObjectUtils.firstNonNull(repos.countByInboundDeliveryRequest(deliveryRequestId), 0l);
	}

	public Long countByOutboundDeliveryRequest(Integer deliveryRequestId) {
		return ObjectUtils.firstNonNull(repos.countByOutboundDeliveryRequest(deliveryRequestId), 0l);
	}

	public Long countByPackingDetail(Integer packginDetailId) {
		return ObjectUtils.firstNonNull(repos.countByPackingDetail(packginDetailId), 0l);
	}

	public List<DeliveryRequestSerialNumber> findRemainingOutbound(Integer deliveryRequestDetailId, Integer packingDetailId) {
		return repos.findRemainingOutbound(deliveryRequestDetailId, packingDetailId);
	}

	public List<DeliveryRequestSerialNumber> findHavingSerialNumberAndNoOutbound(Integer inboundDeliveryRequestDetailId, Integer locationId, Integer packingDetailId, Integer limit) {
		List<DeliveryRequestSerialNumber> data = repos.findHavingSerialNumberAndNoOutbound(inboundDeliveryRequestDetailId, locationId, packingDetailId);
		return data.subList(0, Math.min(limit, data.size()));
	}

	public void automaticFillOutboundSerialNumberScript(Integer limit) {
		List<DeliveryRequestSerialNumber> data = repos.automaticFillOutboundSerialNumberQuery1();
		data = data.subList(0, Math.min(limit, data.size()));
		int i = 0;
		for (DeliveryRequestSerialNumber drsn : data) {
			System.out.println("------------------------------------");
			System.out.println(UtilsFunctions.formatDouble(i * 100.0 / (double) data.size()) + " %");
			System.out.println(i);
			Integer inboundDeliveryRequestDetailId = drsn.getInboundStockRow().getDeliveryRequestDetail().getId();
			List<DeliveryRequest> outboundList = repos.automaticFillOutboundSerialNumberQuery2(inboundDeliveryRequestDetailId);
			for (DeliveryRequest outbound : outboundList) {
				Integer packingQuantity = drsn.getPackingDetail().getParent().getQuantity();
				Integer packingDetailQuantity = drsn.getPackingDetail().getQuantity();
				Double stockRowQuantity = outbound.getStockRowList().stream().filter(sr -> sr.getInboundDeliveryRequestDetail().getId().equals(inboundDeliveryRequestDetailId))
						.mapToDouble(sr -> sr.getQuantity()).sum();
				Double quantity = -stockRowQuantity * packingDetailQuantity / packingQuantity;
				Double usedQuantity = (double) repos.countByOutboundDelievryRequestAndInboundeDeliveryDetailAndPackingDetail(outbound.getId(), inboundDeliveryRequestDetailId,
						drsn.getPackingDetail().getId());
				System.out.println(outbound.getReference());
				System.out.println("quantity : " + quantity);
				System.out.println("usedQuantity : " + usedQuantity);
				if (quantity > usedQuantity) {
					drsn.setOutboundDeliveryRequest(outbound);
					repos.save(drsn);
					deliveryRequestService.calculateMissingSerialNumber(outbound.getId());
					break;
				}
			}
			i++;
		}
	}

	///////////////// Mobile
	public ResponseEntity<String> scanOutboundSnMobile(Integer id, String serialNumber) {
		DeliveryRequest dn = deliveryRequestService.findOne(id);
		DeliveryRequest deliveryRequest = dn;

		List<StockRow> list = deliveryRequest.getStockRowList().stream()
				.filter(i -> i.getInboundDeliveryRequest().getIsSnRequired() && i.getPacking().getDetailList().stream().filter(pd -> pd.getHasSerialnumber()).count() > 0).collect(Collectors.toList());
		List<SerialNumberSummary> serialNumberSummaryList;

		serialNumberSummaryList = new ArrayList<DeliveryRequestSerialNumberView.SerialNumberSummary>();

		Map<String, SerialNumberSummary> map = new HashMap<String, SerialNumberSummary>(); // key (inboundDeliveryRequestDetailId;packingDetailId)
		for (StockRow stockRow : list) {
			stockRow.getPacking().getDetailList().stream().filter(pd -> pd.getHasSerialnumber()).forEach(pd -> {
				String key = stockRow.getInboundDeliveryRequestDetail().getId() + ";" + pd.getId();
				Integer inboundDeliveryRequestDetailId = stockRow.getInboundDeliveryRequestDetail().getId();
				String partNumberName = stockRow.getPartNumberName();
				Integer inboundDeliveryRequestId = stockRow.getInboundDeliveryRequestId();
				String inboundDeliveryRequestReference = stockRow.getInboundDeliveryRequestReference();
				String packingName = stockRow.getPacking().getName();
				Double usedQuantity = (double) findByDeliveryRequest(id).stream()
						.filter(i -> i.getInboundStockRow().getDeliveryRequestDetail().getId().equals(inboundDeliveryRequestDetailId) && i.getPackingDetail().getId().equals(pd.getId())).count();

				map.putIfAbsent(key, new SerialNumberSummary(inboundDeliveryRequestDetailId, pd.getId(), partNumberName, inboundDeliveryRequestId, inboundDeliveryRequestReference, packingName,
						pd.getType(), usedQuantity));
				map.get(key).setQuantity(map.get(key).getQuantity() + pd.getQuantity() * (-stockRow.getQuantity() / stockRow.getPacking().getQuantity()));
			});
		}

		for (String key : map.keySet())
			serialNumberSummaryList.add(map.get(key));

		List<DeliveryRequestSerialNumber> inboundList1 = new ArrayList<DeliveryRequestSerialNumber>();
		serialNumberSummaryList.stream().filter(i -> i.getRemainingQuantity() > 0.0)
				.forEach(i -> inboundList1.addAll(findRemainingOutbound(i.getInboundDeliveryRequestDetailId(), i.getPackingDetailId())));
		List<DeliveryRequestSerialNumber> matchList = inboundList1.stream().filter(sn -> serialNumber.equals(sn.getSerialNumber())).collect(Collectors.toList());
		if (!matchList.isEmpty()) {
			for (DeliveryRequestSerialNumber dns : matchList) {
				dns.setOutboundDeliveryRequest(dn);
//				deliveryRequestService.updateMissingSerialNumber(id, serialNumberSummaryList.stream().filter(i -> i.getRemainingQuantity() > 0).count() > 0);
				// correction azdad
				deliveryRequestService.calculateMissingSerialNumber(id);
			}
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN).body("SN scanned successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).body("The SN does not exist or already scanned");
		}
	}

}
