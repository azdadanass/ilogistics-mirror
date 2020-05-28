package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;


public class Report implements Serializable {

	private Integer id;

	private Date effectiveStartDate;
	private Date effectiveEndDate;

	private Double sales = 0.0;
	private Double directRevenues = 0.0;
	private Double crossRevenue = 0.0;
	private Double allRevenues = 0.0;
	private Double invoiceHT = 0.0;
	private Double invoiceTTC = 0.0;
	private Double cashInHT = 0.0;
	private Double cashInTTC = 0.0;
	private Double cashCrossRevenue = 0.0;
	private Double allCashInHT = 0.0;
	private Double allCashInTTC = 0.0;

	private Double costIbuy = 0.0;
	private Double costIexpense = 0.0;
	private Double costPayroll = 0.0;
	private Double crossCost = 0.0;
	private Double costLob = 0.0;
	private Double costSales = 0.0;
	private Double costSupport = 0.0;
	private Double costTpsr = 0.0;
	private Double allCosts = 0.0; // sum costs without finance cost & tpsrcost

	private Double cashIbuyHT = 0.0;
	private Double cashIbuyTTC = 0.0;
	private Double cashIexpenseHT = 0.0;
	private Double cashIexpenseTTC = 0.0;
	private Double cashCrossCost = 0.0;

	private Double cashLobHT = 0.0;
	private Double cashLobTTC = 0.0;
	private Double cashSalesHT = 0.0;
	private Double cashSalesTTC = 0.0;
	private Double cashSupportHT = 0.0;
	private Double cashSupportTTC = 0.0;
	private Double cashTpsrHT = 0.0;
	private Double cashTpsrTTC = 0.0;

	private Double allCashOutHT = 0.0;
	private Double allCashOutTTC = 0.0;

	private Double projectHours = 0.0;
	private Double deliveryHours = 0.0;
	private Double customerDeliveryHours = 0.0;
	private Double lobDeliveryHours = 0.0;
	private Double inLobHours = 0.0;

	private Double invoiceDeductions = 0.0;
	private Double cashInvoiceDeductions = 0.0;
	private Double ibuyDeductions = 0.0;

	//costSupport details
	private Double costSupportIbuy = 0.0;
	private Double costSupportIexpense = 0.0;
	private Double costSupportPayroll = 0.0;
	private Double costSupportCross = 0.0;
	private Double costSupportIbuyDeductions = 0.0;

	//costSales details
	private Double costSalesIbuy = 0.0;
	private Double costSalesIexpense = 0.0;
	private Double costSalesPayroll = 0.0;
	private Double costSalesCross = 0.0;
	private Double costSalesIbuyDeductions = 0.0;

	private Double costSalesCustomerIbuy = 0.0;
	private Double costSalesCustomerIexpense = 0.0;
	private Double costSalesCustomerPayroll = 0.0;
	private Double costSalesCustomerCross = 0.0;
	private Double costSalesCustomerIbuyDeductions = 0.0;

	//costLob details
	private Double costLobIbuy = 0.0;
	private Double costLobIexpense = 0.0;
	private Double costLobPayroll = 0.0;
	private Double costLobCross = 0.0;
	private Double costLobIbuyDeductions = 0.0;

	private Project project;

	private Date costStartDate;
	private Date costEndDate;

	private Double costIbuyNew = 0.0;
	private Double costIexpenseNew = 0.0;
	private Double costPayrollNew = 0.0;

	// Calculable
	private Double costFinance = 0.0;

	// Others
	private Integer idproject;
	private String projectName;
	private String lob;
	private String customer;
	private String manager;
	private String managerFullname;
	private Date projectStartDate;
	private Date projectEndDate;
	private String projectDuration;
	private String projectStatus;
	private String projectType;
	private Double projectTax;
	private Double projectAmortization;
	private Double projectDepreciation;

	private Double projectTotalSalesCost;
	private Double projectTotalSupportCost;
	private Double projectTpsr;

