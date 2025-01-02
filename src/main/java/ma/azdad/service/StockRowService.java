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
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Customer;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.PartNumber;
import ma.azdad.model.Po;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.Site;
import ma.azdad.model.StockRow;
import ma.azdad.model.User;
import ma.azdad.model.Warehouse;
import ma.azdad.repos.CustomerRepos;
import ma.azdad.repos.PartNumberRepos;
import ma.azdad.repos.PoRepos;
import ma.azdad.repos.ProjectRepos;
import ma.azdad.repos.SiteRepos;
import ma.azdad.repos.StockRowRepos;
import ma.azdad.repos.UserRepos;
import ma.azdad.utils.ChartContainer;
import ma.azdad.utils.ChartData;
import ma.azdad.utils.Color;
import ma.azdad.utils.Serie;

@Component
public class StockRowService extends GenericService<Integer, StockRow, StockRowRepos> {

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

	@Autowired
	PartNumberRepos partNumberRepos;

	@Override
	public StockRow findOne(Integer id) {
		StockRow stockRow = super.findOne(id);
		Hibernate.initialize(stockRow.getPacking());
		stockRow.getPacking().getDetailList().forEach(i -> Hibernate.initialize(i));
		return stockRow;
	}

	public Set<Integer> findCustomerIdList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		Set<Integer> result = new HashSet<Integer>();
		List<Object[]> data = repos.findCustomerIdList(username, warehouseList, assignedProjectList);
		data.forEach(tab -> {
			if (tab[0] != null)
				result.add((Integer) tab[0]);
			if (tab[1] != null)
				result.add((Integer) tab[1]);
		});
		return result;
	}

	public Set<Integer> findCustomerIdListWithStock(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		Set<Integer> result = new HashSet<Integer>();
		List<Object[]> data = repos.findCustomerIdListWithStock(username, warehouseList, assignedProjectList);
		data.forEach(tab -> {
			if (tab[0] != null)
				result.add((Integer) tab[0]);
			if (tab[1] != null)
				result.add((Integer) tab[1]);
		});
		return result;
	}

	public Set<Integer> findInStockByCustomerOwner(Integer customerId) {
		return repos.findInStockByCustomerOwner(customerId);
	}

	public Boolean isSotckEmpty(Integer customerId) {
		return findInStockByCustomerOwner(customerId).size() == 0;
	}

	public Set<Integer> findInStockByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return repos.findInStockByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
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
				DeliveryRequestDetail inboundDeliveryRequestDetail = deliveryRequestDetailService
						.findByDeliveryRequestAndPartNumber(stockRow.getInboundDeliveryRequest().getId(), stockRow.getPartNumber().getId()).get(0);
				result.add(new StockRow(detail, -newStockRowQuantity, deliveryRequest, stockRow.getStatus(), stockRow.getOriginNumber(), stockRow.getPartNumber(), stockRow.getInboundDeliveryRequest(),
						inboundDeliveryRequestDetail, stockRow.getLocation(), currentDate, stockRow.getPacking()));
				if (UtilsFunctions.compareDoubles(quantity, 0.0, 4) == 0)
					break;
			}
		}
		return result;
	}

	// must be sorted by quantity !!!!!
	public List<StockRow> findRemainingToPrepare(DeliveryRequestDetail deliveryRequestDetail) {
		return repos.findRemainingToPrepare(deliveryRequestDetail.getDeliveryRequest().getProject().getId(), deliveryRequestDetail.getInboundDeliveryRequest().getWarehouse().getId(),
				deliveryRequestDetail.getPartNumber().getId(), deliveryRequestDetail.getStatus(), deliveryRequestDetail.getOriginNumber(), deliveryRequestDetail.getInboundDeliveryRequest().getId());
	}

	public List<StockRow> getStockSituationByResource(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return repos.getStockSituationByResource(username, warehouseList, assignedProjectList);
	}

	public List<StockRow> findByInboundDeliveryRequest(Integer deliveryRequestId) {
		return repos.findByInboundDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> getStockSituationByInboundDeliveryRequest(Integer deliveryRequestId) {
		return repos.getStockSituationByInboundDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> findAttachedOutboundDeliveryRequestList(Integer deliveryRequestId) {
		return repos.findAttachedOutboundDeliveryRequestList(deliveryRequestId);
	}

	public List<StockRow> findByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findByDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> findByResource(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return repos.findByResource(username, warehouseList, assignedProjectList);
	}

	public void updateLocation(StockRow groupStockRow) {
		System.out.println("groupStockRow.getNewLocation() " + groupStockRow.getNewLocation());
		System.out.println("getInboundDeliveryRequest" + groupStockRow.getInboundDeliveryRequest().getId());
		System.out.println("groupStockRow.getPartNumber().getId() " + groupStockRow.getPartNumber().getId());
		System.out.println("groupStockRow.getLocation().getId() " + groupStockRow.getLocation().getId());
		System.out.println("groupStockRow.getStatus() " + groupStockRow.getStatus());
		if (groupStockRow.getNewLocation() != null)
			repos.updateLocation(groupStockRow.getNewLocation(), groupStockRow.getInboundDeliveryRequest().getId(), groupStockRow.getPartNumber().getId(), groupStockRow.getLocation().getId(),
					groupStockRow.getStatus());
	}

	// REPORTING
	public List<Integer> findCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		return repos.findCompanyOwnerList(username, warehouseList, assignedProjectList);
	}

//	public List<Integer> findCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
//		if (assignedProjectList == null || assignedProjectList.isEmpty())
//			assignedProjectList = Arrays.asList(-1);
//		return repos.findCustomerOwnerList(username, warehouseList, assignedProjectList);
//	}

	private Map<Integer, Double> findProjectStockGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<Object[]> data = repos.findProjectStockGroupByPartNumber(username, warehouseList, assignedProjectList, companyId);
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		data.forEach(i -> result.put((Integer) i[0], (Double) i[1]));
		return result;
	}

	public List<StockRow> findByCompanyOwnerAndGroupByPartNumber(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCompanyOwnerAndGroupByPartNumber2(username, warehouseList, assignedProjectList, companyId);

		// set project stock
//		Map<Integer, Double> map1 = findProjectStockGroupByPartNumber(username, warehouseList, assignedProjectList, companyId);
//		for (StockRow stockRow : result)
//			stockRow.setProjectSubTypeStockQuantity(map1.getOrDefault(stockRow.getPartNumberId(), 0.0));

		// set pending quantity = pending outbounds qty
		Map<Integer, Double> map2 = deliveryRequestDetailService.findPendingQuantityByCompanyOwnerAndProjectSubTypeStockGroupByPartNumber(username, warehouseList, assignedProjectList, companyId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(map2.getOrDefault(stockRow.getPartNumberId(), 0.0));

		// set forecast quantity
//		Map<Integer, Double> map3 = deliveryRequestDetailService.findForecastQuantityGroupByPartNumber(username, warehouseList, assignedProjectList, companyId);
//		for (StockRow stockRow : result)
//			stockRow.setForecastQuantity(map3.getOrDefault(stockRow.getPartNumberId(), 0.0));

		return result;
	}

	public List<StockRow> findByCustomerOwnerAndGroupByPartNumber(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCustomerOwnerAndGroupByPartNumber(username, warehouseList, assignedProjectList, customerId);
		// set pending quantity = pending outbounds qty
		Map<Integer, Double> map = deliveryRequestDetailService.findPendingQuantityByCustomerOwnerGroupByPartNumber(username, warehouseList, assignedProjectList, customerId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(map.getOrDefault(stockRow.getPartNumberId(), 0.0));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndGroupByPartNumber(Integer customerId, List<Integer> projectIdList) {
		if (projectIdList == null || projectIdList.isEmpty())
			projectIdList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCustomerOwnerAndGroupByPartNumber(customerId, projectIdList);
		// set pending quantity = pending outbounds qty
		Map<Integer, Double> map = deliveryRequestDetailService.findPendingQuantityByCustomerOwnerGroupByPartNumber(customerId, projectIdList);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(map.getOrDefault(stockRow.getPartNumberId(), 0.0));
		return result;
	}

	public List<StockRow> findBySupplierOwnerAndGroupByPartNumber(Integer supplierId, List<Integer> projectIdList) {
		if (projectIdList == null || projectIdList.isEmpty())
			projectIdList = Arrays.asList(-1);
		List<StockRow> result = repos.findBySupplierOwnerAndGroupByPartNumber(supplierId, projectIdList);
		// set pending quantity = pending outbounds qty
		Map<Integer, Double> map = deliveryRequestDetailService.findPendingQuantityBySupplierOwnerGroupByPartNumber(supplierId, projectIdList);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(map.getOrDefault(stockRow.getPartNumberId(), 0.0));
		return result;
	}

	public List<StockRow> findByCompanyOwnerAndProjectAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCompanyOwnerAndProjectAndGroupByPartNumber(username, warehouseList, assignedProjectList, companyId, projectId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCompanyOwnerAnPartNumberAndProject(username, warehouseList, assignedProjectList, companyId,
					stockRow.getPartNumber().getId(), projectId));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndProjectAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCustomerOwnerAndProjectAndGroupByPartNumber(username, warehouseList, assignedProjectList, customerId, projectId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCustomerOwnerAnPartNumberAndProject(username, warehouseList, assignedProjectList, customerId,
					stockRow.getPartNumber().getId(), projectId));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndProjectAndGroupByPartNumber(Integer customerId, Integer projectId) {
		List<StockRow> result = repos.findByCustomerOwnerAndProjectAndGroupByPartNumber(customerId, projectId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCustomerOwnerAnPartNumberAndProject(customerId, stockRow.getPartNumber().getId(), projectId));
		return result;
	}

	public List<StockRow> findBySupplierOwnerAndProjectAndGroupByPartNumber(Integer supplierId, Integer projectId) {
		List<StockRow> result = repos.findBySupplierOwnerAndProjectAndGroupByPartNumber(supplierId, projectId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityBySupplierOwnerAnPartNumberAndProject(supplierId, stockRow.getPartNumber().getId(), projectId));
		return result;
	}

	public List<StockRow> findByCompanyOwnerAndWarehouseAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer warehouseId) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCompanyOwnerAndWarehouseAndGroupByPartNumber(username, warehouseList, assignedProjectList, companyId, warehouseId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCompanyOwnerAnPartNumberAndWarehouse(username, warehouseList, assignedProjectList, companyId,
					stockRow.getPartNumber().getId(), warehouseId));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndWarehouseAndGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			Integer warehouseId) {
		if (assignedProjectList == null || assignedProjectList.isEmpty())
			assignedProjectList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCustomerOwnerAndWarehouseAndGroupByPartNumber(username, warehouseList, assignedProjectList, customerId, warehouseId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(deliveryRequestDetailService.findPendingQuantityByCustomerOwnerAnPartNumberAndWarehouse(username, warehouseList, assignedProjectList, customerId,
					stockRow.getPartNumber().getId(), warehouseId));
		return result;
	}

	public List<StockRow> findByCustomerOwnerAndWarehouseAndGroupByPartNumber(Integer customerId, List<Integer> projectIdList, Integer warehouseId) {
		if (projectIdList == null || projectIdList.isEmpty())
			projectIdList = Arrays.asList(-1);
		List<StockRow> result = repos.findByCustomerOwnerAndWarehouseAndGroupByPartNumber(customerId, projectIdList, warehouseId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(
					deliveryRequestDetailService.findPendingQuantityByCustomerOwnerAnPartNumberAndWarehouse(customerId, projectIdList, stockRow.getPartNumber().getId(), warehouseId));
		return result;
	}

	public List<StockRow> findBySupplierOwnerAndWarehouseAndGroupByPartNumber(Integer supplierId, List<Integer> projectIdList, Integer warehouseId) {
		if (projectIdList == null || projectIdList.isEmpty())
			projectIdList = Arrays.asList(-1);
		List<StockRow> result = repos.findBySupplierOwnerAndWarehouseAndGroupByPartNumber(supplierId, projectIdList, warehouseId);
		for (StockRow stockRow : result)
			stockRow.setPendingQuantity(
					deliveryRequestDetailService.findPendingQuantityBySupplierOwnerAnPartNumberAndWarehouse(supplierId, projectIdList, stockRow.getPartNumber().getId(), warehouseId));
		return result;
	}

	public List<StockRow> findCurrentStockByPartNumberAndCompanyOwner(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return repos.findCurrentStockByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId);
	}

	public List<StockRow> findCurrentStockByPartNumberAndCustomerOwner(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return repos.findCurrentStockByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId);
	}

	public List<StockRow> findCurrentStockByPartNumberAndCustomerOwner(Integer customerId, List<Integer> projectIdList, Integer partNumberId) {
		return repos.findCurrentStockByPartNumberAndCustomerOwner(customerId, projectIdList, partNumberId);
	}

	public List<StockRow> findCurrentStockByPartNumberAndSupplierOwner(Integer supplierId, List<Integer> projectIdList, Integer partNumberId) {
		return repos.findCurrentStockByPartNumberAndSupplierOwner(supplierId, projectIdList, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCompanyOwner(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return repos.findStockHistoryByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId) {
		return repos.findStockHistoryByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(Integer customerId, List<Integer> projectIdList, Integer partNumberId) {
		return repos.findStockHistoryByPartNumberAndCustomerOwner(customerId, projectIdList, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndSupplierOwner(Integer supplierId, List<Integer> projectIdList, Integer partNumberId) {
		return repos.findStockHistoryByPartNumberAndSupplierOwner(supplierId, projectIdList, partNumberId);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCompanyOwner(Integer companyId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId,
			Integer projectId) {
		return repos.findStockHistoryByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId, projectId, DeliveryRequestType.OUTBOUND);
	}

	public List<StockRow> findStockHistoryByPartNumberAndCustomerOwner(Integer customerId, String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer partNumberId,
			Integer projectId) {
		return repos.findStockHistoryByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId, projectId, DeliveryRequestType.OUTBOUND);
	}

	public List<StockRow> findStockHistoryByProjectAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return repos.findStockHistoryByProjectAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<StockRow> findStockHistoryByWarehouseAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer warehouseId) {
		return repos.findStockHistoryByWarehouseAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, warehouseId);
	}

	public List<StockRow> findStockHistoryByCompanyOwnerGroupByPartNumberAndStatus(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			Integer projectId) {
		return repos.findStockHistoryByCompanyOwnerGroupByPartNumberAndStatus(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<StockRow> findStockHistoryByCustomerOwnerGroupByPartNumberAndStatus(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId,
			Integer projectId) {
		return repos.findStockHistoryByCustomerOwnerGroupByPartNumberAndStatus(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	public List<StockRow> findStockHistoryByProjectAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		return repos.findStockHistoryByProjectAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	public List<StockRow> findStockHistoryByProjectAndCustomerOwner(Integer customerId, Integer projectId) {
		return repos.findStockHistoryByProjectAndCustomerOwner(customerId, projectId);
	}

	public List<StockRow> findStockHistoryByProjectAndSupplierOwner(Integer supplierId, Integer projectId) {
		return repos.findStockHistoryByProjectAndSupplierOwner(supplierId, projectId);
	}

	public List<StockRow> findStockHistoryByWarehouseAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> projectIdList, Integer customerId, Integer warehouseId) {
		return repos.findStockHistoryByWarehouseAndCustomerOwner(username, warehouseList, projectIdList, customerId, warehouseId);
	}

	public List<StockRow> findStockHistoryByWarehouseAndCustomerOwner(Integer customerId, List<Integer> projectIdList, Integer warehouseId) {
		return repos.findStockHistoryByWarehouseAndCustomerOwner(customerId, projectIdList, warehouseId);
	}

	public List<StockRow> findStockHistoryByWarehouseAndSupplierOwner(Integer supplierId, List<Integer> projectIdList, Integer warehouseId) {
		return repos.findStockHistoryByWarehouseAndSupplierOwner(supplierId, projectIdList, warehouseId);
	}

//	public List<StockRow> findStockHistoryByDestinationAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer destinationId, Integer projectId) {
//		return repos.findStockHistoryByDestinationAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, destinationId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByExternalRequesterAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedExternalRequesterList, Integer companyId, Integer externalRequesterId, Integer projectId) {
//		return repos.findStockHistoryByExternalRequesterAndCompanyOwner(username, warehouseList, delegatedExternalRequesterList, companyId, externalRequesterId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByPoAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedPoList, Integer companyId, Integer poId, Integer projectId) {
//		return repos.findStockHistoryByPoAndCompanyOwner(username, warehouseList, delegatedPoList, companyId, poId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByDestinationAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer destinationId, Integer projectId) {
//		return repos.findStockHistoryByDestinationAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, destinationId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByExternalRequesterAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedExternalRequesterList, Integer customerId, Integer externalRequesterId, Integer projectId) {
//		return repos.findStockHistoryByExternalRequesterAndCustomerOwner(username, warehouseList, delegatedExternalRequesterList, customerId, externalRequesterId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByPoAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedPoList, Integer customerId, Integer poId, Integer projectId) {
//		return repos.findStockHistoryByPoAndCustomerOwner(username, warehouseList, delegatedPoList, customerId, poId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByDestinationProjectAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationProjectList, Integer companyId, Integer destinationProjectId, Integer projectId) {
//		return repos.findStockHistoryByDestinationProjectAndCompanyOwner(username, warehouseList, delegatedDestinationProjectList, companyId, destinationProjectId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByDestinationProjectAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationProjectList, Integer customerId, Integer destinationProjectId, Integer projectId) {
//		return repos.findStockHistoryByDestinationProjectAndCustomerOwner(username, warehouseList, delegatedDestinationProjectList, customerId, destinationProjectId, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByYearAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearList, Integer companyId, Integer year, Integer projectId) {
//		return repos.findStockHistoryByYearAndCompanyOwner(username, warehouseList, delegatedYearList, companyId, year, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByYearAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearList, Integer customerId, Integer year, Integer projectId) {
//		return repos.findStockHistoryByYearAndCustomerOwner(username, warehouseList, delegatedYearList, customerId, year, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByYearAndMonthAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearAndMonthList, Integer companyId, String yearAndMonth, Integer projectId) {
//		return repos.findStockHistoryByYearAndMonthAndCompanyOwner(username, warehouseList, delegatedYearAndMonthList, companyId, yearAndMonth, projectId);
//	}
//
//	public List<StockRow> findStockHistoryByYearAndMonthAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> delegatedYearAndMonthList, Integer customerId, String yearAndMonth, Integer projectId) {
//		return repos.findStockHistoryByYearAndMonthAndCustomerOwner(username, warehouseList, delegatedYearAndMonthList, customerId, yearAndMonth, projectId);
//	}

	public List<StockRow> findStockHistoryByDestinationCustomerAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer customerId) {
		return repos.findStockHistoryByDestinationCustomerAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, ProjectTypes.STOCK.getValue(), customerId);
	}

//	public List<Project> findLightProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
//		List<Integer> idList = repos.findProjectIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
//		if (idList.isEmpty() || idList == null)
//			return null;
//		return projectRepos.findLight(idList);
//	}

	public List<Project> findLightProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<Project> havingStockList = repos.findProjectListByCompanyOwnerAndHavingStock(username, warehouseList, assignedProjectList, companyId);
		List<Project> notHavingStock = repos.findProjectListByCompanyOwnerAndNotHavingStock(username, warehouseList, assignedProjectList, companyId);
		List<Project> result = new ArrayList<Project>();
		result.addAll(havingStockList);
		result.addAll(notHavingStock);
		return result;
	}

	public List<Project> findLightProjectCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		List<Project> havingStockList = repos.findProjectListByCustomerOwnerAndHavingStock(username, warehouseList, assignedProjectList, customerId);
		List<Project> notHavingStock = repos.findProjectListByCustomerOwnerAndNotHavingStock(username, warehouseList, assignedProjectList, customerId);
		List<Project> result = new ArrayList<Project>();
		result.addAll(havingStockList);
		result.addAll(notHavingStock);
		return result;
	}

	public List<Project> findLightProjectCustomerOwnerList(Integer customerId, List<Integer> projectIdList) {
		List<Project> havingStockList = repos.findProjectListByCustomerOwnerAndHavingStock(customerId, projectIdList);
		List<Project> notHavingStock = repos.findProjectListByCustomerOwnerAndNotHavingStock(customerId, projectIdList);
		List<Project> result = new ArrayList<Project>();
		result.addAll(havingStockList);
		result.addAll(notHavingStock);
		return result;
	}

	public List<Project> findLightProjectSupplierOwnerList(Integer supplierId, List<Integer> projectIdList) {
		List<Project> havingStockList = repos.findProjectListBySupplierOwnerAndHavingStock(supplierId, projectIdList);
		List<Project> notHavingStock = repos.findProjectListBySupplierOwnerAndNotHavingStock(supplierId, projectIdList);
		List<Project> result = new ArrayList<Project>();
		result.addAll(havingStockList);
		result.addAll(notHavingStock);
		return result;
	}

	public List<Warehouse> findLightWarehouseCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<Warehouse> havingStockList = repos.findWarehouseListByCompanyOwnerAndHavingStock(username, warehouseList, assignedProjectList, companyId);
		List<Warehouse> notHavingStock = repos.findWarehouseListByCompanyOwnerAndNotHavingStock(username, warehouseList, assignedProjectList, companyId);
		List<Warehouse> result = new ArrayList<Warehouse>();
		result.addAll(havingStockList);
		result.addAll(notHavingStock);
		return result;
	}

	public List<Warehouse> findLightWarehouseCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		List<Warehouse> havingStockList = repos.findWarehouseListByCustomerOwnerAndHavingStock(username, warehouseList, assignedProjectList, customerId);
		List<Warehouse> notHavingStock = repos.findWarehouseListByCustomerOwnerAndNotHavingStock(username, warehouseList, assignedProjectList, customerId);
		List<Warehouse> result = new ArrayList<Warehouse>();
		result.addAll(havingStockList);
		result.addAll(notHavingStock);
		return result;
	}

