package ma.azdad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Customer;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Po;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.Site;
import ma.azdad.model.StockRow;
import ma.azdad.model.User;
import ma.azdad.repos.CustomerRepos;
import ma.azdad.repos.PoRepos;
import ma.azdad.repos.ProjectRepos;
import ma.azdad.repos.SiteRepos;
import ma.azdad.repos.StockRowRepos;
import ma.azdad.repos.UserRepos;
import ma.azdad.utils.ChartContainer;
import ma.azdad.utils.Series;

@Component
@Transactional
public class StockRowService extends GenericServiceOld<StockRow> {

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	ProjectRepos projectRepos;

	@Autowired
	CustomerRepos customerRepos;

	@Autowired
	HighchartsService highchartsService;

	@Autowired
	SiteRepos siteRepos;

	@Autowired
	DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	UserRepos userRepos;

	@Autowired
	PoRepos poRepos;

	@Override
	public StockRow findOne(Integer id) {
		StockRow stockRow = super.findOne(id);
		Hibernate.initialize(stockRow.getPacking());
		stockRow.getPacking().getDetailList().forEach(i -> Hibernate.initialize(i));
		return stockRow;
	}

	public Set<Integer> findInStockByCustomerOwner(Integer customerId) {
		return stockRowRepos.findInStockByCustomerOwner(customerId);
	}

	public Boolean isSotckEmpty(Integer customerId) {
		return findInStockByCustomerOwner(customerId).size() == 0;
	}

	public Set<Integer> findInStockByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return stockRowRepos.findInStockByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public Boolean isSotckEmpty(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return findInStockByCustomerOwner(username, warehouseList, assignedProjectList, customerId).size() == 0;
	}

