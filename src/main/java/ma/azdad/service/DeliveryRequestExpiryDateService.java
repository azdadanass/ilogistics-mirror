package ma.azdad.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestExpiryDate;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowStatus;
import ma.azdad.repos.DeliveryRequestExpiryDateRepos;
import ma.azdad.repos.DeliveryRequestRepos;
import ma.azdad.repos.StockRowRepos;

@Component
public class DeliveryRequestExpiryDateService extends GenericService<Integer, DeliveryRequestExpiryDate, DeliveryRequestExpiryDateRepos> {

	@Autowired
	DeliveryRequestExpiryDateRepos deliveryRequestExpiryDateRepos;

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	DeliveryRequestRepos deliveryRequestRepos;

	@Override
	public DeliveryRequestExpiryDate findOne(Integer id) {
		DeliveryRequestExpiryDate deliveryRequestExpiryDate = super.findOne(id);
		// Hibernate.initialize(deliveryRequestExpiryDate.get..);
		return deliveryRequestExpiryDate;
	}

	public List<DeliveryRequestExpiryDate> findByDeliveryRequest(Integer deliveryRequestId) {
		List<DeliveryRequestExpiryDate> result = deliveryRequestExpiryDateRepos.findByDeliveryRequest(deliveryRequestId);
		result.forEach(i -> {
			Hibernate.initialize(i.getStockRow().getPartNumber());
			Hibernate.initialize(i.getStockRow().getLocation());
		});

		return result;
	}

	public List<Date> findRemainingExpiryDateList(Integer partNumberId, Integer inboundDeliveryRequestId, StockRowStatus stockRowStatus, Integer locationId) {
		return deliveryRequestExpiryDateRepos.findRemainingExpiryDateList(partNumberId, inboundDeliveryRequestId, DeliveryRequestType.OUTBOUND, stockRowStatus, locationId);
	}

	public List<Date> findRemainingExpiryDateList(StockRow outboundStockRow) {
		return findRemainingExpiryDateList(outboundStockRow.getPartNumber().getId(), outboundStockRow.getInboundDeliveryRequest().getId(), outboundStockRow.getStatus(), outboundStockRow.getLocation().getId());
	}

	public Double findRemainingQuantity(DeliveryRequestExpiryDate dred) {
		return deliveryRequestExpiryDateRepos.findRemainingQuantity(dred.getStockRow().getPartNumber().getId(), dred.getStockRow().getInboundDeliveryRequest().getId(), DeliveryRequestType.OUTBOUND, dred.getStockRow().getStatus(), dred.getStockRow().getLocation().getId(), dred.getExpiryDate());
	}

	public void generateForOutboundAssociatedWithInbound(Integer inboundDeliveryRequestId) {
		List<DeliveryRequestExpiryDate> expiryList = findByDeliveryRequest(inboundDeliveryRequestId);
		if (expiryList.isEmpty())
			return;
		// test if we have one expiry date per stock row
		if (expiryList.stream().map(i -> i.getStockRow().getId()).count() > expiryList.stream().map(i -> i.getStockRow().getId()).distinct().count()) {
			System.err.println("multiple expiry date per stock row");
			return;
		}

		List<Integer> associatedOutboundIdList = stockRowRepos.findAssociatedOutboundWithInbound(inboundDeliveryRequestId, DeliveryRequestType.OUTBOUND);
		for (Integer outboundDeliveryRequestId : associatedOutboundIdList) {
			List<Integer> associatedInboundIdList = stockRowRepos.findAssociatedInboundWithOutbound(outboundDeliveryRequestId);
			// skip outbound that have others inbounds
			if (associatedInboundIdList.size() != 1)
				continue;
			List<DeliveryRequestExpiryDate> outboundExpiryList = findByDeliveryRequest(outboundDeliveryRequestId);
			if (!outboundExpiryList.isEmpty())
				continue;
			for (StockRow stockRow : deliveryRequestRepos.findById(outboundDeliveryRequestId).get().getStockRowList()) {
				List<DeliveryRequestExpiryDate> list = deliveryRequestExpiryDateRepos.findByDeliveryRequestAndPartNumberAndStatusAndLocation(inboundDeliveryRequestId, stockRow.getPartNumber().getId(), stockRow.getStatus(), stockRow.getLocation().getId());
				if (list.size() != 1)
					continue;
				DeliveryRequestExpiryDate dred = new DeliveryRequestExpiryDate();
				dred.setStockRow(stockRow);
				dred.setQuantity(-stockRow.getQuantity());
				dred.setExpiryDate(list.get(0).getExpiryDate());
				save(dred);
			}
		}
	}

	public Date findOneExpiryDate(StockRow outboundStockRow) {
		List<DeliveryRequestExpiryDate> list = deliveryRequestExpiryDateRepos.findByDeliveryRequestAndPartNumberAndStatusAndLocation(outboundStockRow.getInboundDeliveryRequest().getId(), outboundStockRow.getPartNumber().getId(), outboundStockRow.getStatus(), outboundStockRow.getLocation().getId());
		if (list.size() == 1)
			return list.get(0).getExpiryDate();
		else
			return null;
	}
}