//	public List<Project> findLightProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, String projectType) {
//		List<Integer> idList = repos.findProjectIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectType);
//		if (idList.isEmpty() || idList == null)
//			return null;
//		return projectRepos.findLight(idList);
//	}

//	public List<Project> findLightProjectCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
//		List<Integer> idList = repos.findProjectIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
//		if (idList.isEmpty() || idList == null)
//			return null;
//		return projectRepos.findLight(idList);
//	}

	public List<Site> findLightDestinationCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = repos.findDestinationIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return siteRepos.findLight(idList);
	}

	public List<Site> findLightDestinationCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = repos.findDestinationIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return siteRepos.findLight(idList);
	}

	public List<User> findLightExternalRequesterCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = repos.findExternalRequesterIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return userRepos.findLight(idList);
	}

	public List<User> findLightExternalRequesterCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = repos.findExternalRequesterIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return userRepos.findLight(idList);
	}

	public List<Po> findLightPoCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = repos.findPoIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId, DeliveryRequestType.OUTBOUND);
		if (idList.isEmpty() || idList == null)
			return null;
		return poRepos.findLight(idList);
	}

	public List<Po> findLightPoCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = repos.findPoIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId, DeliveryRequestType.OUTBOUND);
		if (idList.isEmpty() || idList == null)
			return null;
		return poRepos.findLight(idList);
	}

	public List<PartNumber> findLightPartNumberCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = repos.findPartNumberIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId, DeliveryRequestType.OUTBOUND);
		if (idList.isEmpty() || idList == null)
			return null;
		return partNumberRepos.findLight(idList);
	}

	public List<PartNumber> findLightPartNumberCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = repos.findPartNumberIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId, DeliveryRequestType.OUTBOUND);
		if (idList.isEmpty() || idList == null)
			return null;
		return partNumberRepos.findLight(idList);
	}

	public List<Project> findLightDestinationProjectCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<Integer> idList = repos.findDestinationProjectIdListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Project> findLightDestinationProjectCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<Integer> idList = repos.findDestinationProjectIdListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		if (idList.isEmpty() || idList == null)
			return null;
		return projectRepos.findLight(idList);
	}

	public List<Integer> findLightYearCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return repos.findYearListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<Integer> findLightYearCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		return repos.findYearListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	public List<String> findLightYearAndMonthCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return repos.findYearAndMonthListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<String> findLightYearAndMonthCustomerOwnerList(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		return repos.findYearAndMonthListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
	}

	////////////////////////////////////////////////////////////////////////////////// TMPPPPPPPPPPP
	public List<Project> findProjectListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findProjectListByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<String> findDeliverToOtherNameListByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<String> deliverToCompanyNameList = repos.findDeliverToCompanyNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, projectId);
		List<String> deliverToCustomerNameList = repos.findDeliverToCustomerNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, CompanyType.CUSTOMER, projectId);
		List<String> deliverToSupplierNameList = repos.findDeliverToSupplierNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, CompanyType.SUPPLIER, projectId);
