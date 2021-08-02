package ma.azdad.view;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.repos.DeliveryRequestDetailRepos;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class DeliveryRequestDetailView extends GenericView<Integer, DeliveryRequestDetail, DeliveryRequestDetailRepos, DeliveryRequestDetailService> {

	@Autowired
	private DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	private CacheView cacheView;

	private DeliveryRequestDetail deliveryRequestDetail = new DeliveryRequestDetail();

	private Integer companyId;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			deliveryRequestDetail = deliveryRequestDetailService.findOne(id);
		else if (isViewPage)
			deliveryRequestDetail = deliveryRequestDetailService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		companyId = UtilsFunctions.getIntegerParameter("companyId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = deliveryRequestDetailService.findAll();
	}

	public void refreshList(Integer projectId) {
		list2 = list1 = deliveryRequestDetailService.teamInventory(projectId);
	}

	public void flushDeliveryRequestDetail() {
		deliveryRequestDetailService.flush();
	}

	public void refreshDeliveryRequestDetail() {
		deliveryRequestDetail = deliveryRequestDetailService.findOne(deliveryRequestDetail.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewDeliveryRequestDetail())
			cacheView.accessDenied();
	}

	public Boolean canViewDeliveryRequestDetail() {
		return true;
	}

	// SAVE DELIVERYREQUESTDETAIL
	public Boolean canSaveDeliveryRequestDetail() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveDeliveryRequestDetail() {
		if (!canSaveDeliveryRequestDetail())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateDeliveryRequestDetail())
			return null;
		deliveryRequestDetail = deliveryRequestDetailService.save(deliveryRequestDetail);

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequestDetail.getId());
	}

	public Boolean validateDeliveryRequestDetail() {
		return true;
	}

	// DELETE DELIVERYREQUESTDETAIL
	public Boolean canDeleteDeliveryRequestDetail() {
		return true;
	}

	public String deleteDeliveryRequestDetail() {
		if (canDeleteDeliveryRequestDetail())
			try {
				deliveryRequestDetailService.delete(deliveryRequestDetail);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return addParameters(listPage, "faces-redirect=true");
	}

	// GENERIC
	public void refreshCostHistory(Integer partNumberId) {
		if ("/partNumberReporting.xhtml".equals(currentPath))
			list2 = list1 = deliveryRequestDetailService.findByPartNumberAndDeliveryRequestTypeAndCompany(partNumberId, DeliveryRequestType.INBOUND, companyId, Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED));
	}

	public Double getMaxCost() {
		try {
			return list1.stream().filter(d -> d.getUnitCost() != null).mapToDouble(d -> d.getUnitCost()).max().getAsDouble();
		} catch (Exception e) {
			return null;
		}
	}

	public Double getMinCost() {
		try {
			return list1.stream().filter(d -> d.getUnitCost() != null).mapToDouble(d -> d.getUnitCost()).min().getAsDouble();
		} catch (Exception e) {
			return null;
		}
	}

	public Double getAverageCost() {
		try {
			return list1.stream().filter(d -> d.getUnitCost() != null).mapToDouble(d -> d.getUnitCost()).average().getAsDouble();
		} catch (Exception e) {
			return null;
		}
	}

	// GETTERS & SETTERS
	public DeliveryRequestDetail getDeliveryRequestDetail() {
		return deliveryRequestDetail;
	}

	public void setDeliveryRequestDetail(DeliveryRequestDetail deliveryRequestDetail) {
		this.deliveryRequestDetail = deliveryRequestDetail;
	}

	public Integer getCompanyId() {
		return companyId;
	}

}
