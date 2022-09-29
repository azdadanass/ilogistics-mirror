package ma.azdad.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Company;
import ma.azdad.model.Customer;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestExpiryDate;
import ma.azdad.model.PartNumber;
import ma.azdad.model.Po;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.Site;
import ma.azdad.model.StockRow;
import ma.azdad.model.User;
import ma.azdad.repos.StockRowRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.DeliveryRequestExpiryDateService;
import ma.azdad.service.StockRowService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.ChartContainer;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class StockRowView extends GenericView<Integer, StockRow, StockRowRepos, StockRowService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected StockRowService stockRowService;

	@Autowired
	public CompanyService companyService;

	@Autowired
	public CustomerService customerService;

	@Autowired
	public DeliveryRequestExpiryDateService deliveryRequestExpiryDateService;

	@Autowired
	protected CacheView cacheView;

	private StockRow stockRow = new StockRow();

	private Integer id;

	private Boolean viewDetails = false;
	private Boolean viewAll = false;

	private List<StockRow> deliveryList1;
	private List<StockRow> deliveryList2;

	private List<Company> companyList;
	private List<Customer> customerList;
	private String customerCategory;
	private List<Project> projectList;
	private List<String> projectStrList;
	private List<Site> destinationList;
	private List<String> deliverToOtherNameList;
	private List<User> externalRequesterList;
	private List<Po> poList;
	private List<Project> destinationProjectList;
	private List<Integer> yearList;
	private List<String> yearAndMonthList;
	private List<PartNumber> partNumberList;
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
	private Integer partNumberId;

	private Boolean inStock = true;
	private Boolean customerStockActive;
	private Boolean maxThreshold;
	private int tab = 0;

	private List<ChartContainer> chartList;

	private String projectName = "All";
	private String reportType = "All";
	private String reportTypeValue = "All";
	private List<String> reportTypeValueList;

	private List<StockRow> currentList = null;
	private List<StockRow> historyList1 = null;
	private List<StockRow> historyList2 = null;
	private List<DeliveryRequestExpiryDate> expiryList = null;
	private Boolean removeFullyDelivered = false;

	private Boolean stock;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		initParameters();
		refreshList();
		if (isEditPage)
			stockRow = stockRowService.findOne(id);
		else if (isViewPage)
			stockRow = stockRowService.findOne(id);

	}

	@Override
	public void initParameters() {
		super.initParameters();
		id = UtilsFunctions.getIntegerParameter("id");
		viewAll = UtilsFunctions.getBooleanParameter("viewAll");
		companyId = UtilsFunctions.getIntegerParameter("companyId");
		customerId = UtilsFunctions.getIntegerParameter("customerId");
		maxThreshold = UtilsFunctions.getBooleanParameter("maxThreshold");

	}

	@Override
	public void refreshList() {
		switch (currentPath) {
		case "/stockRowList.xhtml":
			switch (pageIndex) {
			case 1:
				if (companyId != null)
					list2 = list1 = filterByStockSituation(stockRowService.findByCompanyOwnerAndGroupByPartNumber(companyId, sessionView.getUsername(),
							cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
				else if (customerId != null)
					list2 = list1 = filterByStockSituation(stockRowService.findByCustomerOwnerAndGroupByPartNumber(customerId, sessionView.getUsername(),
							cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
				break;
			case 2:
				if (companyId != null)
					list2 = list1 = stockRowService.findOverdueByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
							companyId);
				else if (customerId != null)
					list2 = list1 = stockRowService.findOverdueByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
							customerId);
				break;
			case 3:
				list2 = list1 = stockRowService.getFastMovingItems(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			default:
				break;
			}
			break;
		case "/maxMinThreshold.xhtml":
			getMaxMinThreshold(true);
			break;
		case "/companyList.xhtml":
			companyList = companyService.find(stockRowService.findCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
			break;
		case "/deliveryReporting.xhtml":
			initDeliveryLists();
			refreshDeliveryLists();
			projectStrList = deliveryList1.stream().map(i -> i.getProjectName()).distinct().collect(Collectors.toList());
			break;
		case "/destinationReporting.xhtml":
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
						customerId);
			break;
		case "/deliverToOtherReporting.xhtml":
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
						customerId);
			break;
		case "/externalRequesterReporting.xhtml":
			if (companyId != null)
				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			else if (customerId != null)
				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
						customerId);
			break;
		case "/destinationCustomerReporting.xhtml":
			if (companyId != null)
				customerList = stockRowService.findLightDestinationCustomerCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), companyId);
			break;
		case "/projectFinancial.xhtml":
			if (companyId != null) {
				projectList = stockRowService.findLightProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
						companyId, ProjectTypes.STOCK.getValue());
			}
			break;
		case "/companyFinancial.xhtml":
			this.companyId = this.id;
			if (sessionView.getIsCfo(companyId))
				list2 = list1 = stockRowService.getFinancialSituation(companyId);
			else
				list2 = list1 = stockRowService.getFinancialSituation(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			generateTotalCostChart();
			break;
		case "/viewPo.xhtml":
			initLists(service.findDeliveryListsByPo(id));
		default:
			break;
		}