//		List<String> deliverToOtherOtherNameList = repos.findDeliverToOtherOtherNameListByCompanyOwner(username, warehouseList, assignedProjectList, companyId, CompanyType.OTHER, projectId);
		List<String> result = new ArrayList<>();
		result.addAll(deliverToCompanyNameList);
		result.addAll(deliverToCustomerNameList);
		result.addAll(deliverToSupplierNameList);
//		result.addAll(deliverToOtherOtherNameList);
		if (result.isEmpty() || result == null)
			return null;
		return result;
	}

	public List<StockRow> findStockHistoryByDeliverToEntityAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyOwnerId,
			String deliverToName, Integer projectId) {
		return repos.findStockHistoryByDeliverToEntityAndCompanyOwner(username, warehouseList, assignedProjectList, companyOwnerId, deliverToName, projectId, ProjectTypes.STOCK.getValue());
	}

//	public List<StockRow> findStockHistoryByOutboundDeliveryRequestReturn(List<Integer> outboundSrouceList) {
//		return repos.findStockHistoryByOutboundDeliveryRequestReturn(outboundSrouceList);
//	}

	public List<StockRow> findStockHistoryByCompanyOwnerAndOutboundDeliveryRequestReturn(Integer companyId, List<Integer> outboundSrouceList, List<Integer> partNumberIdList) {
		return repos.findStockHistoryByCompanyOwnerAndOutboundDeliveryRequestReturn(companyId, outboundSrouceList, partNumberIdList);
	}

	public List<StockRow> findStockHistoryByCustomerOwnerAndOutboundDeliveryRequestReturn(Integer customerId, List<Integer> outboundSrouceList, List<Integer> partNumberIdList) {
		return repos.findStockHistoryByCustomerOwnerAndOutboundDeliveryRequestReturn(customerId, outboundSrouceList, partNumberIdList);
	}

	public List<String> findDeliverToOtherNameListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer projectId) {
		List<String> deliverToCompanyNameList = repos.findDeliverToCompanyNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, projectId);
		List<String> deliverToCustomerNameList = repos.findDeliverToCustomerNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, CompanyType.CUSTOMER, projectId);
		List<String> deliverToSupplierNameList = repos.findDeliverToSupplierNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, CompanyType.SUPPLIER, projectId);