	public List<StockRow> generateStockRowFromOutboundDeliveryRequest(DeliveryRequest deliveryRequest) {
		List<StockRow> result = new ArrayList<>();
		Date currentDate = new Date();
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			List<StockRow> stockRowList = findRemainingToPrepare(detail);
			Double quantity = detail.getQuantity();

			for (StockRow stockRow : stockRowList) {
				Double newStockRowQuantity = UtilsFunctions.compareDoubles(quantity, stockRow.getQuantity(), 4) >= 0 ? stockRow.getQuantity() : quantity;
				quantity -= newStockRowQuantity;
				result.add(new StockRow(-newStockRowQuantity, deliveryRequest, stockRow.getStatus(), stockRow.getOriginNumber(), stockRow.getPartNumber(), stockRow.getInboundDeliveryRequest(), stockRow.getUnitCost(), detail.getUnitPrice(), stockRow.getLocation(), currentDate, stockRow.getPacking()));
				if (UtilsFunctions.compareDoubles(quantity, 0.0, 4) == 0)
					break;
			}
		}
		return result;
	}

	// must be sorted by quantity !!!!!
	public List<StockRow> findRemainingToPrepare(DeliveryRequestDetail deliveryRequestDetail) {
		return stockRowRepos.findRemainingToPrepare(deliveryRequestDetail.getDeliveryRequest().getProject().getId(), deliveryRequestDetail.getInboundDeliveryRequest().getWarehouse().getId(), deliveryRequestDetail.getPartNumber().getId(), deliveryRequestDetail.getStatus(), deliveryRequestDetail.getOriginNumber(), deliveryRequestDetail.getInboundDeliveryRequest().getId());
	}

	public List<StockRow> getStockSituationByResource(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return stockRowRepos.getStockSituationByResource(username, warehouseList, assignedProjectList);
	}

	public List<StockRow> findByInboundDeliveryRequest(Integer deliveryRequestId) {
		return stockRowRepos.findByInboundDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> getStockSituationByInboundDeliveryRequest(Integer deliveryRequestId) {
		return stockRowRepos.getStockSituationByInboundDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> findAttachedOutboundDeliveryRequestList(Integer deliveryRequestId) {
		return stockRowRepos.findAttachedOutboundDeliveryRequestList(deliveryRequestId);
	}

	public List<StockRow> findByDeliveryRequest(Integer deliveryRequestId) {
		return stockRowRepos.findByDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> findByResource(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return stockRowRepos.findByResource(username, warehouseList, assignedProjectList);
	}

	public void updateLocation(StockRow groupStockRow) {
		System.out.println("groupStockRow.getNewLocation() " + groupStockRow.getNewLocation());
		System.out.println("getInboundDeliveryRequest" + groupStockRow.getInboundDeliveryRequest().getId());
		System.out.println("groupStockRow.getPartNumber().getId() " + groupStockRow.getPartNumber().getId());
		System.out.println("groupStockRow.getLocation().getId() " + groupStockRow.getLocation().getId());
		System.out.println("groupStockRow.getStatus() " + groupStockRow.getStatus());
		if (groupStockRow.getNewLocation() != null)
			stockRowRepos.updateLocation(groupStockRow.getNewLocation(), groupStockRow.getInboundDeliveryRequest().getId(), groupStockRow.getPartNumber().getId(), groupStockRow.getLocation().getId(), groupStockRow.getStatus());
	}

	// REPORTING
	public List<Integer> findCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		return stockRowRepos.findCompanyOwnerList(username, warehouseList, assignedProjectList);
	}

	public List<Integer> findCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		return stockRowRepos.findCustomerOwnerList(username, warehouseList, assignedProjectList);
	}

	public List<StockRow> findByCompanyOwnerAndGroupByPartNumber(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = stockRowRepos.findByCompanyOwnerAndGroupByPartNumber(username, warehouseList, assignedProjectList, companyId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCompanyOwnerAnPartNumber(username, warehouseList, assignedProjectList, companyId, stockRow.getPartNumber().getId()));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndGroupByPartNumber(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = stockRowRepos.findByCustomerOwnerAndGroupByPartNumber(username, warehouseList, assignedProjectList, customerId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCustomerOwnerAnPartNumber(username, warehouseList, assignedProjectList, customerId, stockRow.getPartNumber().getId()));
		return result;
	}

	public List<StockRow> findByCompanyOwnerAndProjectAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = stockRowRepos.findByCompanyOwnerAndProjectAndGroupByPartNumber(username, warehouseList, assignedProjectList, companyId, projectId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCompanyOwnerAnPartNumberAndProject(username, warehouseList, assignedProjectList, companyId, stockRow.getPartNumber().getId(), projectId));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndProjectAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = stockRowRepos.findByCustomerOwnerAndProjectAndGroupByPartNumber(username, warehouseList, assignedProjectList, customerId, projectId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCustomerOwnerAnPartNumberAndProject(username, warehouseList, assignedProjectList, customerId, stockRow.getPartNumber().getId(), projectId));
		return result;
	}

	public List<StockRow> findCurrentStockByPartNumberAndCompanyOwner(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return stockRowRepos.findCurrentStockByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId);
	}

	public List<StockRow> findCurrentStockByPartNumberAndCustomerOwner(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return stockRowRepos.findCurrentStockByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCompanyOwner(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return stockRowRepos.findStockHistoryByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return stockRowRepos.findStockHistoryByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId);
	}

	public List<StockRow> findStockHistoryByProjectAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return stockRowRepos.findStockHistoryByProjectAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<StockRow> findStockHistoryByProjectAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		return stockRowRepos.findStockHistoryByProjectAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	public List<StockRow> findStockHistoryByDestinationAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer destinationId, Integer projectId) {
		return stockRowRepos.findStockHistoryByDestinationAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, destinationId, projectId);
	}

	public List<StockRow> findStockHistoryByExternalRequesterAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedExternalRequesterList, Integer companyId, Integer externalRequesterId, Integer projectId) {
		return stockRowRepos.findStockHistoryByExternalRequesterAndCompanyOwner(username, warehouseList, delegatedExternalRequesterList, companyId, externalRequesterId, projectId);
	}

	public List<StockRow> findStockHistoryByPoAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedPoList, Integer companyId, Integer poId, Integer projectId) {
		return stockRowRepos.findStockHistoryByPoAndCompanyOwner(username, warehouseList, delegatedPoList, companyId, poId, projectId);
	}

	public List<StockRow> findStockHistoryByDestinationAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer destinationId, Integer projectId) {
		return stockRowRepos.findStockHistoryByDestinationAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, destinationId, projectId);
	}

	public List<StockRow> findStockHistoryByExternalRequesterAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedExternalRequesterList, Integer customerId, Integer externalRequesterId, Integer projectId) {
		return stockRowRepos.findStockHistoryByExternalRequesterAndCustomerOwner(username, warehouseList, delegatedExternalRequesterList, customerId, externalRequesterId, projectId);
	}

	public List<StockRow> findStockHistoryByPoAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedPoList, Integer customerId, Integer poId, Integer projectId) {
		return stockRowRepos.findStockHistoryByPoAndCustomerOwner(username, warehouseList, delegatedPoList, customerId, poId, projectId);
	}

	public List<StockRow> findStockHistoryByDestinationProjectAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationProjectList, Integer companyId, Integer destinationProjectId, Integer projectId) {
		return stockRowRepos.findStockHistoryByDestinationProjectAndCompanyOwner(username, warehouseList, delegatedDestinationProjectList, companyId, destinationProjectId, projectId);
	}

	public List<StockRow> findStockHistoryByDestinationProjectAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationProjectList, Integer customerId, Integer destinationProjectId, Integer projectId) {
		return stockRowRepos.findStockHistoryByDestinationProjectAndCustomerOwner(username, warehouseList, delegatedDestinationProjectList, customerId, destinationProjectId, projectId);
	}

	public List<StockRow> findStockHistoryByYearAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearList, Integer companyId, Integer year, Integer projectId) {
		return stockRowRepos.findStockHistoryByYearAndCompanyOwner(username, warehouseList, delegatedYearList, companyId, year, projectId);
	}

	public List<StockRow> findStockHistoryByYearAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearList, Integer customerId, Integer year, Integer projectId) {
		return stockRowRepos.findStockHistoryByYearAndCustomerOwner(username, warehouseList, delegatedYearList, customerId, year, projectId);
	}

	public List<StockRow> findStockHistoryByYearAndMonthAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearAndMonthList, Integer companyId, String yearAndMonth, Integer projectId) {
		return stockRowRepos.findStockHistoryByYearAndMonthAndCompanyOwner(username, warehouseList, delegatedYearAndMonthList, companyId, yearAndMonth, projectId);
	}

	public List<StockRow> findStockHistoryByYearAndMonthAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearAndMonthList, Integer customerId, String yearAndMonth, Integer projectId) {
		return stockRowRepos.findStockHistoryByYearAndMonthAndCustomerOwner(username, warehouseList, delegatedYearAndMonthList, customerId, yearAndMonth, projectId);
	}

	public List<StockRow> findStockHistoryByDestinationCustomerAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer customerId) {
		return stockRowRepos.findStockHistoryByDestinationCustomerAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, ProjectTypes.STOCK.getValue(), customerId);
	}

	public List<Project> findLightProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<Integer> idList = stockRowRepos.findProjectIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Project> findLightProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, String projectType) {
		List<Integer> idList = stockRowRepos.findProjectIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectType);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Project> findLightProjectCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		List<Integer> idList = stockRowRepos.findProjectIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Site> findLightDestinationCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findDestinationIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return siteRepos.findLight(idList);
	}

	public List<Site> findLightDestinationCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findDestinationIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return siteRepos.findLight(idList);
	}

	public List<User> findLightExternalRequesterCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findExternalRequesterIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return userRepos.findLight(idList);
	}

	public List<User> findLightExternalRequesterCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findExternalRequesterIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return userRepos.findLight(idList);
	}

	public List<Po> findLightPoCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findPoIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return poRepos.findLight(idList);
	}

	public List<Po> findLightPoCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findPoIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return poRepos.findLight(idList);
	}

	public List<Project> findLightDestinationProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findDestinationProjectIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Project> findLightDestinationProjectCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = stockRowRepos.findDestinationProjectIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Integer> findLightYearCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return stockRowRepos.findYearListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<Integer> findLightYearCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		return stockRowRepos.findYearListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	public List<String> findLightYearAndMonthCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return stockRowRepos.findYearAndMonthListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<String> findLightYearAndMonthCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		return stockRowRepos.findYearAndMonthListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	////////////////////////////////////////////////////////////////////////////////// TMPPPPPPPPPPP
	public List<Project> findProjectListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return stockRowRepos.findProjectListByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<String> findDeliverToOtherNameListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<String> deliverToCompanyNameList = stockRowRepos.findDeliverToCompanyNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		List<String> deliverToCustomerNameList = stockRowRepos.findDeliverToCustomerNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, CompanyType.CUSTOMER, projectId);
		List<String> deliverToSupplierNameList = stockRowRepos.findDeliverToSupplierNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, CompanyType.SUPPLIER, projectId);
