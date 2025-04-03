package ma.azdad.view;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.repos.DeliveryRequestDetailRepos;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class DeliveryRequestDetailView extends GenericView<Integer, DeliveryRequestDetail, DeliveryRequestDetailRepos, DeliveryRequestDetailService> {

	@Autowired
	private DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private SessionView sessionView;

	private DeliveryRequestDetail deliveryRequestDetail = new DeliveryRequestDetail();

	private Integer companyId;
	private Integer customerId;

	private DatatableList<DeliveryRequestDetail> datatable1;

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
		else if (isPage("viewDeliveryRequest"))
			initMappingSummary();

	}

	public void initMappingSummary() {
		System.out.println("initMappingSummary");
		datatable1 = new DatatableList<DeliveryRequestDetail>(service.findMappingSummary(id));
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		companyId = UtilsFunctions.getIntegerParameter("companyId");
		customerId = UtilsFunctions.getIntegerParameter("customerId");
		id = UtilsFunctions.getIntegerParameter("id");
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

	// pn quantities
	public Double getPendingOutbound() {
		if (companyId != null)
			return service.findPendingByCompanyOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, id,
					DeliveryRequestType.OUTBOUND);
		else if (customerId != null)
			return service.findPendingByCustomerOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, id,
					DeliveryRequestType.OUTBOUND);
		return null;
	}

	public Double getPendingStockOutbound() {
		if (companyId != null)
			return service.findPendingStockByCompanyOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, id,
					DeliveryRequestType.OUTBOUND);
		else if (customerId != null)
			return service.findPendingStockByCustomerOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, id,
					DeliveryRequestType.OUTBOUND);
		return null;
	}

	public Double getPendingInbound() {
		if (companyId != null)
			return service.findPendingByCompanyOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, id,
					DeliveryRequestType.INBOUND);
		else if (customerId != null)
			return service.findPendingByCustomerOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, id,
					DeliveryRequestType.INBOUND);
		return null;
	}

	public Double getPendingStockInbound() {
		if (companyId != null)
			return service.findPendingStockByCompanyOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, id,
					DeliveryRequestType.INBOUND);
		else if (customerId != null)
			return service.findPendingStockByCustomerOwnerAndPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, id,
					DeliveryRequestType.INBOUND);
		return null;
	}

	// generic
	public void refreshCostHistory(Integer partNumberId) {
		refreshCostHistory(partNumberId, companyId);
	}

	public void refreshCostHistory(Integer partNumberId, Integer companyId) {
		if ("/partNumberReporting.xhtml".equals(currentPath))
			list2 = list1 = deliveryRequestDetailService.findByPartNumberAndDeliveryRequestTypeAndCompany(partNumberId, DeliveryRequestType.INBOUND, InboundType.NEW, companyId,
					Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED));
		else if ("/viewPartNumberPricing.xhtml".equals(currentPath))
			initLists(deliveryRequestDetailService.findByPartNumberAndTypeAndProjectTypeStockAndProjectCompanyAndDeliveryRequestStatus(partNumberId, DeliveryRequestType.INBOUND, companyId,
					Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED)));
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

	@Cacheable("deliveryRequestDetailView.findTransferredAndPendingDetailList")
	public List<DeliveryRequestDetail> findTransferredAndPendingDetailList(Integer outboundDeliveryRequestId) {
		return service.findTransferredAndPendingDetailList(outboundDeliveryRequestId);
	}

	@Cacheable("deliveryRequestDetailView.findReturnedAndPendingDetailList")
	public List<DeliveryRequestDetail> findReturnedAndPendingDetailList(Integer outboundDeliveryRequestId) {
		return service.findReturnedAndPendingDetailList(outboundDeliveryRequestId);
	}

	@Cacheable("deliveryRequestDetailView.findInboundByCompanyOwnerAndPartNumberAndNotDelivered")
	public List<DeliveryRequestDetail> findInboundByCompanyOwnerAndPartNumberAndNotDelivered(Integer partNumberId) {
		return service.findByCompanyOwnerAndPartNumberAndNotDelivered(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId,
				DeliveryRequestType.INBOUND, partNumberId);
	}

	@Cacheable("deliveryRequestDetailView.findOutboundByCompanyOwnerAndPartNumberAndNotDelivered")
	public List<DeliveryRequestDetail> findOutboundByCompanyOwnerAndPartNumberAndNotDelivered(Integer partNumberId) {
		return service.findByCompanyOwnerAndPartNumberAndNotDelivered(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId,
				DeliveryRequestType.OUTBOUND, partNumberId);
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

	public DatatableList<DeliveryRequestDetail> getDatatable1() {
		return datatable1;
	}

	public void setDatatable1(DatatableList<DeliveryRequestDetail> datatable1) {
		this.datatable1 = datatable1;
	}

}
