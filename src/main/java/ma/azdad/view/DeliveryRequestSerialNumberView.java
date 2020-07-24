package ma.azdad.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.StockRow;
import ma.azdad.service.DeliveryRequestSerialNumberService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.StockRowService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class DeliveryRequestSerialNumberView extends GenericViewOld<DeliveryRequestSerialNumber> {

	@Autowired
	private DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private StockRowService stockRowService;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	private DeliveryRequestSerialNumber deliveryRequestSerialNumber = new DeliveryRequestSerialNumber();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
		if (isEditPage)
			deliveryRequestSerialNumber = deliveryRequestSerialNumberService.findOne(selectedId);
		else if (isViewPage)
			deliveryRequestSerialNumber = deliveryRequestSerialNumberService.findOne(selectedId);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		if ("/viewDeliveryRequest.xhtml".equals(currentPath))
			list2 = list1 = deliveryRequestSerialNumberService.findByDeliveryRequest(selectedId);
	}

	public void flushDeliveryRequestSerialNumber() {
		deliveryRequestSerialNumberService.flush();
	}

	public void refreshDeliveryRequestSerialNumber() {
		if (deliveryRequestSerialNumber.getId() != null)
			deliveryRequestSerialNumber = deliveryRequestSerialNumberService.findOne(deliveryRequestSerialNumber.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		if (!canViewDeliveryRequestSerialNumber())
			cacheView.accessDenied();
	}

	public Boolean canViewDeliveryRequestSerialNumber() {
		return true;
	}

	// SAVE DELIVERYREQUESTSERIALNUMBER
	public Boolean canSaveDeliveryRequestSerialNumber() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveDeliveryRequestSerialNumber() {
		if (!canSaveDeliveryRequestSerialNumber())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateDeliveryRequestSerialNumber())
			return null;
		deliveryRequestSerialNumber = deliveryRequestSerialNumberService.save(deliveryRequestSerialNumber);

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequestSerialNumber.getId());
	}

	public Boolean validateDeliveryRequestSerialNumber() {
		return true;
	}

	// DELETE DELIVERYREQUESTSERIALNUMBER
	public Boolean canDeleteDeliveryRequestSerialNumber() {
		return true;
	}

	public String deleteDeliveryRequestSerialNumber() {
		if (canDeleteDeliveryRequestSerialNumber())
			deliveryRequestSerialNumberService.delete(deliveryRequestSerialNumber);
		return addParameters(listPage, "faces-redirect=true");
	}

	// Fill SN
	private Boolean editMode = false;
	private List<Integer> exculdeList = new ArrayList<Integer>();

	public Boolean canEdit(DeliveryRequest deliveryRequest) {
		if (deliveryRequest.getIsInbound())
			return deliveryRequest.getDate4() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && !editMode;
		else if (deliveryRequest.getIsOutbound())
			return deliveryRequest.getDate4() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && !editMode && list1.isEmpty();
		return false;
	}

	public void initEdit(DeliveryRequest deliveryRequest) {
		if (deliveryRequest.getIsOutbound()) {
			// add remaining qty as empty rows
			List<DeliveryRequestSerialNumber> inboundSerialNumberList = deliveryRequestSerialNumberService.findInboundSerialNumberByOutboundDeliveryRequest(deliveryRequest.getId());
			Set<String> inboundSerialNumberKeySet = inboundSerialNumberList.stream().map(i -> i.getKey1()).collect(Collectors.toSet());

			Map<String, Integer> map = new HashMap<>();

			for (Integer stockRowId : deliveryRequest.getStockRowList().stream().map(i -> i.getId()).collect(Collectors.toList())) {
				StockRow outboundStockRow = stockRowService.findOne(stockRowId);
				for (PackingDetail packingDetail : outboundStockRow.getPacking().getDetailList()) {

					map.putIfAbsent(outboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), 0);

					String key1 = outboundStockRow.getKey() + ";" + packingDetail.getId();

					if (!inboundSerialNumberKeySet.contains(key1))
						continue;

					int packingQuantity = (int) (-outboundStockRow.getQuantity() / outboundStockRow.getPacking().getQuantity());
					int n = packingDetail.getQuantity();

					for (int i = 0; i < packingQuantity; i++) {
						int packingNumero = map.get(outboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId()) + 1;
						map.put(outboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), packingNumero);
						for (int j = 0; j < n; j++)
							list1.add(new DeliveryRequestSerialNumber(packingNumero, packingDetail, deliveryRequest, outboundStockRow));

					}

				}
			}
			list1.sort(DeliveryRequestSerialNumberService.COMPARATOR);
		}
	}

	public Boolean canSave(DeliveryRequest deliveryRequest) {
		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsOutbound())
			return cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && editMode;
		return false;
	}

	public void save(DeliveryRequest deliveryRequest) {
		if (!validate(deliveryRequest))
			return;

		if (deliveryRequest.getIsOutbound())
			list1.forEach(i -> i.setOutboundDeliveryRequest(deliveryRequest));

		list1.stream().forEach(i -> deliveryRequestSerialNumberService.save(i));

		// update missingSn field
		if (deliveryRequest.getIsInbound()) {
			if (deliveryRequestSerialNumberService.countByInboundDeliveryRequestAndEmpty(deliveryRequest.getId()) == 0)
				deliveryRequestService.updateMissingSerialNumber(deliveryRequest.getId(), false);
		} else if (deliveryRequest.getIsOutbound())
			deliveryRequestService.updateMissingSerialNumber(deliveryRequest.getId(), false);

		editMode = false;
		refreshList();
	}

	private Boolean validate(DeliveryRequest deliveryRequest) {
		if (deliveryRequest.getIsInbound()) {
			if (list1.stream().filter(i -> i.getSerialNumber() != null && !i.getSerialNumber().isEmpty()).count() > list1.stream().filter(i -> i.getSerialNumber() != null && !i.getSerialNumber().isEmpty()).map(i -> i.getSerialNumber()).distinct().count())
				return FacesContextMessages.ErrorMessages("Duplicate SN !");
		} else if (deliveryRequest.getIsOutbound()) {
			if (list1.stream().filter(i -> i.getInboundStockRow() == null).count() > 0)
				return FacesContextMessages.ErrorMessages("You should fill all packings");
			if (list1.stream().filter(i -> i.getSerialNumber() == null || i.getSerialNumber().isEmpty()).count() > 0)
				return FacesContextMessages.ErrorMessages("SN should not be null");

		}

		return true;
	}

	public void selectSerialNumberListener(DeliveryRequestSerialNumber drsn, DeliveryRequest deliveryRequest) {

		Integer packingNumero = deliveryRequestSerialNumberService.findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(drsn.getTmpPartNumber().getId(), drsn.getTmpInboundDeliveryRequest().getId(), drsn.getSerialNumber());
		list1.removeIf(i -> i.getInboundStockRow() == null && i.getTmpInboundDeliveryRequest().getId().equals(drsn.getTmpInboundDeliveryRequest().getId()) && i.getTmpPartNumber().getId().equals(drsn.getTmpPartNumber().getId()) && i.getPackingNumero().equals(drsn.getPackingNumero()));
		list1.addAll(deliveryRequestSerialNumberService.findByPartNumberAndInboundDeliveryRequestAndPackingNumero(drsn.getTmpPartNumber().getId(), drsn.getTmpInboundDeliveryRequest().getId(), packingNumero));

		exculdeList = list1.stream().filter(i -> i.getId() != null).map(i -> i.getId()).collect(Collectors.toList());

		// list1.sort(DeliveryRequestSerialNumberService.COMPARATOR);

	}

	// GENERIC
	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(DeliveryRequestSerialNumber current) {
		return deliveryRequestSerialNumberService.findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(current, exculdeList);
	}

	// GETTERS & SETTERS
	public DeliveryRequestSerialNumber getDeliveryRequestSerialNumber() {
		return deliveryRequestSerialNumber;
	}

	public void setDeliveryRequestSerialNumber(DeliveryRequestSerialNumber deliveryRequestSerialNumber) {
		this.deliveryRequestSerialNumber = deliveryRequestSerialNumber;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

}