	private Integer costcenter;
	private String currency;
	private Double plannedRevenue;
	private Double plannedCost;
	private Double projectCurrencyMaxValue;
	private Double plannedRevenueMAD;
	private Double plannedCostMAD;
	private Double tpsrBudgeting;
	private Double tpsrPayment;

	public Report() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		return (this.id).equals(((Report) obj).getId());
	}

	public boolean filter(String query) {
		boolean result = false;
		if (projectName != null)
			result = projectName.toLowerCase().contains(query);
		if (!result && manager != null)
			result = manager.toLowerCase().contains(query);
		if (!result && managerFullname != null)
			result = managerFullname.toLowerCase().contains(query);
		return result;
	}

	public Double getOperatingCost(Double costFinance) {
		Double result = 0.0;
		if (costIbuy != null)
			result += costIbuy;
		if (costIexpense != null)
			result += costIexpense;
		if (costPayroll != null)
			result += costPayroll;
		if (crossCost != null)
			result += crossCost;
		if (ibuyDeductions != null)
			result += ibuyDeductions;
		if (costLob != null)
			result += costLob;
		if (costFinance != null)
			result += costFinance;
		//TODO ADD Global Cross Charge
		return result;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public Double getDirectRevenues() {
		return directRevenues;
	}

	public void setDirectRevenues(Double directRevenues) {
		this.directRevenues = directRevenues;
	}

	public Double getCrossRevenue() {
		return crossRevenue;
	}

	public void setCrossRevenue(Double crossRevenue) {
		this.crossRevenue = crossRevenue;
	}

	public Double getAllRevenues() {
		return allRevenues;
	}

	public void setAllRevenues(Double allRevenues) {
		this.allRevenues = allRevenues;
	}

	public Double getInvoiceHT() {
		return invoiceHT;
	}

	public void setInvoiceHT(Double invoiceHT) {
		this.invoiceHT = invoiceHT;
	}

	public Double getInvoiceTTC() {
		return invoiceTTC;
	}

	public void setInvoiceTTC(Double invoiceTTC) {
		this.invoiceTTC = invoiceTTC;
	}

	public Double getCashInHT() {
		return cashInHT;
	}

	public void setCashInHT(Double cashInHT) {
		this.cashInHT = cashInHT;
	}

	public Double getCashInTTC() {
		return cashInTTC;
	}

	public void setCashInTTC(Double cashInTTC) {
		this.cashInTTC = cashInTTC;
	}

	public Double getCashCrossRevenue() {
		return cashCrossRevenue;
	}

	public void setCashCrossRevenue(Double cashCrossRevenue) {
		this.cashCrossRevenue = cashCrossRevenue;
	}

	public Double getAllCashInHT() {
		return allCashInHT;
	}

	public void setAllCashInHT(Double allCashInHT) {
		this.allCashInHT = allCashInHT;
	}

	public Double getAllCashInTTC() {
		return allCashInTTC;
	}

	public void setAllCashInTTC(Double allCashInTTC) {
		this.allCashInTTC = allCashInTTC;
	}

	public Double getCostIbuy() {
		return costIbuy;
	}

	public void setCostIbuy(Double costIbuy) {
		this.costIbuy = costIbuy;
	}

	public Double getCostIexpense() {
		return costIexpense;
	}

	public void setCostIexpense(Double costIexpense) {
		this.costIexpense = costIexpense;
	}

	public Double getCostPayroll() {
		return costPayroll;
	}

	public void setCostPayroll(Double costPayroll) {
		this.costPayroll = costPayroll;
	}

	public Double getCrossCost() {
		return crossCost;
	}

	public void setCrossCost(Double crossCost) {
		this.crossCost = crossCost;
	}

	public Double getCostLob() {
		return costLob;
	}

	public void setCostLob(Double costLob) {
		this.costLob = costLob;
	}

	public Double getCostSales() {
		return costSales;
	}

	public void setCostSales(Double costSales) {
		this.costSales = costSales;
	}

	public Double getCostSupport() {
		return costSupport;
	}

	public void setCostSupport(Double costSupport) {
		this.costSupport = costSupport;
	}

	public Double getCostTpsr() {
		return costTpsr;
	}

	public void setCostTpsr(Double costTpsr) {
		this.costTpsr = costTpsr;
	}

	public Double getAllCosts() {
		return allCosts;
	}

	public void setAllCosts(Double allCosts) {
		this.allCosts = allCosts;
	}

	public Double getCashIbuyHT() {
		return cashIbuyHT;
	}

	public void setCashIbuyHT(Double cashIbuyHT) {
		this.cashIbuyHT = cashIbuyHT;
	}

	public Double getCashIbuyTTC() {
		return cashIbuyTTC;
	}

	public void setCashIbuyTTC(Double cashIbuyTTC) {
		this.cashIbuyTTC = cashIbuyTTC;
	}

	public Double getCashIexpenseHT() {
		return cashIexpenseHT;
	}

	public void setCashIexpenseHT(Double cashIexpenseHT) {
		this.cashIexpenseHT = cashIexpenseHT;
	}

	public Double getCashIexpenseTTC() {
		return cashIexpenseTTC;
	}

	public void setCashIexpenseTTC(Double cashIexpenseTTC) {
		this.cashIexpenseTTC = cashIexpenseTTC;
	}

	public Double getCashCrossCost() {
		return cashCrossCost;
	}

	public void setCashCrossCost(Double cashCrossCost) {
		this.cashCrossCost = cashCrossCost;
	}

	public Double getCashLobHT() {
		return cashLobHT;
	}

	public void setCashLobHT(Double cashLobHT) {
		this.cashLobHT = cashLobHT;
	}

	public Double getCashLobTTC() {
		return cashLobTTC;
	}

	public void setCashLobTTC(Double cashLobTTC) {
		this.cashLobTTC = cashLobTTC;
	}

	public Double getCashSalesHT() {
		return cashSalesHT;
	}

	public void setCashSalesHT(Double cashSalesHT) {
		this.cashSalesHT = cashSalesHT;
	}

	public Double getCashSalesTTC() {
		return cashSalesTTC;
	}

	public void setCashSalesTTC(Double cashSalesTTC) {
		this.cashSalesTTC = cashSalesTTC;
	}

	public Double getCashSupportHT() {
		return cashSupportHT;
	}

	public void setCashSupportHT(Double cashSupportHT) {
		this.cashSupportHT = cashSupportHT;
	}

	public Double getCashSupportTTC() {
		return cashSupportTTC;
	}

	public void setCashSupportTTC(Double cashSupportTTC) {
		this.cashSupportTTC = cashSupportTTC;
	}

	public Double getCashTpsrHT() {
		return cashTpsrHT;
	}

	public void setCashTpsrHT(Double cashTpsrHT) {
		this.cashTpsrHT = cashTpsrHT;
	}

	public Double getCashTpsrTTC() {
		return cashTpsrTTC;
	}

	public void setCashTpsrTTC(Double cashTpsrTTC) {
		this.cashTpsrTTC = cashTpsrTTC;
	}

	public Double getAllCashOutHT() {
		return allCashOutHT;
	}

	public void setAllCashOutHT(Double allCashOutHT) {
		this.allCashOutHT = allCashOutHT;
	}

	public Double getAllCashOutTTC() {
		return allCashOutTTC;
	}

	public void setAllCashOutTTC(Double allCashOutTTC) {
		this.allCashOutTTC = allCashOutTTC;
	}

	public Double getProjectHours() {
		return projectHours;
	}

	public void setProjectHours(Double projectHours) {
		this.projectHours = projectHours;
	}

	public Double getDeliveryHours() {
		return deliveryHours;
	}

	public void setDeliveryHours(Double deliveryHours) {
		this.deliveryHours = deliveryHours;
	}

	public Double getCustomerDeliveryHours() {
		return customerDeliveryHours;
	}

	public void setCustomerDeliveryHours(Double customerDeliveryHours) {
		this.customerDeliveryHours = customerDeliveryHours;
	}

	public Double getLobDeliveryHours() {
		return lobDeliveryHours;
	}

	public void setLobDeliveryHours(Double lobDeliveryHours) {
		this.lobDeliveryHours = lobDeliveryHours;
	}

	public Double getInLobHours() {
		return inLobHours;
	}

	public void setInLobHours(Double inLobHours) {
		this.inLobHours = inLobHours;
	}

	public Double getInvoiceDeductions() {
		return invoiceDeductions;
	}

	public void setInvoiceDeductions(Double invoiceDeductions) {
		this.invoiceDeductions = invoiceDeductions;
	}

	public Double getCashInvoiceDeductions() {
		return cashInvoiceDeductions;
	}

	public void setCashInvoiceDeductions(Double cashInvoiceDeductions) {
		this.cashInvoiceDeductions = cashInvoiceDeductions;
	}

	public Double getIbuyDeductions() {
		return ibuyDeductions;
	}

	public void setIbuyDeductions(Double ibuyDeductions) {
		this.ibuyDeductions = ibuyDeductions;
	}

	public Double getCostSupportIbuy() {
		return costSupportIbuy;
	}

	public void setCostSupportIbuy(Double costSupportIbuy) {
		this.costSupportIbuy = costSupportIbuy;
	}

	public Double getCostSupportIexpense() {
		return costSupportIexpense;
	}

	public void setCostSupportIexpense(Double costSupportIexpense) {
		this.costSupportIexpense = costSupportIexpense;
	}

	public Double getCostSupportPayroll() {
		return costSupportPayroll;
	}

	public void setCostSupportPayroll(Double costSupportPayroll) {
		this.costSupportPayroll = costSupportPayroll;
	}

	public Double getCostSupportCross() {
		return costSupportCross;
	}

	public void setCostSupportCross(Double costSupportCross) {
		this.costSupportCross = costSupportCross;
	}

	public Double getCostSupportIbuyDeductions() {
		return costSupportIbuyDeductions;
	}

	public void setCostSupportIbuyDeductions(Double costSupportIbuyDeductions) {
		this.costSupportIbuyDeductions = costSupportIbuyDeductions;
	}

	public Double getCostSalesIbuy() {
		return costSalesIbuy;
	}

	public void setCostSalesIbuy(Double costSalesIbuy) {
		this.costSalesIbuy = costSalesIbuy;
	}

	public Double getCostSalesIexpense() {
		return costSalesIexpense;
	}

	public void setCostSalesIexpense(Double costSalesIexpense) {
		this.costSalesIexpense = costSalesIexpense;
	}

	public Double getCostSalesPayroll() {
		return costSalesPayroll;
	}

	public void setCostSalesPayroll(Double costSalesPayroll) {
		this.costSalesPayroll = costSalesPayroll;
	}

	public Double getCostSalesCross() {
		return costSalesCross;
	}

	public void setCostSalesCross(Double costSalesCross) {
		this.costSalesCross = costSalesCross;
	}

	public Double getCostSalesIbuyDeductions() {
		return costSalesIbuyDeductions;
	}

	public void setCostSalesIbuyDeductions(Double costSalesIbuyDeductions) {
		this.costSalesIbuyDeductions = costSalesIbuyDeductions;
	}

	public Double getCostSalesCustomerIbuy() {
		return costSalesCustomerIbuy;
	}

	public void setCostSalesCustomerIbuy(Double costSalesCustomerIbuy) {
		this.costSalesCustomerIbuy = costSalesCustomerIbuy;
	}

	public Double getCostSalesCustomerIexpense() {
		return costSalesCustomerIexpense;
	}

	public void setCostSalesCustomerIexpense(Double costSalesCustomerIexpense) {
		this.costSalesCustomerIexpense = costSalesCustomerIexpense;
	}

	public Double getCostSalesCustomerPayroll() {
		return costSalesCustomerPayroll;
	}

	public void setCostSalesCustomerPayroll(Double costSalesCustomerPayroll) {
		this.costSalesCustomerPayroll = costSalesCustomerPayroll;
	}

	public Double getCostSalesCustomerCross() {
		return costSalesCustomerCross;
	}

	public void setCostSalesCustomerCross(Double costSalesCustomerCross) {
		this.costSalesCustomerCross = costSalesCustomerCross;
	}

	public Double getCostSalesCustomerIbuyDeductions() {
		return costSalesCustomerIbuyDeductions;
	}

	public void setCostSalesCustomerIbuyDeductions(Double costSalesCustomerIbuyDeductions) {
		this.costSalesCustomerIbuyDeductions = costSalesCustomerIbuyDeductions;
	}

	public Double getCostLobIbuy() {
		return costLobIbuy;
	}

	public void setCostLobIbuy(Double costLobIbuy) {
		this.costLobIbuy = costLobIbuy;
	}

	public Double getCostLobIexpense() {
		return costLobIexpense;
	}

	public void setCostLobIexpense(Double costLobIexpense) {
		this.costLobIexpense = costLobIexpense;
	}

	public Double getCostLobPayroll() {
		return costLobPayroll;
	}

	public void setCostLobPayroll(Double costLobPayroll) {
		this.costLobPayroll = costLobPayroll;
	}

	public Double getCostLobCross() {
		return costLobCross;
	}

	public void setCostLobCross(Double costLobCross) {
		this.costLobCross = costLobCross;
	}

	public Double getCostLobIbuyDeductions() {
		return costLobIbuyDeductions;
	}

	public void setCostLobIbuyDeductions(Double costLobIbuyDeductions) {
		this.costLobIbuyDeductions = costLobIbuyDeductions;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setCostStartDate(Date costStartDate) {
		this.costStartDate = costStartDate;
	}

	public void setCostEndDate(Date costEndDate) {
		this.costEndDate = costEndDate;
	}

	public void setCostIbuyNew(Double costIbuyNew) {
		this.costIbuyNew = costIbuyNew;
	}

	public void setCostIexpenseNew(Double costIexpenseNew) {
		this.costIexpenseNew = costIexpenseNew;
	}

	public void setCostPayrollNew(Double costPayrollNew) {
		this.costPayrollNew = costPayrollNew;
	}

	public void setIdproject(Integer idproject) {
		this.idproject = idproject;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public void setManagerFullname(String managerFullname) {
		this.managerFullname = managerFullname;
	}

	public void setProjectStartDate(Date projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public void setProjectDuration(String projectDuration) {
		this.projectDuration = projectDuration;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public void setProjectTax(Double projectTax) {
		this.projectTax = projectTax;
	}

	public void setProjectAmortization(Double projectAmortization) {
		this.projectAmortization = projectAmortization;
	}

	public void setProjectDepreciation(Double projectDepreciation) {
		this.projectDepreciation = projectDepreciation;
	}

	public void setProjectTotalSalesCost(Double projectTotalSalesCost) {
		this.projectTotalSalesCost = projectTotalSalesCost;
	}

	public void setProjectTotalSupportCost(Double projectTotalSupportCost) {
		this.projectTotalSupportCost = projectTotalSupportCost;
	}

	public void setCostcenter(Integer costcenter) {
		this.costcenter = costcenter;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setPlannedRevenue(Double plannedRevenue) {
		this.plannedRevenue = plannedRevenue;
	}

	public void setPlannedCost(Double plannedCost) {
		this.plannedCost = plannedCost;
	}

	public void setProjectCurrencyMaxValue(Double projectCurrencyMaxValue) {
		this.projectCurrencyMaxValue = projectCurrencyMaxValue;
	}

	public void setPlannedRevenueMAD(Double plannedRevenueMAD) {
		this.plannedRevenueMAD = plannedRevenueMAD;
	}

	public void setPlannedCostMAD(Double plannedCostMAD) {
		this.plannedCostMAD = plannedCostMAD;
	}

	public Double getPlannedSalesCost() {
		return plannedRevenueMAD * projectTotalSalesCost;
	}

	public Double getPlannedSupportCost() {
		return plannedRevenueMAD * projectTotalSupportCost;
	}

	public Double getPlannedTpsrCost() {
		return plannedRevenueMAD * projectTpsr;
	}

	public Double getPlannedTaxCost() {
		return plannedRevenueMAD * projectTax;
	}

	public Double getPlannedAmortisationAndDepreciationCost() {
		return plannedRevenueMAD * (projectAmortization + projectDepreciation);
	}

	public Double getProjectOperatingMargin(Double costFinance) {
		return allRevenues - getOperatingCost(costFinance);
	}

	public Double getEbitda(Double costFinance) {
		return getProjectOperatingMargin(costFinance) - costSales - costSupport - costTpsr;
	}

	public Double getTax(Double costFinance) {
		return Math.max(0.0, projectTax * getEbitda(costFinance));
	}

	public Double getDepreciationAndAmortisation(Double costFinance) {
		return Math.max(0.0, (projectDepreciation + projectAmortization) * getEbitda(costFinance));
	}

	public Double getNetMargin(Double costFinance) {
		return getEbitda(costFinance) - getTax(costFinance) - getDepreciationAndAmortisation(costFinance);
	}

	public Double getAllCosts(Double costFinance) {
		Double result = 0.0;
		if (allCosts != null)
			result += allCosts;
		if (costFinance != null)
			result += costFinance;
		return result;
	}

	public Double getAllCostsNew(Double costFinance) {
		Double result = 0.0;
		if (allCosts != null)
			result += allCosts;
		if (costFinance != null)
			result += costFinance;
		if (costIbuy != null && costIbuyNew != null)
			result += costIbuyNew - costIbuy;
		if (costIexpense != null && costIexpenseNew != null)
			result += costIexpenseNew - costIexpense;
		if (costPayroll != null && costPayrollNew != null)
			result += costPayrollNew - costPayroll;
		return result;
	}

	public String getAllCostsNewVsPlannedCostPercentage(Double costFinance) {
		if (plannedCostMAD != null && plannedCostMAD != 0.0) {
			Integer percentage = (int) (getAllCostsNew(costFinance) * 100 / plannedCostMAD);
			return String.valueOf(percentage);
		}

		return "0";
	}

	public String getAllCostsNewVsPlannedCostPercentageWidth(Double costFinance) {
		return "width: " + getAllCostsNewVsPlannedCostPercentage(costFinance) + "%";
	}

	public String getAllCostsVsPlannedCostPercentage(Double costFinance) {
		if (plannedCostMAD != null && plannedCostMAD != 0.0) {
			Integer percentage = (int) (getAllCosts(costFinance) * 100 / plannedCostMAD);
			return String.valueOf(percentage);
		}
		return "0";
	}

	public String getAllCostsVsPlannedCostPercentageWidth(Double costFinance) {
		return "width: " + getAllCostsVsPlannedCostPercentage(costFinance) + "%";
	}

	public Double getRemainingBudget(Double costFinance) {
		return getAllCostsNew(costFinance) - getAllCosts(costFinance);
	}

	public String getRemainingBudgetVsPlannedCostPercentage(Double costFinance) {
		if (plannedCostMAD != null && plannedCostMAD != 0.0) {
			Integer percentage = (int) (getRemainingBudget(costFinance) * 100 / plannedCostMAD);
			return String.valueOf(percentage);
		}
		return "0";
	}

	public String getRemainingBudgetVsPlannedCostPercentageWidth(Double costFinance) {
		return "width: " + getRemainingBudgetVsPlannedCostPercentage(costFinance) + "%";
	}

	public Double getMargin() {
		Double margin = 0.0;
		if (allRevenues != null)
			margin += allRevenues;
		if (allCosts != null)
			margin -= allCosts;
		if (costFinance != null)
			margin -= costFinance;
		return margin;
	}

	public Double getMargin(Double costFinance) {
		this.costFinance = costFinance;
		return getMargin();
	}

	public Double getMarginPercent() {
		if (getMargin() != null && allRevenues != 0.0)
			return getMargin() / allRevenues;

		return null;
	}

	public Double getAvailableBudget(Double costFinance) {
		return plannedCostMAD - (getAllCosts(costFinance) + (costIbuyNew - costIbuy) + (costIexpenseNew - costIexpense) + (costPayrollNew - costPayroll));
	}

	public Double getRemainderIbuy() {
		try {
			return costIbuyNew - costIbuy;
		} catch (Exception e) {
			return null;
		}
	}

	public Double getRemainderIexpense() {
		try {
			return costIexpenseNew - costIexpense;
		} catch (Exception e) {
			return null;
		}
	}

	public Double getRemainderPayroll() {
		try {
			return costPayrollNew - costPayroll;
		} catch (Exception e) {
			return null;
		}
	}

	public Double getFairBudget() {
		try {
			return allRevenues * plannedCostMAD / plannedRevenueMAD;
		} catch (Exception e) {
			return null;
		}
	}

	public Double getPlannedMarginMAD() {
		return plannedRevenueMAD - plannedCostMAD;
	}

	// GETTERS & SETTERS
	public Double getCostFinance() {
		return costFinance;
	}

	public void setCostFinance(Double costFinance) {
		this.costFinance = costFinance;
	}

	public Date getCostStartDate() {
		return costStartDate;
	}

	public Date getCostEndDate() {
		return costEndDate;
	}

	public Integer getIdproject() {
		return idproject;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getLob() {
		return lob;
	}

	public String getCustomer() {
		return customer;
	}

	public String getManager() {
		return manager;
	}

	public String getManagerFullname() {
		return managerFullname;
	}

	public Date getProjectStartDate() {
		return projectStartDate;
	}

	public Date getProjectEndDate() {
		return projectEndDate;
	}

	public String getProjectDuration() {
		return projectDuration;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public String getProjectType() {
		return projectType;
	}

	public Integer getCostcenter() {
		return costcenter;
	}

	public String getCurrency() {
		return currency;
	}

	public Double getPlannedRevenue() {
		return plannedRevenue;
	}

	public Double getPlannedCost() {
		return plannedCost;
	}

	public Double getCostIbuyNew() {
		return costIbuyNew;
	}

	public Double getCostIexpenseNew() {
		return costIexpenseNew;
	}

	public Double getCostPayrollNew() {
		return costPayrollNew;
	}

	public Double getProjectCurrencyMaxValue() {
		return projectCurrencyMaxValue;
	}

	public Double getPlannedCostMAD() {
		return plannedCostMAD;
	}

	public Double getPlannedRevenueMAD() {
		return plannedRevenueMAD;
	}

	public void setProjectTpsr(Double projectTpsr) {
		this.projectTpsr = projectTpsr;
	}

	public Double getTpsrBudgeting() {
		return tpsrBudgeting;
	}

	public void setTpsrBudgeting(Double tpsrBudgeting) {
		this.tpsrBudgeting = tpsrBudgeting;
	}

	public Double getTpsrPayment() {
		return tpsrPayment;
	}

	public void setTpsrPayment(Double tpsrPayment) {
		this.tpsrPayment = tpsrPayment;
	}

	public Double getProjectTax() {
		return projectTax;
	}

	public Double getProjectAmortization() {
		return projectAmortization;
	}

	public Double getProjectDepreciation() {
		return projectDepreciation;
	}

	public Double getProjectTotalSalesCost() {
		return projectTotalSalesCost;
	}

	public Double getProjectTotalSupportCost() {
		return projectTotalSupportCost;
	}

	public Double getProjectTpsr() {
		return projectTpsr;
	}

}