//		if (isListPage)
//			switch (pageIndex) {
//			case 1:
//				if (companyId != null)
//					list2 = list1 = filterByStockSituation(stockRowService.findByCompanyOwnerAndGroupByPartNumber(companyId, sessionView.getUsername(),
//							cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
//				else if (customerId != null)
//					list2 = list1 = filterByStockSituation(stockRowService.findByCustomerOwnerAndGroupByPartNumber(customerId, sessionView.getUsername(),
//							cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
//				break;
//			case 2:
//				if (companyId != null)
//					list2 = list1 = stockRowService.findOverdueByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//							companyId);
//				else if (customerId != null)
//					list2 = list1 = stockRowService.findOverdueByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//							customerId);
//				break;
//			case 3:
//				list2 = list1 = stockRowService.getFastMovingItems(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
//			default:
//				break;
//			}
//		else if ("/maxMinThreshold.xhtml".equals(currentPath))
//			getMaxMinThreshold(true);
//		else if ("/companyList.xhtml".equals(currentPath))
//			companyList = companyService.find(stockRowService.findCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()));
//		else if ("/deliveryReporting.xhtml".equals(currentPath)) {
//			initDeliveryLists();
//			refreshDeliveryLists();
//			projectStrList = deliveryList1.stream().map(i -> i.getProjectName()).distinct().collect(Collectors.toList());
////			if (companyId != null)
////				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
////			else if (customerId != null)
////				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId);
//		} else if ("/destinationReporting.xhtml".equals(currentPath)) {
//			if (companyId != null)
//				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
//			else if (customerId != null)
//				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//						customerId);
//		} else if ("/deliverToOtherReporting.xhtml".equals(currentPath)) {
//			if (companyId != null)
//				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
//			else if (customerId != null)
//				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//						customerId);
//		} else if ("/externalRequesterReporting.xhtml".equals(currentPath)) {
//			if (companyId != null)
//				projectList = stockRowService.findProjectListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
//			else if (customerId != null)
//				projectList = stockRowService.findProjectListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//						customerId);
//		} else if ("/destinationCustomerReporting.xhtml".equals(currentPath)) {
//			if (companyId != null)
//				customerList = stockRowService.findLightDestinationCustomerCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(),
//						cacheView.getAssignedProjectList(), companyId);
//		} else if ("/projectFinancial.xhtml".equals(currentPath)) {
//			if (companyId != null) {
//				projectList = stockRowService.findLightProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//						companyId, ProjectTypes.STOCK.getValue());
//			}
//		}
	}

	public void refreshProjectList() {
		if (companyId != null)
			projectList = stockRowService.findLightProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId,
					stock);
		else if (customerId != null)
			projectList = stockRowService.findLightProjectCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId,
					stock);
	}

	public void refreshCustomerList() {
		customerList = customerService.findLight(stockRowService.findCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList()),
				customerCategory, !customerStockActive);
	}

	public void getMaxMinThreshold(Boolean maxThreshold) {
		if (maxThreshold)
			list2 = list1 = stockRowService.findMaxStockThreshold(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
		else
			list2 = list1 = stockRowService.findMinStockThreshold(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
	}

	public void getCostCenterFinancialSituation() {
		list2 = list1 = stockRowService.getCostCenterFinancialSituation(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId,
				projectId);
	}

	public void getDestinationCustomerReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByDestinationCustomerAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(),
					cacheView.getAssignedProjectList(), companyId, customerId);
	}

	public Double getList2TotalCost() {
		return list2.stream().mapToDouble(i -> i.getTotalCost()).sum();
	}

	public Double getList2TotalPrice() {
		return list2.stream().mapToDouble(i -> i.getTotalPrice()).sum();
	}

	// Delivery Reports

	public void initDeliveryLists() {
		if (companyId != null)
			deliveryList1 = stockRowService.findDeliveryListsByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
		if (customerId != null)
			deliveryList1 = stockRowService.findDeliveryListsByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
					customerId);

		changeProjectNameListener();
	}

	public void changeProjectNameListener() {
		this.reportType = "All";
		this.reportTypeValue = "All";

		if ("All".equals(projectName))
			deliveryList2 = new ArrayList<StockRow>(deliveryList1);
		else
			deliveryList2 = deliveryList1.stream().filter(i -> i.getProjectName().equals(projectName)).collect(Collectors.toList());

		refreshDeliveryLists();
	}

	public void changeReportTypeListener() {
		switch (reportType) {
		case "3rd Party / Subcontractor":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getDeliverToEntityName() != null).map(i -> i.getDeliverToEntityName()).distinct()
					.collect(Collectors.toList());
			break;
		case "Customer":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getDestinationProjectCustomerName() != null).map(i -> i.getDestinationProjectCustomerName()).distinct()
					.collect(Collectors.toList());
			break;
		case "Destination Site":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getDestinationName() != null).map(i -> i.getDestinationName()).distinct().collect(Collectors.toList());
			break;
		case "Purchase Order":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getPoNumero() != null).map(i -> i.getPoNumero()).distinct().collect(Collectors.toList());
			break;
		case "Destination Project":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getDestinationProjectName() != null).map(i -> i.getDestinationProjectName()).distinct()
					.collect(Collectors.toList());
			break;
		case "Yearly":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getDeliveryYear() != null).map(i -> i.getDeliveryYear()).distinct().collect(Collectors.toList());
			break;
		case "Monthly":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getDeliveryMonthAndYear() != null).map(i -> i.getDeliveryMonthAndYear()).distinct()
					.collect(Collectors.toList());
			break;
		case "Part Number":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getPartNumberName() != null).map(i -> i.getPartNumberName()).distinct().collect(Collectors.toList());
			break;
		case "Brand":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getPartNumberBrandName() != null).map(i -> i.getPartNumberBrandName()).distinct()
					.collect(Collectors.toList());
			break;
		case "End Customer":
			reportTypeValueList = deliveryList2.stream().filter(i -> i.getEndCustomerName() != null).map(i -> i.getEndCustomerName()).distinct().collect(Collectors.toList());
			break;
		default:
			break;
		}
		refreshDeliveryLists();
	}

	public void setTab(int tab) {
		this.tab = tab;
		refreshDeliveryLists();
	}

	public void refreshDeliveryLists() {
		switch (reportType) {
		case "All":
			initLists(new ArrayList<StockRow>(deliveryList2));
			break;
		case "3rd Party / Subcontractor":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getDeliverToEntityName())).collect(Collectors.toList()));
			break;
		case "Customer":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getDestinationProjectCustomerName())).collect(Collectors.toList()));
			break;
		case "Destination Site":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getDestinationName())).collect(Collectors.toList()));
			break;
		case "Purchase Order":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getPoNumero())).collect(Collectors.toList()));
			break;
		case "Destination Project":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getDestinationProjectName())).collect(Collectors.toList()));
			break;
		case "Yearly":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getDeliveryYear())).collect(Collectors.toList()));
			break;
		case "Monthly":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getDeliveryMonthAndYear())).collect(Collectors.toList()));
			break;
		case "Part Number":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getPartNumberName())).collect(Collectors.toList()));
			break;
		case "Brand":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getPartNumberBrandName())).collect(Collectors.toList()));
			break;
		case "End Customer":
			initLists(deliveryList2.stream().filter(i -> reportTypeValue.equals(i.getEndCustomerName())).collect(Collectors.toList()));
			break;
		default:
			break;
		}

		// add related return from outbound
		List<Integer> dnIdList = list1.stream().map(i -> i.getDeliveryRequest().getId()).collect(Collectors.toList());
		List<Integer> partNumberIdList = list1.stream().map(i -> i.getPartNumber().getId()).collect(Collectors.toList());
		if (!dnIdList.isEmpty() && !partNumberIdList.isEmpty())
			if (companyId != null)
				list1.addAll(stockRowService.findStockHistoryByCompanyOwnerAndOutboundDeliveryRequestReturn(companyId, dnIdList, partNumberIdList));
			else if (customerId != null)
				list1.addAll(stockRowService.findStockHistoryByCustomerOwnerAndOutboundDeliveryRequestReturn(customerId, dnIdList, partNumberIdList));

		if (tab == 2) {
			List<StockRow> result = new ArrayList<>();
			list1.stream().collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getQuantity)))
					.forEach((x, y) -> result.add(new StockRow(y, x)));

			Collections.sort(result, new Comparator<StockRow>() {
				public int compare(StockRow o1, StockRow o2) {
					return o1.getQuantity().compareTo(o2.getQuantity());
				}
			});

			list2 = list1 = result;
		}

