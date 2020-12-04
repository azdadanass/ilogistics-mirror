package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Company;
import ma.azdad.model.Customer;
import ma.azdad.model.PartNumber;
import ma.azdad.model.Po;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.Site;
import ma.azdad.model.StockRow;
import ma.azdad.model.User;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.StockRowService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.ChartContainer;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class StockRowView extends GenericViewOld<StockRow> {

	@Autowired
	protected StockRowService stockRowService;

	@Autowired
	public CompanyService companyService;

	@Autowired
	public CustomerService customerService;

	@Autowired
	protected CacheView cacheView;

	private StockRow stockRow = new StockRow();

	private Boolean viewDetails = false;
	private Boolean viewAll = false;

	private List<Company> companyList;
	private List<Customer> customerList;
	private String customerCategory;
	private List<Project> projectList;
	private List<Site> destinationList;
	private List<String> deliverToOtherNameList;
	private List<User> externalRequesterList;
	private List<Po> poList;
	private List<Project> destinationProjectList;
	private List<Integer> yearList;
	private List<String> yearAndMonthList;
	private Integer companyId;
	private Integer customerId;
	private Integer projectId;
	private Integer destinationId;
	private String deliverToOtherName;
	private Integer externalRequesterId;
	private Integer poId;
	private Integer destinationProjectId;
	private Integer year;
	private String yearAndMonth;

	private Boolean inStock = true;
	private Boolean customerStockActive;
	private Boolean maxThreshold;
	private int tab = 0;

	private List<ChartContainer> chartList;

	private Integer reportType;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		initParameters();
		refreshList();
		if (isEditPage)
			stockRow = stockRowService.findOne(selectedId);
		else if (isViewPage)
			stockRow = stockRowService.findOne(selectedId);

	}

	@Override
	public void initParameters() {
		super.initParameters();
		viewAll = UtilsFunctions.getBooleanParameter("viewAll");
		companyId = UtilsFunctions.getIntegerParameter("companyId");
		customerId = UtilsFunctions.getIntegerParameter("customerId");
		maxThreshold = UtilsFunctions.getBooleanParameter("maxThreshold");
	}

	public void refreshList() {
		if (isListPage)
			switch (pageIndex) {
			case 1:
				if (companyId != null)
					list2 = list1 = filterByStockSituation(stockRowService.findByCompanyOwnerAndGroupByPartNumber(companyId, sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
				else if (customerId != null)
					list2 = list1 = filterByStockSituation(stockRowService.findByCustomerOwnerAndGroupByPartNumber(customerId, sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
				break;
			case 2:
				if (companyId != null)
					list2 = list1 = stockRowService.findOverdueByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
				else if (customerId != null)
					list2 = list1 = stockRowService.findOverdueByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
				break;
			case 3:
				list2 = list1 = stockRowService.getFastMovingItems(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			default:
				break;
			}
		else if ("/maxMinThreshold.xhtml".equals(currentPath))
			getMaxMinThreshold(true);
		else if ("/companyList.xhtml".equals(currentPath))
			companyList = companyService.find(stockRowService.findCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
		else if ("/projectReporting.xhtml".equals(currentPath)) {
			if (companyId != null)
				projectList = stockRowService.findLightProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findLightProjectCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
		} else if ("/deliveryReporting.xhtml".equals(currentPath)) {
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
		} else if ("/destinationReporting.xhtml".equals(currentPath)) {
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
		} else if ("/deliverToOtherReporting.xhtml".equals(currentPath)) {
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
		} else if ("/externalRequesterReporting.xhtml".equals(currentPath)) {
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
		} else if ("/destinationCustomerReporting.xhtml".equals(currentPath)) {
			if (companyId != null)
				customerList = stockRowService.findLightDestinationCustomerCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
		} else if ("/projectFinancial.xhtml".equals(currentPath)) {
			if (companyId != null) {
				projectList = stockRowService.findLightProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, ProjectTypes.STOCK.getValue());
			}
		}
	}

	public void refreshCustomerList() {
		customerList = customerService.findLight(stockRowService.findCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()), customerCategory, !customerStockActive);
	}

	public void getMaxMinThreshold(Boolean maxThreshold) {
		if (maxThreshold)
			list2 = list1 = stockRowService.findMaxStockThreshold(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
		else
			list2 = list1 = stockRowService.findMinStockThreshold(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
	}

	public void getCostCenterFinancialSituation() {
		list2 = list1 = stockRowService.getCostCenterFinancialSituation(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
	}

	public void getDestinationCustomerReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByDestinationCustomerAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, customerId);
	}

	// Delivery Reports

	public void setTab(int tab) {
		this.tab = tab;
		getReportingLists();
	}

	public void refreshReportNameList() {
		if (projectId == null || reportType == null)
			return;

		switch (reportType) {
		case 0:
			refreshDeliverToOtherNameList();
			break;
		case 1:
			refreshExternalRequesterNameList();
			break;
		case 2:
			refreshDestinationNameList();
			break;
		case 3:
			refreshPoNameList();
			break;
		case 4:
			refreshDestinationProjectNameList();
			break;
		case 5:
			refreshYearNameList();
			break;
		case 6:
			refreshYearAndMonthNameList();
			break;
		default:
			break;
		}
	}

	public void getReportingLists() {
		switch (reportType) {
		case 0:
			getDeliverToOtherReportingLists();
			break;
		case 1:
			getExternalRequesterReportingLists();
			break;
		case 2:
			getDestinationReportingLists();
			break;
		case 3:
			getPoReportingLists();
			break;
		case 4:
			getDestinationProjectReportingLists();
			break;
		case 5:
			getYearReportingLists();
			break;
		case 6:
			getYearAndMonthReportingLists();
			break;
		default:
			break;
		}

		// add related return from outbound
		list1.addAll(stockRowService.findStockHistoryByOutboundDeliveryRequestReturn(list1.stream().map(i -> i.getDeliveryRequest().getId()).collect(Collectors.toList())));

		if (tab == 2 || tab == 3) {
			List<StockRow> result = new ArrayList<>();
			Map<PartNumber, Double> priceMap = list1.stream().filter(s -> s.getqTotalCost() != null).collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getqTotalCost)));
			list1.stream().collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getQuantity))).forEach((x, y) -> result.add(new StockRow(y, x)));
			result.forEach(s -> s.setqTotalCost(priceMap.get(s.getPartNumber())));
			list2 = list1 = result;

		}
	}

	public void refreshDeliverToOtherNameList() {
		if (companyId != null)
			deliverToOtherNameList = stockRowService.findDeliverToOtherNameListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			deliverToOtherNameList = stockRowService.findDeliverToOtherNameListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);

		if (deliverToOtherNameList != null)
			deliverToOtherNameList = deliverToOtherNameList.stream().distinct().collect(Collectors.toList());
	}

	public void refreshExternalRequesterNameList() {
		if (companyId != null)
			externalRequesterList = stockRowService.findLightExternalRequesterCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			externalRequesterList = stockRowService.findLightExternalRequesterCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
	}

	public void refreshDestinationNameList() {
		if (companyId != null)
			destinationList = stockRowService.findLightDestinationCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			destinationList = stockRowService.findLightDestinationCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
	}

	public void refreshPoNameList() {
		if (companyId != null)
			poList = stockRowService.findLightPoCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			poList = stockRowService.findLightPoCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
	}

	public void refreshDestinationProjectNameList() {
		if (companyId != null)
			destinationProjectList = stockRowService.findLightDestinationProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			destinationProjectList = stockRowService.findLightDestinationProjectCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
	}

	public void refreshYearNameList() {
		if (companyId != null)
			yearList = stockRowService.findLightYearCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			yearList = stockRowService.findLightYearCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
	}

	public void refreshYearAndMonthNameList() {
		if (companyId != null)
			yearAndMonthList = stockRowService.findLightYearAndMonthCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
		else if (customerId != null)
			yearAndMonthList = stockRowService.findLightYearAndMonthCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
	}

	public void getDeliverToOtherReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByDeliverToEntityAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, deliverToOtherName, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByDeliverToOtherAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, deliverToOtherName, projectId);
	}

	public void getExternalRequesterReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByExternalRequesterAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, externalRequesterId, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByExternalRequesterAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, externalRequesterId, projectId);
	}

	public void getDestinationReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByDestinationAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, destinationId, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByDestinationAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, destinationId, projectId);
	}

	public void getPoReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByPoAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, poId, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByPoAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, poId, projectId);
	}

	public void getDestinationProjectReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByDestinationProjectAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, destinationProjectId, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByDestinationProjectAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, destinationProjectId, projectId);
	}

	public void getYearReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByYearAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, year, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByYearAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, year, projectId);
	}

	public void getYearAndMonthReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByYearAndMonthAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, yearAndMonth, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByYearAndMonthAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, yearAndMonth, projectId);
	}

	public void getProjectReportingLists(Boolean currentStock) {
		if (currentStock) {
			if (companyId != null)
				list2 = list1 = filterByStockSituation(stockRowService.findByCompanyOwnerAndProjectAndGroupByPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId));
			else if (customerId != null)
				list2 = list1 = filterByStockSituation(stockRowService.findByCustomerOwnerAndProjectAndGroupByPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId));
		} else {
			if (companyId != null)
				list2 = list1 = stockRowService.findStockHistoryByProjectAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
			else if (customerId != null)
				list2 = list1 = stockRowService.findStockHistoryByProjectAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, projectId);
		}
	}

	public void getPartNumberReportingLists(Boolean currentStock) {
		if (currentStock) {
			if (companyId != null)
				list2 = list1 = stockRowService.findCurrentStockByPartNumberAndCompanyOwner(companyId, sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), selectedId);
			else if (customerId != null)
				list2 = list1 = stockRowService.findCurrentStockByPartNumberAndCustomerOwner(customerId, sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), selectedId);
		} else {
			if (companyId != null)
				list2 = list1 = stockRowService.findStockHistoryByPartNumberAndCompanyOwner(companyId, sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), selectedId);
			else if (customerId != null)
				list2 = list1 = stockRowService.findStockHistoryByPartNumberAndCustomerOwner(customerId, sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), selectedId);
		}
	}

	private List<StockRow> filterByStockSituation(List<StockRow> list) {
		if (list == null || list.isEmpty())
			return null;
		List<StockRow> result = new ArrayList<>();

		for (StockRow stockRow : list)
			if (stockRow.getQuantity() > 0) {
				if (inStock)
					result.add(stockRow);
			} else {
				if (!inStock)
					result.add(stockRow);
			}

		return result;
	}

	public void refreshStockRow() {
		stockRowService.flush();
		stockRow = stockRowService.findOne(stockRow.getId());
	}

	public Double getqTotalCost() {
		return list2.stream().filter(s -> s.getqTotalCost() != null).mapToDouble(s -> s.getqTotalCost()).sum();
	}

	// SAVE STOCKROW
	public Boolean canSaveStockRow() {
		return true;
	}

	public String saveStockRow() {
		if (!canSaveStockRow())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateStockRow())
			return null;
		stockRow = stockRowService.save(stockRow);
		return addParameters(viewPage, "faces-redirect=true", "id=" + stockRow.getId());
	}

	public Boolean validateStockRow() {
		return true;
	}

	// DELETE STOCKROW
	public Boolean canDeleteStockRow() {
		return true;
	}

	public String deleteStockRow() {
		if (canDeleteStockRow())
			stockRowService.delete(stockRow);
		return addParameters(listPage, "faces-redirect=true");
	}

	// UPDATE LOCATION
	public Boolean canUpdateLocation() {
		return sessionView.isWM();
	}

	public void updateLocation() {
		if (!canUpdateLocation())
			return;
		stockRowService.updateLocation(stockRow);
	}

	// charts
	public void generateTotalCostChart() {
		chartList = stockRowService.generateTotalCostChart(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
	}

	// GENERIC
	public List<StockRow> findByInboundDeliveryRequest(Integer deliveryRequestId) {
		return stockRowService.findByInboundDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> getStockSituationByInboundDeliveryRequest(Integer deliveryRequestId) {
		return stockRowService.getStockSituationByInboundDeliveryRequest(deliveryRequestId);
	}

	public List<StockRow> findAttachedOutboundDeliveryRequestList(Integer deliveryRequestId) {
		return stockRowService.findAttachedOutboundDeliveryRequestList(deliveryRequestId);
	}

	public List<StockRow> findByDeliveryRequest(Integer deliveryRequestId) {
		return stockRowService.findByDeliveryRequest(deliveryRequestId);
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public StockRowService getStockRowService() {
		return stockRowService;
	}

	public void setStockRowService(StockRowService stockRowService) {
		this.stockRowService = stockRowService;
	}

	public StockRow getStockRow() {
		return stockRow;
	}

	public void setStockRow(StockRow stockRow) {
		this.stockRow = stockRow;
	}

	public Boolean getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(Boolean viewDetails) {
		this.viewDetails = viewDetails;
	}

	public Boolean getViewAll() {
		return viewAll;
	}

	public void setViewAll(Boolean viewAll) {
		this.viewAll = viewAll;
	}

	public List<Company> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Boolean getInStock() {
		return inStock;
	}

	public void setInStock(Boolean inStock) {
		this.inStock = inStock;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<Site> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<Site> destinationList) {
		this.destinationList = destinationList;
	}

	public Integer getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	public String getDeliverToOtherName() {
		return deliverToOtherName;
	}

	public void setDeliverToOtherName(String deliverToOtherName) {
		this.deliverToOtherName = deliverToOtherName;
	}

	public Boolean getMaxThreshold() {
		return maxThreshold;
	}

	public void setMaxThreshold(Boolean maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	public List<ChartContainer> getChartList() {
		return chartList;
	}

	public void setChartList(List<ChartContainer> chartList) {
		this.chartList = chartList;
	}

	public List<String> getDeliverToOtherNameList() {
		return deliverToOtherNameList;
	}

	public void setDeliverToOtherNameList(List<String> deliverToOtherNameList) {
		this.deliverToOtherNameList = deliverToOtherNameList;
	}

	public int getTab() {
		return tab;
	}

	public Integer getExternalRequesterId() {
		return externalRequesterId;
	}

	public void setExternalRequesterId(Integer externalRequesterId) {
		this.externalRequesterId = externalRequesterId;
	}

	public List<User> getExternalRequesterList() {
		return externalRequesterList;
	}

	public void setExternalRequesterList(List<User> externalRequesterList) {
		this.externalRequesterList = externalRequesterList;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public List<Po> getPoList() {
		return poList;
	}

	public void setPoList(List<Po> poList) {
		this.poList = poList;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public List<Project> getDestinationProjectList() {
		return destinationProjectList;
	}

	public void setDestinationProjectList(List<Project> destinationProjectList) {
		this.destinationProjectList = destinationProjectList;
	}

	public Integer getDestinationProjectId() {
		return destinationProjectId;
	}

	public void setDestinationProjectId(Integer destinationProjectId) {
		this.destinationProjectId = destinationProjectId;
	}

	public List<Integer> getYearList() {
		return yearList;
	}

	public void setYearList(List<Integer> yearList) {
		this.yearList = yearList;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<String> getYearAndMonthList() {
		return yearAndMonthList;
	}

	public void setYearAndMonthList(List<String> yearAndMonthList) {
		this.yearAndMonthList = yearAndMonthList;
	}

	public String getYearAndMonth() {
		return yearAndMonth;
	}

	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}

	public String getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public Boolean getCustomerStockActive() {
		return customerStockActive;
	}

	public void setCustomerStockActive(Boolean customerStockActive) {
		this.customerStockActive = customerStockActive;
	}

}