//		List<String> deliverToOtherOtherNameList = repos.findDeliverToOtherOtherNameListByCustomerOwner(username, warehouseList, assignedProjectList, customerId, CompanyType.OTHER, projectId);
		List<String> result = new ArrayList<>();
		result.addAll(deliverToCompanyNameList);
		result.addAll(deliverToCustomerNameList);
		result.addAll(deliverToSupplierNameList);
//		result.addAll(deliverToOtherOtherNameList);
		if (result.isEmpty() || result == null)
			return null;
		return result;
	}

	public List<StockRow> findStockHistoryByDeliverToOtherAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerOwnerId,
			String deliverToName, Integer projectId) {
		return repos.findStockHistoryByDeliverToEntityAndCustomerOwner(username, warehouseList, assignedProjectList, customerOwnerId, deliverToName, projectId, ProjectTypes.STOCK.getValue());
	}

	////////////////////////////////////////////////////////////////////////////////
	public List<Project> findProjectListByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return repos.findProjectListByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<Customer> findLightDestinationCustomerCompanyOwnerList(String username, List<Integer> warehouseList, List<Integer> delegatedDestinationCustomerList, Integer companyId) {
		List<Integer> idList = repos.findDestinationCustomerIdListByCompanyOwner(username, warehouseList, delegatedDestinationCustomerList, companyId, ProjectTypes.STOCK.getValue());
		if (idList.isEmpty() || idList == null)
			return null;
		return customerRepos.findLight(idList);
	}

	public List<StockRow> findOverdueByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findOverdueByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findOverdueByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return repos.findOverdueByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<StockRow> findMaxStockThreshold(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findMaxStockThreshold(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findMinStockThreshold(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findMinStockThreshold(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> getCostCenterFinancialSituation(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		return repos.getCostCenterFinancialSituation(username, warehouseList, assignedProjectList, companyId, projectId);
	}

	public List<StockRow> getFinancialSituation(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.getFinancialSituation(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> getFinancialSituation(Integer companyId) {
		return repos.getFinancialSituation(companyId);
	}

	// generate chart
//	public List<ChartContainer> generateTotalCostChartOld(Integer companyId) {
//		List<ChartContainer> result = new ArrayList<>();
//		Calendar c = Calendar.getInstance();
//		Integer currentMonth = c.get(Calendar.MONTH);
//		for (int i = 0; i < 5; i++) {
//			c.set(Calendar.MONTH, 0);
//			Double[] data = new Double[12];
//			for (int j = 0; j < (i != 0 ? 12 : currentMonth + 1); j++) {
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//				data[j] = repos.getTotalCostBeforeDate(companyId, c.getTime());
//				if (j != 11)
//					c.add(Calendar.MONTH, 1);
//
//				if (j == 0) {
//					System.out.println(c.get(Calendar.YEAR));
//					System.out.println("old :" + data[j]);
//				}
//
//			}
//			Series[] series = { new Series("Cost Trend", "blue", data) };
//			result.add(new ChartContainer("" + c.get(Calendar.YEAR), "container_" + c.get(Calendar.YEAR),
//					highchartsService.generateLineBasic("container_" + c.get(Calendar.YEAR), "Sotck Cost Center Trend",
//							"" + c.get(Calendar.YEAR), "MAD", series)));
//			c.add(Calendar.YEAR, -1);
//		}
//		return result;
//	}

	private List<ChartContainer> generateTotalCostChart(List<Object[]> data) { // data[0] : year,data[1] : month,
																				// data[2] : total cost
		List<ChartContainer> result = new ArrayList<>();
		int currentYear = UtilsFunctions.getCurrentYear();
		int currentMonth = UtilsFunctions.getCurrentMonth();
		for (int year = currentYear; year >= currentYear - 5; year--) {
			Double[] yearData = new Double[12];
			for (int month = 1; month <= (year != currentYear ? 12 : currentMonth); month++) {
				final int y = year;
				final int m = month;
				yearData[month - 1] = data.stream().filter(i -> (int) i[0] < y || ((int) i[0] == y && (int) i[1] <= m)).mapToDouble(i -> (double) i[2]).sum();
			}
//			Series[] series = { new Series("Cost Trend", "blue", yearData) };

			List<Serie<Double>> series = new ArrayList<Serie<Double>>();
			series.add(new Serie<Double>("Cost Trend", Arrays.asList(yearData), Color.BLUE));
			result.add(new ChartContainer("" + year, "container_" + year,
					highchartsService.generateBasicLineChart("container_" + year, "Sotck Cost Center Trend", "" + year, Arrays.asList("MAD"), series)));

		}

		return result;
	}

	public List<ChartContainer> generateTotalCostChart(Integer companyId) {
//		List<ChartContainer> result = new ArrayList<>();
//		List<Object[]> data = repos.getTotalCostPerYearAndMonth(companyId);
//		int currentYear = UtilsFunctions.getCurrentYear();
//		int currentMonth = UtilsFunctions.getCurrentMonth();
//		for (int year = currentYear; year >= currentYear - 5; year--) {
//			Double[] yearData = new Double[12];
//			for (int month = 1; month <= (year != currentYear ? 12 : currentMonth); month++) {
//				final int y = year;
//				final int m = month;
//				yearData[month - 1] = data.stream().filter(i -> (int) i[0] < y || ((int) i[0] == y && (int) i[1] <= m))
//						.mapToDouble(i -> (double) i[2]).sum();
//			}
//			Series[] series = { new Series("Cost Trend", "blue", yearData) };
//			result.add(new ChartContainer("" + year, "container_" + year, highchartsService
//					.generateLineBasic("container_" + year, "Sotck Cost Center Trend", "" + year, "MAD", series)));
//		}
//
//		return result;
		List<Object[]> data = repos.getTotalCostPerYearAndMonth(companyId);
		return generateTotalCostChart(data);
	}

	public List<ChartContainer> generateTotalCostChart(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
//		List<ChartContainer> result = new ArrayList<>();
//		List<Object[]> data = repos.getTotalCostPerYearAndMonth(username, warehouseList, assignedProjectList,
//				companyId);
//		int currentYear = UtilsFunctions.getCurrentYear();
//		int currentMonth = UtilsFunctions.getCurrentMonth();
//		for (int year = currentYear; year >= currentYear - 5; year--) {
//			Double[] yearData = new Double[12];
//			for (int month = 1; month <= (year != currentYear ? 12 : currentMonth); month++) {
//				final int y = year;
//				final int m = month;
//				yearData[month - 1] = data.stream().filter(i -> (int) i[0] < y || ((int) i[0] == y && (int) i[1] <= m))
//						.mapToDouble(i -> (double) i[2]).sum();
//			}
//			Series[] series = { new Series("Cost Trend", "blue", yearData) };
//			result.add(new ChartContainer("" + year, "container_" + year, highchartsService
//					.generateLineBasic("container_" + year, "Sotck Cost Center Trend", "" + year, "MAD", series)));
//		}
//
//		return result;

		List<Object[]> data = repos.getTotalCostPerYearAndMonth(username, warehouseList, assignedProjectList, companyId);
		return generateTotalCostChart(data);
	}

//	public List<ChartContainer> generateTotalCostChartOld(String username, List<Integer> warehouseList,
//			List<Integer> assignedProjectList, Integer companyId) {
//		List<ChartContainer> result = new ArrayList<>();
//		Calendar c = Calendar.getInstance();
//		Integer currentMonth = c.get(Calendar.MONTH);
//		for (int i = 0; i < 5; i++) {
//			c.set(Calendar.MONTH, 0);
//			Double[] data = new Double[12];
//			for (int j = 0; j < (i != 0 ? 12 : currentMonth + 1); j++) {
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//				data[j] = repos.getTotalCostBeforeDate(username, warehouseList, assignedProjectList, companyId,
//						c.getTime());
//				if (j != 11)
//					c.add(Calendar.MONTH, 1);
//			}
//			Series[] series = { new Series("Cost Trend", "blue", data) };
//			result.add(new ChartContainer("" + c.get(Calendar.YEAR), "container_" + c.get(Calendar.YEAR),
//					highchartsService.generateLineBasic("container_" + c.get(Calendar.YEAR), "Sotck Cost Center Trend",
//							"" + c.get(Calendar.YEAR), "MAD", series)));
//			c.add(Calendar.YEAR, -1);
//		}
//		return result;
//	}

//	public List<ChartContainer> generateTotalCostChartNew(String username, List<Integer> warehouseList,
//			List<Integer> assignedProjectList, Integer companyId) {
//		List<ChartContainer> result = new ArrayList<>();
//		Calendar c = Calendar.getInstance();
//		Integer currentMonth = c.get(Calendar.MONTH);
//		for (int i = 0; i < 5; i++) {
//			c.set(Calendar.MONTH, 0);
//			Double[] data = new Double[12];
//			for (int j = 0; j < (i != 0 ? 12 : currentMonth + 1); j++) {
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//				data[j] = repos.getTotalCostBeforeDate(username, warehouseList, assignedProjectList, companyId,
//						c.getTime());
//				if (j != 11)
//					c.add(Calendar.MONTH, 1);
//			}
//			Series[] series = { new Series("Cost Trend", "blue", data) };
//			result.add(new ChartContainer("" + c.get(Calendar.YEAR), "container_" + c.get(Calendar.YEAR),
//					highchartsService.generateLineBasic("container_" + c.get(Calendar.YEAR), "Sotck Cost Center Trend",
//							"" + c.get(Calendar.YEAR), "MAD", series)));
//			c.add(Calendar.YEAR, -1);
//		}
//		return result;
//	}

	public List<ChartContainer> generateProjectTotalCostChart(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer projectId) {
		List<ChartContainer> result = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		Integer currentMonth = c.get(Calendar.MONTH);
		for (int i = 0; i < 5; i++) {
			c.set(Calendar.MONTH, 0);
			Double[] data = new Double[12];
			for (int j = 0; j < (i != 0 ? 12 : currentMonth + 1); j++) {
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
				data[j] = repos.getProjectTotalCostBeforeDate(username, warehouseList, assignedProjectList, companyId, projectId, c.getTime());
				if (j != 11)
					c.add(Calendar.MONTH, 1);
			}
//			Series[] series = { new Series("Cost Trend", "blue", data) };
//			result.add(new ChartContainer("" + c.get(Calendar.YEAR), "container_" + c.get(Calendar.YEAR),
//					highchartsService.generateLineBasic("container_" + c.get(Calendar.YEAR), "Sotck Cost Center Trend",
//							"" + c.get(Calendar.YEAR), "MAD", series)));

			List<Serie<Double>> series = new ArrayList<Serie<Double>>();
			series.add(new Serie<Double>("Cost Trend", Arrays.asList(data), Color.BLUE));
			result.add(new ChartContainer("" + c.get(Calendar.YEAR), "container_" + c.get(Calendar.YEAR),
					highchartsService.generateBasicLineChart("container_" + c.get(Calendar.YEAR), "Sotck Cost Center Trend", "" + c.get(Calendar.YEAR), Arrays.asList("MAD"), series)));

			c.add(Calendar.YEAR, -1);
		}
		return result;
	}

	// fast moving items
	public List<StockRow> getFastMovingItems(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<StockRow> list = repos.findByCompanyOwnerGroupbyPartNumberAndDeliveryRequest(username, warehouseList, assignedProjectList, companyId);
		Map<String, StockRow> map = new HashMap<>();
		Map<String, Set<Integer>> mapOutbounds = new HashMap<>();
		Map<String, Set<Integer>> mapCustomers = new HashMap<>();
		for (StockRow stockRow : list) {
			String key = stockRow.getPartNumberId() + ";" + stockRow.getProjectId();
			map.putIfAbsent(key, new StockRow(stockRow.getPartNumber(), stockRow.getProjectName(), 0.0, 0.0));
			mapOutbounds.putIfAbsent(key, new HashSet<Integer>());
			mapCustomers.putIfAbsent(key, new HashSet<Integer>());
			StockRow current = map.get(key);
			if (stockRow.getDeliveryRequest().getIsOutbound()) {
				mapOutbounds.get(key).add(stockRow.getDeliveryRequestId());
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
		List<StockRow> result = repos.findReturnedStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
		result.forEach(i -> {
			Hibernate.initialize(i.getDeliveryRequest());
		});

		return result;
	}

	public Long countReturnedStockRowList(Integer outboundDeliveryRequestId) {
		return repos.countReturnedStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public List<StockRow> findTransferredStockRowList(Integer outboundDeliveryRequestId) {
		List<StockRow> result = repos.findTransferredStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
		result.forEach(i -> {
			Hibernate.initialize(i.getDeliveryRequest());
		});

		return result;
	}

	public Long countTransferredStockRowList(Integer outboundDeliveryRequestId) {
		return repos.countTransferredStockRowList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public List<CompanyType> findOwnerTypeListByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findOwnerTypeListByDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> findDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findDeliveryListsByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findDeliveryListsByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return repos.findDeliveryListsByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<StockRow> findDeliveryListsByCompanyOwner2(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findDeliveryListsByCompanyOwner2(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findDeliveryListsByCustomerOwner2(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findDeliveryListsByCustomerOwner2(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findDeliveryListsByCustomerOwner(Integer customerId, List<Integer> projectIdList) {
		return repos.findDeliveryListsByCustomerOwner(customerId, projectIdList);
	}

	public List<StockRow> findSdmDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findSdmDeliveryListsByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findSdmDeliveryListsByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return repos.findSdmDeliveryListsByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<StockRow> findSdmDeliveryListsByCustomerOwner(Integer customerId, List<Integer> assignedProjectList) {
		return repos.findSdmDeliveryListsByCustomerOwner(customerId, assignedProjectList);
	}

	public List<StockRow> findSdmDeliveryListsByDeliverToSupplier(Integer supplierId, List<Integer> assignedProjectList) {
		return repos.findSdmDeliveryListsByDeliverToSupplier(supplierId, assignedProjectList);
	}

	public List<StockRow> findIsmDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return repos.findIsmDeliveryListsByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}

	public List<StockRow> findIsmDeliveryListsByCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		return repos.findIsmDeliveryListsByCustomerOwner(username, warehouseList, assignedProjectList, customerId);
	}

	public List<StockRow> findIsmDeliveryListsByCustomerOwner(Integer customerId, List<Integer> assignedProjectList) {
		return repos.findIsmDeliveryListsByCustomerOwner(customerId, assignedProjectList);
	}

	public List<StockRow> findIsmDeliveryListsByDeliverToSupplier(Integer supplierId, List<Integer> assignedProjectList) {
		return repos.findIsmDeliveryListsByDeliverToSupplier(supplierId, assignedProjectList);
	}

	public List<StockRow> findDeliveryListsByDeliverToSupplier(Integer supplierId, List<Integer> projectIdList) {
		System.out.println(supplierId);
		System.out.println(projectIdList);
		System.out.println(repos.findDeliveryListsByDeliverToSupplier(supplierId, projectIdList));
		return repos.findDeliveryListsByDeliverToSupplier(supplierId, projectIdList);
	}

	public List<StockRow> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}

	public List<StockRow> findByPoAndDeliveredWithoutBoqMapping(Integer poId) {
		return repos.findByPoAndDeliveredWithoutBoqMapping(poId);
	}

	// PN Reporting quantities
	public Double findPhysicalInventoryByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId) {
		return ObjectUtils.firstNonNull(repos.findPhysicalInventoryByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId), 0.0);
	}

	public Double findStockInventoryByPartNumberAndCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId) {
		return ObjectUtils.firstNonNull(repos.findStockInventoryByPartNumberAndCompanyOwner(username, warehouseList, assignedProjectList, companyId, partNumberId), 0.0);
	}

	public Double findPhysicalInventoryByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId) {
		return ObjectUtils.firstNonNull(repos.findPhysicalInventoryByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId), 0.0);
	}

	public Double findStockInventoryByPartNumberAndCustomerOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId) {
		return ObjectUtils.firstNonNull(repos.findStockInventoryByPartNumberAndCustomerOwner(username, warehouseList, assignedProjectList, customerId, partNumberId), 0.0);
	}

	public List<StockRow> findReturnedStockRowListGroupByPartNumber(Integer outboundDeliveryRequestId) {
		return repos.findReturnedStockRowListGroupByPartNumber(outboundDeliveryRequestId);
	}

	public Map<PartNumber, Double> findReturnedQuantityMap(Integer outboundDeliveryRequestId) {
		return findReturnedStockRowListGroupByPartNumber(outboundDeliveryRequestId).stream().collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getQuantity)));
	}

	public Map<Integer, Double> findQuantityPartNumberMapByDeliveryRequest(Integer deliveryRequest) {
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		List<Object[]> data = repos.findQuantityByDeliveryRequestGroupByPartNumber(deliveryRequest);
		for (Object[] row : data)
			result.put((Integer) row[0], (Double) row[1]);
		return result;
	}

	public void updateOwnerId(Integer deliveryRequestId) {
		repos.updateOwnerId(deliveryRequestId);
	}

	public void updateInboundOwnerId(Integer deliveryRequestId) {
		repos.updateInboundOwnerId(deliveryRequestId);
	}
}