//		if (tab == 2 || tab == 3) {
//			List<StockRow> result = new ArrayList<>();
//			Map<PartNumber, Double> priceMap = list1.stream().filter(s -> s.getqTotalCost() != null)
//					.collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getqTotalCost)));
//			list1.stream().collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getQuantity)))
//					.forEach((x, y) -> result.add(new StockRow(y, x)));
//			result.forEach(s -> s.setqTotalCost(priceMap.get(s.getPartNumber())));
//			list2 = list1 = result;
//		}

	}

//	public void getReportingLists() {
//		switch (reportType) {
//		case 0:
//			getDeliverToOtherReportingLists();
//			break;
//		case 1:
//			getExternalRequesterReportingLists();
//			break;
//		case 2:
//			getDestinationReportingLists();
//			break;
//		case 3:
//			getPoReportingLists();
//			break;
//		case 4:
//			getDestinationProjectReportingLists();
//			break;
//		case 5:
//			getYearReportingLists();
//			break;
//		case 6:
//			getYearAndMonthReportingLists();
//			break;
//		case 7:
//			getPartNumberReportingLists();
//			break;
//		default:
//			break;
//		}

	// add related return from outbound
//		List<Integer> dnIdList = list1.stream().map(i -> i.getDeliveryRequest().getId()).collect(Collectors.toList());
//		List<Integer> partNumberIdList = list1.stream().map(i -> i.getPartNumber().getId()).collect(Collectors.toList());
//
//		list1.addAll(stockRowService.findStockHistoryByOutboundDeliveryRequestReturn(dnIdList, partNumberIdList));
//
//		if (tab == 2 || tab == 3) {
//			List<StockRow> result = new ArrayList<>();
//			Map<PartNumber, Double> priceMap = list1.stream().filter(s -> s.getqTotalCost() != null)
//					.collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getqTotalCost)));
//			list1.stream().collect(Collectors.groupingBy(StockRow::getPartNumber, Collectors.summingDouble(StockRow::getQuantity)))
//					.forEach((x, y) -> result.add(new StockRow(y, x)));
//			result.forEach(s -> s.setqTotalCost(priceMap.get(s.getPartNumber())));
//			list2 = list1 = result;
//		}
//	}