//		List<String> deliverToOtherOtherNameList = stockRowRepos.findDeliverToOtherOtherNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, CompanyType.OTHER, projectId);
		List<String> result = new ArrayList<>();
		result.addAll(deliverToCompanyNameList);
		result.addAll(deliverToCustomerNameList);
		result.addAll(deliverToSupplierNameList);
//		result.addAll(deliverToOtherOtherNameList);
		if (result.isEmpty() || result == null)
			return null;
		return result;
	}

	public List<StockRow> findStockHistoryByDeliverToEntityAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyOwnerId, String deliverToName, Integer projectId) {
		return stockRowRepos.findStockHistoryByDeliverToEntityAndCompanyOwner(username, warehouseList, assignedProjectList, companyOwnerId, deliverToName, projectId, ProjectTypes.STOCK.getValue());
	}

	public List<StockRow> findStockHistoryByOutboundDeliveryRequestReturn(List<Integer> outboundSrouceList) {
		return stockRowRepos.findStockHistoryByOutboundDeliveryRequestReturn(outboundSrouceList);
	}

	public List<String> findDeliverToOtherNameListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<String> deliverToCompanyNameList = stockRowRepos.findDeliverToCompanyNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		List<String> deliverToCustomerNameList = stockRowRepos.findDeliverToCustomerNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, CompanyType.CUSTOMER, projectId);
		List<String> deliverToSupplierNameList = stockRowRepos.findDeliverToSupplierNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, CompanyType.SUPPLIER, projectId);
