package ma.azdad.view;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestExpiryDate;
import ma.azdad.model.StockRow;
import ma.azdad.repos.DeliveryRequestExpiryDateRepos;
import ma.azdad.service.DeliveryRequestExpiryDateService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class DeliveryRequestExpiryDateView extends GenericView<Integer, DeliveryRequestExpiryDate, DeliveryRequestExpiryDateRepos, DeliveryRequestExpiryDateService> {

	@Autowired
	private DeliveryRequestExpiryDateService deliveryRequestExpiryDateService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private DeliveryRequestView deliveryRequestView;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	private DeliveryRequestExpiryDate deliveryRequestExpiryDate = new DeliveryRequestExpiryDate();

	private DeliveryRequest deliveryRequest;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
		if (isEditPage)
			deliveryRequestExpiryDate = deliveryRequestExpiryDateService.findOne(id);
		else if (isViewPage)
			deliveryRequestExpiryDate = deliveryRequestExpiryDateService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if ("/viewDeliveryRequest.xhtml".equals(currentPath)) {
			list1 = deliveryRequestExpiryDateService.findByDeliveryRequest(id);
			deliveryRequest = deliveryRequestView.getDeliveryRequest();
			if (deliveryRequest.getIsOutbound()) {
				addRemainingToOutbound();
				autoSaveOutboundExpiryList();
			}

		}
	}

	public void flushDeliveryRequestExpiryDate() {
		deliveryRequestExpiryDateService.flush();
	}

	public void refreshDeliveryRequestExpiryDate() {
		if (deliveryRequestExpiryDate.getId() != null)
			deliveryRequestExpiryDate = deliveryRequestExpiryDateService.findOne(deliveryRequestExpiryDate.getId());
	}

	// Fill EXPRTY DATE
	private Boolean editMode = false;

	public Boolean canEdit(DeliveryRequest deliveryRequest) {
		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsOutbound())
			return deliveryRequest.getDate4() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && !editMode && (list1.isEmpty() || list1.stream().filter(i -> i.getId() == null).count() > 0);
		return false;
	}

	public void initEdit(DeliveryRequest deliveryRequest) {
		if (list1.isEmpty() && deliveryRequest.getIsInbound())
			generateInboundExpiryList(deliveryRequest);

	}

	private void addRemainingToOutbound() {
		Map<Integer, Double> map = list1.stream().collect(Collectors.groupingBy(DeliveryRequestExpiryDate::getStockRowId, Collectors.summingDouble(DeliveryRequestExpiryDate::getQuantity)));
		deliveryRequest.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable() && -i.getQuantity() > map.getOrDefault(i.getId(), 0.0)).forEach(i -> list1.add(new DeliveryRequestExpiryDate(i, true, deliveryRequestExpiryDateService.findOneExpiryDate(i))));
	}

	private void generateInboundExpiryList(DeliveryRequest deliveryRequest) {
		deliveryRequest.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable()).forEach(i -> list1.add(new DeliveryRequestExpiryDate(i, true)));
	}

	// auto save outbound list if no conflict
	private void autoSaveOutboundExpiryList() {
		System.out.println("autoSaveOutboundList");
		list1.stream().filter(i -> i.getExpiryDate() != null && i.getId() == null).forEach(i -> deliveryRequestExpiryDateService.save(i));
		if (list1.size() == list1.stream().filter(i -> i.getExpiryDate() != null).count())
			deliveryRequestService.updateMissingExpiry(deliveryRequest.getId(), false);
	}

	public void changeQuantityListener(DeliveryRequestExpiryDate row, int index) {
		if (UtilsFunctions.compareDoubles(row.getQuantity(), 0.0, 4) <= 0)
			FacesContextMessages.ErrorMessages("Quantity should be greather than 0");
		else if (UtilsFunctions.compareDoubles(row.getTmpQuantity(), row.getQuantity(), 4) != 0) {
			list1.add(++index, new DeliveryRequestExpiryDate(row.getStockRow(), row.getTmpQuantity() - row.getQuantity(), false));
			row.setTmpQuantity(row.getQuantity());
		}
	}

	public void cancelQuantityChange(DeliveryRequestExpiryDate row) {
		row.setQuantity(row.getTmpQuantity());
	}

	public void remove(DeliveryRequestExpiryDate row, int index) {
		if (!row.getInitial()) {
			if (list1.stream().filter(i -> i.getId() == null && UtilsFunctions.compareDoubles(i.getQuantity(), i.getTmpQuantity(), 4) != 0).count() > 0)
				return;
			DeliveryRequestExpiryDate previousRow = list1.get(index - 1);
			previousRow.setQuantity(previousRow.getQuantity() + row.getQuantity());
			previousRow.setTmpQuantity(previousRow.getQuantity());
			list1.remove(index);
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

		list1.stream().forEach(i -> deliveryRequestExpiryDateService.save(i));

		deliveryRequestService.updateMissingExpiry(deliveryRequest.getId(), false);

		editMode = false;
		refreshList();
	}

	private Boolean validate(DeliveryRequest deliveryRequest) {
		if (list1.stream().filter(i -> i.getExpiryDate() == null).count() > 0)
			return FacesContextMessages.ErrorMessages("Expiry Date should not be null");
		else if (list1.stream().map(i -> i.getStockRow().getId() + UtilsFunctions.getFormattedDate(i.getExpiryDate())).distinct().count() < list1.size())
			return FacesContextMessages.ErrorMessages("The Combination (stockRow,expiryDate) should be unique");

		if (deliveryRequest.getIsOutbound() && list1.stream().filter(i -> i.getId() == null && i.getQuantity() > deliveryRequestExpiryDateService.findRemainingQuantity(i)).count() > 0)
			return FacesContextMessages.ErrorMessages("Quantity should not be gerather than inbound remaining quantity");

		return true;
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewDeliveryRequestExpiryDate())
			cacheView.accessDenied();
	}

	public Boolean canViewDeliveryRequestExpiryDate() {
		return true;
	}

	// SAVE DELIVERYREQUESTEXPIRYDATE
	public Boolean canSaveDeliveryRequestExpiryDate() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveDeliveryRequestExpiryDate() {
		if (!canSaveDeliveryRequestExpiryDate())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateDeliveryRequestExpiryDate())
			return null;
		deliveryRequestExpiryDate = deliveryRequestExpiryDateService.save(deliveryRequestExpiryDate);

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequestExpiryDate.getId());
	}

	public Boolean validateDeliveryRequestExpiryDate() {
		return true;
	}

	// DELETE DELIVERYREQUESTEXPIRYDATE
	public Boolean canDeleteDeliveryRequestExpiryDate() {
		return true;
	}

	public String deleteDeliveryRequestExpiryDate() {
		if (canDeleteDeliveryRequestExpiryDate())
			try {
				deliveryRequestExpiryDateService.delete(deliveryRequestExpiryDate);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return addParameters(listPage, "faces-redirect=true");
	}

	// GENERIC
	public List<Date> findRemainingExpiryDateList(StockRow outboundStockRow) {
		return deliveryRequestExpiryDateService.findRemainingExpiryDateList(outboundStockRow);
	}

	// GETTERS & SETTERS
	public DeliveryRequestExpiryDate getDeliveryRequestExpiryDate() {
		return deliveryRequestExpiryDate;
	}

	public void setDeliveryRequestExpiryDate(DeliveryRequestExpiryDate deliveryRequestExpiryDate) {
		this.deliveryRequestExpiryDate = deliveryRequestExpiryDate;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

}