//	public void refreshDeliverToOtherNameList() {
//		if (companyId != null)
//			deliverToOtherNameList = stockRowService.findDeliverToOtherNameListByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(),
//					cacheView.getAssignedProjectList(), companyId, projectId);
//		else if (customerId != null)
//			deliverToOtherNameList = stockRowService.findDeliverToOtherNameListByCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(),
//					cacheView.getAssignedProjectList(), customerId, projectId);
//
//		if (deliverToOtherNameList != null)
//			deliverToOtherNameList = deliverToOtherNameList.stream().distinct().collect(Collectors.toList());
//	}

//	public void refreshExternalRequesterNameList() {
//		if (companyId != null)
//			externalRequesterList = stockRowService.findLightExternalRequesterCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(),
//					cacheView.getAssignedProjectList(), companyId, projectId);
//		else if (customerId != null)
//			externalRequesterList = stockRowService.findLightExternalRequesterCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(),
//					cacheView.getAssignedProjectList(), customerId, projectId);
//	}
//
//	public void refreshDestinationNameList() {
//		if (companyId != null)
//			destinationList = stockRowService.findLightDestinationCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//					companyId, projectId);
//		else if (customerId != null)
//			destinationList = stockRowService.findLightDestinationCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//					customerId, projectId);
//	}
//
//	public void refreshPoNameList() {
//		if (companyId != null)
//			poList = stockRowService.findLightPoCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, projectId);
//		else if (customerId != null)
//			poList = stockRowService.findLightPoCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId,
//					projectId);
//	}
//
//	public void refreshPartNumberNameList() {
//		if (companyId != null)
//			partNumberList = stockRowService.findLightPartNumberCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//					companyId, projectId);
//		else if (customerId != null)
//			partNumberList = stockRowService.findLightPartNumberCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//					customerId, projectId);
//	}