//		List<String> deliverToOtherOtherNameList = stockRowRepos.findDeliverToOtherOtherNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, CompanyType.OTHER, projectId);
		List<String> result = new ArrayList<>();
		result.addAll(deliverToCompanyNameList);
		result.addAll(deliverToCustomerNameList);
		result.addAll(deliverToSupplierNameList);
//		result.addAll(deliverToOtherOtherNameList);
		if (result.isEmpty() || result == null)
			return null;
		return result;
	}

	public List<StockRow> findStockHistoryByDeliverToOtherAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerOwnerId, String deliverToName, Integer projectId) {
		return stockRowRepos.findStockHistoryByDeliverToEntityAndCustomerOwner(username, warehouseList, assignedProjectList, customerOwnerId, deliverToName, projectId, ProjectTypes.STOCK.getValue());
	}

	////////////////////////////////////////////////////////////////////////////////
	public List<Project> findProjectListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return stockRowRepos.findProjectListByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<Customer> findLightDestinationCustomerCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationCustomerList, Integer companyId) {
		List<Integer> idList = stockRowRepos.findDestinationCustomerIdListByCompanyOwner(username, warehouseList, delegatedDestinationCustomerList, companyId, ProjectTypes.STOCK.getValue());
		if (idList.isEmpty() || idList == null)
			return null;
		return customerRepos.findLight(idList);
	}

	public List<StockRow> findOverdueByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return stockRowRepos.findOverdueByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findOverdueByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return stockRowRepos.findOverdueByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<StockRow> findMaxStockThreshold(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return stockRowRepos.findMaxStockThreshold(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findMinStockThreshold(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return stockRowRepos.findMinStockThreshold(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> getCostCenterFinancialSituation(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return stockRowRepos.getCostCenterFinancialSituation(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	// generate char
	public List<ChartContainer> generateTotalCostChart(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<ChartContainer> result = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		Integer currentMonth = c.get(Calendar.MONTH);
		for (int i = 0; i < 5; i++) {
			c.set(Calendar.MONTH, 0);
			Double[] data = new Double[12];
			for (int j = 0; j < (i != 0 ? 12 : currentMonth + 1); j++) {
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
				data[j] = stockRowRepos.getTotalCostBeforeDate(username, warehouseList, assignedProjectList, companyId, projectId, c.getTime());
				if (j != 11)
					c.add(Calendar.MONTH, 1);
			}
			Series[] series = { new Series("Cost Trend", "blue", data) };
			result.add(new ChartContainer("" + c.get(Calendar.YEAR), "container_" + c.get(Calendar.YEAR), highchartsService.generateLineBasic("container_" + c.get(Calendar.YEAR), "Sotck Cost Center Trend", "" + c.get(Calendar.YEAR), "MAD", series)));
			c.add(Calendar.YEAR, -1);
		}
		return result;
	}

	// fast moving items
	public List<StockRow> getFastMovingItems(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<StockRow> list = stockRowRepos.findByCompanyOwnerGroupbyPartNumberAndDeliveryRequest(username, warehouseList, assignedProjectList, companyId);
		Map<String, StockRow> map = new HashMap<>();
		Map<String, Set<Integer>> mapOutbounds = new HashMap<>();
		Map<String, Set<Integer>> mapCustomers = new HashMap<>();
		for (StockRow stockRow : list) {
			String key = stockRow.getPartNumber().getId() + ";" + stockRow.getDeliveryRequest().getProject().getId();
			map.putIfAbsent(key, new StockRow(stockRow.getPartNumber(), stockRow.getDeliveryRequest().getProject().getName(), 0.0, 0.0));
			mapOutbounds.putIfAbsent(key, new HashSet<Integer>());
			mapCustomers.putIfAbsent(key, new HashSet<Integer>());
			StockRow current = map.get(key);
			if (stockRow.getDeliveryRequest().getIsOutbound()) {
				mapOutbounds.get(key).add(stockRow.getDeliveryRequest().getId());
				// TODO remove try & catch
				try {
					mapCustomers.get(key).add(stockRow.getDeliveryRequest().getDestinationProject().getCustomer().getId());
				} catch (Exception e) {
				}
				current.setOutboundQuantity(current.getOutboundQuantity() + stockRow.getQuantity());
			} else if (stockRow.getDeliveryRequest().getIsInbound())
				current.setInboundQuantity(current.getInboundQuantity() + stockRow.getQuantity());

			current.setTmpNumberOfOutbounds(mapOutbounds.get(key).size());
			current.setTmpNumberOfCustomers(mapCustomers.get(key).size());
		}

		List<StockRow> result = new ArrayList<>(map.values());

		Collections.sort(result, new Comparator<StockRow>() {
			@Override
			public int compare(StockRow o1, StockRow o2) {
				return o2.getTmpNumberOfOutbounds().compareTo(o1.getTmpNumberOfOutbounds());
			}
		});

		return result;
	}

	public List<StockRow> findReturnedStockRowList(Integer outboundDeliveryRequestId) {
		List<StockRow> result = stockRowRepos.findReturnedStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
		result.forEach(i -> {
			Hibernate.initialize(i.getDeliveryRequest());
		});

		return result;
	}

	public Long countReturnedStockRowList(Integer outboundDeliveryRequestId) {
		return stockRowRepos.countReturnedStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public List<StockRow> findTransferredStockRowList(Integer outboundDeliveryRequestId) {
		List<StockRow> result = stockRowRepos.findTransferredStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
		result.forEach(i -> {
			Hibernate.initialize(i.getDeliveryRequest());
		});

		return result;
	}

	public Long countTransferredStockRowList(Integer outboundDeliveryRequestId) {
		return stockRowRepos.countTransferredStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

}