//	public void refreshDestinationProjectNameList() {
//		if (companyId != null)
//			destinationProjectList = stockRowService.findLightDestinationProjectCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(),
//					cacheView.getAssignedProjectList(), companyId, projectId);
//		else if (customerId != null)
//			destinationProjectList = stockRowService.findLightDestinationProjectCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(),
//					cacheView.getAssignedProjectList(), customerId, projectId);
//	}

//	public void refreshYearNameList() {
//		if (companyId != null)
//			yearList = stockRowService.findLightYearCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId,
//					projectId);
//		else if (customerId != null)
//			yearList = stockRowService.findLightYearCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId,
//					projectId);
//	}

//	public void refreshYearAndMonthNameList() {
//		if (companyId != null)
//			yearAndMonthList = stockRowService.findLightYearAndMonthCompanyOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//					companyId, projectId);
//		else if (customerId != null)
//			yearAndMonthList = stockRowService.findLightYearAndMonthCustomerOwnerList(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
//					customerId, projectId);
//	}

//	public void getDeliverToOtherReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByDeliverToEntityAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, deliverToOtherName, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByDeliverToOtherAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, deliverToOtherName, projectId);
//	}

//	public void getExternalRequesterReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByExternalRequesterAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, externalRequesterId, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByExternalRequesterAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, externalRequesterId, projectId);
//	}

	public void getPartNumberReportingLists() {
		if (companyId != null)
			list2 = list1 = stockRowService.findStockHistoryByPartNumberAndCompanyOwner(companyId, sessionView.getUsername(), cacheView.getWarehouseList(),
					cacheView.getAssignedProjectList(), partNumberId, projectId);
		else if (customerId != null)
			list2 = list1 = stockRowService.findStockHistoryByPartNumberAndCustomerOwner(customerId, sessionView.getUsername(), cacheView.getWarehouseList(),
					cacheView.getAssignedProjectList(), partNumberId, projectId);
	}

//	public void getDestinationReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByDestinationAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, destinationId, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByDestinationAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, destinationId, projectId);
//	}
//
//	public void getPoReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByPoAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, poId, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByPoAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, poId, projectId);
//	}
//
//	public void getDestinationProjectReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByDestinationProjectAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, destinationProjectId, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByDestinationProjectAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, destinationProjectId, projectId);
//	}
//
//	public void getYearReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByYearAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, year, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByYearAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, year, projectId);
//	}
//
//	public void getYearAndMonthReportingLists() {
//		if (companyId != null)
//			list2 = list1 = stockRowService.findStockHistoryByYearAndMonthAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId, yearAndMonth, projectId);
//		else if (customerId != null)
//			list2 = list1 = stockRowService.findStockHistoryByYearAndMonthAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), customerId, yearAndMonth, projectId);
//	}

	public void getProjectReportingLists(Boolean currentStock) {
		if (currentStock) {
			if (companyId != null)
				list2 = list1 = filterByStockSituation(stockRowService.findByCompanyOwnerAndProjectAndGroupByPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), companyId, projectId));
			else if (customerId != null)
				list2 = list1 = filterByStockSituation(stockRowService.findByCustomerOwnerAndProjectAndGroupByPartNumber(sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), customerId, projectId));
		} else {
			if (companyId != null)
				list2 = list1 = stockRowService.findStockHistoryByProjectAndCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), companyId, projectId);
			else if (customerId != null)
				list2 = list1 = stockRowService.findStockHistoryByProjectAndCustomerOwner(sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), customerId, projectId);
		}
	}

	public void initCurrentList() {
		if (currentList == null) {
			if (companyId != null)
				currentList = stockRowService.findCurrentStockByPartNumberAndCompanyOwner(companyId, sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), id);
			else if (customerId != null)
				currentList = stockRowService.findCurrentStockByPartNumberAndCustomerOwner(customerId, sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), id);
		}
		list2 = list1 = currentList;
	}

	public void initHistorylist() {
		if (removeFullyDelivered)
			initHistoryList2();
		else
			initHistorylist1();
	}

	private void initHistorylist1() {
		if (historyList1 == null) {
			if (companyId != null)
				historyList1 = stockRowService.findStockHistoryByPartNumberAndCompanyOwner(companyId, sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), id);
			else if (customerId != null)
				historyList1 = stockRowService.findStockHistoryByPartNumberAndCustomerOwner(customerId, sessionView.getUsername(), cacheView.getWarehouseList(),
						cacheView.getAssignedProjectList(), id);
		}
		list2 = list1 = historyList1;
	}

	private void initHistoryList2() {
		if (historyList2 == null) {
			initMapInboundDeliveryRequestMapDeliveryRequestQuantity();
			historyList2 = historyList1.stream().filter(sr -> mapInboundDnMapDnQuantity.containsKey(sr.getInboundDeliveryRequest())).collect(Collectors.toList());
		}
		list2 = list1 = historyList2;
	}

	private Map<DeliveryRequest, Map<Integer, Double>> mapInboundDnMapDnQuantity = null;

	private void initMapInboundDeliveryRequestMapDeliveryRequestQuantity() {
		if (mapInboundDnMapDnQuantity != null)
			return;

		initHistorylist1();
		// <inboundDn,Map<dnId,Qty>>
		Map<DeliveryRequest, Map<Integer, Double>> result = new HashMap<>();
		for (StockRow stockRow : historyList1) {
			DeliveryRequest inboundDeliveryRequest = stockRow.getInboundDeliveryRequest();
			Integer deliveryRequestId = stockRow.getDeliveryRequest().getId();
			result.putIfAbsent(inboundDeliveryRequest, new HashMap<Integer, Double>());
			result.get(inboundDeliveryRequest).putIfAbsent(deliveryRequestId, 0.0);
			result.get(inboundDeliveryRequest).put(deliveryRequestId, result.get(inboundDeliveryRequest).get(deliveryRequestId) + stockRow.getQuantity());
		}
		// remove fully delivered
		result.values().removeIf(v -> v.values().stream().mapToDouble(qty -> qty).sum() == 0.0);
		mapInboundDnMapDnQuantity = result;
	}

	public void initExpiryList() {
		if (expiryList != null)
			return;

		expiryList = new ArrayList<DeliveryRequestExpiryDate>();

		initMapInboundDeliveryRequestMapDeliveryRequestQuantity();
		List<Integer> deliveryRequestIdList = mapInboundDnMapDnQuantity.values().stream().map(v -> v.keySet()).collect(ArrayList::new, List::addAll, List::addAll);
		List<DeliveryRequestExpiryDate> deliveryRequestExpiryDateList = deliveryRequestExpiryDateService
				.findByPartNumberAndDeliveryRequestListGroupByExpiryDateAndDeliveryRequestAndInboundDeliveryRequest(id, deliveryRequestIdList);

		System.out.println("mapInboundDnMapDnQuantity : " + mapInboundDnMapDnQuantity);

		for (DeliveryRequest inbound : mapInboundDnMapDnQuantity.keySet()) {
			Map<Integer, Double> map = mapInboundDnMapDnQuantity.get(inbound);
			Map<Integer, Double> mapDnExpiryDate = deliveryRequestExpiryDateList.stream().filter(i -> i.getInboundDeliveryRequestId().equals(inbound.getId()))
					.collect(Collectors.groupingBy(DeliveryRequestExpiryDate::getDeliveryRequestId, Collectors.summingDouble(DeliveryRequestExpiryDate::getQuantity)));
			Boolean test = true;
			for (Integer dnId : map.keySet()) {
				Double dnQty = map.get(dnId);
				Double expiryQty = mapDnExpiryDate.getOrDefault(dnId, 0.0);
				if (Math.abs(dnQty) != Math.abs(expiryQty)) {
					test = false;
					break;
				}
			}
			if (test) {
				Set<Date> dateSet = deliveryRequestExpiryDateList.stream().filter(i -> i.getInboundDeliveryRequestId().equals(inbound.getId())).map(i -> i.getExpiryDate())
						.distinct().collect(Collectors.toSet());
				for (Date date : dateSet) {
					Double quantity = deliveryRequestExpiryDateList.stream().filter(i -> inbound.getId().equals(i.getInboundDeliveryRequestId()) && date.equals(i.getExpiryDate()))
							.mapToDouble(i -> i.getDeliveryRequestId().equals(i.getInboundDeliveryRequestId()) ? i.getQuantity() : -i.getQuantity()).sum();
					if (quantity == 0.0)
						continue;
					DeliveryRequestExpiryDate deliveryRequestExpiryDate = new DeliveryRequestExpiryDate();
					deliveryRequestExpiryDate.setQuantity(quantity);
					deliveryRequestExpiryDate.setInboundDeliveryRequestId(inbound.getId());
					deliveryRequestExpiryDate.setInboundDeliveryRequestReference(inbound.getReference());
					deliveryRequestExpiryDate.setExpiryDate(date);
					expiryList.add(deliveryRequestExpiryDate);
				}
			}
		}

		System.out.println("expiryList " + expiryList);

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
			try {
				stockRowService.delete(stockRow);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

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
		switch (currentPath) {
		case "/projectFinancial.xhtml":
			chartList = stockRowService.generateProjectTotalCostChart(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId,
					projectId);
			break;
		case "/companyFinancial.xhtml":
			if (sessionView.getIsCfo(companyId))
				chartList = stockRowService.generateTotalCostChart(companyId);
			else
				chartList = stockRowService.generateTotalCostChart(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
			break;

		default:
			break;
		}

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
	
	@Cacheable("stockRowView.findDeliveryListsByPo")
	public List<StockRow> findDeliveryListsByPo(Integer poId){
		return service.findDeliveryListsByPo(poId);
	}

	// GETTERS & SETTERS

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

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
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

	public Integer getPartNumberId() {
		return partNumberId;
	}

	public void setPartNumberId(Integer partNumberId) {
		this.partNumberId = partNumberId;
	}

	public List<PartNumber> getPartNumberList() {
		return partNumberList;
	}

	public void setPartNumberList(List<PartNumber> partNumberList) {
		this.partNumberList = partNumberList;
	}

	public List<StockRow> getCurrentList() {
		return currentList;
	}

	public void setCurrentList(List<StockRow> currentList) {
		this.currentList = currentList;
	}

	public Boolean getRemoveFullyDelivered() {
		return removeFullyDelivered;
	}

	public void setRemoveFullyDelivered(Boolean removeFullyDelivered) {
		this.removeFullyDelivered = removeFullyDelivered;
	}

	public List<DeliveryRequestExpiryDate> getExpiryList() {
		return expiryList;
	}

	public void setExpiryList(List<DeliveryRequestExpiryDate> expiryList) {
		this.expiryList = expiryList;
	}

	public List<String> getProjectStrList() {
		return projectStrList;
	}

	public void setProjectStrList(List<String> projectStrList) {
		this.projectStrList = projectStrList;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<String> getReportTypeValueList() {
		return reportTypeValueList;
	}

	public void setReportTypeValueList(List<String> reportTypeValueList) {
		this.reportTypeValueList = reportTypeValueList;
	}

	public String getReportTypeValue() {
		return reportTypeValue;
	}

	public void setReportTypeValue(String reportTypeValue) {
		this.reportTypeValue = reportTypeValue;
	}

	public Boolean getStock() {
		return stock;
	}

	public void setStock(Boolean stock) {
		this.stock = stock;
	}

}
